package com.weiwu.ql.main.contact.group;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.weiwu.ql.R;
import com.weiwu.ql.base.BaseActivity;


public class GroupInfoActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_info_activity);
        GroupInfoFragment fragment = new GroupInfoFragment();
        fragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().replace(R.id.group_manager_base, fragment).commitAllowingStateLoss();
    }

    @Override
    public void finish() {
        super.finish();
//        setResult(1001);
    }
}
