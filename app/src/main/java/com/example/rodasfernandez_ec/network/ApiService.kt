package com.example.rodasfernandez_ec.network

import com.example.rodasfernandez_ec.model.Denuncia
import com.example.rodasfernandez_ec.model.DenunciaPage
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @POST("denuncias/")
    fun registrarDenuncia(@Body denuncia: Denuncia): Call<Denuncia>

    @GET("denuncias/")
    fun listarDenuncias(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): Call<DenunciaPage>

    @GET("denuncias/{id}")
    fun obtenerDenunciaPorId(@Path("id") id: Long): Call<Denuncia>

    @GET("denuncias/ciudadano/{dni}")
    fun obtenerDenunciasPorDni(@Path("dni") dni: String): Call<List<Denuncia>>

    @PUT("denuncias/{id}")
    fun atenderDenuncia(@Path("id") id: Long): Call<Denuncia>
}
