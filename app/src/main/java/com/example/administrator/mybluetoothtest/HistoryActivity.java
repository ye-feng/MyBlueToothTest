package com.example.administrator.mybluetoothtest;

import android.content.Intent;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HistoryActivity extends BaseActivity {
    private ListView lv_history;
    private Button btn_close;
    private List<Map<String, Object>> list = new ArrayList<>();
    private Map<String, Object> map = null;
    private File[] files = null;
    private SimpleAdapter simpleAdapter;
    private String PATH;
    private long time;

    /**
     * 初始化布局文件
     **/
    @Override
    protected int getLayoutId() {
        return R.layout.activity_history_list;
    }

    /**
     * 初始化控件
     **/
    @Override
    protected void findView() {
        lv_history = findViewById(R.id.history_datas);
        btn_close = findViewById(R.id.btn_close);
    }

    /**
     * 初始化View
     **/
    @Override
    protected void initView() {
//打开指定目录
        PATH = Environment.getExternalStorageDirectory() + "/data";
        File dataDir = new File(PATH);
        if (!dataDir.exists()) {
            dataDir.mkdir();
        }
        files = dataDir.listFiles();
        if (files.length > 0) {
            int datanum = 0;
            map = new HashMap<>();
            for (File file : files) {
                datanum++;
                map = new HashMap<>();
                map.put("datanum", datanum);
                map.put("name", file.getName());
                time = file.lastModified();
                Date date = new Date(time);
                map.put("time",  new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss").format(date) );
                list.add(map);
            }
            simpleAdapter =
                    new SimpleAdapter(this, list, R.layout.history_data_item, new String[]{"datanum", "name", "time"},
                            new int[]{R.id.history_data_item_number, R.id.history_data_item_name, R.id.history_data_item_time}
                    );
            lv_history.setAdapter(simpleAdapter);
        } else {
            showToast("没有历史实验数据");
            finish();
        }
    }

    /**
     * 设置监听事件
     **/
    @Override
    protected void setOnClick() {
        btn_close.setOnClickListener(this);
        lv_history.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String filePath = files[position].getAbsolutePath();
                showToast(filePath);
               Intent intent = new Intent(HistoryActivity.this,HistoryData.class);
               intent.putExtra("filePath",filePath);
               startActivity(intent);
               finish();
                //         startActivity(intent);
            }
        });
    }
    /**
     * 初始化数据
     **/
    @Override
    protected void initData() {
    }
    @Override
    protected void clickEvent(View v) {
        switch (v.getId()) {
            case R.id.btn_close:
//                skipAnotherActivity(this, MainAcitvity.class);
                finish();
            default:
                break;
        }
    }
}
