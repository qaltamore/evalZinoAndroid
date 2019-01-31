package com.example.fulminight.zino

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

class Topic(val id: Int, val title: String)
class Message(val topicId: Int, val messageId: Int, val content: String)

interface ForumService {
    @GET("/topics")
    fun listTopics(): Call<List<Topic>>

    @GET("/topics/{topicId}")
    fun listMessages(@Query("topicId") idTopic: Int): Call<List<Message>>

    @POST("/topics/new")
    fun newTopic(@Query("title") title: String): Call<Boolean>

    @POST("/topics/{topicId}/new")
    fun newMessage(@Query("topicId") topicId: Int, @Query("messageContent") messageContent: String): Call<Boolean>
}