package com.vboss.okline.ui.card;

import android.database.Cursor;
import android.os.AsyncTask;

import com.okline.ocp.RechargeSync;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;
import com.vboss.okline.base.DefaultSubscribe;
import com.vboss.okline.base.OKLineApp;
import com.vboss.okline.data.RecordRepository;
import com.vboss.okline.data.UserRepository;
import com.vboss.okline.data.entities.CardLog;
import com.vboss.okline.data.local.DBService;
import com.vboss.okline.ui.user.Utils;

import org.json.JSONObject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * OkLine(HangZhou) co., Ltd. <br/>
 * Author: WangShuai <br/>
 * Email : wangshuai@okline.cn <br/>
 * Date  : 2017/4/28 9:12 <br/>
 * Summary  : recharge transaction record synchronous to server
 */

public class RechargeSynchronousHelper {
    private static final String TAG = RechargeSynchronousHelper.class.getSimpleName();
    public static final String P_ID = "olApp";
    public static final String P_CODE = "OKLine";
    private static RechargeSynchronousHelper instance;
    private RechargeSync rechargeSync;
    private DBService dbService;
    private BriteDatabase db;
    private static final String SQL_QUERY = "select * from " + CardLog.TABLE_NAME;
    private static final String SQL_DELETE = CardLog.ORDERNO + " =? ";

    private RechargeSynchronousHelper() {
        rechargeSync = new RechargeSync();
        dbService = DBService.getInstance(OKLineApp.context);
        db = dbService.getBriteDatabase();
    }

    public static RechargeSynchronousHelper getInstance() {
        if (instance == null) {
            synchronized (RechargeSynchronousHelper.class) {
                if (instance == null) {
                    instance = new RechargeSynchronousHelper();
                }
            }
        }
        return instance;
    }

    /**
     * synchronous card recharge transaction
     *
     * @param cardLog card transaction
     */
    public void rechargeSynchronousToServer(final CardLog cardLog) {
        Utils.showLog(TAG,"充值同步："+cardLog.toString());
        Timber.tag(TAG).i("cardLog %s ", cardLog.toString());
        new AsyncTask<CardLog, String, String>() {

            @Override
            protected String doInBackground(CardLog... params) {
                String olNo = UserRepository.getInstance(OKLineApp.context).getOlNo();
                String merNo = "6";
                CardLog temp = params[0];
                String result = "";
                try {
                    result = rechargeSync.rechargeSync(olNo, temp.orderNo(), temp.amount(),
                            temp.cardId(), temp.cardMainType(), merNo, P_ID, P_CODE);
                    Timber.tag(TAG).i("result %s ", result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                try {
                    Utils.showLog(TAG,"同步结果："+result);
                    Timber.tag(TAG).i("recharge transaction synchronous result %s ", result);
                    JSONObject jsonObject = new JSONObject(result);
                    int resultCode = jsonObject.optInt("resultCode");
                    if (resultCode != 0) {
                        RecordRepository.getInstance(OKLineApp.context).saveTransLog(cardLog);
                        deleteTransaction(cardLog);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    RecordRepository.getInstance(OKLineApp.context).saveTransLog(cardLog);
                }
            }
        }.execute(cardLog);
    }

    private void deleteTransaction(final CardLog cardLog) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int code = db.delete(CardLog.TABLE_NAME, SQL_DELETE, cardLog.orderNo());
                Timber.tag(TAG).i("delete result code : %s", code);
            }
        }).start();

    }

    /**
     * synchronous record log to server
     */
    public void synchronousRecord() {
        db.createQuery(CardLog.TABLE_NAME, SQL_QUERY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscribe<SqlBrite.Query>(TAG) {
                    @Override
                    public void onNext(SqlBrite.Query query) {
                        super.onNext(query);
                        Cursor cursor = query.run();
                        if (cursor != null) {
                            Timber.tag(TAG).i("cursor count %s", cursor.getCount());
                            try {
                                while (cursor.moveToNext()) {
                                    CardLog cardLog = CardLog.MAPPER.map(cursor);
                                    rechargeSynchronousToServer(cardLog);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                cursor.close();
                            }
                        }

                    }
                });
    }

}
