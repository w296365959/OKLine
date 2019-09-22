package com.vboss.okline.jpush;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.vboss.okline.R;
import com.vboss.okline.ui.card.notice.CardNoticeFragment;
import com.vboss.okline.ui.card.notice.CardNoticeFragment_2;

import timber.log.Timber;

/**
 * OkLine(HangZhou) co., Ltd. <br/>
 * Author: WangShuai <br/>
 * Email : wangshuai@okline.cn <br/>
 * Date  : 2017/4/21 10:54 <br/>
 * Summary  : JPush activity card notice
 */
public class JPushActivity extends AppCompatActivity {
    private static final String TAG = JPushActivity.class.getSimpleName();

    FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jpush);
        Timber.tag(TAG).i(" ******* onCreate");
        init(getIntent().getExtras());
    }

    /**
     * init data
     *
     * @param args Bundle
     */
    private void init(Bundle args) {
        try {
            JPushEntity data = null;
            if (args != null) {
                data = args.getParcelable(JPushHelper.KEY_JPUSH_DATA);
                Timber.tag(TAG).i("data %s", data);
            } else {
                finish();
            }
            if (data != null) {
                fragmentManager.beginTransaction().replace(R.id.container_notice,
                        CardNoticeFragment_2.newInstance(data)).commit();
            } else {
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Timber.tag(TAG).i(" **** onNewIntent");
        init(intent.getExtras());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Timber.tag(TAG).i("requestCode %s \nresultCode %s \ndata %s", requestCode, resultCode,
                data == null ? "null" : data.getExtras().toString());
        if (requestCode == CardNoticeFragment.RECHAREGE_REQUEST_CODE && resultCode == RESULT_OK) {
            finish();
        }
    }
}
