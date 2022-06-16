package com.example.saathealth_task.fragments

import android.os.Bundle
import android.view.View
import com.example.saathealth_task.R
import com.example.saathealth_task.utils.Constants
import com.example.saathealth_task.utils.makeGone
import com.example.saathealth_task.utils.makeVisible
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_login.*
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment: BaseFragment(), View.OnClickListener {

    override fun getLayoutRes(): Int {
        return R.layout.fragment_login
    }


    @Inject
    lateinit var auth: FirebaseAuth


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvRegister.setOnClickListener(this)
        btnLogin.setOnClickListener(this)
        initSharedPreferences()
    }

    private fun validateAndLogin() {
        val email = etEmailLogin.text.toString().trim()
        val pass = etPassLogin.text.toString().trim()
        if(email.isEmpty() || pass.isEmpty()) showToast("Enter all fields")
        else {
            showProgressFrame()
            login(email, pass)
        }
    }

    private fun login(email: String, pass: String) {
        auth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    hideProgressFrame()
                    showToast("Logged in successfully")
                    setSharedPreferences(Constants.EMAIL, Constants.EMAIL)
                    replaceFragment(Constants.SPLASH_ID,null)
                } else {
                    hideProgressFrame()
                    hideProgressFrame()
                    showToast("Something went wrong, try again later...")
                }
            }
    }


    override fun onClick(view: View?) {
        when(view?.id) {
            R.id.tvRegister -> addFragment(Constants.SIGNUP_ID, null)
            R.id.btnLogin -> validateAndLogin()
        }
    }

    private fun showProgressFrame() {
        pfLogin.makeVisible()
    }

    private fun hideProgressFrame() {
        pfLogin.makeGone()
    }

}