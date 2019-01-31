package com.example.fulminight.zino

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import kotlinx.android.synthetic.main.activity_add_topic.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AddTopicActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_topic)

        findViewById<Button>(R.id.validateNewTopic).setOnClickListener {

            var url = "http://192.168.43.224:2999/topics/new/"
            var retrofit = Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            var service = retrofit.create(ForumService::class.java)
            var hasBeenAdded = service.newTopic(newTopicEditText.text.toString())

            hasBeenAdded.enqueue(object : Callback<Boolean> {
                override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                    if(response.body() == true)
                        println("SUCCES : Topic Added")
                    else
                        error("onResponse KO : Topic Not Added")
                }

                override fun onFailure(call: Call<Boolean>?, t: Throwable?) {
                    error("onFailure KO : Topic Not Added")
                }
            })

            var i = Intent(this, MainActivity::class.java)
            startActivity(i)
        }
    }
}
