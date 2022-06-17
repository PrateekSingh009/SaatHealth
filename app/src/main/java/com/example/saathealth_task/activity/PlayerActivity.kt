package com.example.saathealth_task.activity

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.TextView
import android.media.AudioManager
import android.widget.Toast
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.saathealth_task.Adapter.videoAdapter
import com.example.saathealth_task.R
import com.example.saathealth_task.model.Users
import com.example.saathealth_task.model.VideoData
import com.example.saathealth_task.utils.Constants
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_award.*

class PlayerActivity : AppCompatActivity(), AudioManager.OnAudioFocusChangeListener {

    private lateinit var player : ExoPlayer
    private lateinit var playerView : PlayerView
    private lateinit var video_list : RecyclerView
 private var auth = FirebaseAuth.getInstance()
    private var isFullscreen: Boolean = false
    private var audioManager: AudioManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }
        setTheme(R.style.playerActivityTheme)

        setContentView(R.layout.activity_player)

        val url : String = intent.extras!!.getString("Url").toString()
        val caption : String = intent.extras!!.getString("Caption").toString()


        playerView = findViewById(R.id.playerView)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window,playerView).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
        try{

            createPlayer(url,caption)
            initializeBinding()


        }catch (e: Exception){
            Snackbar.make(playerView, e.localizedMessage!!.toString(), 10000).show()
        }

    }
    private fun createPlayer(url : String,caption: String){

        nextVideoList()


        findViewById<TextView>(R.id.videoTitle).text = caption

        player = ExoPlayer.Builder(this)
            .build()
        val mediaItem = MediaItem.fromUri(url)
        player.setMediaItem(mediaItem)
        player.prepare()
        findViewById<ImageButton>(R.id.playPauseBtn).setImageResource(R.drawable.pause_icon)
        player.playWhenReady = true
        playInFullscreen(enable = isFullscreen)
        playerView.player = player
        playerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
        player.videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT

        //for removing exoplayer's custom layout buttons
        playerView.setShowPreviousButton(false)
        playerView.setShowNextButton(false)
        playerView.setShowRewindButton(false)
        playerView.setShowFastForwardButton(false)

    }

    private fun nextVideoList(){

        val dataList = ArrayList<VideoData>()

        FirebaseDatabase.getInstance().getReference("VideoSet").addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(snap in snapshot.children){
                        val data = snap.getValue(VideoData::class.java)
                        dataList.add(data!!)

                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.i("Error" , error.toString())
            }

        })

        video_list  = findViewById(R.id.video_list)

        video_list.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = videoAdapter(this@PlayerActivity ,dataList)
        }
    }
    private fun incrementLevel(){
        FirebaseDatabase.getInstance().getReference("users").child(auth.currentUser!!.uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){

                    val data = snapshot.getValue(Users::class.java)
                    var points : Int = data!!.points
                    points=points+1
                    data.points = points
                    FirebaseDatabase.getInstance().getReference("users").child(auth.currentUser!!.uid).setValue(data).addOnSuccessListener {
                        Toast.makeText(this@PlayerActivity,"You won a point..!!",Toast.LENGTH_SHORT).show()
                    }


                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.i("Error" , error.toString())
            }

        })
    }

    private fun initializeBinding() {




//        findViewById<ImageButton>(R.id.orientationBtn).setOnClickListener {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
//                if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
//                    ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
//                else
//                    ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
//        }

        findViewById<ImageButton>(R.id.backBtn).setOnClickListener {
            finish()
        }
        findViewById<ImageButton>(R.id.playPauseBtn).setOnClickListener {
            if (player.isPlaying) pauseVideo()
            else playVideo()
        }
//        findViewById<ImageButton>(R.id.nextBtn).setOnClickListener { nextPrevVideo() }
//        findViewById<ImageButton>(R.id.prevBtn).setOnClickListener { nextPrevVideo(isNext = false) }

        findViewById<ImageButton>(R.id.fullScreenBtn).setOnClickListener {
            if (isFullscreen) {
                isFullscreen = false
                playInFullscreen(enable = false)
            } else {
                isFullscreen = true
                playInFullscreen(enable = true)
            }
        }

        player.addListener(object: Player.Listener{
            override fun onPlaybackStateChanged(playbackState: Int) {
                super.onPlaybackStateChanged(playbackState)
                if(playbackState == Player.STATE_ENDED)
                {
                    incrementLevel()
                    player.stop()

                    val intent =  Intent(applicationContext, MainActivity::class.java)
                    startActivity(intent)
                    finish()

                }
            }

        })


    }
    private fun playVideo(){
        findViewById<ImageButton>(R.id.playPauseBtn).setImageResource(R.drawable.pause_icon)
        player.play()
    }
    private fun pauseVideo(){
        findViewById<ImageButton>(R.id.playPauseBtn).setImageResource(R.drawable.play_icon)
        player.pause()
    }
    //    private fun nextPrevVideo(isNext: Boolean = true){
//        if(isNext) setPosition()
//        else setPosition(isIncrement = false)
//        createPlayer()
//    }
    private fun playInFullscreen(enable: Boolean){
        if(enable){
            playerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
            player.videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
            findViewById<ImageButton>(R.id.fullScreenBtn).setImageResource(R.drawable.fullscreen_exit_icon)
        }else{
            playerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
            player.videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT
            findViewById<ImageButton>(R.id.fullScreenBtn).setImageResource(R.drawable.fullscreen_icon)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        player.pause()
        audioManager?.abandonAudioFocus(this)
    }


    override fun onAudioFocusChange(focusChange: Int) {
        if(focusChange <= 0) pauseVideo()
    }

    override fun onResume() {
        super.onResume()
        if(audioManager == null) audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        audioManager!!.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN)

    }
}