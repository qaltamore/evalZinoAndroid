package com.example.fulminight.zino

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import kotlinx.android.synthetic.main.activity_add_message.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AddMessageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_message)

        var i = this.intent
        var topicId = i.getIntExtra("topicId", 1)

        findViewById<Button>(R.id.validateNewMessage).setOnClickListener {
            var messageContent = newMessageContent.text.toString()
            var url = "http://192.168.43.224:2999/topics/"+topicId+"/"
            var retrofit = Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            var service = retrofit.create(ForumService::class.java)
            var hasBeenAdded = service.newMessage(topicId, messageContent)

            hasBeenAdded.enqueue(object : Callback<Boolean> {
                override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                    if(response.body() == true)
                        println("SUCCES : Message Added")
                    else
                        error("onResponse KO : Message Not Added")
                }

                override fun onFailure(call: Call<Boolean>?, t: Throwable?) {
                    error("onFailure KO : Message Not Added")
                }
            })

            var i = Intent(this, MessagesListActivity::class.java)
            startActivity(i)
        }
    }
}
