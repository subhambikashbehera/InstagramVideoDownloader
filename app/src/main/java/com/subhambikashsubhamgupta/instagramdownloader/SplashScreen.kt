package com.subhambikashsubhamgupta.instagramdownloader

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import java.lang.Exception

class SplashScreen : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        supportActionBar?.hide()
//        val thread =object :Thread(){
//            override fun run() {
//                super.run()
//                try {
//                    sleep(500)
//                }catch (e:Exception)
//                {
//                    e.printStackTrace()
//                }finally {
//                    val intent=Intent(this@SplashScreen,MainActivity::class.java)
//                    startActivity(intent)
//                    finish()
//                }
//            }
//        }
//        thread.start()


        Handler().postDelayed({val intent=Intent(this@SplashScreen,MainActivity::class.java)
            startActivity(intent)
            finish()},500)

    }





}