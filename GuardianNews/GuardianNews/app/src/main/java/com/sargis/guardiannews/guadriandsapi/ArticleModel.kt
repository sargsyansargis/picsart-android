package com.sargis.guardiannews.guadriandsapi

import com.google.gson.annotations.SerializedName


import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.sargis.guardiannews.guardianscache.Converter

@Entity(tableName = "articles")
data class ArticleModel(
    @PrimaryKey @field:SerializedName("id") val id: String,
    @field:SerializedName("type") val type: String,
    @field:SerializedName("sectionId") val sectionId: String,
    @field:SerializedName("webTitle") val webTitle: String,
    @field:SerializedName("webPublicationDate") val webPublicationDate: String,

    @TypeConverters(Converter::class)
    @field:SerializedName("fields") val fields: ArticleFields?,
    @field:SerializedName("webUrl") val webUrl: String,
    @field:SerializedName("apiUrl") val apiUrl: String,
    @field:SerializedName("sectionName") val sectionName: String,
    @field:SerializedName("page") var page: Int,
    @field:SerializedName("liked") var liked: Boolean = false,
    @field:SerializedName("saved") var saved: Boolean = false,
    @field:SerializedName("deleted") var deleted: Boolean = false
)
