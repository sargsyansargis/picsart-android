package com.sargis.guardiannews.guardianscache

import androidx.paging.DataSource
import androidx.room.*
import com.sargis.guardiannews.guadriandsapi.ArticleModel

@Dao
interface ArticleDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(posts: List<ArticleModel>)

    @Query("SELECT * FROM articles")
    fun posts(): DataSource.Factory<Int, ArticleModel>

    @Query("SELECT * FROM articles WHERE saved = 'true'")
    fun getSavedArticled(): DataSource.Factory<Int, ArticleModel>

    @Query("SELECT * FROM articles WHERE id = :searchId")
    fun findArticle(searchId: String): ArticleModel?


    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(article: ArticleModel)

    @Delete
    fun delete(article: ArticleModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(posts: ArticleModel)

}