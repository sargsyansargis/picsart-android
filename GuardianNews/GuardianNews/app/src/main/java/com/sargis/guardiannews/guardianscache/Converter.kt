package com.sargis.guardiannews.guardianscache

import androidx.room.TypeConverter
import com.sargis.guardiannews.guadriandsapi.ArticleFields

class Converter {
    @TypeConverter
    fun storedStringToLanguages(value: String?): ArticleFields {
        return ArticleFields(value)
    }

    @TypeConverter
    fun languagesToStoredString(fields: ArticleFields?): String {
        return fields?.thumbnail ?: ""
    }
}