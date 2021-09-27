package ru.intersvyaz.course.android.retrofit.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "users")
data class User(
    @PrimaryKey
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    @ColumnInfo(name = "name")
    val name: String,
    @SerializedName("email")
    @ColumnInfo(name = "email")
    val email: String,
    @SerializedName("avatar")
    @ColumnInfo(name = "avatar")
    val avatar: String
)
