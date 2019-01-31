package com.example.fulminight.zino

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory



/*class ExampleSingleton {
    companion object {
        var stockage : Int = 6
    }
}*/

class MainActivity : AppCompatActivity() {

    class MainActivityAdapter(context: Context?, resource: Int, textViewResourceId: Int) : ArrayAdapter<String>(context, resource, textViewResourceId)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Récupérer nom d'utilisateur ?
        //Build.MODEL

        var topicListAdaptater = MainActivity.MainActivityAdapter(this, R.layout.line, R.id.topicTitle)

        var t = Thread(Runnable {
            var url = "http://192.168.43.224:2999/topics/"
            var retrofit = Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            var service = retrofit.create(ForumService::class.java)
            var topicRequest = service.listTopics()

            runOnUiThread {
                topicRequest.enqueue(object : Callback<List<Topic>> {
                    override fun onResponse(call: Call<List<Topic>>, response: Response<List<Topic>>) {
                        var allTopics = response.body()
                        if (allTopics != null) {
                            for (t in allTopics) {
                                topicListAdaptater.add(t.title)
                                println(" one topic : ${t.id} - ${t.title} ")
                            }
                            topicsList.adapter = topicListAdaptater
                        }
                    }

                    override fun onFailure(call: Call<List<Topic>>, t: Throwable) {
                        error("onFailure KO")
                    }
                })
            }
        })
        t.start()

        findViewById<Button>(R.id.addTopic).setOnClickListener {
            var i = Intent(this, AddTopicActivity::class.java)
            startActivity(i)
        }

        findViewById<ListView>(R.id.topicsList).setOnItemClickListener { parent, view, position, id ->
            var i = Intent(this, MessagesListActivity::class.java)
            println(position + 1)
            i.putExtra("topicId", position + 1)
            startActivity(i)
        }

    }
}
