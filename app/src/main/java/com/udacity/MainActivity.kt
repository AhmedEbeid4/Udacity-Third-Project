package com.udacity

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0
    private var URL =""
    private var fileName=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        custom_button.setOnClickListener {
            if(custom_button.buttonState != ButtonState.Loading){
                if(!radio_glide.isChecked && !radio_load_app.isChecked  && !radio_retrofit.isChecked){
                    val snack = Snackbar.make(it,"Please Choose One",Snackbar.LENGTH_LONG)
                    snack.show()
                }else{
                    if(radio_glide.isChecked){
                        URL = "https://github.com/bumptech/glide/archive/refs/heads/master.zip"
                        fileName= "Glide"
                    }
                    else if(radio_load_app.isChecked){
                        URL = "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
                        fileName="ND940C3-Project"
                    }
                    else if(radio_retrofit.isChecked){
                        URL = "https://github.com/square/retrofit/archive/refs/heads/master.zip"
                        fileName="Retrofit"
                    }
                    download()
                }
            }else{
                Toast.makeText(applicationContext,"Wait The button is working",Toast.LENGTH_SHORT).show()
            }
        }
        createChannel(CHANNEL_ID, "NotificationChannelName")
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)

            custom_button.buttonState = ButtonState.Completed

            val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager

            val query = DownloadManager.Query()
            query.setFilterById(id!!)

            val cursor = downloadManager.query(query)
            if (cursor.moveToFirst()) {
                val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))

                var downloadStatus = "Fail"
                if (DownloadManager.STATUS_SUCCESSFUL == status) {
                    downloadStatus = "Success"
                }

                val notificationManager = getSystemService(NotificationManager::class.java)
                notificationManager.sendNotification(
                    CHANNEL_ID,
                    getString(R.string.notification_description),
                    applicationContext,
                    fileName,
                    downloadStatus

                )
            }
        }
    }

    private fun download() {
        val request =
            DownloadManager.Request(Uri.parse(URL))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)
    }

    private fun createChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )

            notificationChannel.description = getString(R.string.app_description)
            notificationChannel.enableLights(true)
            notificationChannel.enableVibration(true)
            notificationChannel.lightColor = Color.BLUE

            val notificationManager = getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    companion object {
        private const val CHANNEL_ID = "channelId"
    }

    override fun onBackPressed() {

    }

}
