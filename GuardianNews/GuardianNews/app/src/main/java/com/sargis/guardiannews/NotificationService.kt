package com.sargis.guardiannews

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.widget.Toast


class NotificationService : Service() {
    var context: Context = this
    var handler: Handler? = null


    override fun onCreate() {
        handler = Handler()
        runnable = Runnable {
            val checkNewEntry = CheckNewArticle(this)
            checkNewEntry.execute()


            handler?.postDelayed(runnable, 5000)
        }
        handler?.postDelayed(runnable, 5000)
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        handler?.removeCallbacks(runnable)
        Toast.makeText(this, "Notifications Stopped...", Toast.LENGTH_LONG).show()
    }



    companion object {
        var runnable: Runnable? = null
    }
}