package com.sargis.guardiannews.guadriandsapi

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName


import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.sargis.guardiannews.guardianscache.Converter

@Entity(tableName = "articles")
data class ArticleModel(
    @PrimaryKey @field:SerializedName("id") val id: String,
    @field:SerializedName("type") val type: String?,
    @field:SerializedName("sectionId") val sectionId: String?,
    @field:SerializedName("webTitle") val webTitle: String?,
    @field:SerializedName("webPublicationDate") val webPublicationDate: String?,

    @TypeConverters(Converter::class)
    @field:SerializedName("fields") val fields: ArticleFields?,
    @field:SerializedName("webUrl") val webUrl: String?,
    @field:SerializedName("apiUrl") val apiUrl: String?,
    @field:SerializedName("sectionName") val sectionName: String?,
    @field:SerializedName("page") var page: Int,
    @field:SerializedName("liked") var liked: Boolean = false,
    @field:SerializedName("saved") var saved: Boolean = false,
    @field:SerializedName("deleted") var deleted: Boolean = false
):Parcelable{
    override fun writeToParcel(p0: Parcel?, p1: Int) {
        p0?.apply {
            writeString(id)
            writeString(type)
            writeString(sectionId)
            writeString(webTitle)
            writeString(webPublicationDate)
            writeString(fields?.thumbnail)
            writeString(webUrl)
            writeString(apiUrl)
            writeString(sectionName)
            writeInt(page)
            writeByte(if(liked) 1 else 0)
            writeByte(if(saved) 1 else 0)
            writeByte(if(deleted) 1 else 0)

        }
    }

    override fun describeContents(): Int {
       return 0
    }

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        ArticleFields(parcel.readString()),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte()
    )



    companion object CREATOR : Parcelable.Creator<ArticleModel> {
        override fun createFromParcel(parcel: Parcel): ArticleModel {
            return ArticleModel(parcel)
        }

        override fun newArray(size: Int): Array<ArticleModel?> {
            return arrayOfNulls(size)
        }
    }
}
