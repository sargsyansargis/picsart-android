package com.sargis.guardiannews.guardianscache

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sargis.guardiannews.guadriandsapi.ArticleModel

@Database(
    entities = [ArticleModel::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converter::class)
abstract class SavedArticlesDB : RoomDatabase() {

    companion object {
        @Volatile
        private var INSTANCE: GuardianDB? = null

        fun getInstance(context: Context): GuardianDB =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                GuardianDB::class.java, "SavedArticles.db"
            )
                .build()
    }
}