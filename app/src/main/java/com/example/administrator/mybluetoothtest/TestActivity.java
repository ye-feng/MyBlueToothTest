package com.example.administrator.mybluetoothtest;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


import lecho.lib.hellocharts.view.LineChartView;


public class TestActivity extends BaseActivity implements View.OnClickListener {
    private Boolean FLAG_START = false;
    private BluetoothSocket mBlueToothSocket;
    private BluetoothAdapter mBluetoothAdapter;
    private InputStream inputStream;
    private boolean blueToothThread = false;
    private boolean blueToothThreadState = true;
    private TestBean mtestBean;
    private List<Integer> test_datas = new ArrayList<>();
    //消息处理队列
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            myLineChart.setPointsValues(test_datas);
            myLineChart.setLinesDatas();
            myLineChart.startLineChartView();
            tv.setText(smsg);   //显示数据
            super.handleMessage(msg);
        }
    };
    private LineChartView test_LineChartView;
    private TextView tv;
    private MyLineChart myLineChart;
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
        Log.d("TAG","id:"+test_LineChartView.toString());
        tv = findViewById(R.id.tv);
    }

    /**
     * 初始化View
     **/
    @Override
    protected void initView() {
        myLineChart=new MyLineChart();//得到一个MyLineChart实例
        myLineChart.initView(test_LineChartView);
        myLineChart.setAxis();          //设置坐标轴
        myLineChart.startLineChartView();//开始显示图标
        myLineChart.resetViewport();
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

    }


    private String fmsg;
    private String smsg;
    private Thread ReadThread = new Thread() {
        @Override
        public void run() {
            int num;
            byte[] buffer = new byte[2];
            Integer[] buffer_new = new Integer[1024];
            int i = 0;
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

                        int data = DataUtil.byteArrToShort(buffer);
                        Log.d("TAG", "data:" + data);
                        if (num > 0) {
//                            if (FLAG_START) {
                               test_datas.add(data);
                            smsg += data;   //写入接收缓存
                            Log.d("TAG", "s:" + smsg);
                            if (inputStream.available() == 0) break;  //短时间没有数据才跳出进行显示
                        }
                    }
                    //发送显示消息，进行显示刷新
                    handler.sendMessage(handler.obtainMessage());

                } catch (IOException e) {
                }
            }
        }
    };
}
