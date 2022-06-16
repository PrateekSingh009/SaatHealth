package com.example.saathealth_task.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import com.example.saathealth_task.R
import com.example.saathealth_task.utils.Constants

class SplashFragment: BaseFragment() {
    override fun getLayoutRes(): Int {
        return R.layout.fragment_splash
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Handler(Looper.myLooper()!!).postDelayed({
            replaceFragment(Constants.HOME_ID, null)
        },1500)
    }
}