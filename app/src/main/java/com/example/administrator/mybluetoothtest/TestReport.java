package com.example.administrator.mybluetoothtest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lecho.lib.hellocharts.view.LineChartView;
public class TestReport extends BaseActivity {
    private String jsonString;
    private Button btn_save;
    private Button btn_return;
    private JSONObject jsonObject;
    private TextView tv_Rm;
    private TextView tv_R50;
    private TextView tv_E;
    private TextView tv_Power;
    private TextView tv_Pull_rate;
    private TextView tv_sample_number;
    private TextView tv_sample_name;
    private TextView tv_person;
    private TextView tv_evaluation_methods;
    private TextView tv_dough_time;
    private TextView tv_gluten;
    private TextView tv_wet;
    private TextView tv_water;
    private JSONArray json_test_datas;
    private List<Float> test_datas=new ArrayList<>();
    private LineChartView test_report_linechartview;
    private MyLineChart myLineChart;
    /**
     * 初始化布局文件
     **/
    @Override
    protected int getLayoutId() {
        return R.layout.activity_testreport;
    }
    /**
     * 初始化控件
     **/
    @Override
    protected void findView() {
        btn_save = findViewById(R.id.btn_save);
        btn_return = findViewById(R.id.btn_return);
        tv_Rm = findViewById(R.id.tv_Rm);
        tv_R50 = findViewById(R.id.tv_R50);
        tv_E = findViewById(R.id.tv_E);
        tv_Power = findViewById(R.id.tv_Power);
        tv_Pull_rate = findViewById(R.id.tv_Pull_rate);
        tv_sample_number = findViewById(R.id.tv_sample_number);
        tv_sample_name = findViewById(R.id.tv_sample_name);
        tv_person = findViewById(R.id.tv_person);
        tv_evaluation_methods = findViewById(R.id.tv_evaluation_methods);
        tv_dough_time = findViewById(R.id.tv_dough_time);
        tv_gluten = findViewById(R.id.tv_gluten);
        tv_wet = findViewById(R.id.tv_wet);
        tv_water = findViewById(R.id.tv_water);
        test_report_linechartview = findViewById(R.id.test_report_linechartview);
    }
    /**
     * 初始化View
     **/
    @Override
    protected void initView() {
        Intent intent = getIntent();
        jsonString = intent.getStringExtra("TESTBEAN");
        jsonObject = (JSONObject) JSON.parse(jsonString);
        json_test_datas= jsonObject.getJSONArray("test_datas");
        for(int i=1;i<json_test_datas.size();i++)
        {
            Float f=json_test_datas.getFloat(i);
            test_datas.add(f);
        }
        Log.d("TAG","test_datas"+test_datas.toString());
        myLineChart = new MyLineChart();//得到一个MyLineChart实例
        myLineChart.initView(test_report_linechartview);
        myLineChart.setAxis();          //设置坐标轴
        myLineChart.startLineChartView();//开始显示图标
        myLineChart.resetViewport();
        myLineChart.setPointsValues(test_datas);
        myLineChart.setLinesDatas();
        myLineChart.startLineChartView();
    }

    /**
     * 设置监听事件
     **/
    @Override
    protected void setOnClick() {
        btn_save.setOnClickListener(this);
        btn_return.setOnClickListener(this);
    }
    /**
     * 初始化数据
     **/
    @Override
    protected void initData() {
        Log.d("JSON", jsonObject.toJSONString());
        tv_Rm.setText(jsonObject.getString("rm"));
        tv_R50.setText(jsonObject.getString("r50"));
        tv_E.setText(jsonObject.getString("e"));
        tv_Power.setText(jsonObject.getString("power"));
        tv_Pull_rate.setText(jsonObject.getString("pull_rate"));
        tv_sample_number.setText(jsonObject.getString("test_number"));
        tv_sample_name.setText(jsonObject.getString("sample_name"));
        tv_person.setText(jsonObject.getString("person"));
        tv_evaluation_methods.setText(jsonObject.getString("evaluation_methods"));
        tv_dough_time.setText(jsonObject.getString("dough_time"));
        tv_gluten.setText(jsonObject.getString("gluten"));
        tv_wet.setText(jsonObject.getString("wet"));
        tv_water.setText(jsonObject.getString("water"));
        Log.d("TAG", jsonString);
    }
    @Override
    protected void clickEvent(View v) {
        switch (v.getId()) {
            case R.id.btn_save:
                Save();
                break;
            case R.id.btn_return:
                skipAnotherActivity(this, MainAcitvity.class);
                finish();
                break;
            default:
                break;
        }
    }
    //保存功能实现
    private void Save() {
        //显示对话框输入文件名
        LayoutInflater factory = LayoutInflater.from(this);  //图层模板生成器句柄
        final View DialogView = factory.inflate(R.layout.sname, null);  //用sname.xml模板生成视图模板
        new AlertDialog.Builder(this)
                .setTitle("文件名")
                .setView(DialogView)   //设置视图模板
                .setPositiveButton("确定",
                        new DialogInterface.OnClickListener() //确定按键响应函数
                        {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                EditText text1 = (EditText) DialogView.findViewById(R.id.sname);  //得到文件名输入框句柄
                                String filename = text1.getText().toString();  //得到文件名
                                try {
                                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {  //如果SD卡已准备好
                                        filename = filename + ".txt";   //在文件名末尾加上.txt
                                        File sdCardDir = Environment.getExternalStorageDirectory();  //得到SD卡根目录
                                        File BuildDir = new File(sdCardDir, "/data");   //打开data目录，如不存在则生成
                                        if (BuildDir.exists() == false) BuildDir.mkdirs();
                                        File saveFile = new File(BuildDir, filename);  //新建文件句柄，如已存在仍新建文档
                                        FileOutputStream stream = new FileOutputStream(saveFile);  //打开文件输入流
                                        stream.write(jsonString.getBytes());
                                        stream.close();
                                        showToast("存储成功");
                                        skipAnotherActivity(TestReport.this, MainAcitvity.class);
                                        finish();

                                    } else {
                                        showToast("没有存储卡");
                                    }
                                } catch (IOException e) {
                                    return;
                                }
                            }
                        })
                .setNegativeButton("取消",   //取消按键响应函数,直接退出对话框不做任何处理
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).show();  //显示对话框
    }
}
