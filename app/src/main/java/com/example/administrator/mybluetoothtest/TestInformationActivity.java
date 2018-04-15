package com.example.administrator.mybluetoothtest;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class TestInformationActivity extends BaseActivity {
    private Button btn_Start;
    private Button btn_Return;


    /**
     * 初始化布局文件
     **/
    @Override
    protected int getLayoutId() {
        return R.layout.activity_testinformation;
    }

    /**
     * 初始化控件
     **/
    @Override
    protected void findView() {
        btn_Start = findViewById(R.id.btn_start);
        btn_Return = findViewById(R.id.btn_return);
    }

    /**
     * 初始化View
     **/
    @Override
    protected void initView() {

    }

    /**
     * 设置监听事件
     **/
    @Override
    protected void setOnClick() {
        btn_Start.setOnClickListener(this);
        btn_Return.setOnClickListener(this);
    }

    /**
     * 初始化数据
     **/
    @Override
    protected void initData() {

    }

    @Override
    protected void clickEvent(View v) {
        switch (v.getId()) {
            case R.id.btn_start:
                ArrayList<String> information = new ArrayList<>();
                Intent intent = new Intent(TestInformationActivity.this,TestActivity.class);
   //             intent.putStringArrayListExtra("testinformation", information);
                startActivity(intent);
                break;
            case R.id.btn_return:
                skipAnotherActivity(this, MainAcitvity.class);
                break;
            default:
                break;
        }
    }
}
