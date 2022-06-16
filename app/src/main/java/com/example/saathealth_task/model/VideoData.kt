package com.example.saathealth_task.model

data class VideoData(
    val thumbnail:String,
    val video:String,
    val title:String
){
    constructor():this("","","")
}
