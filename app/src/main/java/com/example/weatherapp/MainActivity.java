package com.example.weatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.weatherapp.adapter.FutureWeatherAdapter;
import com.example.weatherapp.bean.DayWeatherBean;
import com.example.weatherapp.bean.WeatherBean;
import com.example.weatherapp.util.NetUtil;
import com.google.gson.Gson;

import java.util.List;
import java.util.concurrent.Future;

public class MainActivity extends AppCompatActivity {

    private AppCompatSpinner mSpinner;
    private ArrayAdapter<String> mSpAdapter;
    private String[] mCities;


    private TextView  tvWeather,tvTem,tvTemLowHigh,tvWin;
    private ImageView ivWeather;
    private RecyclerView rlvFutureWeather;
    private FutureWeatherAdapter mWeatherAdapter;
    private Handler mHandler = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                String weather = (String) msg.obj;
                Log.d("fan", "-------主线程收到了天气数据-weather---" + weather);

            Gson gson = new Gson();
            // 将weather解析成WeatherBean这个类
            WeatherBean weatherBean = gson.fromJson(weather, WeatherBean.class);
            Log.d("fan", "-------解析后的数据-weather---" + weatherBean.toString());

            updateUiOfWeather(weatherBean);

            }
        }
    };


    private void updateUiOfWeather(WeatherBean weatherBean) {
        // 对数据进行判空，防止空指针异常
        if (weatherBean == null) {
            return;
        }
        List<DayWeatherBean> dayWeathers = weatherBean.getDayWeathers();
        DayWeatherBean todayWeather = dayWeathers.get(0);
        if (todayWeather == null) {
            return;

        }
        tvTem.setText(todayWeather.getTem1() + "°C");
        tvWeather.setText(todayWeather.getWea() + "(" + todayWeather.getDate() + ")");
        tvTemLowHigh.setText(todayWeather.getTem2() + "°C" + "~" + todayWeather.getTem1() + "°C");
        tvWin.setText(todayWeather.getWin() + todayWeather.getWinSpeed());
//        tvAir.setText("空气" + todayWeather.getAir() + todayWeather.getAirLevel() + "\n" + todayWeather.getAirTips());
        ivWeather.setImageResource(getImgResOfWeather(todayWeather.getWeaImg()));

        dayWeathers.remove(0);// 去掉当前天的天气

        // 未来天气的展示
        mWeatherAdapter = new FutureWeatherAdapter(this,dayWeathers);



        rlvFutureWeather.setAdapter(mWeatherAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        rlvFutureWeather.setLayoutManager(layoutManager);
    }

    private int getImgResOfWeather(String weaStr) {
    // xue、lei、shachen、wu、bingbao、yun、yu、yin、qing
        int result = 0;
        switch (weaStr){
            case "qing":
                result = R.drawable.qingtian;
                break;
            case "yin":
                result = R.drawable.yin;
                break;
            case "yu":
                result = R.drawable.dayv;
                break;
            case "yun":
                result = R.drawable.ye_duoyvn;
                break;
            case "bingbao":
                result = R.drawable.bingbao;
                break;
            case "wu":
                result = R.drawable.wu;
                break;
            case "shachen":
                result = R.drawable.shacehnbao;
                break;
            case "lei":
                result = R.drawable.leiyv;
                break;
            case "xue":
                result = R.drawable.daxve;
                break;
            default:
                result = R.drawable.qingtian;
                break;
        }
        return result;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        rlvFutureWeather = findViewById(R.id.rlv_future_weather);
        // 对Spinner的操作
        mSpinner = findViewById(R.id.sp_city);
        mCities = getResources().getStringArray(R.array.cities);
        mSpAdapter = new ArrayAdapter<>(this, R.layout.sp_item_layout,mCities);
        mSpinner.setAdapter(mSpAdapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String selectedCity = mCities[position];
                getWeatherOfCity(selectedCity);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // 对控件进行绑定
        ivWeather = findViewById(R.id.iv_weather);
        tvWeather = findViewById(R.id.tv_weather);
        tvTem = findViewById(R.id.tv_tem);
        tvTemLowHigh = findViewById(R.id.tv_tem_low_high);
        tvWin = findViewById(R.id.tv_win);

    }

    private void getWeatherOfCity(String selectedCity) {
        // 开启子线程，请求网络
        new Thread(new Runnable() {
            @Override
            public void run() {
                String weatherOfCity = NetUtil.GetWeatherOfCity(selectedCity);
                // 使用handler将数据传递给主线程
                Message message = Message.obtain();// 比new Message效率高
                message.what = 0;// 用于标识
                message.obj = weatherOfCity;// 把数据放到消息里面去，发送出去
                mHandler.sendMessage(message);
            }
        }).start();
    }
}
// 别忘记在manifest写网络请求权限