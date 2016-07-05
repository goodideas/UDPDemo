package com.example.administrator.udpdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private EditText etIp;
    private ToggleButton tbConnect;
    private TextView tvShowRecv;
    private EditText etInput;
    private Button btnSend, btnStop;
    //    private Handler handler;
    private UdpHelper udpHelper;
    private static final String IP = "192.168.0.119";
    private String recvData;
    private RecvCallBack recvCallBack;

    private SingleUdp singleUdp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findView();


        etIp.setText(IP);
        //滚动
        tvShowRecv.setMovementMethod(new ScrollingMovementMethod());
        tvShowRecv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                tvShowRecv.setText("");
                return false;
            }
        });

        recvCallBack = new RecvCallBack() {
            @Override
            public void onRecv(String data) {
                recvData = data;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvShowRecv.append(recvData + "\n");
                    }
                });
            }
        };

        singleUdp = SingleUdp.getUdpInstance();
        btnSend.setOnClickListener(this);
        btnStop.setOnClickListener(this);
        tbConnect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked){
//                    udpHelper = UdpHelper.getInstance(etIp.getText().toString());
//                    udpHelper.setRecvCallBack(recvCallBack);
//                }else{
//                    udpHelper.closeUdp();
//                }

                if (isChecked) {

                    singleUdp.setUdpIp(etIp.getText().toString());
                    singleUdp.setUdpRemotePort(9987);
                    singleUdp.setUdpLocalPort(9988);
                    singleUdp.start();
                    singleUdp.setOnReceiveListen(new OnReceiveListen() {
                        @Override
                        public void onReceiveData(byte[] data) {
                            Log.e(TAG,"data="+new String(data).trim());
                        }
                    });

                } else {
                    singleUdp.stop();
                }

            }
        });

    }

    private void findView() {
        etIp = (EditText) findViewById(R.id.etIp);
        tbConnect = (ToggleButton) findViewById(R.id.tbConnect);
        tvShowRecv = (TextView) findViewById(R.id.tvShowRecv);
        etInput = (EditText) findViewById(R.id.etInput);
        btnSend = (Button) findViewById(R.id.btnSend);
        btnStop = (Button) findViewById(R.id.btnStop);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSend:
//                if(udpHelper!=null){
//                    udpHelper.send(etInput.getText().toString().getBytes());
//                }
                singleUdp.send(etInput.getText().toString().getBytes());
                break;
            case R.id.btnStop:
//                if(udpHelper!=null){
//                    Log.e(TAG,"udpHelper.setRecvStop(true);");
//                    udpHelper.setRecvStop(true);
//                }
                break;

        }
    }
}
