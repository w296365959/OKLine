package com.vboss.okline.ui.contact.TransferAccounts;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter;
import com.vboss.okline.R;
import com.vboss.okline.base.DefaultSubscribe;
import com.vboss.okline.data.RecordRepository;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Jiang Zhongyuan <br/>
 * Email   : jiangzhongyuan@okline.cn <br/>
 * Date    : 17/5/2 <br/>
 * Summary : 在这里描述Class的主要功能
 */
public class TransDateAdapter extends ExpandableRecyclerAdapter<TransDateObj, TransLog, TransParentHolder, TransChildHolder> {
    private static final String TAG = "TransDateAdapter";
    private Context context;
    private LayoutInflater inflater;
    private ExpandCollapseListener expandCollapseListener = new ExpandCollapseListener() {
        @Override
        public void onParentExpanded(int i) {
            TransDateObj dateObj = getParentList().get(i);
            List<TransLog> recordByDates = dateObj.getChildList();
            if (recordByDates.size() <= 0) {
                loadChild(dateObj, i);
            }
        }

        @Override
        public void onParentCollapsed(int i) {

        }
    };

    public TransDateAdapter(Context context, @NonNull List<TransDateObj> parentList) {
        super(parentList);
        this.context = context;
        inflater = LayoutInflater.from(context);
        setExpandCollapseListener(expandCollapseListener);
    }

    public void refresh(@NonNull List<TransDateObj> datas) {
        refresh(datas, false);
    }

    public void refresh(@NonNull List<TransDateObj> datas, boolean expandGroup) {
        getParentList().clear();
        getParentList().addAll(datas);
        notifyParentDataSetChanged(true);
        if (expandGroup) {
            expandAllParents();
            if (datas.size() > 0) {
                expandCollapseListener.onParentExpanded(0);
            }
        } else {
            collapseAllParents();
        }
    }

    public void addDatas(@NonNull List<TransDateObj> datas) {
        getParentList().addAll(datas);
        notifyParentDataSetChanged(true);
    }

    @NonNull
    @Override
    public TransParentHolder onCreateParentViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.trans_record_date_parent, viewGroup, false);
        return new TransParentHolder(view);
    }

    @NonNull
    @Override
    public TransChildHolder onCreateChildViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.trans_record_date_child, viewGroup, false);
        return new TransChildHolder(view);
    }

    @Override
    public void onBindParentViewHolder(@NonNull TransParentHolder parentHolder, int i, @NonNull TransDateObj dateObj) {
        parentHolder.bind(dateObj);
    }

    @Override
    public void onBindChildViewHolder(@NonNull TransChildHolder childHolder, int i, int i1, @NonNull TransLog cardLog) {
        childHolder.bind(cardLog);
    }

    private void loadChild(final TransDateObj dateObj, final int position) {
        List<TransLog> list = new ArrayList<>();
        for (int i = 0 ; i < 3 ; i++){
            TransLog transLog = new TransLog();
            transLog.setTransTime("11:11");
            transLog.setTransState("已到账");
            transLog.setOtherCardName("中国银行");
            transLog.setOtherCardNo("66666");
            transLog.setTransMoney("2");
            transLog.setMyCardName("招商银行");
            transLog.setMyCardNo("7777");
            list.add(transLog);
        }
        dateObj.setList(list);
        notifyChildRangeInserted(position, 0, list.size());
        Log.i(TAG,"childList: " +list.toString());

//        RecordRepository repository = RecordRepository.getInstance(context);
//        repository.getTransLogList(dateObj.getCardName())
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new DefaultSubscribe<List<CardLog>>("RecordDateAdapter") {
//                    @Override
//                    public void onNext(List<CardLog> list) {
//                        if (list.size() > 0) {
//                            dateObj.setList(list);
//                            notifyChildRangeInserted(position, 0, list.size());
//                        }
//                    }
//                });
    }
}
