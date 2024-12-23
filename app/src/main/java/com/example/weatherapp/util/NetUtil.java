package com.example.weatherapp.util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class NetUtil {

    public static final String URL_WEATHER_WITH_FUTURE = "https://v1.yiketianqi.com/free/week?unescape=1&appid=42898523&appsecret=1i9AflJR";
//"https://tianqiapi.com/api?version=v1&appid=42898523&appsecret=1i9AflJR"

//    public static String doGet(String urlStr) {
//        String result = "";
//        HttpURLConnection connection = null;
//        InputStreamReader inputStreamReader = null;
//        BufferedReader bufferedReader = null;
//        // 连接网络
//        try {
//            URL urL = new URL(urlStr);
//            connection = (HttpURLConnection) urL.openConnection();
//            connection.setRequestMethod("GET");
//            connection.setConnectTimeout(5000);
//            connection.setReadTimeout(5000);
//
//            connection.connect();
//            int responseCode = connection.getResponseCode();
//            if (responseCode == 200) {
//                InputStream inputStream = connection.getInputStream();
//                inputStreamReader = new InputStreamReader(inputStream);
//                bufferedReader = new BufferedReader(inputStreamReader);
//
//                StringBuilder stringBuilder = new StringBuilder();
//                String line = "";
//                while ((line = bufferedReader.readLine()) != null) {
//                    stringBuilder.append(line);
//                }
//                result = stringBuilder.toString();
//            } else {
//                Log.e("fan", "请求失败，状态码：" + responseCode);
//            }
//
//
//            // 从连接中读取数据（二进制）
//            InputStream inputStream = connection.getInputStream();
//            inputStreamReader = new InputStreamReader(inputStream);
//
//            // 二进制流
//            bufferedReader = new BufferedReader(inputStreamReader);
//
//            // 从缓存区中一行行读取字符串
//            StringBuilder stringBuilder = new StringBuilder();
//            String line = "";
//            while ((line = bufferedReader.readLine()) != null) {
//                stringBuilder.append(line);
//            }
//            result = stringBuilder.toString();
//
//        } catch (Exception e) {
//            Log.e("fan", "请求失败：" + e.toString());
//            e.printStackTrace();
//        }finally {
//            if (connection != null) {
//                connection.disconnect();
//            }
//
//            if (inputStreamReader != null) {
//                try {
//                    inputStreamReader.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            if (bufferedReader != null) {
//                try {
//                    bufferedReader.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        Log.d("fan", "doGet:result is :  " + result);
//        return result;
//    }
    public static String doGet(String urlStr) {
        String result = "";
        HttpURLConnection connection = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
    
        try {
            URL urL = new URL(urlStr);
            connection = (HttpURLConnection) urL.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            // 设置 User-Agent
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");

            connection.connect();
            int responseCode = connection.getResponseCode();
            Log.d("fan", "HTTP状态码：" + responseCode);

            if (responseCode == 200) {
                InputStream inputStream = connection.getInputStream();
                inputStreamReader = new InputStreamReader(inputStream);
                bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder stringBuilder = new StringBuilder();
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                result = stringBuilder.toString();
                Log.d("fan", "完整返回结果：" + result);
            } else {
                Log.e("fan", "请求失败，状态码：" + responseCode);
            }
        } catch (Exception e) {
            Log.e("fan", "请求失败：" + e.toString());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            try {
                if (inputStreamReader != null) inputStreamReader.close();
                if (bufferedReader != null) bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }


    public static String GetWeatherOfCity(String city) {
        // 拼接出获取天气数据的URL
        // http://v1.yiketianqi.com/free/week?unescape=1&appid=42898523&appsecret=1i9AflJR
        String cityEncoded = null;
        try {
            cityEncoded = URLEncoder.encode(city, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        String weatherUrl = URL_WEATHER_WITH_FUTURE + "&city=" + cityEncoded;

//        String weatherUrl = URL_WEATHER_WITH_FUTURE + "&city=" + city;
        Log.d("fan","------------weather-------------" + weatherUrl);
        String weatherResult = doGet(weatherUrl);
        Log.d("fan","----------weatherResult---------" + weatherResult);
        return weatherResult;
    }
}
