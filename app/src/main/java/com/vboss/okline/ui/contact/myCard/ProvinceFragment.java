package com.vboss.okline.ui.contact.myCard;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.vboss.okline.R;
import com.vboss.okline.base.BaseActivity;
import com.vboss.okline.base.BaseFragment;
import com.vboss.okline.base.helper.FragmentToolbar;
import com.vboss.okline.ui.home.MainActivity;
import com.vboss.okline.ui.user.Utils;
import com.vboss.okline.utils.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.content.ContentValues.TAG;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author : Lin Zhangbin
 * Email : hedgehog@okline.cn
 * Date : 2017/6/5 21:07
 * Desc :
 */

public class ProvinceFragment extends BaseFragment {
    @BindView(R.id.toolbar_contact_newcard)
    FragmentToolbar toolbar;
    Unbinder unbinder;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.spinner_pro)
    Spinner mProvinceSpinner;
    @BindView(R.id.spinner_city)
    Spinner mCitySpinner;
    @BindView(R.id.spinner_area)
    Spinner mAreaSpinner;
    private MainActivity activity;


    // 判断是否有区
    private boolean hasArea = false;

    // 省份
    private String[] mProvinceDatas;
    // 城市
    private String[] mCitiesDatas;
    // 地区
    private String[] mAreaDatas;

    // 列表选择的省市区
    private String selectedPro = "";
    private String selectedCity = "";
    private String selectedArea = "";

    private ArrayAdapter<String> mProvinceAdapter;
    private ArrayAdapter<String> mCityAdapter;
    private ArrayAdapter<String> mAreaAdapter;

    // 存储省对应的所有市
    private Map<String, String[]> mCitiesDataMap = new HashMap<String, String[]>();
    // 存储市对应的所有区
    private Map<String, String[]> mAreaDataMap = new HashMap<String, String[]>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
        BeginJsonCitisData(InitData());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contact_province_content, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbar.setActionTitle(getResources().getString(R.string.title_edit_address));
        toolbar.setActionMenuVisible(View.GONE);
        toolbar.setActionMenuClickable(false);
        toolbar.setOnNavigationClickListener(new FragmentToolbar.OnNavigationClickListener() {
            @Override
            public void onNavigation(View v) {
                activity.removeSecondFragment();
                TextUtils.showOrHideSoftIM(toolbar, false);
            }
        });

        mProvinceAdapter = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, mProvinceDatas);
        mProvinceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mProvinceSpinner.setAdapter(mProvinceAdapter);

        // 省份选择
        mProvinceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedPro = "";
                selectedPro = (String) parent.getSelectedItem();
                // 根据省份更新城市区域信息
                updateCity(selectedPro);
                Log.d(TAG, "mProvinceSpinner has excuted !" + "selectedPro is " + selectedPro);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // 市选择
        mCitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCity = "";
                selectedCity = (String) parent.getSelectedItem();
                updateArea(selectedCity);
                Log.d(TAG, "mCitySpinner has excuted !" + "selectedCity is " + selectedCity);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // 区选择
        mAreaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedArea = "";
                selectedArea = (String) parent.getSelectedItem();
                tvAddress.setText("已选择: " + selectedPro + selectedCity + selectedArea);
                Log.d(TAG, "mAreaSpinner has excuted !" + "selectedArea is " + selectedArea);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * 根据市 选择对应的区
     *
     * @param city 城市
     */
    private void updateArea(String city) {

        String[] areas = mAreaDataMap.get(city);

        // 存在区
        if (areas != null) {
            // 存在区
            mAreaSpinner.setVisibility(View.VISIBLE);
            mAreaAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, areas);
            mAreaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mAreaSpinner.setAdapter(mAreaAdapter);
            mAreaAdapter.notifyDataSetChanged();
            mAreaSpinner.setSelection(0);
        } else {
            tvAddress.setText("已选择: " + selectedPro + selectedCity);
            mAreaSpinner.setVisibility(View.GONE);
        }

    }

    /**
     * 根据省份更新城市数据
     *
     * @param pro 省份
     */
    private void updateCity(String pro) {

        String[] cities = mCitiesDataMap.get(pro);
        for (int i = 0; i < cities.length; i++) {
            // 存在区
            mCityAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, cities);
            mCityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mCitySpinner.setAdapter(mCityAdapter);
            mCityAdapter.notifyDataSetChanged();
            mCitySpinner.setSelection(0);
        }
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
        AssetManager mAssetManager = activity.getAssets();
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


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
