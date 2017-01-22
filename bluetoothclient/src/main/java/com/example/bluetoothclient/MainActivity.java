package com.example.bluetoothclient;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static com.example.bluetoothclient.R.id.tv_msg;

public class MainActivity extends AppCompatActivity {

    @butterknife.BindView(R.id.search_btn)
    Button mSearchBtn;
    @butterknife.BindView(R.id.send_btn)
    Button mSendBtn;
    @butterknife.BindView(tv_msg)
    TextView mTvMsg;

    private StringBuilder mSb;
    private BluetoothAdapter mBTadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        butterknife.ButterKnife.bind(this);
        checkBT();
    }

    private void checkBT(Context context) {
        mBTadapter = BluetoothAdapter.getDefaultAdapter();
        if(mBTadapter != null){
            if(!mBTadapter.isEnabled()){
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                // 设置蓝牙可见性, 最多300秒
                intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
                context.startActivity(intent);
            }else {
                System.out.println("本地设备驱动异常");
            }
        }
    }

    @butterknife.OnClick({R.id.search_btn, R.id.send_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_btn:
                break;
            case R.id.send_btn:
                break;
        }
    }

    /**
     * UI文本输出
     *
     * @param msg
     */
    public void show(String msg) {
        mSb.append(msg + "\n");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTvMsg.setText(mSb.toString());
            }
        });
    }
}
