package com.example.movie.data.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

//    https://api.themoviedb.org/3/movie/popular?api_key=40bb9b419730b4e8c5b34f37897995d0&page=1
//    https://api.themoviedb.org/3/movie/559907?api_key=40bb9b419730b4e8c5b34f37897995d0
//    https://image.tmdb.org/t/p/w342/if4hw3Ou5Sav9Em7WWHj66mnywp.jpg

const val API_KEY = "40bb9b419730b4e8c5b34f37897995d0"
const val BASE_URL = "https://api.themoviedb.org/3/"
const val POSTER_BASE_URL = "https://image.tmdb.org/t/p/w342/"
const val FIRST_PAGE = 449
const val POST_PER_PAGE = 20

object ApiClient {
    fun getClient(): ApiInterface {
        //create interceptor to put API key
        val requestInterceptor = Interceptor { chain ->
            //interceptor take only one argument which is a lambda function so paranthesis can be omitted
            val url = chain.request()
                .url()
                .newBuilder()
                .addQueryParameter("api_key", API_KEY)
                .build()

            val request = chain.request()
                .newBuilder()
                .url(url)
                .build()

            return@Interceptor chain.proceed(request)
        }

        val okHttpClient: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(requestInterceptor)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)

    }
}