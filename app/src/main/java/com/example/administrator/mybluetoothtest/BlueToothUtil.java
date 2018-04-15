package com.example.administrator.mybluetoothtest;


import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class BlueToothUtil {

    public static final String MY_UUID ="00001101-0000-1000-8000-00805F9B34FB";
    public Boolean isOpenBlueTooth(Context context) {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(context, "无法打开手机蓝牙，请确认手机是否有蓝牙功能！", Toast.LENGTH_LONG).show();
            return false;
        }else {
            //如果蓝牙未开启，则提示开启蓝牙
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                context.startActivity(enableBtIntent);
                return false;
            }
        }
        return true;
    }
//    public Boolean initBlueTooth(){
//
//    }
}
