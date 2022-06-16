package com.example.saathealth_task.fragments

import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat.invalidateOptionsMenu
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.saathealth_task.R
import com.example.saathealth_task.activity.PlayerActivity
import com.example.saathealth_task.model.VideoData
import com.example.saathealth_task.utils.Constants
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.jjoe64.graphview.series.LineGraphSeries
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*
import org.imaginativeworld.whynotimagecarousel.ImageCarousel
import org.imaginativeworld.whynotimagecarousel.listener.CarouselListener
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem

@AndroidEntryPoint
class HomeFragment: BaseFragment(), View.OnClickListener {



    override fun getLayoutRes(): Int {
        return R.layout.fragment_home
    }


    private var auth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initSharedPreferences()


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Adding Data
        //addData()
        var dbref : DatabaseReference = FirebaseDatabase.getInstance().getReference("VideoSet")


        awdBtn.setOnClickListener {
            replaceFragment(Constants.AWARD_ID,null)

        }





//        val carousel: ImageCarousel = view.findViewById(com.google.firebase.database.R.id.carousel)


        carousel.registerLifecycle(viewLifecycleOwner)

        val list = mutableListOf<CarouselItem>()

        val dataList : ArrayList<VideoData> = ArrayList()



        dbref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(snap in snapshot.children){
                        val data = snap.getValue(VideoData::class.java)
                        dataList.add(data!!)
                        list.add(
                            CarouselItem(
                                imageUrl = data!!.thumbnail,
                                caption =data!!.title
                            )
                        )
                    }
                    carousel.setData(list)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.i("Error" , error.toString())
            }

        })

        carousel.carouselListener = object : CarouselListener {
            override fun onClick(position: Int, carouselItem: CarouselItem) {
                //Log.i("List",dataList.toString())


                dbref.child(position.toString()).child("video").get()
                    .addOnSuccessListener {
                        Log.i("video",it.toString())
                        val intent =  Intent(context, PlayerActivity::class.java)
                        intent.putExtra("Url",it.value.toString())
                        intent.putExtra("Caption",carouselItem.caption)


                        activity!!.startActivity(intent)

                    }


            }

        }





    }

    override fun onClick(view: View?) {
        when(view?.id) {

        }
    }

//    private fun addData(){
//        var listData: ArrayList<VideoData> = ArrayList()
//
//        val v1 : VideoData = VideoData("https://firebasestorage.googleapis.com/v0/b/saathealth-task.appspot.com/o/image%2FCaptain%20America%20Civil%20War.png?alt=media&token=92758e7d-1e8e-4a44-9a6e-451e95a607b5","https://firebasestorage.googleapis.com/v0/b/saathealth-task.appspot.com/o/video%2FCaptain%20America%20Civil%20War.mp4?alt=media&token=2679d3aa-7d5f-4e39-ad32-c110e6baae84","Marvel's Captain America: Civil War - Trailer")
//        val v2 : VideoData = VideoData("https://firebasestorage.googleapis.com/v0/b/saathealth-task.appspot.com/o/image%2FToy%20Story%20.png?alt=media&token=71c9b88e-6c8f-45c8-ac9f-8f13f847c48b","https://firebasestorage.googleapis.com/v0/b/saathealth-task.appspot.com/o/video%2FToy%20Story.mp4?alt=media&token=580aa873-4002-4868-9e89-e57df91072b5","Toy Story Trailer 1")
//        val v3 : VideoData = VideoData("https://firebasestorage.googleapis.com/v0/b/saathealth-task.appspot.com/o/image%2Fminion.png?alt=media&token=eff1cf47-fb4f-4ad0-a602-5f72d8b3f20b","https://firebasestorage.googleapis.com/v0/b/saathealth-task.appspot.com/o/video%2Fminions.mp4?alt=media&token=71f813bd-58c6-41f7-b96e-8306c3d3fcf3","Minions The Rise of Gru Trailer 1")
//        val v4 : VideoData = VideoData("https://firebasestorage.googleapis.com/v0/b/saathealth-task.appspot.com/o/image%2Fblack%20adam.png?alt=media&token=3c10dfac-d25c-46e2-8b7c-7e0a8ec9bfc4","https://firebasestorage.googleapis.com/v0/b/saathealth-task.appspot.com/o/video%2Fblack%20adam.mp4?alt=media&token=0794c294-28cd-4d25-9af9-066e41336b58","Black Adam - Official Trailer 1")
//        val v5 : VideoData = VideoData("https://firebasestorage.googleapis.com/v0/b/saathealth-task.appspot.com/o/image%2Fminion.png?alt=media&token=eff1cf47-fb4f-4ad0-a602-5f72d8b3f20b","https://firebasestorage.googleapis.com/v0/b/saathealth-task.appspot.com/o/video%2Fendgame.mp4?alt=media&token=ac2b6b24-0009-4520-9414-2f82932cb3e5","Avengers Endgame - Official Trailer")
//
//
//
//        listData.add(v1)
//        listData.add(v2)
//        listData.add(v3)
//        listData.add(v4)
//        listData.add(v5)
//        dbref.setValue(listData).addOnSuccessListener {
//            Log.i("Data added","Successfully")
//        }
//    }

}


