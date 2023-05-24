package com.example.dynamiclinksdemo

import android.app.Notification
import android.app.Notification.BigTextStyle
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.CountDownTimer
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.util.*


class MyFirebaseMessagingService : FirebaseMessagingService() {

    private var timer: CountDownTimer? = null
    var percent = 1.67


    override fun onNewToken(token: String) {
        val notificationManager = "!23"
        super.onNewToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        if (message.notification != null) {
            Log.e( "onMessageReceived: ",message.data.toString() )
            showNotification(message)
        }
        super.onMessageReceived(message)
    }

    private fun showNotification(message: RemoteMessage) {

        val intent = Intent(this, MainActivity::class.java)
        val channel_id = "notification_channel";
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        val customRemoteViews = RemoteViews(packageName,R.layout.notification)
        customRemoteViews.setTextViewText(R.id.notification_title,message.data["title"])
        customRemoteViews.setTextViewText(R.id.notification_text,message.data["text"])
        val builder = NotificationCompat.Builder(this, channel_id)
                        .setAutoCancel(true)
                        .setOnlyAlertOnce(true)
                        .setContentIntent(pendingIntent)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setPriority(Notification.PRIORITY_MAX)
                        .setContentTitle(message.data["title"])
                        .setContentText(message.data["text"])
                        .setLargeIcon(BitmapFactory.decodeResource(applicationContext.resources, R.mipmap.ic_launcher))
                        .setStyle(NotificationCompat.BigTextStyle().bigText(message.data["text"]))
            .setStyle(NotificationCompat.BigPictureStyle().bigPicture(BitmapFactory.decodeResource(applicationContext.resources, R.mipmap.ic_launcher_round))
                .bigLargeIcon(null))




        val notificationManager :NotificationManager =
            getSystemService (Context.NOTIFICATION_SERVICE) as NotificationManager
        // Check if the Android Version is greater than Oreo
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannel(channel_id, "web_app", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        notificationManager.notify(1, builder.build())
        startTimer(60000,customRemoteViews,notificationManager,builder)

    }

    fun getCustomDesign(message: RemoteMessage):RemoteViews{
        val remoteViews =RemoteViews(this.packageName, R.layout.notification)
        remoteViews.setTextViewText(R.id.notification_title,message.data["title"])
        //remoteViews.setTextViewText(R.id.notification_text,message.data["text"])
        remoteViews.setProgressBar(R.id.timer_progress,100,20,false)
        return remoteViews
    }

    private fun formatTime(millis: Long): String {
        val seconds = (millis / 1000) % 60
        val minutes = (millis / (1000 * 60)) % 60
        val hours = (millis / (1000 * 60 * 60)) % 24

        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }


    private fun startTimer(duration: Long, remoteViews: RemoteViews, notificationManager: NotificationManager, builder: NotificationCompat.Builder) {
        Looper.prepare()
        timer = object : CountDownTimer(duration, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = (millisUntilFinished / 1000) % 60
                remoteViews.setProgressBar(R.id.timer_progress,100, percent.toInt(),false)
                percent+=1.67
                remoteViews.setTextViewText(R.id.timer_text, formatTime(millisUntilFinished))
                notificationManager.notify(1,builder.build())
            }

            override fun onFinish() {
                // Timer finished, perform any necessary actions
            }
        }.start()
        Looper.loop()
    }

}