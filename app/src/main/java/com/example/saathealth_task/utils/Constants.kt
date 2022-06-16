package com.example.saathealth_task.utils

import androidx.fragment.app.Fragment
import com.example.saathealth_task.fragments.*


class Constants {
    companion object {
        const val LOGIN_ID = "LOGIN_ID"
        const val HOME_ID = "HOME_ID"
        const val APP_NAME = "SAATHEALTH_TASK"
        const val EMAIL = "EMAIL"
        const val SIGNUP_ID = "SIGNUP_ID"
        const val SPLASH_ID = "SPLASH_ID"
        const val AWARD_ID = "AWARD_ID"
        const val DATA_SUCCESS = "DATA_SUCCESS"
        const val DATA_FAILURE = "DATA_FAILURE"

        fun getFragmentClass(id: String): Class<Fragment> {
            return when(id) {
                LOGIN_ID -> LoginFragment::class.java as Class<Fragment>
                HOME_ID -> HomeFragment::class.java as Class<Fragment>
                SPLASH_ID -> SplashFragment::class.java as Class<Fragment>
                SIGNUP_ID -> SignupFragment::class.java as Class<Fragment>
                AWARD_ID -> AwardFragment::class.java as Class<Fragment>
                else -> LoginFragment::class.java as Class<Fragment>
            }
        }
    }
}