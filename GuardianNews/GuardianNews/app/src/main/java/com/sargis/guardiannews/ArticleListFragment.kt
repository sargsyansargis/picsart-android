package com.sargis.guardiannews

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import com.sargis.guardiannews.guadriandsapi.ArticleModel
import com.sargis.guardiannews.guardianscache.GuardianBoundaryCallback
import com.sargis.guardiannews.guardianscache.GuardianDB
import com.sargis.guardiannews.guardianscache.SavedArticlesDB
import kotlinx.android.synthetic.main.fragment_articels_list.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class ArticlesListFragment : Fragment() {

    private lateinit var viewModel: ArticleListFragmentViewModel
    private val adapter = ArticleListAdapter()
    private lateinit var pageListData: LiveData<PagedList<ArticleModel>>
    private val localBroadcast = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            updateData()


        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ArticleListFragmentViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_articels_list, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter(savedInstanceState)

       activity?.registerReceiver(localBroadcast, IntentFilter(LOCAL_ACTION))
    }

    private fun updateData() {
        pageListData.value?.dataSource?.invalidate()
    }

    private fun initAdapter(bundle: Bundle?) {
        articles_list.adapter = adapter
        articles_list.layoutManager = LinearLayoutManager(context)
        adapter.articleListListener = object : ArticleListListener {
            override fun onItemChanged(model: ArticleModel) {
                EXECUTOR.execute { GuardianDB.getInstance(context!!).articleDao().update(model) }
            }

            override fun onItemDeleted(model: ArticleModel) {
                EXECUTOR.execute { GuardianDB.getInstance(context!!).articleDao().insert(model) }

            }

            override fun onItemSaved(model: ArticleModel) {
                EXECUTOR.execute {
                    if (model.saved) {
                        SavedArticlesDB.getInstance(context!!).articleDao().insert(model)
                    } else {
                        SavedArticlesDB.getInstance(context!!).articleDao().delete(model)
                    }
                }
            }
        }

        val config = PagedList.Config.Builder()
            .setPageSize(30)
            .setEnablePlaceholders(false)
            .build()

        pageListData = initializedPagedListBuilder(config, bundle).build()

        pageListData.observe(this, Observer<PagedList<ArticleModel>> { pagedList ->
            adapter.submitList(pagedList)
        })
    }

    private fun initializedPagedListBuilder(config: PagedList.Config, bundle: Bundle?):
            LivePagedListBuilder<Int, ArticleModel> {

        val dataSourceFactory =
            if (bundle == null && !verifyAvailableNetwork(activity!! as AppCompatActivity)) SavedArticlesDB.getInstance(
                context!!
            ).articleDao().posts() else
                GuardianDB.getInstance(context!!).articleDao().posts()

        val livePagedListBuilder =
            LivePagedListBuilder<Int, ArticleModel>(
                dataSourceFactory,
                config
            )

        livePagedListBuilder.setBoundaryCallback(
            GuardianBoundaryCallback(
                GuardianDB.getInstance(
                    context!!
                )
            )
        )


        return livePagedListBuilder
    }

    override fun onStop() {
        super.onStop()
       activity?.unregisterReceiver(localBroadcast)
    }


}

fun verifyAvailableNetwork(activity: AppCompatActivity): Boolean {
    val connectivityManager =
        activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo = connectivityManager.activeNetworkInfo
    return networkInfo != null && networkInfo.isConnected
}

val LOCAL_ACTION = "com.sargis.guardiannews.broadcast"

val EXECUTOR: ExecutorService = Executors.newSingleThreadExecutor()