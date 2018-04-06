package com.example.administrator.mybluetoothtest;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private EditText edit0;
    private ScrollView sv;
    private TextView dis;
    private BluetoothAdapter mBluetoothAdapter;
    private final int REQUEST_ENABLE_BT = 1;
    private BluetoothSocket _socket;
    private final int REQUEST_CONNECT_DEVICE = 1;
    private BluetoothDevice _device = null;
    private InputStream is;
    private boolean bThread = false;
    private boolean bRun = true;

    private String fmsg="";
    private String smsg="";
    private final static String MY_UUID = "00001101-0000-1000-8000-00805F9B34FB";
    private String TAG = "TAG";
    private String filename = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        edit0 = findViewById(R.id.Edit0);   //得到输入框句柄
        sv = findViewById(R.id.ScrollView01);  //得到翻页句柄
        dis = findViewById(R.id.in);      //得到数据显示句柄

        //如果打开本地蓝牙设备不成功，提示信息，结束程序
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "无法打开手机蓝牙，请确认手机是否有蓝牙功能！", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        //如果蓝牙未开启，则提示开启蓝牙
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBtIntent);
        }
    }

    //注册发送按键点击事件
    public void onSendButtonClicked(View view) {
        int i;
        int n = 0;
        OutputStream os = null;   //蓝牙连接输出流
        try {
            os = _socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] bos = edit0.getText().toString().getBytes();
        for (i = 0; i < bos.length; i++) {
            if (bos[i] == 0x0a) n++;
        }
        byte[] bos_new = new byte[bos.length + n];
        n = 0;
        for (i = 0; i < bos.length; i++) { //手机中换行为0a,将其改为0d 0a后再发送
            if (bos[i] == 0x0a) {
                bos_new[n] = 0x0d;
                n++;
                bos_new[n] = 0x0a;
            } else {
                bos_new[n] = bos[i];
            }
            n++;
        }
        try {
            os.write(bos_new);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE:     //连接结果，由DeviceListActivity设置返回
                // 响应返回结果
                if (resultCode == Activity.RESULT_OK) {   //连接成功，由DeviceListActivity设置返回
                    // MAC地址，由DeviceListActivity设置返回
                    String address = data.getExtras()
                            .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                    // 得到蓝牙设备句柄
                    _device = mBluetoothAdapter.getRemoteDevice(address);

                    // 用服务号得到socket
                    try {
                        _socket = _device.createRfcommSocketToServiceRecord(UUID.fromString(MY_UUID));
                    } catch (IOException e) {
                        Toast.makeText(this, "连接失败！", Toast.LENGTH_SHORT).show();
                    }
                    //连接socket
                    Button btn = findViewById(R.id.Button03);
                    try {
                        _socket.connect();
                        Toast.makeText(this, "连接" + _device.getName() + "成功！", Toast.LENGTH_SHORT).show();
                        btn.setText("断开");
                    } catch (IOException e) {
                        try {
                            Toast.makeText(this, "连接失败！", Toast.LENGTH_SHORT).show();
                            _socket.close();
                            _socket = null;
                        } catch (IOException ee) {
                            Toast.makeText(this, "连接失败！", Toast.LENGTH_SHORT).show();
                        }

                        return;
                    }

                    //打开接收线程
                    try {
                        is = _socket.getInputStream();   //得到蓝牙数据输入流
                    } catch (IOException e) {
                        Toast.makeText(this, "接收数据失败！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (bThread == false) {
                        ReadThread.start();
                        bThread = true;
                    } else {
                        bRun = true;
                    }
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //消息处理队列
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            dis.setText(smsg);   //显示数据
            sv.scrollTo(0, dis.getMeasuredHeight()); //跳至数据最后一页
            super.handleMessage(msg);
        }
    };
    private Thread ReadThread = new Thread() {
        @Override
        public void run() {
            int num = 0;
            byte[] buffer = new byte[1024];
            byte[] buffer_new = new byte[1024];
            int i = 0;
            int n = 0;
            bRun = true;
            //接收线程
            while (true) {
                try {
                    while (is.available() == 0) {
                        while (bRun == false) {
                        }
                    }
                    while (true) {
                        num = is.read(buffer);         //读入数据
                        n = 0;
                        String s0 = new String(buffer, 0, num);
                        fmsg += s0;    //保存收到数据
                        for (i = 0; i < num; i++) {
                            //0d------回车符号－－－－－－"/r"0a------换行符号－－－－－－"/n"
                            if ((buffer[i] == 0x0d) && (buffer[i + 1] == 0x0a)) {
                                buffer_new[n] = 0x0a;
                                i++;
                            } else {
                                buffer_new[n] = buffer[i];
                            }
                            n++;
                        }
                        String s = new String(buffer_new, 0, n);
                        smsg += s;   //写入接收缓存
                        if (is.available() == 0) break;  //短时间没有数据才跳出进行显示
                    }
                    //发送显示消息，进行显示刷新
                    handler.sendMessage(handler.obtainMessage());

                } catch (IOException e) {
                }
            }
        }
    };


    public void onConnectButtonClicked(View view) {
        if (!mBluetoothAdapter.isEnabled()) {  //如果蓝牙服务不可用则提示
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBtIntent);
            return;
        }
        //如未连接设备则打开DeviceListActivity进行设备搜索
        Button btn = findViewById(R.id.Button03);
        if (_socket == null) {
            Log.d(TAG, "10");
            Intent serverIntent = new Intent(this, DeviceListActivity.class); //跳转程序设置
            startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);  //设置返回宏定义
            Log.d(TAG, "11");
        } else {
            //关闭连接socket
            try {

                is.close();
                _socket.close();
                _socket = null;
                bRun = false;
                btn.setText("连接");
            } catch (IOException e) {
                Log.d(TAG,"关闭连接socket失败");
            }
        }
    }

    public void onSaveButtonClicked(View view) {
//显示对话框输入文件名
        LayoutInflater factory = LayoutInflater.from(MainActivity.this);  //图层模板生成器句柄
        final View DialogView = factory.inflate(R.layout.sname, null);  //用sname.xml模板生成视图模板
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("文件名")
                .setView(DialogView)   //设置视图模板
                .setPositiveButton("确定",
                        new DialogInterface.OnClickListener() //确定按键响应函数
                        {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                EditText text1 = DialogView.findViewById(R.id.sname);  //得到文件名输入框句柄
                                filename = text1.getText().toString();  //得到文件名

                                try {
                                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {  //如果SD卡已准备好

                                        filename = filename + ".txt";   //在文件名末尾加上.txt
                                        File sdCardDir = Environment.getExternalStorageDirectory();  //得到SD卡根目录
                                        File BuildDir = new File(sdCardDir, "/data");   //打开data目录，如不存在则生成
                                        if (!BuildDir.exists()) BuildDir.mkdirs();
                                        File saveFile = new File(BuildDir, filename);  //新建文件句柄，如已存在仍新建文档
                                        FileOutputStream stream = new FileOutputStream(saveFile);  //打开文件输入流
                                        stream.write(fmsg.getBytes());
                                        stream.close();
                                        Toast.makeText(MainActivity.this, "存储成功！", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(MainActivity.this, "没有存储卡！", Toast.LENGTH_LONG).show();
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

    public void onClearButtonClicked(View view) {
        smsg = "";
        fmsg = "";
        dis.setText(smsg);
        return;
    }

    public void onQuitButtonClicked(View view) {
        finish();
    }

    protected void onDestroy() {
        if (_socket != null)  //关闭连接socket
            try {
                _socket.close();
            } catch (IOException e) {
                Log.e(TAG, "onDestroy: socket关闭失败");
            }
        super.onDestroy();
    }
}
