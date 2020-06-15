package com.test.weatherforcast;

/**
 * 程序的入口类
 */

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

import com.test.weatherforcast.login.Login_Activity;

import org.litepal.tablemanager.Connector;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);//这里用了一个SharedPreference，用来判断是否有缓存数据，如果有缓存数据直接读取缓存的数据
        if(prefs.getString("weather",null) != null){
            Intent intent1 = new Intent(this, Login_Activity.class);
            startActivity(intent1);
            finish();
        }

    }
}
