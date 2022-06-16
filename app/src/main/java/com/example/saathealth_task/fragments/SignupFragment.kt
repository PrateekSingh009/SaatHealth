package com.example.saathealth_task.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.example.newapp.eventbus.MessageEvent
import com.example.saathealth_task.R
import com.example.saathealth_task.activity.viewModel.TestViewModel
import com.example.saathealth_task.utils.Constants
import com.example.saathealth_task.utils.makeGone
import com.example.saathealth_task.utils.makeVisible
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_login.tvLogin
import kotlinx.android.synthetic.main.fragment_signup.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import javax.inject.Inject

@AndroidEntryPoint
class SignupFragment: BaseFragment(), View.OnClickListener {
    override fun getLayoutRes(): Int {
        return R.layout.fragment_signup
    }

    private val viewModel: TestViewModel by activityViewModels()

    @Inject
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!(EventBus.getDefault().isRegistered(this))) EventBus.getDefault().register(this)
        initSharedPreferences()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvLogin.setOnClickListener(this)
        btnRegister.setOnClickListener(this)
    }

    @Subscribe
    fun onMessageEvent(event: MessageEvent) {
        when (event.getString()) {
            Constants.DATA_SUCCESS -> onSignUpSuccess()
            Constants.DATA_FAILURE -> onSignUpFailure()
        }
    }

    private fun onSignUpFailure() {
        showToast("Data Upload Failed")
        replaceFragment(Constants.SPLASH_ID,null)
        hideProgressFrame()
    }

    private fun onSignUpSuccess() {
        showToast("Account created successfully...")
        replaceFragment(Constants.SPLASH_ID, null)
        hideProgressFrame()
    }

    override fun onClick(view: View?) {
        when(view?.id) {
            R.id.tvLogin -> popBackStack()
            R.id.btnRegister -> validateAndCreate()
        }
    }

    private fun validateAndCreate() {
        val name = etName.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val password = etPass.text.toString().trim()
        val phone = etPhone.text.toString().trim()

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || phone.isEmpty()) showToast(
            "Enter all fields"
        )
        else {
            showProgressFrame()
            createAccount(name, email, password,phone)
        }
    }

    private fun createAccount(name: String, email: String, pass: String,phone: String) {
        auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    setSharedPreferences(Constants.EMAIL, email)
                    viewModel.insertData(name, email,phone)
                } else {
                    showToast("Something went wrong, try again later...")
                    hideProgressFrame()
                }
            }
    }


    private fun showProgressFrame() {
        pfSignup.makeVisible()
    }

    private fun hideProgressFrame() {
        pfSignup.makeGone()
    }
}