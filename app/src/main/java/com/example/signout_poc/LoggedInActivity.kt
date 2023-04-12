package com.example.signout_poc

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.work.*
import com.example.signout_poc.Constants.EXPIRY_TIME
import com.example.signout_poc.Constants.FORMATTED_DATE
import com.example.signout_poc.databinding.ActivityLoggedInBinding
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class LoggedInActivity : AppCompatActivity() {
    private val binding: ActivityLoggedInBinding by lazy {
        ActivityLoggedInBinding.inflate(layoutInflater)
    }

    private val sharedPreferences: UserPreference by lazy {
        UserPreference(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        displayInputFields()
        setUpListeners()
        oneTime()
        //myPeriodicWork(15)
        //myOneTimeWork(1)

    }

    private fun displayInputFields() {
        binding.uiTvEnteredName.text = sharedPreferences.getUserName()
        binding.uiTvEnteredPassword.text = sharedPreferences.getUserPassword()
    }

    private fun setUpListeners() {
        binding.uiButtonLogout.setOnClickListener {
            actionSubmit()
            //myOneTimeTask()
        }
    }

    private fun actionSubmit() {
        sharedPreferences.clearData()
        startActivity(Intent(this, HomeActivity::class.java))
    }

    private fun myPeriodicWork(requestTime: Long) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .build()

        val myRequest = PeriodicWorkRequest.Builder(
            MyWorker::class.java,
            requestTime, TimeUnit.MINUTES
        )
            .setInitialDelay(requestTime, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .addTag("myId")
            .build()

        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork(
                "myId",
                ExistingPeriodicWorkPolicy.UPDATE,
                myRequest
            )
    }

    private fun myOneTimeWork(requestTime: Long) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .setRequiresCharging(true)
            .build()

        val myWorkRequest: WorkRequest = OneTimeWorkRequest.Builder(MyWorker::class.java)
            .setInitialDelay(requestTime, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(this).enqueue(myWorkRequest)
    }

    private fun oneTime() {
        val formattedDate = sharedPreferences.getRequestTime()
        Log.d("formattedDate", "formattedDate: ${formattedDate}")

        val currentTimeMillis = System.currentTimeMillis()
        val currTimeSeconds = currentTimeMillis / 1000
        val currTimeMinutes = currTimeSeconds / 60

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .build()

        val dateFormat = SimpleDateFormat("dd/MM/yyyy hh:mm:ss a", Locale.getDefault())
        val date = dateFormat.parse(formattedDate)
        Log.e("myOneTimeWork", "formattedDate: ${formattedDate}")
        val requestTime = date.time - System.currentTimeMillis()
        Log.e("myOneTimeWork", "refreshTime: ${date.time}")
        Log.e("myOneTimeWork", "currentTime: ${System.currentTimeMillis()}")
        Log.e("myOneTimeWork", "currTimeSeconds: ${currTimeSeconds}")
        Log.e("myOneTimeWork", "currTimeMinutes: ${currTimeMinutes}")
        Log.e("myOneTimeWork", "requestTime: ${requestTime}")

        if (requestTime <= 0) {
            Log.e("myOneTimeWork", "Formatted date is over!")
            return
        }

        val myWorkRequest: WorkRequest = OneTimeWorkRequest.Builder(MyWorker::class.java)
            .setInitialDelay(requestTime, TimeUnit.MILLISECONDS)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(this).enqueue(myWorkRequest)
    }
}