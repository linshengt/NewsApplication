package com.linshengt.newsapplication.Model;

import android.content.Context;
import android.os.Handler;
import android.os.Message;


import com.linshengt.newsapplication.Bean.NewsInfo;
import com.linshengt.newsapplication.Utils.HLog;
import com.linshengt.volley.Request;
import com.linshengt.volley.RequestQueue;
import com.linshengt.volley.Response;
import com.linshengt.volley.VolleyError;
import com.linshengt.volley.toolbox.JsonObjectRequest;
import com.linshengt.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by linshengt on 2016/9/8.
 */
public class LoadNewsModel {
    private String TAG = "LoadNewsModel";
    private Context context;
    private List<NewsInfo> list;
    private final int REPEAT_MSG = 0x01;
    private final int CALLBACK_OK = 0x02;
    private final int CALLBACK_ERROR = 0x04;

    public LoadNewsModel(Context context) {
        this.context = context;
    }

    public void getNewsFromNet(String newsType){
        String JSONDateUrl = String.format("http://v.juhe.cn/toutiao/index?type=%s&key=2efd2343e08092f8b1b3b244ac9cb8a0", newsType);
        HLog.i(TAG, JSONDateUrl);
        list = new ArrayList<>();

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, JSONDateUrl, null,
                new Response.Listener<JSONObject>() {
                    public void onResponse(JSONObject response) {
                        HLog.i(TAG, response.toString());
                        parseNews(response, context);
                    }
                }, new Response.ErrorListener() {
            public void onErrorResponse(
                    VolleyError arg0) {
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void parseNews(JSONObject jsonObject, Context Context){
        int resultcode, errorcode;

        try {
            errorcode = jsonObject.getInt("error_code");

            if (errorcode == 0){
                JSONObject result = jsonObject.getJSONObject("result");

                JSONArray jsonArray = result.getJSONArray("data");

                HLog.i(TAG, "parseNews-->1");
                HLog.i(TAG, "jsonArray.length()-->"+jsonArray.length());
                for(int i = 0; i < jsonArray.length(); i++){
//                for(int i = 0; i < 1; i++){

                    JSONObject news = jsonArray.getJSONObject(i);
                    HLog.i(TAG, "parseNews-->news" + news);

                    NewsInfo newsInfo = new NewsInfo();
                    if(news.has("title")==false){
                        continue;
                    }
                    newsInfo.setTitle(news.getString("title"));
                    HLog.i(TAG, "parseNews-->2");


                    if(news.has("date")==false){
                        continue;
                    }
                    newsInfo.setDate(news.getString("date"));

                    HLog.i(TAG, "parseNews-->2");

                    newsInfo.setAuthor_name(news.getString("author_name"));

                    HLog.i(TAG, "parseNews-->3");

                    newsInfo.setThumbnail_pic_s(news.getString("thumbnail_pic_s"));

                    HLog.i(TAG, "parseNews-->4");
                    newsInfo.setUrl(news.getString("url"));
                    HLog.i(TAG, "parseNews-->5");
                    newsInfo.setCategory(news.getString("category"));
                    HLog.i(TAG, "parseNews-->6");

                    newsInfo.setCollection(0);
                    list.add(newsInfo);
                }
                HLog.i(TAG, "parseNews-->2");
                handler.sendEmptyMessage(CALLBACK_OK);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case CALLBACK_OK:
                    if (callback != null){
                        HLog.i(TAG, "Handler-->callback!=null");
                        callback.getNewsInfo(list);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    public void setCallback(LoadNewsModel.callback callback) {
        this.callback = callback;
    }

    private callback callback = null;
    public interface callback{
        public void getNewsInfo(List<NewsInfo> list);
    }

}
