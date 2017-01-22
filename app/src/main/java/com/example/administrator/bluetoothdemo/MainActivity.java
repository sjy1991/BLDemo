package com.example.administrator.bluetoothdemo;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity{


    @BindView(R.id.start_btn)
    Button mStartBtn;
    @BindView(R.id.msg_tv)
    TextView mMsgTv;

    StringBuilder mSb;
    private BluetoothAdapter mBTAdapter;
    private BluetoothSocket mBTSocket;
    private ReadThread mReadThread;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mSb = new StringBuilder();
        // 拿到本地蓝牙设备
        mBTAdapter = BluetoothAdapter.getDefaultAdapter();


    }

    /**
     * UI文本输出
     * @param msg
     */
    public void showMsg(final String msg){
        mSb.append(msg + "\n");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mMsgTv.setText(mSb.toString());
            }
        });
    }


    /**
     * 开启服务器
     */
    private class ServerThread extends Thread{
        @Override
        public void run() {
            try {
                // 拿到一个蓝牙监听,根据uuid来连接
                BluetoothServerSocket msServerSocket = mBTAdapter.listenUsingRfcommWithServiceRecord
                        ("btspp", UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));

                showMsg("服务端:等待连接");

                // 开始连接
                mBTSocket = msServerSocket.accept(1000 * 60 * 10);

                showMsg("服务端:连接成功");

                mReadThread = new ReadThread();
                mReadThread.start();

                showMsg("服务端:启动接收数据");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 读取数据
     */
    private class ReadThread extends Thread{
        @Override
        public void run() {
            byte[] buffer = new byte[1024];
            int bytes;
            InputStream mmInStream = null;
            try {
                mmInStream = mBTSocket.getInputStream();
                showMsg("服务端:获取输入流");

            } catch (IOException e) {
                e.printStackTrace();
            }
            while (true){
                try {
                    if((bytes = mmInStream.read(buffer)) > 0){
                        byte[] buf_data = new byte[bytes];
                        for (int i = 0; i < bytes; i++){
                            buf_data[i] = buffer[i];
                        }
                        String s = new String(buf_data);
                        showMsg("服务端:读取数据~~" + s);
                    }
                } catch (IOException e) {
                    try {
                        mmInStream.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    break;
                }
            }

        }
    }



    @OnClick(R.id.start_btn)
    public void onClick() {
        ServerThread serverThread = new ServerThread();
        serverThread.start();
    }
}
