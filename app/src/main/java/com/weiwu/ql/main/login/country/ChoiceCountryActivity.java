package com.weiwu.ql.main.login.country;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.tencent.common.Constant;
import com.tencent.common.dialog.DialogMaker;
import com.tencent.common.http.BaseBean;
import com.tencent.common.http.HttpCallBack;
import com.tencent.common.http.YHttp;
import com.tencent.qcloud.tim.uikit.component.TitleBarLayout;
import com.tencent.qcloud.tim.uikit.utils.TextComparator;
import com.weiwu.ql.R;
import com.weiwu.ql.base.BaseActivity;
import com.weiwu.ql.data.repositories.MineRepository;
import com.weiwu.ql.main.mine.MineContract;
import com.weiwu.ql.utils.ToastHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 选择国家和地区
 */
public class ChoiceCountryActivity extends BaseActivity implements MineContract.ICountryView {

    private TitleBarLayout mTitleBar;

    private ListView mListChoiceCountry;
    private ImageView mIvChoiceCountryHitLetter;
    private TextView mTvChoiceCountryHitLetter;
    private LetterIndexView mLivChoiceCountryIndex;

    private ChoiceCountryAdapter choiceCountryAdapter;

    private ArrayList<CountryEntity> listDatas = new ArrayList<>();
    private ArrayList<CountryEntity> currentDatas = new ArrayList<>();//筛选出来的数据
    private MineContract.ICountryPresenter mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_country);
        setPresenter(new CountryPresenter(MineRepository.getInstance()));
        initView();
        getData();
    }

    private void initView() {
        mTitleBar = findViewById(R.id.title);
        mTitleBar.setTitle(getResources().getString(R.string.choice_country), TitleBarLayout.POSITION.MIDDLE);
        mTitleBar.setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTitleBar.getRightIcon().setVisibility(View.GONE);

        mListChoiceCountry = (ListView) findViewById(R.id.list_choice_country);
        mIvChoiceCountryHitLetter = (ImageView) findViewById(R.id.iv_choice_country_hit_letter);
        mTvChoiceCountryHitLetter = (TextView) findViewById(R.id.tv_choice_country_hit_letter);
        mLivChoiceCountryIndex = (LetterIndexView) findViewById(R.id.liv_choice_country_index);

        choiceCountryAdapter = new ChoiceCountryAdapter(this, listDatas);
        mListChoiceCountry.setAdapter(choiceCountryAdapter);
    }

    //数据请求
    private void getData() {
        mPresenter.getCountryList();
//        DialogMaker.showProgressDialog(this, getString(R.string.empty), true);
        /*Map  httpRequestParams = new HashMap();
        httpRequestParams.put("type", "country");
        YHttp.obtain().post(Constant.URL_CONUNTRY_CODE, null, new HttpCallBack<BaseBean>() {

            @Override
            public void onSuccess(BaseBean bean) {
                if (bean.getData() == null) {
                    return;
                }
                List<CountryEntity> countryEntities = JSON.parseArray(bean.getData().toString(), CountryEntity.class);
                readLocalPhoneDatas(countryEntities);
            }

            @Override
            public void onFailed(String error) {

            }
        });*/
    }

    /**
     * 读取手机本地所有数据
     *
     * @param localDatas
     */
    private void readLocalPhoneDatas(List<CountryEntity> localDatas) {
        try {
            //1，首先排序，#排第一  其他字母按照阿拉伯字母排序
            Collections.sort(localDatas, new Comparator<CountryEntity>() {
                @Override
                public int compare(CountryEntity o1, CountryEntity o2) {
                    String leading1 = TextUtils.isEmpty(TextComparator.getLeadingUp(o1.getCountry())) ? "#" : TextComparator.getLeadingUp(o1.getCountry());
                    String leading2 = TextUtils.isEmpty(TextComparator.getLeadingUp(o2.getCountry())) ? "#" : TextComparator.getLeadingUp(o2.getCountry());
                    if ("#".equals(leading1) || "#".equals(leading2)) {
                        if (leading1.equals(leading2)) {
                            return 0;
                        } else if ("#".equals(leading1) || !"#".equals(leading2)) {
                            return -1;
                        } else {
                            return 1;
                        }
                    } else {
                        return leading1.compareTo(leading2);
                    }
                }
            });
            //一开始加载所有的
            currentDatas.addAll(localDatas);
        } catch (Exception e) {
            ToastHelper.showToast(getApplicationContext(), "获取联系人失败，请打开相应权限控制！");
        }
        updateData();
    }

    /**
     * 更新adapter
     */
    private void updateData() {
        listDatas.clear();
        //2，加入对应的字母集合中
        Map<String, ArrayList<CountryEntity>> map = new LinkedHashMap<>();
        for (int i = 0; i < currentDatas.size(); i++) {
            CountryEntity bean = currentDatas.get(i);
            String leading = TextUtils.isEmpty(TextComparator.getLeadingUp(bean.getCountry())) ? "#" : TextComparator.getLeadingUp(bean.getCountry());
            ArrayList<CountryEntity> list = map.get(leading);
            if (list == null) {
                list = new ArrayList<>();
            }
            list.add(bean);
            map.put(leading, list);
        }


        //3，找到对应分组在列表中的位置
        Map<String, Integer> indexMap = new LinkedHashMap<>();
        int index = 0;

        Set<Map.Entry<String, ArrayList<CountryEntity>>> entries = map.entrySet();
        Iterator<Map.Entry<String, ArrayList<CountryEntity>>> iterator = entries.iterator();

        while (iterator.hasNext()) {
            Map.Entry<String, ArrayList<CountryEntity>> next = iterator.next();
            String key = next.getKey();

            //添加标题  不需要name和phone
            CountryEntity bean = new CountryEntity();
            bean.setTitle(key);
            bean.setTitle(true);
            listDatas.add(bean);

            //添加分组数据
            ArrayList<CountryEntity> value = next.getValue();
            for (CountryEntity CountryEntity : value) {
                listDatas.add(CountryEntity);
            }

            //存放分组大类标题的位置。
            indexMap.put(key, index);
            //因为包含标题类，所以位置加1
            index = index + value.size() + 1;
//            System.out.println("key::::"+key+"                "+"value:::::::::"+value+"                "+"value.size:::::::::::::::::"+value.size());
        }

        new LivIndex(mListChoiceCountry, mLivChoiceCountryIndex, mTvChoiceCountryHitLetter, mIvChoiceCountryHitLetter, indexMap);

        choiceCountryAdapter.notifyDataSetChanged();
    }

    @Override
    public void setPresenter(MineContract.ICountryPresenter presenter) {
        mPresenter = presenter;
        mPresenter.attachView(this);
    }

    @Override
    public Activity getActivityObject() {
        return this;
    }

    @Override
    public void countryReceive(CountryData data) {
        if (data.getData() == null) {
            return;
        }
//        List<CountryEntity> countryEntities = JSON.parseArray(bean.getData().toString(), CountryEntity.class);
        readLocalPhoneDatas(data.getData());
    }
}
