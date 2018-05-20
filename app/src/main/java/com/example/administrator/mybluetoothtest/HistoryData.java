package com.example.administrator.mybluetoothtest;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.view.LineChartView;

public class HistoryData extends BaseActivity {
    private Button history_data_btn_return;
    private TextView history_data_Rm;
    private TextView history_data_R50;
    private TextView history_data_E;
    private TextView history_data_Power;
    private TextView history_data_Pull_rate;
    private TextView history_data_sample_number;
    private TextView history_data_sample_name;
    private TextView history_data_person;
    private TextView history_data_evaluation_methods;
    private TextView history_data_dough_time;
    private TextView history_data_gluten;
    private TextView history_data_wet;
    private TextView history_data_water;
    private LineChartView history_data_linechartview;
    private String filePath;
    private JSONObject jsonObject;
    private String readLine;
    private JSONArray json_test_datas;
    private List<Float> test_datas = new ArrayList<>();
    private MyLineChart myLineChart;

    /**
     * 初始化布局文件
     **/
    @Override
    protected int getLayoutId() {
        return R.layout.activity_history_data;
    }

    /**
     * 初始化控件
     **/
    @Override
    protected void findView() {
        history_data_btn_return = findViewById(R.id.history_data_btn_return);
        history_data_Rm = findViewById(R.id.history_data_Rm);
        history_data_R50 = findViewById(R.id.history_data_R50);
        history_data_E = findViewById(R.id.history_data_E);
        history_data_Power = findViewById(R.id.history_data_Power);
        history_data_Pull_rate = findViewById(R.id.history_data_Pull_rate);
        history_data_sample_number = findViewById(R.id.history_data_sample_number);
        history_data_sample_name = findViewById(R.id.history_data_sample_name);
        history_data_person = findViewById(R.id.history_data_person);
        history_data_evaluation_methods = findViewById(R.id.history_data_evaluation_methods);
        history_data_dough_time = findViewById(R.id.history_data_dough_time);
        history_data_gluten = findViewById(R.id.history_data_gluten);
        history_data_wet = findViewById(R.id.history_data_wet);
        history_data_water = findViewById(R.id.history_data_water);
        history_data_linechartview = findViewById(R.id.history_data_linechartview);
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
        history_data_btn_return.setOnClickListener(this);
    }

    /**
     * 初始化数据
     **/
    @Override
    protected void initData() {
        Intent intent = getIntent();
        filePath = intent.getStringExtra("filePath");
        File file = new File(filePath);
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(fileInputStream));
            try {
                readLine = br.readLine();
                Log.d("TAG", readLine);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        jsonObject = (JSONObject) JSON.parse(readLine);
        json_test_datas = jsonObject.getJSONArray("test_datas");
        for (int i = 1; i < json_test_datas.size(); i++) {
            Float f = json_test_datas.getFloat(i);
            test_datas.add(f);
        }
        Log.d("TAG", "test_datas" + test_datas.toString());
        myLineChart = new MyLineChart();//得到一个MyLineChart实例
        myLineChart.initView(history_data_linechartview);
        myLineChart.setAxis();          //设置坐标轴
        myLineChart.startLineChartView();//开始显示图标
        myLineChart.resetViewport();
        myLineChart.setPointsValues(test_datas);
        myLineChart.setLinesDatas();
        myLineChart.startLineChartView();
        history_data_Rm.setText(jsonObject.getString("rm"));
        history_data_R50.setText(jsonObject.getString("r50"));
        history_data_E.setText(jsonObject.getString("e"));
        history_data_Power.setText(jsonObject.getString("power"));
        history_data_Pull_rate.setText(jsonObject.getString("pull_rate"));
        history_data_sample_number.setText(jsonObject.getString("test_number"));
        history_data_sample_name.setText(jsonObject.getString("sample_name"));
        history_data_person.setText(jsonObject.getString("person"));
        history_data_evaluation_methods.setText(jsonObject.getString("evaluation_methods"));
        history_data_dough_time.setText(jsonObject.getString("dough_time"));
        history_data_gluten.setText(jsonObject.getString("gluten"));
        history_data_wet.setText(jsonObject.getString("wet"));
        history_data_water.setText(jsonObject.getString("water"));
    }

    @Override
    protected void clickEvent(View v) {
        switch (v.getId()) {
            case R.id.history_data_btn_return:
                skipAnotherActivity(this, HistoryActivity.class);
                finish();
            default:
                break;
        }
    }
}
