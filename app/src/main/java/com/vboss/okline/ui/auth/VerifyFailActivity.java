package com.vboss.okline.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.vboss.okline.R;
import com.vboss.okline.utils.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 身份验证失败
 */
public class VerifyFailActivity extends AppCompatActivity {


    @BindView(R.id.btn_again_sumbit)
    Button btnAgain;
    @BindView(R.id.tv_fail)
    TextView tvFail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rz_fail);
        ButterKnife.bind(this);
        btnAgain = (Button)findViewById(R.id.btn_again_sumbit);
        Intent intent = getIntent();
        if(intent!=null){
            String text = intent.getStringExtra("FAILE");
            if(!StringUtils.isNullString(text)){
                tvFail.setText(String.format(getString(R.string.tip4),text));
            }else{
                tvFail.setVisibility(View.GONE);
            }
        }
    }

    @OnClick({R.id.btn_again_sumbit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_again_sumbit:
                startActivity(new Intent(this,CameraIDActivity.class));
                finish();
                break;
        }
    }
}
