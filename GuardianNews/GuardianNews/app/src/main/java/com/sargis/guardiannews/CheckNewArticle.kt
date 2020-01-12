package com.sargis.guardiannews

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.util.Log
import com.sargis.guardiannews.guadriandsapi.GuardiansService
import com.sargis.guardiannews.guadriandsapi.MainResponse
import com.sargis.guardiannews.guardianscache.GuardianDB
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CheckNewArticle(var context: Context) {

    fun execute() {
        GuardiansService.getApi().getArticles("10", 1, 5).enqueue(object : Callback<MainResponse> {
            override fun onFailure(call: Call<MainResponse>, t: Throwable) {

            }

            override fun onResponse(
                call: Call<MainResponse>,
                response: Response<MainResponse>
            ) {
                EXECUTOR.execute {
                    val isNewFeedItem = GuardianDB.getInstance(context).articleDao()
                        .findArticle(response.body()!!.articleResponse.articleModels[0].id) == null
                    if (isNewFeedItem) {
                       val  mainHandler = Handler(context.mainLooper)
                        val myRunnable = Runnable {
                            var intent = Intent()
                            intent.action = "com.sargis.guardiannews.android.action.newarticles"
                            intent.setClass(context, NewArticleBroadcast::class.java)
                            context.sendBroadcast(intent)
                            intent =Intent(LOCAL_ACTION)
                            context.sendBroadcast(intent)
                        }
                        mainHandler.post(myRunnable)

                    }
                }
            }

        })
    }

    companion object {
        val EXECUTOR: ExecutorService = Executors.newSingleThreadExecutor()
    }
}