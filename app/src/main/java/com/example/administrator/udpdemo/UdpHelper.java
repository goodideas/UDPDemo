package com.example.administrator.udpdemo;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Arrays;

public class UdpHelper {
    private InetAddress inetAddress;
    private static final int PORT = 5678;

    private DatagramSocket udpSocket;
    private DatagramPacket udpPacket;
    private DatagramPacket sendUdpPacket;

    private byte[] udpReceiveByte = new byte[512];
    private boolean udpReceiveWhile = true;
    private RecvCallBack recvCallBack;
    private static final String TAG = "UdpBroadcastHelper";
    private boolean stopRecv = false;
    private Handler handler = new Handler();
    private static UdpHelper UdpInstance;

    private UdpHelper(String ip) {
        startUdp(ip);
    }

    public static synchronized UdpHelper getInstance(String ip) {
        if (UdpInstance == null) {
            UdpInstance = new UdpHelper(ip);
            Log.e(TAG, "getInstance");
        }
        return UdpInstance;
    }


    private void startUdp(String ip) {
        try {

            inetAddress = InetAddress.getByName(ip);
            udpSocket = new DatagramSocket(9998);

//            Log.e(TAG,"getInetAddress = "+udpSocket.getInetAddress().toString()+" getInetAddress().getHostAddress"+udpSocket.getInetAddress().getHostAddress());
            receiveUdp();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setRecvStop(boolean isStop) {
        this.stopRecv = isStop;
    }


    public void receiveUdp() {
        Log.e(TAG, "receiveUdp");
        udpPacket = new DatagramPacket(udpReceiveByte, udpReceiveByte.length);
        new Thread() {
            public void run() {
                while (udpReceiveWhile) {

                    if (stopRecv) {
                        Log.e(TAG, "end");
                        udpReceiveWhile = false;
                    }
                    try {
//                        System.arraycopy(emptyByte,0,udpReceiveByte,0,512);
                        Arrays.fill(udpReceiveByte, (byte) 0x00);
                        udpSocket.receive(udpPacket);
                        Log.e(TAG,"recvBuffersize"+udpSocket.getReceiveBufferSize());
                        int len = udpPacket.getLength();
                        if (len > 0) {
                            String receiveStr = new String(udpReceiveByte);
                            Log.e(TAG, " receiveStr.trim()=" + receiveStr.trim());
                            if (recvCallBack != null) {
                                recvCallBack.onRecv(receiveStr.trim());
                            }

//                            Log.e(TAG, "receiveStr=" + receiveStr + " ip=" + udpPacket.getAddress().toString());

                            //说明有接收到设备的数据

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }


    public void setRecvCallBack(RecvCallBack recvCallBack) {
        this.recvCallBack = recvCallBack;
    }


    //udp发送广播
    public void send(byte[] data) {

        Log.e(TAG, "udp发送方法");
        if (data != null) {
            sendUdpPacket = new DatagramPacket(data, data.length,
                    inetAddress, PORT);

        }
        new Thread() {
            @Override
            public void run() {
                try {
                    if (udpSocket != null) {
                        udpSocket.send(sendUdpPacket);
                        Log.e(TAG, "udp发送");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    //关掉udp，把单例一起关掉
    public void closeUdp() {
        udpReceiveWhile = false;
        if (udpSocket != null) {
            udpSocket.close();
        }
        UdpInstance = null;
        Log.e(TAG, "closeUdp");
    }


}