package com.kinson.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ScrollView;

import com.android.pulltorefresh.PullToRefreshBase;
import com.android.pulltorefresh.PullToRefreshHorizontalScrollView;
import com.android.pulltorefresh.PullToRefreshScrollView;
import com.android.volley.service.IRequest;
import com.android.volley.service.IResponse;
import com.android.volley.service.NetworkDataCache;
import com.android.volley.service.VolleyService;
import com.kinson.demo.model.User;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private PullToRefreshScrollView pullToRefreshScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pullToRefreshScrollView =  this.findViewById(R.id.pullToRefreshScrollView);
        pullToRefreshScrollView.setPullToRefreshOverScrollEnabled(true);
        pullToRefreshScrollView.setScrollingWhileRefreshingEnabled(true);
        pullToRefreshScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                pullToRefreshScrollView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pullToRefreshScrollView.onRefreshComplete();
                    }
                },5000);
            }
        });


        loadDatas();
    }

    private void loadDatas() {
        String url = "";
        final HashMap<String, String> map = new HashMap<>();
        NetworkDataCache networkDataCache = new NetworkDataCache(this, url, map);
        String cacheData = networkDataCache.queryCacheData(url);

        VolleyService.newInstance("").setTypeClass(User.class).setRequest(new IRequest() {
            @Override
            public HashMap<String, String> getParams(String tag) {
                return map;
            }
        }).setResponse(new IResponse<User>() {
            @Override
            public void onSuccess(String tag, String srcData, User user) {

            }

            @Override
            public void onFailure(String tag, int errorCode, String errorMsg) {

            }
        }).doGet();
    }
}
