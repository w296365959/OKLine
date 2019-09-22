package com.vboss.okline.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.vboss.okline.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PersonalInfoApprovalActivity extends AppCompatActivity {
    public static final String CARD_ICON = "cardIcon";
    public static final String MER_NAME = "merName";
    public static final String OL_NO = "olNo";
    public static final String BLUETOOTH_ADDRESS = "ocard_address";
    @BindView(R.id.container)
    FrameLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info_approval);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        String cardIcon = intent.getStringExtra(CARD_ICON);
        String merName = intent.getStringExtra(MER_NAME);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.container,CardOpenAuthenticFragment.newInstance(cardIcon,merName, true)).commit();
    }
}
