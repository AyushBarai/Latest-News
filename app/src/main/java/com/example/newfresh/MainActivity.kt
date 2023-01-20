package com.example.newfresh

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest

class MainActivity : AppCompatActivity(), NewsItemCli {
    private lateinit var adapter: NewsListAda

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val recyclerview = findViewById<RecyclerView>(R.id.view)
        recyclerview.layoutManager = LinearLayoutManager(this)
        fetchData()
        adapter = NewsListAda(this)
        recyclerview.adapter = adapter

    }

    private fun fetchData() {
        val url = "https://newsapi.org/v2/everything?domains=wsj.com&X-Api-Key=09051d84a5f145a08e5a1858bd5ace40"
        val request = JsonObjectRequest(
            Request.Method.GET, url, null, {
                val newsjsonArray = it.getJSONArray("articles")
                val newsarray = ArrayList<News>()
                for(i in 0 until newsjsonArray.length()) {
                    val  newsjsonObject = newsjsonArray.getJSONObject(i)
                    val news = News(
                        newsjsonObject.getString("title"),
                        newsjsonObject.getString("author"),
                        newsjsonObject.getString("url"),
                        newsjsonObject.getString("urlToImage")
                    )
                    newsarray.add(news)
                }
                adapter.update(newsarray)
            }, {
            })




        // Add a request (in this example, called stringRequest) to your RequestQueue.
        MySingleton.getInstance(this).addToRequestQueue(request)
    }

    override fun onItemClicked(item: News) {
        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(this, Uri.parse(item.url))
    }
}