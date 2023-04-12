package com.example.signout_poc

import android.content.Context
import android.content.Intent
import android.icu.text.SimpleDateFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.signout_poc.Constants.EXPIRY_TIME
import com.example.signout_poc.Constants.FORMATTED_DATE
import com.example.signout_poc.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

class HomeActivity : AppCompatActivity() {
    var formattedDate: String? = null
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val sharedPreferences: UserPreference by lazy {
        UserPreference(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        checkPreviousFieldSavedInDb()
        setUpOnClickListeners()
        showCurrentTime()
    }


    private fun checkPreviousFieldSavedInDb() {
        if (sharedPreferences.getUserName() != null) {
            navigateToNextScreen()
            finish()
        }
    }

    private fun validateName(): Boolean {
        return if (binding.uiEtUserName.text.toString().isEmpty()) {
            binding.uiEtUserName.error = "Enter user name"
            binding.uiEtUserName.requestFocus()
            false
        } else {
            binding.uiEtUserName.error = null
            true
        }
    }

    private fun validatePassword(): Boolean {
        val passwordToString: String = binding.uiEtPassword.text.toString()
        if(passwordToString.isEmpty()) {
            binding.uiEtPassword.error = "Enter Password"
            binding.uiEtPassword.requestFocus()
            return false
        }
        return if(passwordToString.isNotEmpty()) {
            if(passwordToString.length < 3) {
                binding.uiEtPassword.error = "Enter more than 3"
                binding.uiEtPassword.requestFocus()
                false
            } else {
                binding.uiEtPassword.error = null
                true
            }
        } else {
            binding.uiEtPassword.error = "Enter more than 3"
            false
        }
    }

    private fun saveInputFieldsToDb() {

        val userName: String = binding.uiEtUserName.text.toString().trim()
        val userPassword: String = binding.uiEtPassword.text.toString().trim()

        sharedPreferences.setUserName(userName)
        sharedPreferences.setUserPassword(userPassword)
    }

    private fun showCurrentTime() {

        EXPIRY_TIME = "2023-04-12T11:07:00.540Z"
        binding.uiTvCurrentTime.text = "Refresh Token exp: ${EXPIRY_TIME}"

        val inputDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val outputDateFormat = SimpleDateFormat("dd/MM/yyyy hh:mm:ss a", Locale.getDefault())

        val date = inputDateFormat.parse(EXPIRY_TIME)
        formattedDate = outputDateFormat.format(date)
        binding.uiTvRefreshTime.text = formattedDate
        sharedPreferences.setRequestTime(formattedDate)
    }

    private fun navigateToNextScreen() {
        val intent = Intent(this@HomeActivity, LoggedInActivity::class.java)
        Log.d("myOneTimeWork", "navigateToNextScreen: ${formattedDate}")
        startActivity(intent)
    }

    private fun setUpOnClickListeners() {
        binding.uiButtonLogin.setOnClickListener {
            submitAction()
        }
    }

    private fun submitAction() {
        if(validateName() && validatePassword()) {
            saveInputFieldsToDb()
            navigateToNextScreen()
        }
    }
}