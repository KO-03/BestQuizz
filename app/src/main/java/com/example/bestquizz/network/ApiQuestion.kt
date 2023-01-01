package com.example.bestquizz.network

import com.example.bestquizz.model.QuestionResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

object ApiQuestion {
    // URL de l'api
    private val BASE_URL = "https://opentdb.com/"

    // initialisation de l'objet Moshi qui gère les api
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(BASE_URL)
            .build()
    }

    // intialisation de la class
    val questionService: QuestionService by lazy {
        retrofit.create(QuestionService::class.java)
    }

}

interface QuestionService {
    // interface où on met les route de notre api
    @GET("api.php")
    fun fetchQuestions(
        @Query("amount") amount: Int, @Query("category") category: Int,
        @Query("difficulty") difficulty: String, @Query("type") type: String
    ): Call<QuestionResponse>
}