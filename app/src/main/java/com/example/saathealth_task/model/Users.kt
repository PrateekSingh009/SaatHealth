package com.example.saathealth_task.model

data class Users(
    var name: String? = null,
    var email: String? = null,
    var phone: String? = null,
    var points : Int = 0,
    var level : Int = 0,
    var badge : String? = null

)
