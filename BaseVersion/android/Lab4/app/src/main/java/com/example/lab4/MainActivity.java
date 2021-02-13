package com.example.lab4;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.example.lab4.model.User;
import com.example.lab4.service.GetService;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MainActivity extends AppCompatActivity {
    private ImageView imageView;
    private TextView textView;
    private EditText username;
    private EditText password;
    private static final  String TAG="MainActivity";
    private String responseStr="3";
    private final Lock lock = new ReentrantLock();
    private int count = 0;
    private CheckBox remember;
    private SharedPreferences pref;
    private GetService getService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getService = new GetService();
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        imageView=findViewById(R.id.imageView);
        textView=findViewById(R.id.textView);
        username=findViewById(R.id.username);
        password=findViewById(R.id.password);
        remember=findViewById(R.id.remember);
        /**
         * 获取SharedPreferenced对象
         * 第一个参数是生成xml的文件名
         * 第二个参数是存储的格式：只能被创建这个文件的当前应用访问，若文件不存在会创建文件；若创建的文件已存在则会覆盖掉原来的文件
         */
        pref= getSharedPreferences("User", Context.MODE_PRIVATE);
        Intent intent=getIntent();
        //设置背景图片和字符串
        if(intent.getStringExtra("picture")!=null) imageView.setImageResource(Integer.parseInt(intent.getStringExtra("picture")));
        if(intent.getStringExtra("str")!=null) textView.setText(intent.getStringExtra("str"));
        if(intent.getStringExtra("count")!=null) count=Integer.parseInt(intent.getStringExtra("count"));
        boolean isRemember=pref.getBoolean("remember",false);//取出数据,第一个参数是写入是的键，第二个参数是如果没有获取到数据就默认返回的值。
        if(isRemember){
            //将账号和密码都设置到文本中
            String account=pref.getString("username","");
            String pass=pref.getString("password","");
            username.setText(account);
            password.setText(pass);
            remember.setChecked(true);
        }
        imageView.setOnTouchListener(new OnSwipeTouchListener(getApplicationContext()) {//滑动监听器
            public void onSwipeTop() {
            }
            public void onSwipeRight() {
                if (count == 0) {
                    imageView.setImageResource(R.drawable.good_night_img);
                    textView.setText("Good Night");
                    count = 1;
                } else {
                    imageView.setImageResource(R.drawable.good_morning_img);
                    textView.setText("Good Morning");
                    count = 0;
                }
            }
            public void onSwipeLeft() {
                if (count == 0) {
                    imageView.setImageResource(R.drawable.good_night_img);
                    textView.setText("Good Night");
                    count = 1;
                } else {
                    imageView.setImageResource(R.drawable.good_morning_img);
                    textView.setText("Good Morning");
                    count = 0;
                }
            }
            public void onSwipeBottom() {
            }
        });
    }
    public void onRegister(View view)
    {
        Intent intent = new Intent(this,RegisterActivity.class); // 准备跳转到RegisterActivity，注册的活动
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
        intent.putExtra("count",Integer.toString(count));//保证跳转后的背景和字相同
        //可能会有取消记住密码
        SharedPreferences.Editor editor=pref.edit();
        if(!remember.isChecked()) editor.clear();
        //提交数据存入到xml文件中
        editor.commit();
        startActivity(intent);
        finish();
        overridePendingTransition(0,0);//设置切换Activity时没有动画
    }
    public void onSign(View v) {//登录事件相应
        responseStr="3";
        String subUsername = username.getText().toString().trim();
        String subPassword = password.getText().toString().trim();
        if(subPassword.length()==0||subUsername.length()==0)
        {
            Toast.makeText(getApplicationContext(), "用户名或者密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        User user = new User(subUsername,subPassword);
        responseStr = getService.checkLogin(user);

        if(responseStr.equals("0")) {//返回0登录成功
            Toast.makeText(getApplicationContext(), "成功登录", Toast.LENGTH_SHORT).show();
            //记住密码功能
            SharedPreferences.Editor editor=pref.edit();
            if(remember.isChecked()){
                //通过editor对象写入数据
                editor.putBoolean("remember",true);
                editor.putString("username",subUsername);
                editor.putString("password",subPassword);
            }else {
                editor.clear();
            }
            //提交数据存入到xml文件中
            editor.commit();
            // 准备跳转到WekcomeActtivity
            Intent intent = new Intent(this, WelcomeActivity.class);
            intent.putExtra("count", Integer.toString(count));
            intent.putExtra("username",username.getText().toString().trim());
            if(count==0)//跳转后背景图片一致
            {
                int r=R.drawable.good_morning_img;
                intent.putExtra("picture", Integer.toString(r));
            }
            else
            {
                int r=R.drawable.good_night_img;
                intent.putExtra("picture",Integer.toString(r));
            }
            startActivity(intent);
            finish();
            overridePendingTransition(0, 0);//设置切换Activity时没有动画
        }
        else if(responseStr.equals("1"))//密码错误
        {
            Toast.makeText(getApplicationContext(), "密码错误", Toast.LENGTH_SHORT).show();
        }
        else if(responseStr.equals("2"))//用户不存在
        {
            Toast.makeText(getApplicationContext(), "用户不存在", Toast.LENGTH_SHORT).show();
        }
        else{//responseStr==”3“表示没变，也就是子线程没有访问到后台
            Toast.makeText(getApplicationContext(), "网络连接失败", Toast.LENGTH_SHORT).show();
        }
    }
}