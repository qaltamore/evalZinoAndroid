package com.example.fulminight.zino

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ArrayAdapter
import android.widget.Button
import kotlinx.android.synthetic.main.activity_messages_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MessagesListActivity : AppCompatActivity() {

    class MessagesListActivityAdapter(context: Context?, resource: Int, textViewResourceId: Int) : ArrayAdapter<String>(context, resource, textViewResourceId)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messages_list)

        var i = this.intent
        var topicId = i.getIntExtra("topicId", 1)

        var messagesListAdaptater = MessagesListActivity.MessagesListActivityAdapter(this, R.layout.message_line, R.id.messageContent)

        var t = Thread(Runnable {
            var url = "http://192.168.43.224:2999/topics/"+topicId+"/"
            var retrofit = Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            var service = retrofit.create(ForumService::class.java)
            var messagesRequest = service.listMessages(topicId)

            runOnUiThread {
                messagesRequest.enqueue(object : Callback<List<Message>> {
                    override fun onResponse(call: Call<List<Message>>, response: Response<List<Message>>) {
                        var allMessages = response.body()
                        if (allMessages != null) {
                            for (m in allMessages) {
                                messagesListAdaptater.add(m.content)
                                println(" one message : ${m.content} ")
                            }
                            messagesList.adapter = messagesListAdaptater
                        }
                    }
                    override fun onFailure(call: Call<List<Message>>, t: Throwable) {
                        error("onFailure KO")
                    }
                })
            }
        })
        t.start()

        findViewById<Button>(R.id.addMessage).setOnClickListener {
            var i = Intent(this, AddMessageActivity::class.java)
            i.putExtra("topicId", topicId)
            startActivity(i)
        }
    }
}
