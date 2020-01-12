package com.sargis.guardiannews

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    private val FRAGMENT_TAG = "article_list_fragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var articlesListFragment = supportFragmentManager.findFragmentByTag(FRAGMENT_TAG)
        if (articlesListFragment == null) {
            articlesListFragment = ArticlesListFragment()
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, articlesListFragment, FRAGMENT_TAG).commit()
        }

        val intent = Intent(this, NotificationService::class.java)
        startService(intent)
    }
}
