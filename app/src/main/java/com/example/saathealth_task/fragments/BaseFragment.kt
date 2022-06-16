package com.example.saathealth_task.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.example.saathealth_task.R
import com.example.saathealth_task.utils.Constants
import com.example.saathealth_task.activity.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
abstract class BaseFragment: Fragment() {

    abstract fun getLayoutRes(): Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(getLayoutRes(), container, false)
    }

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    fun addFragment(id: String, bundle: Bundle?) {
        getReqFragmentManager().commit {
            setReorderingAllowed(true)
            add(R.id.fragment_container_view, Constants.getFragmentClass(id), bundle, id)
            addToBackStack(id)
        }
    }

    fun replaceFragment(id: String, bundle: Bundle?) {
        getReqFragmentManager().commit {
            setReorderingAllowed(true)
            replace(R.id.fragment_container_view, Constants.getFragmentClass(id), bundle)
        }
    }

    fun popBackStack() {
        getReqFragmentManager().popBackStack()
    }

    fun showToast(msg: String) {
        Toast.makeText(requireContext(),msg, Toast.LENGTH_SHORT).show()
    }

    private fun getReqFragmentManager(): FragmentManager = (requireActivity() as MainActivity).getReqFragmentManager()



    fun initSharedPreferences() {
        sharedPreferences = requireActivity().getSharedPreferences(Constants.APP_NAME, Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
    }

    fun setSharedPreferences(key: String, value: String) {
        editor.putString(key, value).apply()
    }

    fun getSharedPreferences(key: String): String {
        return sharedPreferences.getString(key, "") ?: ""
    }

    fun clearSharedPreferences() {
        editor.clear().apply()
    }
    fun deleteSharedPreferences(key: String) {
        editor.remove(key).apply()
    }
    fun finish() {
        (requireActivity() as MainActivity).finish()
    }
}