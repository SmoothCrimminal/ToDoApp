package com.codinginflow.mvvmtodo.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.*

@Entity
@Parcelize
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val desc: String,
    val isImportant: Boolean = false,
    val isCompleted: Boolean = false,
    val date: String
) : Parcelable {
}