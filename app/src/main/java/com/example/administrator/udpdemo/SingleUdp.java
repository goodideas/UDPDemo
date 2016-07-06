package com.example.administrator.udpdemo;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;

/**
 * Created by Administrator
 * on 2016/7/5.
 */
public class SingleUdp {

    private static final String TAG = "SingleUdp";
    public static final int ONE_KB = 1024;
    public static final int HALF_KB = 512;
    private String ipAddress;
    private int udpLocalPort = -1;
    private int udpRemotePort = -1;
    private DatagramSocket udpSocket;
    private DatagramPacket udpReceivePacket;
    private DatagramPacket udpSendPacket;
    private InetAddress inetAddress;
    private byte[] udpReceiveBytes;
    private OnReceiveListen onReceiveListen;//接收监听
    private Thread udpReceiveThread;
    private static SingleUdp UdpInstance;

    //私有构造器
    private SingleUdp() {
        init();
    }

    //单例
    public static synchronized SingleUdp getUdpInstance() {
        if (UdpInstance == null) {
            UdpInstance = new SingleUdp();
        }
        return UdpInstance;
    }

    //设置监听
    public void setOnReceiveListen(OnReceiveListen receiveListen) {
        this.onReceiveListen = receiveListen;
    }

    //设置Udp的IP
    public void setUdpIp(String ip) {
        this.ipAddress = ip;
    }

    //设置Udp的本地端口
    public void setUdpLocalPort(int port) {
        this.udpLocalPort = port;
    }

    //设置Udp的远程端口
    public void setUdpRemotePort(int port) {
        this.udpRemotePort = port;
    }

    //初始化
    private void init() {
        udpReceiveBytes = new byte[HALF_KB];
        udpReceivePacket = new DatagramPacket(udpReceiveBytes, HALF_KB);
    }

    //启动udp
    public void start() {

        try {
            inetAddress = InetAddress.getByName(ipAddress);
            if (udpLocalPort != -1) {
                udpSocket = new DatagramSocket(udpLocalPort);
            } else {
                udpSocket = new DatagramSocket();
            }
        } catch (UnknownHostException | SocketException e) {
            e.printStackTrace();
        }

        receiveUdp();
    }

    //关闭udp
    public void stop() {
        udpReceiveThread.interrupt();
        if (udpSocket != null) {
            udpSocket.close();
        }
        UdpInstance = null;
    }

    //发送
    public void send(byte[] data){
        if(udpSendPacket==null){
            udpSendPacket = new DatagramPacket(
                    data, data.length, inetAddress, udpRemotePort);
        }

        new Thread() {
            @Override
            public void run() {
                try {
                    if (udpSocket != null) {
                        udpSocket.send(udpSendPacket);
                        Log.e(TAG, "udp发送成功！");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, "udp发送失败！");
                }
            }
        }.start();
    }



    //接收线程
    private void receiveUdp() {
        udpReceiveThread = new Thread() {
            public void run() {
                while (!udpReceiveThread.isInterrupted()) {
                    try {
                        //初始化赋值 (byte)0x00
                        Arrays.fill(udpReceiveBytes, (byte) 0x00);
                        //会阻塞
                        udpSocket.receive(udpReceivePacket);
                        int len = udpReceivePacket.getLength();
                        if (len > 0) {
                            if (onReceiveListen != null) {
                                onReceiveListen.onReceiveData(udpReceiveBytes);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        break;
                    }
                }
            }
        };
        udpReceiveThread.start();
//        myHandler myHandler = new myHandler(Looper.myLooper());
//        Message message = myHandler.obtainMessage();
//        message.what = 123;
//        message.sendToTarget();

    }

//    class myHandler extends Handler{
//
//        public myHandler(Looper looper){
//            super(looper);
//        }
//
//        @Override
//        public void handleMessage(Message msg) {
////            super.handleMessage(msg);
//            if(msg.what == 123){
//                Log.e(TAG,"myHandler接收");
//            }
//        }
//    }

}


