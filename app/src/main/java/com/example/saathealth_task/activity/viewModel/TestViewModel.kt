package com.example.saathealth_task.activity.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newapp.eventbus.MessageEvent
import com.example.saathealth_task.model.Users
import com.example.saathealth_task.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus

class TestViewModel : ViewModel() {


    private var auth = FirebaseAuth.getInstance()
    private var database = FirebaseDatabase.getInstance()
    private lateinit var ref: DatabaseReference

    fun insertData(name: String, email: String, phone: String) {
        ref = database.reference.child("users").child(auth.currentUser?.uid!!)
        viewModelScope.launch {
            Users().apply {
                this.name = name
                this.email = email
                this.phone = phone
                this.points = 0
                this.level = 0
                this.badge = "Rookie"
                ref.setValue(this).addOnCompleteListener {
                    if (it.isSuccessful) EventBus.getDefault()
                        .post(MessageEvent(Constants.DATA_SUCCESS))
                    else EventBus.getDefault().post(MessageEvent(Constants.DATA_FAILURE))
                }
            }
        }
    }


}