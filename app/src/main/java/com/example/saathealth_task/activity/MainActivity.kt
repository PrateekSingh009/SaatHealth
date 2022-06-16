package com.example.saathealth_task.activity

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.WindowManager
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.example.saathealth_task.R

import com.example.saathealth_task.utils.Constants
import com.example.saathealth_task.utils.makeGone
import com.example.saathealth_task.utils.makeVisible
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= 21) {
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = this.resources.getColor(R.color.LightBlack)
        }

        initSharedPreferences()
        verifyAuth()


    }

    private fun verifyAuth() {
        if(getSharedPreferences(Constants.EMAIL)!="") addFragment(Constants.SPLASH_ID)
        else addFragment(Constants.LOGIN_ID)
    }

    fun getReqFragmentManager(): FragmentManager = supportFragmentManager

    private fun addFragment(id: String) {
        getReqFragmentManager().commit {
            setReorderingAllowed(true)
            add(R.id.fragment_container_view, Constants.getFragmentClass(id), null, id)
        }
    }

    private fun replaceFragment(id: String) {
        getReqFragmentManager().commit {
            setReorderingAllowed(true)
            replace(R.id.fragment_container_view, Constants.getFragmentClass(id), null)
        }
    }


    private fun initSharedPreferences() {
        sharedPreferences = getSharedPreferences(Constants.APP_NAME, Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
    }

    private fun getSharedPreferences(key: String): String {
        return sharedPreferences.getString(key, "") ?: ""
    }
}