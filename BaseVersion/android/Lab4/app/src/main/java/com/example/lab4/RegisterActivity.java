package com.example.lab4;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lab4.model.User;
import com.example.lab4.service.GetService;

public class RegisterActivity extends AppCompatActivity {
    ImageView imageView;
    TextView textView;
    int count = 0;
    public EditText username;
    public EditText password;
    public EditText surepass;
    public static final String TAG="MainActivity";
    public String responseStr="3";
    public GetService getService;
    protected void onCreate(Bundle savedInstanceState) {
        getService = new GetService();
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);
        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.textView);
        username=findViewById(R.id.username);
        password=findViewById(R.id.password);
        surepass=findViewById(R.id.surepass);
        Intent intent=getIntent();
        imageView.setImageResource(Integer.parseInt(intent.getStringExtra("picture")));
        textView.setText(intent.getStringExtra("str"));
        count=Integer.parseInt(intent.getStringExtra("count"));
        imageView.setOnTouchListener(new OnSwipeTouchListener(getApplicationContext()) {
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
    public void onSubmit(View view) {
        if(!userCheck(view)) return;//检查用户名和密码合法性
        responseStr="3";

        String subUsername = username.getText().toString().trim();
        String subPassword = password.getText().toString().trim();
        User user = new User(subUsername,subPassword);

        responseStr = getService.addUser(user);

        if(responseStr.equals("0")) {
            Toast.makeText(getApplicationContext(), "注册成功,进入登录", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, WelcomeActivity.class); // 准备跳转到新的MainActtivity
            intent.putExtra("count", Integer.toString(count));
            intent.putExtra("username",username.getText().toString().trim());
            if(count==0)
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
        else if(responseStr.equals("1"))
        {
            Toast.makeText(getApplicationContext(), "用户名已被使用", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getApplicationContext(), "网络连接失败", Toast.LENGTH_SHORT).show();
        }
    }
    public boolean userCheck(View view)
    {
        String subUsername = username.getText().toString().trim();
        String subPass = password.getText().toString().trim();
        String subSure = surepass.getText().toString().trim();
        //输入检验
        if (subUsername.length() == 0)
        {
            Toast.makeText(getApplicationContext(), "请输入用户名", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (subPass.length() == 0)
        {
            Toast.makeText(getApplicationContext(), "请输入密码", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if ((subUsername.charAt(0) > 'z' || subUsername.charAt(0) < 'a') && (subUsername.charAt(0) > 'Z' || subUsername.charAt(0) < 'A'))
        {
            Toast.makeText(getApplicationContext(), "用户名开头必须为字母", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (subUsername.length() > 10 || subUsername.length() < 5)
        {
            Toast.makeText(getApplicationContext(), "用户名的长度应介于5到10之间", Toast.LENGTH_SHORT).show();
            return false;
        }
        else {
            boolean big = false;
            for (int i = 0; i < subUsername.length(); i++) {
                char c = subUsername.charAt(i);
                if (!((c <= 'Z' && c >= 'A') || (c <= 'z' && c >= 'a') || (c <= '9' && c >= '0') || (c == '_'))) {
                    Toast.makeText(getApplicationContext(), "用户名只能包含字母数字和下划线", Toast.LENGTH_SHORT).show();
                    return false;
                }
                if ((c <= 'Z' && c >= 'A')) big = true;
            }
            if (!big) {
                Toast.makeText(getApplicationContext(), "用户名至少一个大写字母", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (subPass.length() < 6 || subPass.length() > 12) {
                Toast.makeText(getApplicationContext(), "密码应该在6到12位之间", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (!subPass.equals(subSure)) {
                Toast.makeText(getApplicationContext(), "两次输入密码不相等", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }
}
