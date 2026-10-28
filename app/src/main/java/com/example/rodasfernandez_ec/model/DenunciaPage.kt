package com.example.rodasfernandez_ec.model

import com.google.gson.annotations.SerializedName

data class DenunciaPage(
    @SerializedName("content")
    val content: List<Denuncia> = emptyList(),

    @SerializedName("totalElements")
    val totalElements: Long? = null,

    @SerializedName("totalPages")
    val totalPages: Int? = null,

    @SerializedName("size")
    val size: Int? = null,

    @SerializedName("number")
    val number: Int? = null,

    @SerializedName("last")
    val last: Boolean? = null
)
