package com.example.prov_proyecto

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface RecetasAPI {
    // Tipo de solicitud que queremos hacer en el API
    @GET
    // Regresará los datos del API
    fun getRecetas(@Url url: String): Call<RecetasResponse>
}