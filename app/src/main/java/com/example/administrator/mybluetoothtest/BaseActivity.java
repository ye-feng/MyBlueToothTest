package com.example.administrator.mybluetoothtest;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;


public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        findView();
        initView();
        setOnClick();
        initData();
    }
    /**
     * 初始化布局文件
     **/
    protected abstract int getLayoutId();

    /**
     * 初始化控件
     **/
    protected abstract void findView();
    /**
     * 初始化View
     **/
    protected abstract void initView();
    /**
     * 设置监听事件
     **/
    protected abstract void setOnClick();
    /**
     * 初始化数据
     **/
    protected abstract void initData();

    /**
     * 跳转Activity
     * skip Another Activity
     */
    public static void skipAnotherActivity(Activity activity,
                                           Class<? extends Activity> cls) {
        Intent intent = new Intent(activity, cls);
        activity.startActivity(intent);
    }
    public void showToast(String text) {
        if (!TextUtils.isEmpty(text)) {
            Toast.makeText(this, text,Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        clickEvent(v);
    }

    protected abstract void clickEvent(View v);
}
