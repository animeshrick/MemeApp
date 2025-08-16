package com.memeapplication

import com.memeapplication.ApiEndpoints.GET_MEME
import retrofit2.Call
import retrofit2.http.GET

interface ApiInterface {
    @GET(GET_MEME)
    fun getMemeApiData(): Call<MemeDataClass>
}