package com.sargis.guardiannews.guadriandsapi

import com.google.gson.annotations.SerializedName

data class RepoResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("userTier")
    val userTier: String,
    @SerializedName("total")
    val total: Int,
    @SerializedName("startIndex")
    val startIndex: Int,
    @SerializedName("pageSize")
    val pageSize: Int,
    @SerializedName("currentPage")
    val currentPage: Int,
    @SerializedName("pages")
    val pagesCount: Int,
    @SerializedName("orderBy")
    val orderBy: String,
    @SerializedName("results")
    val articleModels: List<ArticleModel>

)