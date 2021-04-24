package com.apps.newsapp;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NewsItemClicked {

    RecyclerView recyclerView;
    ArrayList<News> newsArrayList;
    NewListAdapter adapter;
    private RequestQueue requestQueue;
    JSONArray jsonArray;
    ProgressBar progressBar;
    boolean loadMore = false;
    int length = 0;
    boolean allLoaded = false;
    FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progressbar);
        newsArrayList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);

        ActionBar actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#ffffff"));
        actionBar.setBackgroundDrawable(colorDrawable);
        actionBar.setTitle(Html.fromHtml("<center><font color='#000000'>N E W S</font></center>"));

        fab = findViewById(R.id.fab);
        fab.setVisibility(View.GONE);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                layoutManager.scrollToPositionWithOffset(0, 0);
            }
        });


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        adapter = new NewListAdapter(newsArrayList, MainActivity.this);
        recyclerView.setAdapter(adapter);
        requestQueue = Volley.newRequestQueue(this);
        fetchData();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = ((LinearLayoutManager) recyclerView.getLayoutManager());
                int lastVisiblePosition = layoutManager.findLastVisibleItemPosition();

                if (lastVisiblePosition >= 2) {
                    fab.setVisibility(View.VISIBLE);
//                    fab.startAnimation(fadeIn);
                } else {
                    fab.setVisibility(View.GONE);
//                    fab.startAnimation(fadeOut);

                }
                Log.d("TAG", "onScrolled: " + lastVisiblePosition);
                if (!allLoaded) {
                    if (lastVisiblePosition == length - 1) {
                        Log.d("TAG", "onScrolled: started");
                        if (length <= newsArrayList.size()) {
                            if (!loadMore) {
                                progressBar.setVisibility(View.VISIBLE);
                                loadMore = true;
                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        loadMore = false;
                                        progressBar.setVisibility(View.GONE);
                                        loadMoreNews();
                                    }
                                }, 2000);
                            }
                        } else {
                            allLoaded = true;
                        }
                    }
                }

            }
        });
    }


    private void fetchData() {
        String url = "https://saurav.tech/NewsAPI/top-headlines/category/health/in.json";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("TAG", "onResponse: got response");
                    jsonArray = response.getJSONArray("articles");
                    Log.d("TAG", "onResponse: " + jsonArray.toString());
                    for (int i = length; i < length + 10; i++) {
                        JSONObject x = jsonArray.getJSONObject(i);
                        News news = new News(
                                x.getString("title"),
                                x.getString("author"),
                                x.getString("url"),
                                x.getString("description"),
                                x.getString("urlToImage")
                        );
                        newsArrayList.add(news);
                        Log.d("TAG", "onResponse: " + news.getTitle());
                    }
                    length += 10;
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    private void loadMoreNews() {
        Log.d("TAG", "loadMoreNews: " + allLoaded);
        try {
            int f = jsonArray.length() - length >= 10 ? length + 10 : jsonArray.length();
            for (int i = length; i < f; i++) {
                JSONObject x = jsonArray.getJSONObject(i);
                News news = new News(
                        x.getString("title"),
                        x.getString("author"),
                        x.getString("url"),
                        x.getString("description"),
                        x.getString("urlToImage")
                );
                newsArrayList.add(news);
                Log.d("TAG", "onResponse: " + news.getTitle());
            }
            length += 10;
            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClicked(News item) {
        Toast.makeText(this, "Redirecting...", Toast.LENGTH_SHORT).show();
        String url = item.getUrl();
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, Uri.parse(url));
    }
} 