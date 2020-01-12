package com.sargis.guardiannews.guadriandsapi

import android.util.Log
import androidx.paging.PageKeyedDataSource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GuardianDataSource : PageKeyedDataSource<Int, ArticleModel>() {

    private val api = GuardiansService.getApi()

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, ArticleModel>
    ) {
        api.getArticles("10", 1, params.requestedLoadSize).enqueue(object : Callback<MainResponse> {
            override fun onFailure(call: Call<MainResponse>, t: Throwable) {
                Log.e("GuardianService", "Failure")

            }

            override fun onResponse(call: Call<MainResponse>, response: Response<MainResponse>) {
                val resp = response.body()?.articleResponse
                callback.onResult(
                    resp!!.articleModels,
                    maxOf(resp.currentPage - 1, 1),
                    (resp.currentPage + 1)
                )
            }
        })
    }

    override fun loadAfter(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, ArticleModel>
    ) {
        api.getArticles("10", params.key, params.requestedLoadSize)
            .enqueue(object : Callback<MainResponse> {
                override fun onFailure(call: Call<MainResponse>, t: Throwable) {
                    Log.e("GuardianService", "Failure")
                }

                override fun onResponse(
                    call: Call<MainResponse>,
                    response: Response<MainResponse>
                ) {
                    val resp = response.body()?.articleResponse
                    callback.onResult(
                        resp!!.articleModels,
                        (resp.currentPage + 1)
                    )
                }
            })
    }

    override fun loadBefore(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, ArticleModel>
    ) {
        api.getArticles("10", params.key, params.requestedLoadSize)
            .enqueue(object : Callback<MainResponse> {
                override fun onFailure(call: Call<MainResponse>, t: Throwable) {
                    Log.e("GuardianService", "Failure")
                }

                override fun onResponse(
                    call: Call<MainResponse>,
                    response: Response<MainResponse>
                ) {
                    val resp = response.body()?.articleResponse
                    callback.onResult(
                        resp!!.articleModels,
                        maxOf(resp.currentPage - 1, 1)
                    )
                }
            })
    }

}