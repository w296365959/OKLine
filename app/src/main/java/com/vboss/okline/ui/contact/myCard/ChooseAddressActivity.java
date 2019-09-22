package com.vboss.okline.ui.contact.myCard;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import com.vboss.okline.R;
import com.vboss.okline.base.BaseActivity;
import com.vboss.okline.base.helper.FragmentToolbar;
import com.vboss.okline.ui.contact.NonScrollableViewPager;
import com.vboss.okline.ui.user.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

import static android.content.ContentValues.TAG;


/**
 * OKLine(HangZhou) co.,Ltd.
 * Author : Lin Zhangbin
 * Email : hedgehog@okline.cn
 * Date : 2017/6/6 11:15
 * Desc :
 */

public class ChooseAddressActivity extends BaseActivity {
    //选中的地址
    public String selectedAddress = "";
    public String selectedProvince = "";
    public String selectedCity = "";
    public String selectedArea = "";
    // 省份
    public String[] mProvinceDatas;
    // 城市
    public String[] mCitiesDatas;
    // 地区
    public String[] mAreaDatas;
    // 存储省对应的所有市
    public Map<String, String[]> mCitiesDataMap = new HashMap<String, String[]>();
    // 存储市对应的所有区
    public Map<String, String[]> mAreaDataMap = new HashMap<String, String[]>();
    public String key;
    @BindView(R.id.toolbar_choose_address)
    FragmentToolbar toolbar;
    @BindView(R.id.choose_address_viewpager)
    NonScrollableViewPager viewPager;
    private ArrayList<Fragment> mFragmentList;
    private ChooseProvinceFragment chooseProvinceFragment;
    private ChooseCityFragment chooseCityFragment;
    private ChooseAreaFragment chooseAreaFragment;
    // 判断是否有区
    private boolean hasArea = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_address_activity);
        ButterKnife.bind(this);
        initToobar();
        BeginJsonCitisData(InitData());
        initData();
        initView();

    }

    private void initData() {
        //add by linzhangbin 2017/6/12 区分不同界面来选择地区
        Bundle extras = getIntent().getExtras();
        key = extras.getString("chooseAddress");
        //add by linzhangbin 2017/6/12 区分不同界面来选择地区 end
        mFragmentList = new ArrayList<>();
        chooseProvinceFragment = new ChooseProvinceFragment();
        chooseCityFragment = new ChooseCityFragment();
        chooseAreaFragment = new ChooseAreaFragment();
        mFragmentList.add(chooseProvinceFragment);
        mFragmentList.add(chooseCityFragment);
        mFragmentList.add(chooseAreaFragment);

        viewPager.setOffscreenPageLimit(0);
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragmentList.get(position);
            }

            @Override
            public int getCount() {
                return mFragmentList.size();
            }
        });

    }

    private void initToobar() {
        toolbar.setActionTitle(getResources().getString(R.string.title_edit_address));
        toolbar.setActionMenuVisible(View.GONE);
        toolbar.setActionMenuClickable(false);
        //add by linzhangbin 2017/6/8 返回问题
        toolbar.setOnNavigationClickListener(new FragmentToolbar.OnNavigationClickListener() {
            @Override
            public void onNavigation(View v) {
                finish();
            }
        });
        //add by linzhangbin 2017/6/8 返回问题
    }

    private void initView() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0 :
                        toolbar.setOnNavigationClickListener(new FragmentToolbar.OnNavigationClickListener() {
                            @Override
                            public void onNavigation(View v) {
                                finish();
                            }
                        });
                        break;

                    case 1 :

                        String[] mCityDatas = updateCity(selectedProvince);
                        Timber.tag(TAG).i("selectedProvince : %s  onviewCreated : %s",selectedProvince, mCityDatas[0]);
                        EditAddressAdapter adapter = new EditAddressAdapter(ChooseAddressActivity.this, mCityDatas);
                        chooseCityFragment.lvChooseCity.setAdapter(adapter);

                        toolbar.setOnNavigationClickListener(new FragmentToolbar.OnNavigationClickListener() {
                            @Override
                            public void onNavigation(View v) {
                                viewPager.setCurrentItem(0);
                            }
                        });
                        break;

                    case 2 :
                        String[] mAreaDatas = updateArea(selectedCity);
                        Timber.tag(TAG).i("selectedCity : %s  onviewCreated : %s",selectedCity, mAreaDatas[0]);
                        EditAddressAdapter adapter2 = new EditAddressAdapter(ChooseAddressActivity.this, mAreaDatas);
                        chooseAreaFragment.lvChooseArea.setAdapter(adapter2);

                        toolbar.setOnNavigationClickListener(new FragmentToolbar.OnNavigationClickListener() {
                            @Override
                            public void onNavigation(View v) {
                                viewPager.setCurrentItem(1);
                            }
                        });
                        break;

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 根据省份更新城市数据
     *
     * @param pro 省份
     */
    public String[] updateCity(String pro) {

        return mCitiesDataMap.get(pro);
    }

    /**
     * 根据市 选择对应的区
     *
     * @param city 城市
     */
    public String[] updateArea(String city) {

        return mAreaDataMap.get(city);
    }

    /**
     * 开始解析城市数据
     *
     */
    private void BeginJsonCitisData(String cityJson) {
        if (!android.text.TextUtils.isEmpty(cityJson)) {
            try {
                JSONObject object = new JSONObject(cityJson);
                JSONArray array = object.getJSONArray("citylist");

                // 获取省份的数量
                mProvinceDatas = new String[array.length()];
                String mProvinceStr = null;
                // 循环遍历
                for (int i = 0; i < array.length(); i++) {

                    // 循环遍历省份，并将省保存在mProvinceDatas[]中
                    JSONObject mProvinceObject = array.getJSONObject(i);
                    if (mProvinceObject.has("p")) {
                        mProvinceStr = mProvinceObject.getString("p");
                        mProvinceDatas[i] = mProvinceStr;
                    } else {
                        mProvinceDatas[i] = "unknown province";
                    }

                    JSONArray cityArray;
                    String mCityStr = null;
                    try {
                        // 循环遍历省对应下的城市
                        cityArray = mProvinceObject.getJSONArray("c");
                    } catch (Exception e) {
                        e.printStackTrace();
                        continue;
                    }

                    mCitiesDatas = new String[cityArray.length()];
                    for (int j = 0; j < cityArray.length(); j++) {
                        // 循环遍历城市，并将城市保存在mCitiesDatas[]中
                        JSONObject mCityObject = cityArray.getJSONObject(j);
                        if (mCityObject.has("n")) {
                            mCityStr = mCityObject.getString("n");
                            mCitiesDatas[j] = mCityStr;
                        } else {
                            mCitiesDatas[j] = "unknown city";
                        }

                        // 循环遍历地区
                        JSONArray areaArray;

                        try {
                            // 判断是否含有第三级区域划分，若没有，则跳出本次循环，重新执行循环遍历城市
                            areaArray = mCityObject.getJSONArray("a");
                        } catch (Exception e) {
                            e.printStackTrace();
                            continue;
                        }

                        // 执行下面过程则说明存在第三级区域
                        mAreaDatas = new String[areaArray.length()];
                        for (int m = 0; m < areaArray.length(); m++) {

                            // 循环遍历第三级区域，并将区域保存在mAreaDatas[]中
                            JSONObject areaObject = areaArray.getJSONObject(m);
                            if (areaObject.has("s")) {
                                mAreaDatas[m] = areaObject.getString("s");
                            } else {
                                mAreaDatas[m] = "unknown area";
                            }
                            Log.d(TAG, mAreaDatas[m]);
                        }

                        // 存储市对应的所有第三级区域
                        mAreaDataMap.put(mCityStr, mAreaDatas);
                    }

                    // 存储省份对应的所有市
                    mCitiesDataMap.put(mProvinceStr, mCitiesDatas);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 从asset目录下读取城市json文件转化为String类型
     *
     * @return String
     */
    private String InitData() {
        StringBuffer sb = new StringBuffer();
        AssetManager mAssetManager = this.getAssets();
        try {
            InputStream is = mAssetManager.open("city.json");
            byte[] data = new byte[is.available()];
            int len = -1;
            while ((len = is.read(data)) != -1) {
                sb.append(new String(data, 0, len, "utf-8"));
            }
            is.close();
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();

        }
        return null;
    }


}
