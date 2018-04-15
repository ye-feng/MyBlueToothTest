package com.example.administrator.mybluetoothtest;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class DeviceListActivity extends BaseActivity {

    private static Boolean FLAG_REPEAT = false;
    public static String EXTRA_DEVICE_ADDRESS = "设备地址"; // 返回时数据标签
    private BluetoothAdapter mBtAdapter; // 成员域
    private SimpleAdapter mPariedDviceSimpleAdapter;
    private SimpleAdapter mNewDviceSimpleAdapter;
    private String TAG = "TAG";
    private Button scanButton;
    private Button cancleButton;
    private ArrayList<String> paried_advice_name = new ArrayList<>();
    private ArrayList<String> new_advice_name = new ArrayList<>();
    List<Map<String, Object>> paried_advice_listItems = new ArrayList<>();
    List<Map<String, Object>> new_advice_listItems = new ArrayList<>();


    /**
     * 初始化布局文件
     **/
    @Override
    protected int getLayoutId() {
        return R.layout.device_list;
    }

    /**
     * 初始化控件
     **/
    @Override
    protected void findView() {
        scanButton = findViewById(R.id.button_scan);
        cancleButton = findViewById(R.id.button_cancel);
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
        scanButton.setOnClickListener(this);
        cancleButton.setOnClickListener(this);
    }

    /**
     * 初始化数据
     **/
    @Override
    protected void initData() {
        // 得到本地蓝牙句柄
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        // 得到已配对蓝牙设备列表
        Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();
        //添加已配对设备到列表并显示
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                Map<String, Object> listItem = new HashMap<>();
                int device_type = device.getBluetoothClass().getMajorDeviceClass();
                switch (device_type) {
                    case BluetoothClass.Device.Major.PHONE:
                        listItem.put("img", R.mipmap.ic_bluetooth_item_phone);
                        break;
                    case BluetoothClass.Device.Major.COMPUTER:
                        listItem.put("img", R.mipmap.ic_bluetooth_item_pc);
                        break;
                    default:
                        listItem.put("img", R.mipmap.ic_bluetooth_item);
                }
                listItem.put("name", device.getName());
                listItem.put("address", device.getAddress());
                paried_advice_listItems.add(listItem);
            }
        } else {
            String noDevices = "No devices have been paired";
            Map<String, Object> listItem = new HashMap<>();
            listItem.put("img", R.mipmap.ic_bluetooth_item);
            listItem.put("name", noDevices);
            listItem.put("address", noDevices);
            paried_advice_listItems.add(listItem);
        }
        // 注册接收查找到设备action接收器
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mReceiver, filter);
        // 注册查找结束action接收器
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mReceiver, filter);
        /**开始服务和设备查找*/


        mPariedDviceSimpleAdapter = new SimpleAdapter(this, paried_advice_listItems, R.layout.bluetootnadvice_item, new String[]{"img", "name"}, new int[]{R.id.bluetooth_item_image, R.id.bluetooth_item_name});
        mNewDviceSimpleAdapter = new SimpleAdapter(this, new_advice_listItems, R.layout.bluetootnadvice_item, new String[]{"img", "name"}, new int[]{R.id.bluetooth_item_image, R.id.bluetooth_item_name});
        // 设置已配队设备列表
        ListView pairedListView = findViewById(R.id.paired_devices);
        pairedListView.setAdapter(mPariedDviceSimpleAdapter);
        pairedListView.setOnItemClickListener(mDeviceClickListener);
        // 设置新查找设备列表
        ListView newDevicesListView = findViewById(R.id.new_devices);
        newDevicesListView.setAdapter(mNewDviceSimpleAdapter);
        newDevicesListView.setOnItemClickListener(mDeviceClickListener);
    }

    @Override
    protected void clickEvent(View v) {
        switch (v.getId()) {
            case R.id.button_scan:
                if (mBtAdapter.isDiscovering()) {// 关闭再进行的服务查找
                    mBtAdapter.cancelDiscovery();
                }
                mBtAdapter.startDiscovery();
                scanButton.setText("正在搜寻蓝牙设备");
                break;
            case R.id.button_cancel:
                finish();
                break;
            default:
                break;
        }

    }


    private AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // 准备连接设备，关闭服务查找
            mBtAdapter.cancelDiscovery();
            // 得到mac地址
            HashMap<String, String> map = (HashMap<String, String>) parent.getItemAtPosition(position);
            String address = map.get("address");
            SharedPreferences mSharedPreferences = DeviceListActivity.this.getSharedPreferences("BULETOOTH_ADDRESS", Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = mSharedPreferences.edit();
            edit.putString("connected_address", address);
            edit.commit();
            showToast("存储成功：" + address);
            // 设置返回数据
//            Intent intent = new Intent();
//            intent.putExtra(EXTRA_DEVICE_ADDRESS, address);
//            // 设置返回值并结束程序
//            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    };

    // 查找到设备和搜索完成action监听器
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // 查找到设备action
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // 得到蓝牙设备
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // 如果是已配对的则略过，已得到显示，其余的在添加到列表中进行显示
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    for (int i = 0; i < mNewDviceSimpleAdapter.getCount(); i++) {
                        Map<String, Object> listItem = (Map<String, Object>) mNewDviceSimpleAdapter.getItem(i);
                        if (listItem.get("name").toString().equals(device.getName())) {
                            FLAG_REPEAT = true;
                            break;
                        }
                    }
                    if (!FLAG_REPEAT) {
                        Map<String, Object> newlistItem = new HashMap<>();
                        int device_type = device.getBluetoothClass().getMajorDeviceClass();
                        switch (device_type) {
                            case BluetoothClass.Device.Major.PHONE:
                                newlistItem.put("img", R.mipmap.ic_bluetooth_item_phone);
                                break;
                            case BluetoothClass.Device.Major.COMPUTER:
                                newlistItem.put("img", R.mipmap.ic_bluetooth_item_pc);
                                break;
                            default:
                                newlistItem.put("img", R.mipmap.ic_bluetooth_item);
                        }
                        newlistItem.put("name", device.getName());
                        newlistItem.put("address", device.getAddress());
                        new_advice_listItems.add(newlistItem);
                        mNewDviceSimpleAdapter.notifyDataSetChanged();
                        Log.d(TAG, device.getName());
                    } else {
                        FLAG_REPEAT = false;
                    }

                }
                // 搜索完成action
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                if (new_advice_name.size() == 0) {
                    String noDevices = "没有找到新设备";
                    new_advice_name.add(noDevices);
                }
                scanButton.setText("搜寻结束，重新搜索");
            }


        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // 关闭服务查找
        if (mBtAdapter != null) {
            mBtAdapter.cancelDiscovery();
        }

        // 注销action接收器
        this.unregisterReceiver(mReceiver);
    }


}
