package com.example.lab4;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lab4.model.User;
import com.example.lab4.service.GetService;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class WelcomeActivity extends AppCompatActivity {
    ImageView imageView;
    TextView textView;
    private String username;
    private String responseStr;
    int count = 0;
    public static final String TAG="WelcomeActivity";
    private GetService getService;

    protected void onCreate(Bundle savedInstanceState) {
        getService = new GetService();
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);
        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.textView);
        Intent intent=getIntent();
        imageView.setImageResource(Integer.parseInt(intent.getStringExtra("picture")));
        username=intent.getStringExtra("username");
        textView.setText(username);
        count=Integer.parseInt(intent.getStringExtra("count"));
        imageView.setOnTouchListener(new OnSwipeTouchListener(getApplicationContext()) {
            public void onSwipeTop() {
            }

            public void onSwipeRight() {
                if (count == 0) {
                    imageView.setImageResource(R.drawable.good_night_img);
                    count = 1;
                } else {
                    imageView.setImageResource(R.drawable.good_morning_img);
                    count = 0;
                }
            }
            public void onSwipeLeft() {
                if (count == 0) {
                    imageView.setImageResource(R.drawable.good_night_img);
                    count = 1;
                } else {
                    imageView.setImageResource(R.drawable.good_morning_img);
                    count = 0;
                }
            }
            public void onSwipeBottom() {
            }
        });
    }
    public void onReturn(View view)
    {
        Intent intent = new Intent(this,MainActivity.class); // 准备跳转到新的MainActtivity
        if(count==0)
        {
            int r=R.drawable.good_morning_img;
            intent.putExtra("picture", Integer.toString(r));
            intent.putExtra("str","Good Morning");
        }
        else
        {
            int r=R.drawable.good_night_img;
            intent.putExtra("picture",Integer.toString(r));
            intent.putExtra("str","Good Night");
        }
        intent.putExtra("count",Integer.toString(count));
        startActivity(intent);
        finish();
        overridePendingTransition(0,0);
    }
    public void onChange(View view)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("修改密码");
        // 通过LayoutInflater来加载一个xml的布局文件作为一个View对象
        View v=LayoutInflater.from(this).inflate(R.layout.changepassword, null);
        // 设置我们自己定义的布局文件作为弹出框的Content
        builder.setView(v);

        final EditText newpassword=v.findViewById(R.id.newpass);

        builder.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String passStr=newpassword.getText().toString().trim();
                        if(userCheck(passStr)) {
                            User user = new User(username,passStr);
                            responseStr = getService.updateUserPass(user);
                            if(responseStr.equals("0"))
                                Toast.makeText(getApplicationContext(), "修改成功", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(getApplicationContext(), "网络连接失败", Toast.LENGTH_SHORT).show();
//                            changeWord(passStr);
                        }
                    }
                });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        Toast.makeText(getApplicationContext(), "告辞！", Toast.LENGTH_SHORT).show();
                    }
                });
        builder.show();
    }
    public void changeWord(final String newpass)
    {
        Thread child=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://116.63.218.228:8080/demo-0.0.1-SNAPSHOT/UpdatePassword?username="+username+"&password="+newpass);
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
        if(responseStr.equals("0"))
            Toast.makeText(getApplicationContext(), "修改成功", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getApplicationContext(), "网络连接失败", Toast.LENGTH_SHORT).show();
    }
    public boolean userCheck(String subPass)
    {
        if (subPass.length() == 0)
        {
            Toast.makeText(getApplicationContext(), "请输入新密码", Toast.LENGTH_SHORT).show();
            return false;
        }
        else{
            if (subPass.length() < 6 || subPass.length() > 12) {
                Toast.makeText(getApplicationContext(), "密码应该在6到12位之间", Toast.LENGTH_SHORT).show();
                return false;
            }
            for (int i = 0; i < subPass.length(); i++) {
                char c = subPass.charAt(i);
                if (!((c <= 'Z' && c >= 'A') || (c <= 'z' && c >= 'a') || (c <= '9' && c >= '0') || (c == '_'))) {
                    Toast.makeText(getApplicationContext(), "密码只能包含字母数字和下划线", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        }
        return true;
    }
}
