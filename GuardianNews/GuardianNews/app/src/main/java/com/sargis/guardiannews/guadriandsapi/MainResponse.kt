package com.sargis.guardiannews.guadriandsapi

import com.google.gson.annotations.SerializedName

data class MainResponse(
    @SerializedName("response")
    val articleResponse: RepoResponse
)