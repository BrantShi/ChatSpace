package com.example.lab4.service;

import android.util.Log;

import com.example.lab4.model.User;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 将与服务器的链接封装起来，便于维护
 */

public class GetService {

    private static final  String TAG="GetService";
    private String basicUrl = "http://59.110.55.6:8080/test002-0.0.1-SNAPSHOT/";
    private String responseStr;
    private User user;

    public String checkLogin(final User user){
        this.user = user;
        Thread child=new Thread(new Runnable() {
            @Override
            public void run() {//通过子线程来访问后台
                try {
                    URL url = new URL("http://59.110.55.6:8080/test002-0.0.1-SNAPSHOT/CheckUser?username=" + user.getUsername() + "&password=" + user.getPassword());
                    Log.d(TAG, "sendGETRequest: "+url.toString());//在日志中打印
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");//设置为Get明文传播
                    conn.setConnectTimeout(5000);//最大连接时间
                    conn.connect();
                    if (conn.getResponseCode()==200)//请求处理完毕
                    {
                        InputStream inputStream = conn.getInputStream();
                        int len = 0;
                        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                        byte[] data = new byte[1024];
                        while ((len = inputStream.read(data)) != -1) {
                            outStream.write(data, 0, len);
                        }
                        outStream.close();
                        inputStream.close();

                        return responseStr;
                        Log.v(TAG, "后台返回： " + responseStr);
                        responseStr = new String(outStream.toByteArray());//后台返回的字符串
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        child.start();
        try {
            child.join();//等待200ms让子线程先完成
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return responseStr;
    }

    /**
     *
     * @param user
     * @return 对应responstr
     */
    public String addUser(final User user){
        Thread child=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://116.63.218.228:8080/demo-0.0.1-SNAPSHOT/AddUser?username=" + user.getUsername() + "&password=" + user.getPassword());
                    Log.d(TAG, "sendGETRequest: "+url.toString());
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(5000);
                    conn.connect();
                    if (conn.getResponseCode()==200)//请求处理完毕
                    {
                        InputStream inputStream = conn.getInputStream();//得到返回流
                        int len = 0;
                        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                        byte[] data = new byte[1024];
                        while ((len = inputStream.read(data)) != -1) {
                            outStream.write(data, 0, len);
                        }
                        outStream.close();
                        inputStream.close();
                        responseStr = new String(outStream.toByteArray());
                        Log.v(TAG, "data = " + responseStr);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        child.start();//开始子线程
        try {
            child.join();//等待子线程
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return responseStr;
    }

    public String updateUserPass(final User user){
        Thread child=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://116.63.218.228:8080/demo-0.0.1-SNAPSHOT/UpdatePassword?username="+user.getUsername()+"&password="+user.getPassword());
                    Log.d(TAG, "GETRsendequest: "+url.toString());
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(5000);
                    conn.connect();
                    if (conn.getResponseCode()==200)//请求处理完毕
                    {
                        InputStream inputStream = conn.getInputStream();//得到返回流
                        int len = 0;
                        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                        byte[] data = new byte[1024];
                        while ((len = inputStream.read(data)) != -1) {
                            outStream.write(data, 0, len);
                        }
                        outStream.close();
                        inputStream.close();
                        responseStr = new String(outStream.toByteArray());
                        Log.v(TAG, "data = " + responseStr);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        child.start();//开始子线程
        try {
            child.join();//等待子线程
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return responseStr;
    }
}
