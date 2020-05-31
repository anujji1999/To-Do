package com.anuj.to_do

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.anuj.to_do.onboarding.OnBoardingActivity

class SplashActivity : AppCompatActivity() {

    private val SPLASH_TIME_OUT:Long = 3000
    lateinit var sharedPreferences : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            // This method will be executed once the timer is over
            // Start your app main activity

            setupSharedPreference()
            val isBoardingSuccess = sharedPreferences.getBoolean("on_boarded_successfully", false)

            if(isBoardingSuccess) {
                val intent = Intent(this@SplashActivity, MainActivity::class.java)
                startActivity(intent)
            }else{
                val intent = Intent(this@SplashActivity, OnBoardingActivity::class.java)
                startActivity(intent)
            }

            // close this activity
            finish()
        }, SPLASH_TIME_OUT)
    }

    private fun setupSharedPreference() {
        sharedPreferences = getSharedPreferences("notes_app_pref", Context.MODE_PRIVATE)
    }
}
