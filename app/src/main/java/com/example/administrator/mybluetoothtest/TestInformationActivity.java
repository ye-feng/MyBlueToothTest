package com.example.administrator.mybluetoothtest;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class TestInformationActivity extends BaseActivity {
    private Button btn_Start;
    private Button btn_Return;
    private EditText et_sample_number;
    private EditText et_sample_name;
    private EditText et_person;
    private Spinner sp_evaluation_methods;
    private EditText et_dough_time;
    private EditText et_gluten;
    private EditText et_wet;
    private EditText et_water;
    private Intent intent;

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
        btn_Return = findViewById(R.id.btn_cancle);
        et_sample_number = findViewById(R.id.et_sample_number);
        et_sample_name = findViewById(R.id.et_sample_name);
        et_person = findViewById(R.id.et_person);
        sp_evaluation_methods = findViewById(R.id.sp_evalution_methods);
        et_dough_time = findViewById(R.id.et_time);
        et_gluten = findViewById(R.id.et_gluten);
        et_wet = findViewById(R.id.et_wet);
        et_water = findViewById(R.id.et_water);
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
        sp_evaluation_methods.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] methods = getResources().getStringArray(R.array.methods);
                intent.putExtra("sp_evaluation_methods", methods[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                String[] methods = getResources().getStringArray(R.array.methods);
                intent.putExtra("sp_evaluation_methods", methods[0]);
            }
        });
    }

    /**
     * 初始化数据
     **/
    @Override
    protected void initData() {
        intent = new Intent(TestInformationActivity.this, TestActivity.class);
        et_sample_number.setText("0");
        et_water.setText("0");
        et_wet.setText("0");
        et_gluten.setText("0");
        et_dough_time.setText("0");
    }

    @Override
    protected void clickEvent(View v) {
        switch (v.getId()) {
            case R.id.btn_start:
                intent.putExtra("et_sample_name", et_sample_name.getText().toString());
                Log.d("TAG", et_sample_name.getText().toString());
                intent.putExtra("et_person", et_person.getText().toString());
                intent.putExtra("et_sample_number", et_sample_number.getText().toString());
                intent.putExtra("et_dough_time", et_dough_time.getText().toString());
                intent.putExtra("et_gluten", et_gluten.getText().toString());
                intent.putExtra("et_wet", et_wet.getText().toString());
                intent.putExtra("et_water", et_water.getText().toString());
                startActivity(intent);
                finish();
                break;
            case R.id.btn_cancle:
                skipAnotherActivity(this, MainAcitvity.class);
                finish();
                break;
            default:
                break;
        }
    }
}
