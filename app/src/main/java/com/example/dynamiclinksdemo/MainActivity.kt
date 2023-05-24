package com.example.dynamiclinksdemo

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.MyBroadcastReveiver
import com.example.dynamiclinksdemo.databinding.ActivityMainBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging


class MainActivity : AppCompatActivity() {
    lateinit var token :String
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        FirebaseApp.initializeApp(this)
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.e(
                    "MainActivity Firebase ",
                    "Fetching FCM registration token failed",
                    task.exception
                )

                return@OnCompleteListener
            }
            token = task.result
            Log.e(
                "MainActivity Firebase ",
                "Fetching FCM registration token passed > "+
                token
            )
        })

        val intent = Intent()
        intent.action = "com.google.firebase.MESSAGING_EVENT"
        sendBroadcast(intent)

    }

    override fun onStart() {
        super.onStart()
        val filter = IntentFilter("com.google.firebase.MESSAGING_EVENT")
        registerReceiver(MyBroadcastReveiver(), filter)
        registerReceiver(MyBroadcastReveiver(), IntentFilter("android.intent.action.BATTERY_LOW"))
    }
}