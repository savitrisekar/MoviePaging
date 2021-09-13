package com.example.movie.data.api

import com.example.movie.data.model.PopularDetails
import com.example.movie.data.model.PopularResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {

//    https://api.themoviedb.org/3/movie/upcoming?api_key=40bb9b419730b4e8c5b34f37897995d0&page=2
//    https://api.themoviedb.org/3/movie/559907?api_key=40bb9b419730b4e8c5b34f37897995d0
//    https://api.themoviedb.org/3/

    @GET("movie/popular")
    fun getUpcomingMovie(@Query("page")page: Int) : Single<PopularResponse>

    @GET("movie/{movie_id}")
    fun getUpcomingDetails(@Path("movie_id") id: Int) : Single<PopularDetails>
}