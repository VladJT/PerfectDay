package jt.projects.repository.push

import android.graphics.drawable.Drawable

data class DataPush(
    val typePush:String,
    val title:String,
    val message:String,
    val icon:Drawable?=null
)
