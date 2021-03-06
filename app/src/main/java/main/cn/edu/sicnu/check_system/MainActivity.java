package main.cn.edu.sicnu.check_system;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import main.cn.edu.sicnu.check_system.Constances.Constances;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public final class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "MainActivity";
    //企业账户
    private EditText accountEdit;
    //密码
    private EditText passwordEdit;
    //登陆
    private Button login;
    //进度提醒
    private ProgressBar progressBar;
    //选择框
    private CheckBox rememberAccountPwd;
    //注册
    private TextView register;
    //忘记密码
    private TextView forget;

    private AppCompatActivity appCompatActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appCompatActivity = this;
        accountEdit = findViewById(R.id.account_edit);
        passwordEdit = findViewById(R.id.password_edit);
        login = findViewById(R.id.login_btn);
        rememberAccountPwd = findViewById(R.id.remember_ap);
        progressBar = findViewById(R.id.progressbar);
        register = findViewById(R.id.register);
        forget = findViewById(R.id.forget);
        login.setOnClickListener(this);
        register.setOnClickListener(this);
        forget.setOnClickListener(this);

        register.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        forget.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线

        //申请权限
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.INTERNET)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.INTERNET},0);
        }//if

        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},0);
        }//if
        //恢复密码
        restore();

    }

    /**
     * 存储账户和密码
     */
    public void saveAccountAndPassword(){
        boolean isSelected = this.rememberAccountPwd.isChecked();
        //此方法会将当前活动的类名作为sharedPreference的作为文件名
        SharedPreferences.Editor editor = getSharedPreferences("data",MODE_PRIVATE).edit();
        String account = this.accountEdit.getText().toString().trim();
        String password = this.passwordEdit.getText().toString().trim();
        editor.putString("account",account);
        editor.putString("password",password);
        editor.putBoolean("isSelected",isSelected);
        editor.apply();
    }

    /**
     * 恢复账户和密码
     */
    public void restore(){
        SharedPreferences sp = getSharedPreferences("data",MODE_PRIVATE);
        boolean isRemember = sp.getBoolean("isSelected",false);
        if(isRemember){
            this.accountEdit.setText(sp.getString("account",""));
            this.passwordEdit.setText(sp.getString("password",""));
            this.rememberAccountPwd.setSelected(true);
        }//if
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.login_btn:
                //将登录按钮和EditText禁用
                login.setEnabled(false);
                accountEdit.setEnabled(false);
                passwordEdit.setEnabled(false);
                //显示进度
                progressBar.setVisibility(View.VISIBLE);
                //账号密码的检测
                //提示登陆中
                Toast.makeText(this,"登陆中...",Toast.LENGTH_SHORT).show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //发起服务器连接
                        OkHttpClient client = new OkHttpClient();
                        //传入参数
                        RequestBody requestBody = new FormBody.Builder()
                                .add("username",accountEdit.getText().toString().trim())
                                .add("password",passwordEdit.getText().toString().trim()).build();
                        Request request = new Request.Builder()
                                .url(Constances.getLOGIN_SERVER_URL())
                                .post(requestBody).build();
                        try {
                            Response response = client.newCall(request).execute();
                            Log.d(TAG, "run: "+"开始连接");
                            //获取返回的数据
                            String responseData = response.body().string();
                            Log.d(TAG, "run: "+responseData);
                            Message message = new Message();
                            if(responseData.equals("false")){
                                message.what = Constances.FAIL;
                            }else{
                                String[] split = responseData.split("&&");
                                Constances.setCurrentCid(split[1]);
                                message.what = Constances.SUCCESS;
                            }
                            handler.sendMessage(message);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

                //判断是否需要存入账号密码，以便下次登陆
                if(rememberAccountPwd.isChecked()){
                    saveAccountAndPassword();
                }
                break;
            case R.id.register:
                Intent intent = new Intent(this,RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.forget:
                //忘记密码找回
                Intent intent1 = new Intent(this,HomeActivity.class);
                startActivity(intent1);
                break;
            default:break;
        }//switch
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case Constances.SUCCESS:
                    //登陆成功
                    Toast.makeText(MainActivity.this, "登陆成功...", Toast.LENGTH_SHORT).show();
                    //将进度条设置为消失
                    login.setEnabled(true);
                    accountEdit.setEnabled(true);
                    passwordEdit.setEnabled(true);
                    progressBar.setVisibility(View.GONE);
                    //跳转活动
                    Intent intent = new Intent(appCompatActivity,HomeActivity.class);
                    startActivity(intent);
                    //销毁当前活动
                    finish();
                    break;
                case Constances.FAIL:
                    //重新恢复登录按钮和EditText
                    login.setEnabled(true);
                    accountEdit.setEnabled(true);
                    passwordEdit.setEnabled(true);
                    //显示进度
                    Toast.makeText(MainActivity.this, "登陆失败", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    break;
                default:break;
            }
        }
    };
}
