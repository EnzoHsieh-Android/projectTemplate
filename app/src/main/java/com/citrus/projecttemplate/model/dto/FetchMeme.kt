package com.citrus.projecttemplate.model.dto

import com.google.gson.annotations.SerializedName

data class Welcome (
    val success: Boolean,
    val data: Data
)


data class Data (
    val memes: List<Meme>
)


data class Meme (
    val id: String = "default",
    val name: String = "default",
    val url: String = "",
    val width: Int = 0,
    val height: Int = 0,

    @SerializedName("box_count")
    val boxCount: Int = 0
)


