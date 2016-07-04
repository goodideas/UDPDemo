package com.example.administrator.udpdemo;

import android.content.Context;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Author Administrator
 * Time 2016/3/22.
 */
public class Utils {

    private static Toast toast;
    public Utils(){}

    /**
     * byte数组转16进制字符串
     */
    public static String bytes2HexString(byte[] b, int byteLength) {
        String ret = "";
        for (int i = 0; i < byteLength; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            ret += hex.toUpperCase();
        }
        return ret;
    }

    /**
     * 16进制字符串转byte数组
     */
    public static byte[] HexString2Bytes(String hexString){

        int stringLength = hexString.length();
        byte[] data = new byte[(stringLength/2)];
        for(int i = 0,j = 0;i<data.length;i++,j=j+2)
        {
            data[i] = (byte)Integer.parseInt(hexString.substring(j,(j+2)), 16);
        }
        return data;
    }


    //数据校验码
    public static byte checkData(String data) {
        byte reData;
        int sum = 0;
        int dataLength = data.length();
        for (int i = 0; i < (dataLength); i = i + 2) {
            sum = sum + Integer.parseInt(data.substring(i, 2 + i), 16);
        }
        String temp = "0" + Integer.toHexString(sum);
        reData = (byte) Integer.parseInt(temp.substring(temp.length() - 2, temp.length()).toUpperCase(), 16);
        return reData;
    }

    public static String hexString2binaryString(String hexString)
    {
        if (hexString == null || hexString.length() % 2 != 0)
            return null;
        String bString = "", tmp;
        for (int i = 0; i < hexString.length(); i++)
        {
            tmp = "0000"
                    + Integer.toBinaryString(Integer.parseInt(hexString
                    .substring(i, i + 1), 16));
            bString += tmp.substring(tmp.length() - 4);
        }
        return bString;
    }

    public static String hexString2Characters(String hexString){
        String tvNameText = null;
        try {
            tvNameText = new String(Utils.HexString2Bytes(hexString),"utf-8").trim();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return tvNameText;
    }




    public static void showToast(Context context,String info){
        if(toast!=null){
            toast.setText(info);
        }else{
            toast = Toast.makeText(context, info, Toast.LENGTH_SHORT);
        }
        toast.show();
    }

    public static void showToast(Context context,String info,int gravity,int xOffset,int yOffset){

        if(toast!=null){
            toast.setText(info);

        }else{
            toast = Toast.makeText(context, info, Toast.LENGTH_SHORT);
        }
        toast.setGravity(gravity,xOffset,yOffset);
        toast.show();
    }

    public static void showToast(Context context,int info){

        if(toast!=null){
            toast.setText(info);

        }else{
            toast = Toast.makeText(context, context.getResources().getString(info), Toast.LENGTH_SHORT);
        }
        toast.show();
    }

    public static void showToast(Context context,int info,int gravity,int xOffset,int yOffset){

        if(toast!=null){
            toast.setText(info);
        }else{
            toast = Toast.makeText(context, context.getResources().getString(info), Toast.LENGTH_SHORT);
        }
        toast.setGravity(gravity,xOffset,yOffset);
        toast.show();
    }



    public static String doWithArmExecuteActionInfoString(String data) {
        String reData = "";
        List<Integer> executeList = new ArrayList<>();
        List<Integer> executeList2 = new ArrayList<>();
        for (int i = 0; i < data.length(); i++) {
            if (data.substring(i, i + 1).equalsIgnoreCase("1")) {
                executeList.add((data.length() - 1 - i));
            }
        }

        for (int j = executeList.size() - 1; j >= 0; j--) {
            executeList2.add(executeList.get(j));
        }

        for (int k = 0; k <executeList2.size(); k++) {
            reData = reData + executeList2.get(k)+" ";
        }
        return reData;
    }


    public static List<Integer> doWithArmExecuteActionInfoList(String executeInfo) {
        List<Integer> executeList = new ArrayList<>();
        List<Integer> executeList2 = new ArrayList<>();
        for (int i = 0; i < executeInfo.length(); i++) {
            if (executeInfo.substring(i, i + 1).equalsIgnoreCase("1")) {
                executeList.add((executeInfo.length() - 1 - i));
            }
        }

        for (int j = executeList.size() - 1; j >= 0; j--) {
            executeList2.add(executeList.get(j));
        }
        return executeList2;
    }

}
