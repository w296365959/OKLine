package com.vboss.okline.ui.contact;

import android.content.Context;
import android.view.View;

import com.vboss.okline.R;
import com.vboss.okline.base.BaseFragment;
import com.vboss.okline.ui.card.CardBaseFragment;
import com.vboss.okline.view.widget.CommonDialog;

import java.net.SocketTimeoutException;

import timber.log.Timber;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author : Lin Zhangbin
 * Email : hedgehog@okline.cn
 * Date : 2017/5/4 15:39
 * Desc :
 */

public abstract class ContactsBaseFragment extends BaseFragment {
    private static final String TAG = ContactsBaseFragment.class.getSimpleName();
    private CommonDialog timeOutDialog;   //time out dialog
    private int methodFlag;   //method flag
    private boolean show;    //timeOut dialog show flag

    @Override
    public void onResume() {
        super.onResume();
        show = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        show = false;
    }

    /**
     * init timeOut dialog
     *
     * @param context Context
     */
    protected void initDialog(Context context) {
        timeOutDialog = new CommonDialog(context);
        timeOutDialog.setTilte(context.getResources().getString(R.string.card_over_time));
        timeOutDialog.setNegativeButton(context.getResources().getString(R.string.cancel));
        timeOutDialog.setPositiveButton(context.getResources().getString(R.string.confirm));
        timeOutDialog.setOnDialogClickListener(new CommonDialog.OnDialogInterface() {
            @Override
            public void cancel(View view, CommonDialog dialog) {
                dialog.dismiss();
            }

            @Override
            public void ensure(View view, CommonDialog dialog) {
                dialog.dismiss();
//                onRetry(methodFlag);
            }
        });
    }


    /**
     * handler SocketTimeOutException
     *
     * @param throwable SocketTimeOutException
     * @param method    int method flag
     */
    public void handlerTimeOut(Throwable throwable, int method) {
        this.methodFlag = method;
        if (throwable == null) {
            Timber.tag(TAG).e("throwable is null");
            return;
        }
        if (throwable instanceof SocketTimeoutException) {
            Timber.tag(TAG).e("throwable %s", throwable.getClass());
            if (timeOutDialog == null) {
                Timber.tag(TAG).e("time out dialog is null, so don't deal SocketTimeOutException !");
                return;
            }
            Timber.tag(TAG).i("show %s", show);
            if (!timeOutDialog.isShowing() && show) {
                timeOutDialog.show();
            }
        }
    }

    /**
     * request time out retry
     *
     * @param method int methodFlag
     */
//    protected abstract void onRetry(int method);
}
