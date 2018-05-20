package com.example.administrator.mybluetoothtest;
import android.util.Log;
import android.view.View;
import android.widget.Button;
public class MainAcitvity extends BaseActivity {
    private Button btn_NewTest;
    private Button btn_OldTests;
    private Button btn_Debug;
    private Button btn_Setting;
    private Button btn_About;
    private Button btn_Other;
    /**
     * 初始化布局文件
     **/
    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }
    /**
     * 初始化控件
     **/
    @Override
    protected void findView() {
        btn_NewTest = findViewById(R.id.newtest);
        btn_OldTests = findViewById(R.id.oldtests);
        btn_Debug = findViewById(R.id.debug);
        btn_Setting = findViewById(R.id.setting);
        btn_About = findViewById(R.id.about);
        btn_Other = findViewById(R.id.other);
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
        btn_NewTest.setOnClickListener(this);
        btn_OldTests.setOnClickListener(this);
        btn_Debug.setOnClickListener(this);
        btn_Setting.setOnClickListener(this);
        btn_About.setOnClickListener(this);
        btn_Other.setOnClickListener(this);
    }
    /**
     * 初始化蓝牙
     **/
    @Override
    protected void initData() {

    }
    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    protected void clickEvent(View v) {
        Log.d("TAG", "1");
        switch (v.getId()) {
            case R.id.newtest:
                skipAnotherActivity(this, TestInformationActivity.class);
                break;
            case R.id.oldtests:
                skipAnotherActivity(this, HistoryActivity.class);
                break;
            case R.id.debug:
                showToast("暂未添加此功能");
                break;
            case R.id.setting:
                skipAnotherActivity(this, DeviceListActivity.class);
                break;
            case R.id.about:
                skipAnotherActivity(this, AboutActivity.class);
                break;
            case R.id.other:
                showToast("暂未添加此功能");
                break;
            default:
                break;
        }
    }
}
