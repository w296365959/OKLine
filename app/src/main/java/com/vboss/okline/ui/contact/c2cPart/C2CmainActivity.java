package com.vboss.okline.ui.contact.c2cPart;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.vboss.okline.R;
import com.vboss.okline.base.BaseActivity;
import com.vboss.okline.base.DefaultSubscribe;
import com.vboss.okline.base.helper.ToolbarHelper;
import com.vboss.okline.data.CardRepository;
import com.vboss.okline.data.entities.CardEntity;
import com.vboss.okline.data.entities.CardType;
import com.vboss.okline.ui.contact.ContactsUtils;
import com.vboss.okline.utils.StringUtils;
import com.vboss.okline.utils.TextUtils;
import com.vboss.okline.utils.ToastUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

import static com.vboss.okline.base.OKLineApp.context;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author : Lin Zhangbin
 * Email : hedgehog@okline.cn
 * Date : 2017/4/10 20:57
 * Desc :
 */

public class C2CmainActivity extends BaseActivity implements InputFilter{
    //输入的最大金额
//    private static final int MAX_VALUE = Integer.MAX_VALUE;
    private static final int MAX_VALUE = 10000;
    //小数点后的位数
    private static final int POINTER_LENGTH = 2;
    private static final String POINTER = ".";
    private static final String ZERO = "0";
    private static final String TAG = "C2CmainActivity";
    Pattern mPattern;
    @BindView(R.id.et_input_transfer_amount)
    EditText etInputTransferAmount;
    @BindView(R.id.tv_confirm_transfer)
    TextView tvConfirmTransfer;
    private int gatheringAmount;
    private C2CPresenter presenter;
    public C2CmainActivity() {
        mPattern = Pattern.compile("([0-9]|\\.)*");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c2c_main_activity);
        ButterKnife.bind(this);
        initToolbar();
        initView();
    }

    private void initToolbar() {
        ToolbarHelper toolbarHelper = new ToolbarHelper(this);
        toolbarHelper.showToolbar(R.string.title_gathering, true);
        toolbarHelper.setNavigationListener(new ToolbarHelper.OnNavigationClickListener() {
            @Override
            public void onNavigationClick() {
                finish();
                TextUtils.showOrHideSoftIM(tvConfirmTransfer,false);
            }
        });
        toolbarHelper.showMenuButton(false);
    }

    private void initView() {
        InputFilter[] filters={this};
        etInputTransferAmount.setFilters(filters);
        //fix BUG1195
        tvConfirmTransfer.setEnabled(false);
        //end to fix BUG1195
        etInputTransferAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Timber.tag(TAG).i(s.toString());
                if (StringUtils.isNullString(s.toString())
                        || "0".equals(s.toString())
                        || "0.".equals(s.toString())) {
                    tvConfirmTransfer.setEnabled(false);
                    tvConfirmTransfer.setBackground(getResources().getDrawable(R.drawable.shape_confirm_transfer));
                }else{
                    double amount = Double.parseDouble(s.toString());
//                    String s2 = String.valueOf(amount1);
//                    if (!TextUtils.isEmpty(s2)) {
//                        Timber.tag(TAG).i(s2);
//                        gatheringAmount = s2;
//                    }
                    gatheringAmount = (int)(amount * 100);
                    Timber.tag(TAG).i("amount:"+ gatheringAmount);
                    tvConfirmTransfer.setBackground(getResources().getDrawable(R.drawable.shape_transfer_clickable));
                    tvConfirmTransfer.setEnabled(true);
                }
            }

            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        tvConfirmTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContactsUtils.hasNfc(C2CmainActivity.this)){
                    CardRepository.getInstance(C2CmainActivity.this)
                            .getTopCard(CardType.BANK_CARD)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe(new DefaultSubscribe<CardEntity>(TAG){
                                @Override
                                public void onError(Throwable throwable) {
                                    super.onError(throwable);
                                }

                                @Override
                                public void onNext(CardEntity entity) {
                                    String cardName = entity.cardName();
                                    String cardNo = entity.cardNo();

                                    if (null != cardNo){
                                        String name = cardName+"(尾号"+cardNo.substring(cardNo.length() - 4)+")";
                                        Intent intent = new Intent(C2CmainActivity.this,C2CwaitingActivity.class);
//                                    Intent intent = new Intent(C2CmainActivity.this,C2CsuccessActivity.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putInt("amount",gatheringAmount);
                                        Timber.tag(TAG).i("gatheringAmount:"+gatheringAmount);
                                        bundle.putString("myCount",name);
                                        intent.putExtras(bundle);
                                        TextUtils.showOrHideSoftIM(tvConfirmTransfer,false);
                                        startActivity(intent);
                                    }else{
                                        ToastUtil.show(context,"请设置默认卡");
                                    }
                                }
                            });
                }

                //TEST
//                UserRepository repository = UserRepository.getInstance(C2CmainActivity.this);
//                repository.c2cCash(2500,"OLHZ310571000000000365")
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribeOn(Schedulers.io())
//                        .subscribe(new DefaultSubscribe<Boolean>(TAG){
//                            @Override
//                            public void onNext(Boolean aBoolean) {
//                                if (aBoolean){
//                                    Timber.tag(TAG).i("~~~~~~~~~~~~`");
//                                }
//                            }
//                        });

            }
        });
    }
    /**
     * @param source    新输入的字符串
     * @param start     新输入的字符串起始下标，一般为0
     * @param end       新输入的字符串终点下标，一般为source长度-1
     * @param dest      输入之前文本框内容
     * @param dstart    原内容起始坐标，一般为0
     * @param dend      原内容终点坐标，一般为dest长度-1
     * @return          输入内容
     */
    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        String sourceText = source.toString();
        String destText = dest.toString();
        //验证删除等按键
        if (TextUtils.isEmpty(sourceText)) {
            return "";
        }
        Matcher matcher = mPattern.matcher(source);
        //已经输入小数点的情况下，只能输入数字
        if(destText.contains(POINTER)) {
            if (!matcher.matches()) {
                return "";
            } else {
                if (POINTER.equals(source)) {  //只能输入一个小数点
                    return "";
                }
            }
            //验证小数点精度，保证小数点后只能输入2位
            int index = destText.indexOf(POINTER);
            int length = dend - index;
            if (length > POINTER_LENGTH) {
                return dest.subSequence(dstart, dend);
            }
        } else {
            //没有输入小数点的情况下，只能输入小数点和数字，但首位不能输入小数点和0
            if (!matcher.matches()) {
                return "";
            } else {
                if ((POINTER.equals(source)) && TextUtils.isEmpty(destText)) {
                    return "";
                }
                //如果首位为“0”，则只能再输“.”
                if(ZERO.equals(destText)){
                    if(!POINTER.equals(sourceText)){
                        return "";
                    }
                }
            }
        }
        //验证输入金额的大小
        double sumText = Double.parseDouble(destText + sourceText);
        if (sumText > MAX_VALUE) {
            return dest.subSequence(dstart, dend);
        }
        return dest.subSequence(dstart, dend) + sourceText;
    }
}
