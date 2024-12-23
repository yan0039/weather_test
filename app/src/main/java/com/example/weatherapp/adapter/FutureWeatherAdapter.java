package com.example.weatherapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp.R;
import com.example.weatherapp.bean.DayWeatherBean;

import java.util.List;

public class FutureWeatherAdapter extends RecyclerView.Adapter<FutureWeatherAdapter.WeatherViewHolder> {
    private Context mContext;
    private List<DayWeatherBean> mWeatherBeans;

    public FutureWeatherAdapter(Context context,List<DayWeatherBean> weatherBeans) {
        mContext = context;
        this.mWeatherBeans = weatherBeans;
    }

    @NonNull
    @Override
    public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.weather_item_layout, parent, false);
        WeatherViewHolder weatherViewHolder = new WeatherViewHolder(view);

        return weatherViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherViewHolder holder, int position) {
        DayWeatherBean weatherBean = mWeatherBeans.get(position);
        holder.tvWeather.setText(weatherBean.getWea());
        holder.tvTem.setText(weatherBean.getTem1() + "°C");
        holder.tvWin.setText(weatherBean.getWin());
        holder.tvDate.setText(weatherBean.getDate());
        holder.tvTemLowHigh.setText(weatherBean.getTem2() + "°C"+"~" + weatherBean.getTem1() + "°C");
        holder.ivWeather.setImageResource(getImgResOfWeather(weatherBean.getWeaImg()));
    }


    @Override
    public int getItemCount() {
//        if (mWeatherBeans == null){
//            return 0;
//        }
        return (mWeatherBeans == null) ? 0 : mWeatherBeans.size();// 有多少个Item，有多少项，返回出去
    }



    class WeatherViewHolder extends  RecyclerView.ViewHolder{

        TextView tvWeather,tvTem,tvTemLowHigh,tvWin,tvDate;
        ImageView ivWeather;
        public WeatherViewHolder(@NonNull View itemView) {
            super(itemView);

            ivWeather = itemView.findViewById(R.id.iv_weather);
            tvWeather = itemView.findViewById(R.id.tv_weather);
            tvTem = itemView.findViewById(R.id.tv_tem);
            tvTemLowHigh = itemView.findViewById(R.id.tv_tem_low_high);
            tvWin = itemView.findViewById(R.id.tv_win);
            tvDate = itemView.findViewById(R.id.tv_date);

        }
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
}
