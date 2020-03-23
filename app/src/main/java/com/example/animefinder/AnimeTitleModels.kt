package com.example.animefinder

import com.google.gson.annotations.SerializedName

class AnimeTitle(
    val title_english: String?,
    val title_native: String?,
    val anilist_id: String?,
    val episode: String?,
    val similarity: Float
)