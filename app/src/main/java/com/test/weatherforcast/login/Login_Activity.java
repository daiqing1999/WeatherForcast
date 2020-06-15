package com.test.weatherforcast.login;
/**
 * 登陆界面
 * */
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.test.weatherforcast.MainActivity;
import com.test.weatherforcast.R;
import com.test.weatherforcast.WeatherActivity;

public class Login_Activity extends AppCompatActivity implements View.OnClickListener {

    private EditText edit_account, edit_password;
    private TextView text_msg;
    private Button btn_login, btn_register;
    private ImageButton openpwd;
    private boolean flag = false;
    private CheckBox save_password;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int flag = prefs.getInt("flag",0);
        setContentView(R.layout.login_activity);
        init();
        if(flag==1){
            edit_account.setText(prefs.getString("username",null));
            edit_password.setText(prefs.getString("password",null));
            save_password.setChecked(true);
        }
    }
    /**
     * 获取界面控件
     * */
    private void init() {
        save_password = findViewById(R.id.save_password);
        edit_account = (EditText) findViewById(R.id.edit_account);
        edit_account.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    edit_account.clearFocus();
                }
                return false;
            }
        });
        edit_password = (EditText) findViewById(R.id.edit_password);
        edit_password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    edit_password.clearFocus();
                    InputMethodManager imm =
                            (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(edit_password.getWindowToken(), 0);
                }
                return false;
            }
        });
        text_msg = (TextView) findViewById(R.id.text_msg);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_register = (Button) findViewById(R.id.btn_register);
        openpwd = (ImageButton) findViewById(R.id.btn_openpwd);
        text_msg.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        openpwd.setOnClickListener(this);
        dbHelper = new DBHelper(this, "Data.db", null, 1);

    }

    @Override
    /**
     * 点击事件
     * */
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                if (edit_account.getText().toString().trim().equals("") | edit_password.getText().
                        toString().trim().equals("")) {
                    Toast.makeText(this, "请输入账号或者注册账号！", Toast.LENGTH_SHORT).show();
                } else {
                    readUserInfo();
                }
                break;
            case R.id.btn_register:
                Intent intent = new Intent(Login_Activity.this, Register_Activity.class);
                startActivity(intent);
                break;
            case R.id.btn_openpwd:
                if (flag == true) {//不可见
                    edit_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    flag = false;
                    openpwd.setBackgroundResource(R.drawable.invisible);
                } else {
                    edit_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    flag = true;
                    openpwd.setBackgroundResource(R.drawable.visible);
                }
                break;

        }
    }

    /**
     * 判断登陆信息
     * */
    protected void readUserInfo() {
        if (login(edit_account.getText().toString(), edit_password.getText().toString())) {//验证成功就跳转到WeatherActivity
            if(save_password.isChecked()){
                SharedPreferences.Editor editor = PreferenceManager.
                        getDefaultSharedPreferences(Login_Activity.this).edit();
                editor.putInt("flag",1);
                editor.putString("username",edit_account.getText().toString());
                editor.putString("password",edit_password.getText().toString());
                editor.apply();
            }
            Toast.makeText(this, "登陆成功！", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, WeatherActivity.class);
            startActivity(intent);
            finish();
        } else {//密码或用户名错误就提示
            Toast.makeText(this, "账户或密码错误，请重新输入！！", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 验证输入的登陆信息和数据库的登录信息是否一致
     * */
    public boolean login(String username, String password) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = "Select * from usertable where username=? and password=?";
        Cursor cursor = db.rawQuery(sql, new String[]{username, password});
        if (cursor.moveToFirst()) {
            cursor.close();
            return true;
        }
        return false;
    }
}
