package com.test.weatherforcast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.test.weatherforcast.gson.Forecast;
import com.test.weatherforcast.gson.HeWeather6;
import com.test.weatherforcast.gson.Hourly;
import com.test.weatherforcast.receiver.BroadCastReceiver;
import com.test.weatherforcast.util.CustomScrollView;
import com.test.weatherforcast.util.HttpUtil;
import com.test.weatherforcast.util.Utility;

import java.io.IOException;
import java.util.Calendar;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
/**
 * 程序的主页面
 */
public class WeatherActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "WeatherActivity";

    public DrawerLayout drawerLayout;

    private ImageButton navButton;

    public SwipeRefreshLayout swipeRefresh;

    private ImageView weatherBackgroundImg;

    private CustomScrollView weatherLayout;

    private TextView titleCity;

    private TextView titleUpdateTime;

    private String weatherId;

    private BroadCastReceiver receiver;

    private TextView tay;

    private TextView degreeText;

    private TextView weatherCondText;

    private LinearLayout forecastLayout;

    private LinearLayout hourlylayout;

    private TextView aqiText;

    private TextView pm25Text;

    private TextView comfortText;

    private TextView suggestionText;

    private int code;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //获取页面主要控件，初始化数据
        code = 100;
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT >= 21){
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            );
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_weather);
        //初始化各控件
        weatherBackgroundImg = findViewById(R.id.weather_background);
        weatherLayout = findViewById(R.id.weather_layout);
        titleCity = findViewById(R.id.title_city);
        titleUpdateTime = findViewById(R.id.title_update_time);
        degreeText = findViewById(R.id.degree_text);
        weatherCondText = findViewById(R.id.weather_cond_text);
        forecastLayout = findViewById(R.id.forecast_layout);
        hourlylayout = findViewById(R.id.hourly_layout);
        aqiText = findViewById(R.id.aqi_text);
        tay = findViewById(R.id.tay);
        pm25Text = findViewById(R.id.pm25_text);
        comfortText = findViewById(R.id.comfort_text);
        suggestionText = findViewById(R.id.suggestion_text);
        drawerLayout = findViewById(R.id.drawer_layout);
        navButton = findViewById(R.id.nav_button);
        swipeRefresh = findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);

        receiver = new BroadCastReceiver(); //实例化广播接收者
        String action = "com.get.WEATHERSUCCESS";
        IntentFilter intentFilter = new IntentFilter(action);
        registerReceiver(receiver,intentFilter); //注册广播

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        loadBackgrounPic();
        String weatherString = prefs.getString("weather",null);
        //判断是否存在缓存数据
        if(weatherString != null){
            //有缓存时直接解析天气数据
            HeWeather6 weather = Utility.handleWeatherResponse(weatherString);
            weatherId = weather.basic.weatherId;
            showWeatherInfo(weather);
        }
        else{
            //无缓存时去服务器查询天气
            weatherId = getIntent().getStringExtra("weather_id");
            weatherLayout.setVisibility(View.INVISIBLE);
            requestWeather(weatherId);
        }
        //下滑刷新监听
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                String id = getId();
                requestWeather(id);
            }
        });

        navButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        degreeText.setOnClickListener(this);
        forecastLayout.setOnClickListener(this);
        hourlylayout.setOnClickListener(this);
        suggestionText.setOnClickListener(this);
    }

    /**
     * 根据天气id请求城市天气信息
     */
    private final String getId(){
        String Id;
        Id = weatherId;
        return Id;
    }
    //控件点击事件
    public void onClick(View v) {
        Intent intent = new Intent(WeatherActivity.this, LoadUrl.class);
        switch (v.getId()) {
            case R.id.degree_text:
                intent.putExtra("url","https://apip.weatherdt.com/h5.html?id=uTLWaQwYSL");
                break;
            case R.id.forecast_layout:
                intent.putExtra("url","https://apip.weatherdt.com/h5.html?id=6AEtSp8A8w");
                break;
            case R.id.hourly_layout:
                intent.putExtra("url","https://apip.weatherdt.com/h5.html?id=BwfxyQBZfb");
                break;
            case R.id.suggestion_text:
                intent.putExtra("url","https://apip.weatherdt.com/h5.html?id=SovPXhe7IY");
                break;
        }
        startActivity(intent);
    }

    //根据城市id，获取天气数据并解析
    public void requestWeather(String weatherid){

        String weatherUrl = "https://api.heweather.net/s6/weather?location=" +
                weatherid + "&key=07b05a215a2c4ba8a30c8debdb1f558e";
        HttpUtil.sendOkHttpRequest(weatherUrl,new Callback(){
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final HeWeather6 weather = Utility.handleWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(weather!=null && "ok".equals(weather.status)){
                            SharedPreferences.Editor editor = PreferenceManager.
                                    getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("weather",responseText);
                            editor.apply();
                            showWeatherInfo(weather);
                        }else{
                            Toast.makeText(WeatherActivity.this,"获取天气信息失败",
                                    Toast.LENGTH_SHORT).show();

                        }
                        //刷新事件结束，隐藏刷新进度条
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this,"获取天气信息失败",
                                Toast.LENGTH_SHORT).show();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });

        loadBackgrounPic();
    }

    /**
     * 处理并展示weather实体类中的数据
     */
    public int getResource(int code){
        String imageName = "a" + code;
        int resId = getResources().getIdentifier(imageName, "drawable" , getPackageName());
        return resId;
    }

    /*获取星期几*/

    public static String getWeek(int a) {
        Calendar cal = Calendar.getInstance();
        int i = cal.get(Calendar.DAY_OF_WEEK);
        i = (i+a)%7;
        switch (i) {
            case 1:
                return "星期日";
            case 2:
                return "星期一";
            case 3:
                return "星期二";
            case 4:
                return "星期三";
            case 5:
                return "星期四";
            case 6:
                return "星期五";
            case 0:
                return "星期六";
            default:
                return "";
        }
    }
//将天气数据展示到页面上
    private void showWeatherInfo(HeWeather6 weather){
        loadBackgrounPic();

        //用广播将天气显示到屏幕上
        Intent intent1 = new Intent();
        intent1.putExtra("weather",weather.now.weatherCond );
        intent1.setAction("com.get.WEATHERSUCCESS");
        sendBroadcast(intent1);

        code = weather.now.weatherCondCode;
        String cityName = weather.basic.cityName;
        weatherId = weather.basic.weatherId;
        String updateTime = weather.update.updateTime.split(" ")[1];
        String degree = weather.now.nowTemperature + "℃";
        String weatherCond = weather.now.weatherCond;
        titleCity.setText(cityName);
        titleUpdateTime.setText(updateTime);
        degreeText.setText(degree);
        weatherCondText.setText(weatherCond);
        forecastLayout.removeAllViews();
        hourlylayout.removeAllViews();
        int i =0;
        //10日天气加载
        for(Forecast forecast:weather.forecastList){
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item,forecastLayout,false);
            int Id = getResource(forecast.weatherCondCode_d);
            TextView dateText = view.findViewById(R.id.date_text);
            TextView dateweek = view.findViewById(R.id.date_week);
            ImageView infoText = view.findViewById(R.id.info_text);
            TextView maxText = view.findViewById(R.id.max_text);
            TextView minText = view.findViewById(R.id.min_text);
            String week = getWeek(i);
            if(i == 0){
                week="今　天";
            }
            else if(i == 1){
                week="明　天";
            }
            dateText.setText(forecast.date.split("-")[1]+"/"+forecast.date.split("-")[2]);
            dateweek.setText(week);
            infoText.setImageResource(Id);
            maxText.setText(forecast.maxTemperature+"℃");
            minText.setText(forecast.minTemperature+"℃");
            forecastLayout.addView(view);
            i++;
        }
        //24小时天气加载
        for(Hourly hourly:weather.hourlyList){
            View view = LayoutInflater.from(this).inflate(R.layout.hourly_item,hourlylayout,false);
            int Id = getResource(hourly.cond_code);
            TextView hourtext = view.findViewById(R.id.hour_text);
            ImageView hourinfo = view.findViewById(R.id.hour_info_text);
            TextView hournow = view.findViewById(R.id.hour_now_text);
            hourtext.setText(hourly.time.split(" ")[1]);
            hourinfo.setImageResource(Id);
            hournow.setText(hourly.tmp+"℃");
            hourlylayout.addView(view);
        }
        aqiText.setText(weather.lifestyleList.get(7).brief);
        pm25Text.setText("0.2");
        comfortText.setText(weather.lifestyleList.get(0).brief);
        suggestionText.setText(weather.lifestyleList.get(0).suggestion);
        weatherLayout.setVisibility(View.VISIBLE);
    }

    /**
     * 加载背景图片
     */
    public void loadBackgrounPic(){//加载背景图片
        String requestBackgrounPic = "https://downloadt.vivo.com.cn/theme/wallpaper/15459021614227724.jpg";//背景图片地址
        Glide.with(WeatherActivity.this).load(requestBackgrounPic).into(weatherBackgroundImg);

    }
}
