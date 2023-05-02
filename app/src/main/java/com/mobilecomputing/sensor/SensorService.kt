package com.mobilecomputing.sensor

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.os.Binder
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.util.UUID

class SensorService: Service() {

    private val binder: Binder = SensorBinder()

    private var CHANNELID = "ServiceChannel"
    private var id: Int = UUID.randomUUID().hashCode()

    lateinit var accelerometerData : SensorLiveData
    lateinit var gyroscopeData : SensorLiveData

    override fun onBind(p0: Intent?): IBinder = binder

    inner class SensorBinder : Binder() {
        val service: SensorService
            get() = this@SensorService
    }

    fun createChannel(
        context: Context,
        channelId: String = CHANNELID,
        importanceLevel: Int = NotificationManager.IMPORTANCE_HIGH
    ) {
        val channel = NotificationChannel(
            channelId,
            "SensorChannel",
            importanceLevel
        ).apply {
            description = "SensorChannel"
        }

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    fun createNotification(
        context: Context,
        title: String,
        content: String,
    ) {
        val builder = NotificationCompat.Builder(context, CHANNELID)
        val notification = builder.setContentTitle(title).setContentText(content)
            .setSmallIcon(R.drawable.ic_launcher_foreground).build()
        NotificationManagerCompat.from(context).notify(id, notification)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        accelerometerData = SensorLiveData(this, Sensor.TYPE_ACCELEROMETER, 2000000)
        gyroscopeData = SensorLiveData(this, Sensor.TYPE_GYROSCOPE, 2000000)
        createChannel(context = this, channelId = CHANNELID, importanceLevel = NotificationManager.IMPORTANCE_DEFAULT)
        createNotification(context = this, title = "SensorReader", content = "SensorService started")
        return super.onStartCommand(intent, flags, startId)
    }
}