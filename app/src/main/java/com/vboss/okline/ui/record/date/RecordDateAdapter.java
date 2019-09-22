package com.vboss.okline.ui.record.date;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter;
import com.vboss.okline.R;
import com.vboss.okline.base.DefaultSubscribe;
import com.vboss.okline.data.RecordRepository;
import com.vboss.okline.data.entities.CardLog;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Jiang Zhongyuan <br/>
 * Email   : jiangzhongyuan@okline.cn <br/>
 * Date    : 17/5/2 <br/>
 * Summary : 在这里描述Class的主要功能
 */
public class RecordDateAdapter extends ExpandableRecyclerAdapter<DateObj, CardLog, ParentHolder, ChildHolder> {
    private Context context;
    private LayoutInflater inflater;
    private ExpandCollapseListener expandCollapseListener = new ExpandCollapseListener() {
        @Override
        public void onParentExpanded(int i) {
            DateObj dateObj = getParentList().get(i);
            List<CardLog> recordByDates = dateObj.getChildList();
            if (recordByDates.size() <= 0) {
                loadChild(dateObj, i);
            }
        }

        @Override
        public void onParentCollapsed(int i) {

        }
    };

    public RecordDateAdapter(Context context, @NonNull List<DateObj> parentList) {
        super(parentList);
        this.context = context;
        inflater = LayoutInflater.from(context);
        setExpandCollapseListener(expandCollapseListener);
    }

    public void refresh(@NonNull List<DateObj> datas) {
        refresh(datas, false);
    }

    public void refresh(@NonNull List<DateObj> datas, boolean expandGroup) {
        getParentList().clear();
        getParentList().addAll(datas);
        notifyParentDataSetChanged(true);
        if (expandGroup) {
            if (datas.size() > 0) {
                expandParent(0);
                expandCollapseListener.onParentExpanded(0);
            }
        } else {
            if (datas.size() > 0) {
                expandParent(0);
                expandCollapseListener.onParentExpanded(0);
            }
        }
    }

    public void addDatas(@NonNull List<DateObj> datas) {
        getParentList().addAll(datas);
        notifyParentDataSetChanged(true);
    }

    @NonNull
    @Override
    public ParentHolder onCreateParentViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.view_record_date_parent, viewGroup, false);
        return new ParentHolder(view);
    }

    @NonNull
    @Override
    public ChildHolder onCreateChildViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.view_record_date_child, viewGroup, false);
        return new ChildHolder(view);
    }

    @Override
    public void onBindParentViewHolder(@NonNull ParentHolder parentHolder, int i, @NonNull DateObj dateObj) {
        Timber.tag("RecordDateAdapter").d("onBindParentViewHolder : " + i);
        parentHolder.bind(dateObj, i);
    }

    @Override
    public void onBindChildViewHolder(@NonNull ChildHolder childHolder, int i, int i1, @NonNull CardLog cardLog) {
        Timber.tag("RecordDateAdapter").d("onBindChildViewHolder : " + i + " , " + i1);
        childHolder.bind(cardLog);
    }

    private void loadChild(final DateObj dateObj, final int position) {
        RecordRepository repository = RecordRepository.getInstance(context);
        repository.getTransLogList(dateObj.getName())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscribe<List<CardLog>>("RecordDateAdapter") {
                    @Override
                    public void onNext(List<CardLog> list) {
                        if (list.size() > 0) {
                            dateObj.setList(list);
                            notifyChildRangeInserted(position, 0, list.size());
                        }
                    }
                });
    }
}
