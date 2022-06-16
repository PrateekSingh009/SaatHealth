package com.example.saathealth_task.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.saathealth_task.R
import com.example.saathealth_task.model.Users
import com.example.saathealth_task.model.VideoData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_award.*
import kotlinx.android.synthetic.main.fragment_home.*
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem


@AndroidEntryPoint
class AwardFragment : BaseFragment() {

    private  var dbref : DatabaseReference = FirebaseDatabase.getInstance().getReference("users")
    private var auth  = FirebaseAuth.getInstance()

    override fun getLayoutRes(): Int {
        return R.layout.fragment_award
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initSharedPreferences()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbref.child(auth.currentUser!!.uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){

                    val data = snapshot.getValue(Users::class.java)
                    points.text = data!!.points.toString()
                    level.text = data!!.level.toString()
                    badge.text = data!!.level.toString()
                    name.text = data!!.name.toString()
                    email.text = data!!.email.toString()

                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.i("Error" , error.toString())
            }

        })


    }



}