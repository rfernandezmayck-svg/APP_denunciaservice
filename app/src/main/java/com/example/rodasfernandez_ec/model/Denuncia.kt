package com.example.rodasfernandez_ec.model

import com.google.gson.annotations.SerializedName

data class Denuncia(
    @SerializedName("id")
    val id: Long? = null,

    @SerializedName("dni")
    val dni: String,

    @SerializedName("tipo")
    val tipo: String,

    @SerializedName("direccion")
    val direccion: String,

    @SerializedName("descripcion")
    val descripcion: String,

    @SerializedName("fecha")
    val fecha: String? = null,

    @SerializedName("estado")
    val estado: Int? = null
)
