package com.vboss.okline.ui.contact.myCard;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hwangjr.rxbus.RxBus;
import com.vboss.okline.R;
import com.vboss.okline.base.BaseActivity;
import com.vboss.okline.base.BaseFragment;
import com.vboss.okline.base.DefaultSubscribe;
import com.vboss.okline.base.EventToken;
import com.vboss.okline.base.OKLineApp;
import com.vboss.okline.base.helper.FragmentToolbar;
import com.vboss.okline.data.UserRepository;
import com.vboss.okline.data.entities.AddableBankInfo;
import com.vboss.okline.ui.home.MainActivity;
import com.vboss.okline.ui.user.Utils;
import com.vboss.okline.utils.PinyinUtils;
import com.vboss.okline.utils.TextUtils;
import com.vboss.okline.utils.ToastUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;


/**
 * OKLine(HangZhou) co.,Ltd.
 * Author : Lin Zhangbin
 * Email : hedgehog@okline.cn
 * Date : 2017/6/12 14:58
 * Desc :
 */
public class ChooseBankFragment extends BaseFragment {
    private static final String TAG = "ChooseBankFragment";
    @BindView(R.id.toolbar_choose_bank)
    FragmentToolbar toolbar;
    @BindView(R.id.lv_choose_bank)
    ListView lvChooseBank;
    Unbinder unbinder;
    private MainActivity activity;
    private BankAdapter bankAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_bank_fragment, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar.setActionTitle(getResources().getString(R.string.title_choose_bank));
        toolbar.setActionMenuVisible(View.GONE);
        toolbar.setActionMenuClickable(false);
        toolbar.setOnNavigationClickListener(new FragmentToolbar.OnNavigationClickListener() {
            @Override
            public void onNavigation(View v) {
                TextUtils.showOrHideSoftIM(toolbar,false);
                activity.removeSecondFragment();
            }
        });

        initContent();
    }

    private void initContent() {
        //调接口拿到银行数据用于展示
//        final List<String> list = new ArrayList<>();
//        for (int i = 0; i < 20; i++) {
//
//            list.add("农业银行" + i);
//        }
//        BankAdapter bankAdapter = new BankAdapter(activity, list);
//        lvChooseBank.setAdapter(bankAdapter);


        UserRepository.getInstance(OKLineApp.context).getAddableBankList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new DefaultSubscribe<List<AddableBankInfo>>(TAG){

                    @Override
                    public void onNext(List<AddableBankInfo> addableBankInfos) {
                        for (AddableBankInfo addableBankInfo : addableBankInfos) {
                            Timber.tag(TAG).i("addableBankInfo : "+addableBankInfo.toString());
                        }

                        bankAdapter = new BankAdapter(activity, addableBankInfos);
                        lvChooseBank.setAdapter(bankAdapter);

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        ToastUtil.show(activity,"获取银行列表失败");
                    }
                });

        lvChooseBank.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                AddableBankInfo item = (AddableBankInfo) bankAdapter.getItem(position);
                String bankName = item.getMerName();

                RxBus.get().post(EventToken.SELECT_BANK,item);
                activity.removeSecondFragment();

            }
        });


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public class BankAdapter extends BaseAdapter {
        private List<AddableBankInfo> list;
        private Context context;

        public BankAdapter(Context context, List<AddableBankInfo> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            AddableBankInfo addableBankInfo = list.get(position);
            String bankName = addableBankInfo.getMerName();
            String merIcon = addableBankInfo.getMerIcon();
            ViewHolder holder;
            if (null == convertView){
                convertView = LayoutInflater.from(context).inflate(R.layout.choose_bank_item, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }

            holder.tvBankName.setText(bankName);
            holder.ivBankIcon.setImageURI(Uri.parse(merIcon));
            if (position == getPositionForSection(PinyinUtils.getFirstHeadWordChar(bankName).charAt(0))){
                holder.tvCatalog.setVisibility(View.VISIBLE);
                holder.tvCatalog.setText(PinyinUtils.getFirstHeadWordChar(bankName));
            }else{
                holder.tvCatalog.setVisibility(View.GONE);
            }


            return convertView;
        }

        public int getPositionForSection(int section) {
            for (int i = 0, m = getCount(); i < m; i++) {
//            char firstChar = getFirstLetter(mList.get(i)).matches("[A-Z]") ? getFirstLetter(mList.get(i)).charAt(0) : '#';
                char firstChar = PinyinUtils.getFirstHeadWordChar(list.get(i).getMerName()).charAt(0);
                if (firstChar == section) {
                    return i;
                }
            }
            return -1;
        }

        class ViewHolder {
            @BindView(R.id.tv_catalog)
            TextView tvCatalog;
            @BindView(R.id.tv_bank_name)
            TextView tvBankName;
            @BindView(R.id.iv_bank_icon)
            SimpleDraweeView ivBankIcon;


            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }
}
