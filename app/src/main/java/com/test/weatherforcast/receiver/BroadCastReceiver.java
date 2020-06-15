package com.test.weatherforcast.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class BroadCastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String weather = intent.getStringExtra("weather");
        Toast.makeText(context, "获取成功,天气：" + weather, Toast.LENGTH_SHORT).show();
    }
}
