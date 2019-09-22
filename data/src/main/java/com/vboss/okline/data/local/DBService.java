package com.vboss.okline.data.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;
import com.squareup.sqldelight.RowMapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Shi Haijun <br/>
 * Email   : haijun@okline.cn <br/>
 * Date    : 2017/3/30 <br/>
 * Summary : 响应式数据库服务类
 */
public class DBService {
    private static final String TAG = DBService.class.getSimpleName();
    private BriteDatabase briteDatabase;
    private static DBService instance;


    public static DBService getInstance(Context context) {
        if (instance == null) {
            instance = new DBService(context);
        }
        return instance;
    }

    private DBService(Context context) {
        SqlBrite brite = new SqlBrite.Builder().logger(new SqlBrite.Logger() {
            @Override
            public void log(String message) {
                Timber.tag("Database").i(message);
            }
        }).build();
        briteDatabase = brite.wrapDatabaseHelper(new DBOpenHelper(context), Schedulers.io());
        briteDatabase.setLoggingEnabled(true);
    }

    /**
     * 删除数据库
     *
     */
    public void deleteData() {
        try {
            DBOpenHelper.deleteDB(briteDatabase.getWritableDatabase());
        } catch (SQLException e) {
            Timber.tag(TAG).e(e, "Fail to delete database data.");
        }
    }

    /**
     * 获取响应式数据库实例
     *
     * @return
     */
    public BriteDatabase getBriteDatabase() {
        return briteDatabase;
    }

    /**
     * 判断某条件下的数据是否存在
     *
     * @param table       数据表表名称
     * @param whereClause 查询条件
     * @param args        查询条件参数
     * @return 有数据则返回true，否则返回false。
     */
    public boolean isDataExisting(String table, String whereClause, String... args) {
        Cursor cursor = briteDatabase.query(buildQuerySql(table, whereClause), args);

        try {
            return cursor != null && cursor.getCount() != 0;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    /**
     * 插入或更新记录
     *
     * @param tableName
     * @param values
     * @return
     */
    public int insert(@NonNull String tableName, @NonNull ContentValues values) {
        return (int) briteDatabase.insert(tableName, values, SQLiteDatabase.CONFLICT_REPLACE);
    }


    /**
     * 插入记录，可指定冲突策略
     *
     * @param tableName
     * @param values
     * @param conflictAlgorithm
     * @return
     */
    public int insert(@NonNull String tableName, @NonNull ContentValues values, int conflictAlgorithm) {
        return (int) briteDatabase.insert(tableName, values, conflictAlgorithm);
    }

    /**
     * 使用替换策略批量插入记录
     *
     * @param table
     * @param list
     * @return
     */
    public int bulkInsert(@NonNull String table, @NonNull List<ContentValues> list) {


        BriteDatabase.Transaction transaction = briteDatabase.newTransaction();

        int count = 0;
        for (ContentValues value : list) {
            if (briteDatabase.insert(table, value, SQLiteDatabase.CONFLICT_REPLACE) != -1) {
                count++;
            }
        }
        transaction.markSuccessful();
        transaction.end();

        Timber.tag(TAG).w(" Insert %d/%d piece of data into %s database", count, list.size(), table);
        return count;
    }

    /**
     * 删除记录
     *
     * @param table
     * @param whereClause
     * @param whereArgs
     * @return
     */
    public int delete(@NonNull String table, @Nullable String whereClause, @Nullable String... whereArgs) {
        return briteDatabase.delete(table, whereClause, whereArgs);
    }

    /**
     * 清空数据库
     *
     * @param table
     */
    public void clear(@NonNull String table) {
        briteDatabase.delete(table, null);
    }

    /**
     * 更新记录
     *
     * @param table
     * @param newValue
     * @param whereClause
     * @param whereArgs
     * @return
     */
    public int update(String table, ContentValues newValue, String whereClause, String... whereArgs) {
        return briteDatabase.update(table, newValue, whereClause, whereArgs);
    }

    /**
     * 查询记录
     *
     * @param table
     * @param mapper
     * @param whereClause
     * @param whereArgs
     * @param <T>
     * @return
     */
    public <T> List<T> select(@NonNull String table, RowMapper<T> mapper, String whereClause, String... whereArgs) {
        Cursor cursor = briteDatabase.query(buildQuerySql(table, whereClause), whereArgs);
        List<T> list = Collections.emptyList();
        if (cursor != null) {
            int count = cursor.getCount();
            list = new ArrayList<>(count);
            while (cursor.moveToNext()) {
                list.add(mapper.map(cursor));
            }
            cursor.close();
        }
        return list;
    }

    /**
     * 静默查询，不接受数据库变动通知。
     *
     * @param table
     * @param mapper
     * @param whereClause
     * @param whereArgs
     * @param <T>
     * @return
     */
    <T> Observable<T> queryOneQuietly(final String table, final Func1<Cursor, T> mapper, final String whereClause, final String... whereArgs) {
        return Observable.fromCallable(new Callable<T>() {
            @Override
            public T call() throws Exception {
                T t = null;
                Cursor cursor = briteDatabase.query(buildQuerySql(table, whereClause), whereArgs);
                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        t = mapper.call(cursor);
                    }
                    cursor.close();
                }
                return t;
            }
        }).subscribeOn(Schedulers.io());
    }


    /**
     * 通过完整的sql语句去静默查询
     *
     * @param sql    完整的sql语句
     * @param mapper mapper for pojo
     * @param <T>    pojo类型
     * @return
     */
    <T> Observable<List<T>> queryListQuietly(final String sql, final Func1<Cursor, T> mapper) {
        return Observable.fromCallable(new Callable<List<T>>() {
            @Override
            public List<T> call() throws Exception {
                List<T> items = Collections.emptyList();

                Cursor cursor = briteDatabase.query(sql);
                if (cursor != null) {
                    items = new ArrayList<T>(cursor.getCount());
                    while (cursor.moveToNext()) {
                        items.add(mapper.call(cursor));
                    }
                    cursor.close();
                }
                return items;
            }
        }).subscribeOn(Schedulers.io());
    }

    /**
     * 静默查询列表。不接受数据库变动通知。
     *
     * @param table       数据库表名
     * @param mapper      mapper for pojo
     * @param whereClause
     * @param whereArgs
     * @param <T>
     * @return
     */
    <T> Observable<List<T>> queryListQuietly(final String table, final Func1<Cursor, T> mapper, final String whereClause, final String... whereArgs) {
        return Observable.fromCallable(new Callable<List<T>>() {
            @Override
            public List<T> call() throws Exception {
                List<T> items = Collections.emptyList();

                Cursor cursor = briteDatabase.query(buildQuerySql(table, whereClause), whereArgs);
                if (cursor != null) {
                    items = new ArrayList<T>(cursor.getCount());
                    while (cursor.moveToNext()) {
                        items.add(mapper.call(cursor));
                    }
                    cursor.close();
                }
                return items;
            }
        }).subscribeOn(Schedulers.io());
    }

    /**
     * 通过whereClause查询单挑数据
     *
     * @param table
     * @param func1
     * @param whereClause
     * @param whereArgs
     * @param <T>
     * @return
     */
    public <T> Observable<T> queryOneWithWhere(String table, final Func1<Cursor, T> func1, String whereClause, String... whereArgs) {
        return briteDatabase.createQuery(table, buildQuerySql(table, whereClause), whereArgs)
                .mapToOne(func1).subscribeOn(Schedulers.io());
    }

    /**
     * 通过传入whereClause查询多条数据
     *
     * @param table
     * @param func1
     * @param whereClause
     * @param whereArgs
     * @param <T>
     * @return
     */
    public <T> Observable<List<T>> queryListWithWhere(String table, Func1<Cursor, T> func1, String whereClause, String... whereArgs) {
        return briteDatabase.createQuery(table, buildQuerySql(table, whereClause), whereArgs)
                .mapToList(func1).subscribeOn(Schedulers.io());
    }

    /**
     * 通过完整的Sql语句查询多条数据
     *
     * @param table
     * @param func1
     * @param sql
     * @param whereArgs
     * @param <T>
     * @return
     */
    public <T> Observable<List<T>> queryListWithSql(String table, Func1<Cursor, T> func1, String sql, String... whereArgs) {
        return briteDatabase.createQuery(table, sql, whereArgs).mapToList(func1);
    }

    /**
     * 生成查询语句
     *
     * @param table
     * @param whereClause
     * @return
     */
    private static String buildQuerySql(String table, String whereClause) {
        return "SELECT * FROM " + table + (!TextUtils.isEmpty(whereClause) ? " WHERE " + whereClause : "");
    }

    /**
     * 一页一页的返回数据
     *
     * @param table    数据库名
     * @param mapper   The mapper of cursor to bean
     * @param orderBy  数据排序方式
     * @param pageSize 每页的数据大小
     * @param <T>      Bean类型
     * @return
     */
    public <T> Observable<List<T>> selectPageByPage(final String table, final Func1<Cursor, T> mapper, final String orderBy, final int pageSize) {
        return Observable.fromCallable(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                //根据指定的pageSize计算总页数
                Cursor cursor = briteDatabase.query("SELECT COUNT(*) FROM " + table);
                if (cursor == null || !cursor.moveToFirst())
                    throw new IllegalStateException("Fail to get data size from " + table);
                int size = cursor.getInt(0);
                Timber.tag(TAG).i("The data size is %d in %s", size, table);
                cursor.close();
                if (cursor.getCount() == 0 || size == 0) {
                    throw new IllegalStateException("No data in " + table);
                }

                size = Math.max(1, size) / pageSize + 1;
                size = size / pageSize + (size % pageSize != 0 ? 1 : 0);
                Timber.tag(TAG).i("The page size of %s is %d", table, size);
                return size;
            }
        }).switchMap(new Func1<Integer, Observable<Integer>>() {
            @Override
            public Observable<Integer> call(Integer integer) {
                //每间隔3秒获取下一页
                return Observable.range(1, integer).zipWith(Observable.interval(3, TimeUnit.SECONDS), new Func2<Integer, Long, Integer>() {
                    @Override
                    public Integer call(Integer integer, Long aLong) {
                        Timber.tag(TAG).i("======>Attempt to select the %d page(s) from %s", integer, table);
                        return integer;
                    }
                });
                /*return Observable.range(1, integer).zipWith(Observable.timer(3, TimeUnit.SECONDS), new Func2<Integer, Long, Integer>() {
                    @Override
                    public Integer call(Integer pageIndex, Long aLong) {
                        Timber.tag(TAG).i("======>Attempt to select the %d page(s) from %s", pageIndex, table);
                        return pageIndex;
                    }
                });*/
            }
        }).concatMap(new Func1<Integer, Observable<? extends List<T>>>() {
            @Override
            public Observable<? extends List<T>> call(Integer pageIndex) {
                return queryByPage(mapper, table, orderBy, pageIndex, pageSize);
            }
        });
    }

    /**
     * 分页查找
     *
     * @param mapper    cursor to bean
     * @param table     表名
     * @param orderBy   排序
     * @param pageIndex 页码
     * @param pageSize  每页数目
     * @param <T>       Bean类型
     * @return
     */
    public <T> Observable<List<T>> queryByPage(@NonNull final Func1<Cursor, T> mapper, @NonNull final String table, final String orderBy, final int pageIndex, final int pageSize) {

        final String sql = SQLiteQueryBuilder.buildQueryString(true, table, null, null, null, null, orderBy, (pageIndex - 1) * pageSize + ", " + pageSize);
        return Observable.fromCallable(new Callable<List<T>>() {
            @Override
            public List<T> call() throws Exception {
                Cursor cursor = briteDatabase.query(sql);
                if (cursor == null || !cursor.moveToFirst()) return Collections.emptyList();
                List<T> data = new ArrayList<T>(cursor.getCount());
                do {
                    data.add(mapper.call(cursor));
                } while (cursor.moveToNext());
                cursor.close();
                Timber.tag(TAG).i("========>Return the %d page(s) from %s", pageIndex, table);
                return data;
            }
        });


    }

}
