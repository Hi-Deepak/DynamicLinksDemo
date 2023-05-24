package com.example

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

class MyBroadcastReveiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.e("onReceive: ","Notification received" )
        Toast.makeText(context,"Notification received",Toast.LENGTH_SHORT).show()
    }
}