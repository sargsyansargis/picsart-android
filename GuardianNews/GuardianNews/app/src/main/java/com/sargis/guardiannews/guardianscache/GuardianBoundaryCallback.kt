package com.sargis.guardiannews.guardianscache

import android.util.Log
import androidx.paging.PagedList
import com.sargis.guardiannews.guadriandsapi.ArticleModel
import com.sargis.guardiannews.guadriandsapi.GuardiansService
import com.sargis.guardiannews.guadriandsapi.MainResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executors
import kotlin.math.max

class GuardianBoundaryCallback(val db: GuardianDB) : PagedList.BoundaryCallback<ArticleModel>() {

    private val api = GuardiansService.getApi()
    private val executor = Executors.newSingleThreadExecutor()
    private val helper = PagingRequestHelper(executor)
    override fun onZeroItemsLoaded() {
        super.onZeroItemsLoaded()
        helper.runIfNotRunning(
            PagingRequestHelper.RequestType.INITIAL,
            object : PagingRequestHelper.Request {
                override fun run(callback: PagingRequestHelper.Request.Callback?) {
                    api.getArticles("10", 1, 30)
                        //2
                        .enqueue(
                            object : Callback<MainResponse> {

                                override fun onFailure(call: Call<MainResponse>?, t: Throwable) {
                                    //3
                                    Log.e(
                                        "BoundaryCallback",
                                        "Failed to load data on initial load!"
                                    )
                                    callback?.recordFailure(t)

                                }

                                override fun onResponse(
                                    call: Call<MainResponse>?,
                                    response: Response<MainResponse>
                                ) {
                                    //4
                                    val posts = response.body()?.articleResponse?.articleModels
                                    posts?.forEach {
                                        it.page = response.body()?.articleResponse?.currentPage ?: 1
                                    }
                                    executor.execute {
                                        db.articleDao().insert(posts ?: listOf())
                                        callback?.recordSuccess()
                                    }
                                }
                            })
                }
            })
    }

    override fun onItemAtEndLoaded(itemAtEnd: ArticleModel) {
        super.onItemAtEndLoaded(itemAtEnd)
        helper.runIfNotRunning(PagingRequestHelper.RequestType.AFTER,
            object : PagingRequestHelper.Request {
                override fun run(callback: PagingRequestHelper.Request.Callback?) {
                    api.getArticles("10", itemAtEnd.page + 1, 30)
                        .enqueue(object : Callback<MainResponse> {

                            override fun onFailure(call: Call<MainResponse>?, t: Throwable) {
                                Log.e("BoundaryCallback", "Failed to load data on end loaded!")
                                callback?.recordFailure(t)
                            }

                            override fun onResponse(
                                call: Call<MainResponse>?,
                                response: Response<MainResponse>
                            ) {

                                val posts = response.body()?.articleResponse?.articleModels
                                posts?.forEach {
                                    it.page = response.body()?.articleResponse?.currentPage ?: 1
                                }
                                executor.execute {
                                    db.articleDao().insert(posts ?: listOf())
                                    callback?.recordSuccess()
                                }
                            }
                        })
                }
            })
    }

    override fun onItemAtFrontLoaded(itemAtFront: ArticleModel) {
        super.onItemAtFrontLoaded(itemAtFront)
        if (itemAtFront.page != 1) {
            helper.runIfNotRunning(
                PagingRequestHelper.RequestType.AFTER,
                object : PagingRequestHelper.Request {
                    override fun run(callback: PagingRequestHelper.Request.Callback?) {
                        api.getArticles("10", max(itemAtFront.page - 1, 1), 30)
                            .enqueue(object : Callback<MainResponse> {

                                override fun onFailure(call: Call<MainResponse>?, t: Throwable) {
                                    Log.e(
                                        "BoundaryCallback",
                                        "Failed to load data on front loaded!"
                                    )
                                    callback?.recordFailure(t)
                                }

                                override fun onResponse(
                                    call: Call<MainResponse>?,
                                    response: Response<MainResponse>
                                ) {

                                    val posts = response.body()?.articleResponse?.articleModels

                                    posts?.forEach {
                                        it.page = response.body()?.articleResponse?.currentPage ?: 1
                                    }

                                    executor.execute {
                                        db.articleDao().insert(posts ?: listOf())
                                        callback?.recordSuccess()
                                    }
                                }
                            })
                    }
                }
            )
        }
    }
}