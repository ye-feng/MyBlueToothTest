package com.example.administrator.mybluetoothtest;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


import lecho.lib.hellocharts.view.LineChartView;


public class TestActivity extends BaseActivity implements View.OnClickListener {
    private static boolean TESTEND = false;
    private static boolean TESTSTART = false;
    private int data_num;
    private Boolean FLAG_START = false;
    private BluetoothSocket mBlueToothSocket = null;
    private BluetoothAdapter mBluetoothAdapter = null;
    private InputStream inputStream = null;
    private boolean blueToothThread = false;
    private boolean blueToothThreadState = true;
    private TestBean mtestBean = new TestBean();
    private List<Float> test_datas = new ArrayList<>();
    private Button btn_save;
    //消息处理队列
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            myLineChart.setPointsValues(test_datas);
            myLineChart.setLinesDatas();
            myLineChart.startLineChartView();
            tv.setText("实时拉伸阻力值："+smsg.toString());   //显示数据
            super.handleMessage(msg);
        }
    };
    private LineChartView test_LineChartView;
    private TextView tv;
    private MyLineChart myLineChart;
    private String jsonString;

    /**
     * 初始化布局文件
     **/
    @Override
    protected int getLayoutId() {
        return R.layout.activity_test;
    }

    /**
     * 初始化控件
     **/
    @Override
    protected void findView() {
        test_LineChartView = findViewById(R.id.test_linechartview);
        Log.d("TAG", "id:" + test_LineChartView.toString());
        tv = findViewById(R.id.tv);
    }

    /**
     * 初始化View
     **/
    @Override
    protected void initView() {
        test_datas.add(0f);
        myLineChart = new MyLineChart();//得到一个MyLineChart实例
        myLineChart.initView(test_LineChartView);
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

    }

    /**
     * 初始化数据
     **/
    @Override
    protected void initData() {

        SharedPreferences mSharedPreferences = this.getSharedPreferences("BULETOOTH_ADDRESS", MODE_PRIVATE);
        String address = mSharedPreferences.getString("connected_address", "FAIL");
        showToast(address);
        Log.d("TAG", "地址：" + address);
        if (!address.equals("FAIL")) {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            BluetoothDevice mBlueToothDevice = mBluetoothAdapter.getRemoteDevice(address.trim());
            // 用服务号得到socket
            try {
                mBlueToothSocket = mBlueToothDevice.createRfcommSocketToServiceRecord(UUID.fromString(BlueToothUtil.MY_UUID));
            } catch (IOException e) {
                showToast("连接失败1！");
            }
            if (mBlueToothSocket.isConnected()) {
                Log.d("TAG", "true");
            } else Log.d("TAG", "flase");
            //连接socket
            try {
                mBlueToothSocket.connect();
                showToast("连接" + mBlueToothDevice.getName() + "成功！");
            } catch (IOException e) {
                try {
                    showToast("连接失败2！");
                    mBlueToothSocket.close();
                    mBlueToothSocket = null;
                } catch (IOException ee) {
                    showToast("连接失败3！");
                }
                return;
            }
            //打开接收线程
            try {
                inputStream = mBlueToothSocket.getInputStream();   //得到蓝牙数据输入流
            } catch (IOException e) {
                Toast.makeText(this, "接收数据失败！", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!blueToothThread) {
                ReadThread.start();
                blueToothThread = true;
            } else {
                blueToothThreadState = true;
            }
        } else {
            showToast("蓝牙未配对，请前往设置界面配对");
        }
    }


    @Override
    protected void clickEvent(View v) {
        switch (v.getId()) {
            case R.id.btn_save:

                break;
            default:
                break;
        }
    }


    private Float smsg;
    private Intent intent;
    private Thread ReadThread = new Thread() {
        @Override
        public void run() {
            int num;
            byte[] buffer = new byte[4];
            blueToothThreadState = true;
            //接收线程
            while (true) {
                try {
                    while (inputStream.available() == 0) {
                        while (!blueToothThreadState) {
                        }
                    }
                    while (true) {
                        num = inputStream.read(buffer);         //读入数据
                        Log.d("TAG", "num:" + num);

                        if (num == 4) {
                            int data = 0;

                            data = buffer[0] * 1000;
                            data = data + buffer[1] * 100;
                            data = data + buffer[2] * 10;
                            data = data + buffer[3];

                            Log.d("TAG", "data:" + (float) (data / 1.25));
                            if (data > 5) {
                                test_datas.add((float) (data / 1.25));
                                smsg = (float)(data/1.25);   //写入接收缓存
                                Log.d("TAG", "s:" + smsg);
                                //   if (inputStream.available() == 0) break;  //短时间没有数据才跳出进行显示
                                //发送显示消息，进行显示刷新
                                handler.sendMessage(handler.obtainMessage());
                                TESTSTART = true;
                            }
                            if ((data < 5) && TESTSTART) {
                                TESTSTART = false;
                                TESTEND = true;
                                test_datas.add(0f);
                                break;
                            }
                        }
                    }
                    if (TESTEND) {
                        TESTEND = false;
                        ChangeData();
                        intent = new Intent(TestActivity.this, TestReport.class);
                        intent.putExtra("TESTBEAN", jsonString);
                        startActivity(intent);
                        finish();
                        break;
                    }


                } catch (IOException e) {
                }
            }
        }
    };

    private void getIntentValue() {
        Intent intent = getIntent();
        mtestBean.setTest_number(Integer.parseInt(intent.getStringExtra("et_sample_number")));
        mtestBean.setSample_name(intent.getStringExtra("et_sample_name"));
        mtestBean.setPerson(intent.getStringExtra("et_person"));
        mtestBean.setEvaluation_methods(intent.getStringExtra("sp_evaluation_methods"));
        mtestBean.setDough_time(Integer.parseInt(intent.getStringExtra("et_dough_time")));
        mtestBean.setGluten(Integer.parseInt(intent.getStringExtra("et_gluten")));
        mtestBean.setWet(Integer.parseInt(intent.getStringExtra("et_wet")));
        mtestBean.setWater(Integer.parseInt(intent.getStringExtra("et_water")));
    }

    private void ChangeData() {
        float Rm = 0;
        for (float f : test_datas)//计算最大拉伸阻力
        {
            if (f > Rm) {
                Rm = f;
            }
        }
        float R50=0;
        if (test_datas.size() > 50) {
            R50 = test_datas.get(50);//面团拉伸50mm时的拉伸阻力
        }
        float E = test_datas.size();//面团延伸性
        float Power = 0;//面团拉伸曲线面积
        for (float f : test_datas) {
            Power = Power + (f / 10);
        }
        float Pull_rate = Rm / E;//计算R/E的比值
        getIntentValue();
        mtestBean.setRm(Rm);
        mtestBean.setR50(R50);
        mtestBean.setE(E);
        mtestBean.setPower(Power);
        mtestBean.setPull_rate(Pull_rate);
        mtestBean.setTest_datas(test_datas);
        jsonString = JSON.toJSONString(mtestBean);
        SharedPreferences mSharedPreferences = this.getSharedPreferences("data_num", MODE_PRIVATE);
        SharedPreferences.Editor edit = mSharedPreferences.edit();
        edit.putInt("num", data_num);
        edit.commit();
        Log.d("TAG", mtestBean.toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}
