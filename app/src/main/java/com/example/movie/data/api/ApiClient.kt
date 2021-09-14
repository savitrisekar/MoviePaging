package com.example.movie.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//    https://api.themoviedb.org/3/movie/upcoming?api_key=40bb9b419730b4e8c5b34f37897995d0&page=2
//    https://api.themoviedb.org/3/movie/559907?api_key=40bb9b419730b4e8c5b34f37897995d0
//    https://image.tmdb.org/t/p/w342/if4hw3Ou5Sav9Em7WWHj66mnywp.jpg
//    https://api.themoviedb.org/3/

object ApiClient {
    fun build(): ApiService {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.themoviedb.org/3/")
            .build()
        return retrofit.create(ApiService::class.java)
    }
}