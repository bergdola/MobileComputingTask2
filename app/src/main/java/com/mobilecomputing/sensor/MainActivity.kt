package com.mobilecomputing.sensor

import android.content.ComponentName
import android.os.Bundle
import androidx.activity.ComponentActivity
import android.content.Intent
import android.content.Context
import android.content.ServiceConnection
import android.widget.TextView
import android.os.IBinder

class MainActivity : ComponentActivity() {

    private lateinit var samplingRateValue:TextView
    private lateinit var accelValue:TextView
    private lateinit var gyroValue:TextView

    private lateinit var sensorService: SensorService
    private var isSensorServiceBound: Boolean = false
    private val sensorServiceConnection = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
            sensorService = (binder as SensorService.SensorBinder).service
            isSensorServiceBound = true
            observeSensors()
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            isSensorServiceBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout)

        samplingRateValue = findViewById(R.id.valueSamplingRate)
        accelValue = findViewById(R.id.valueAccelerometer)
        gyroValue = findViewById(R.id.valueGyroscope)

        startService(Intent(this, SensorService::class.java))
        bindService(
            Intent(this, SensorService::class.java),
            sensorServiceConnection,
            Context.BIND_AUTO_CREATE
        )
    }

    private fun observeSensors() {

        sensorService.accelerometerData.observe(this) { event ->
            val accelX = event?.values?.get(0)
            val accelY = event?.values?.get(1)
            val accelZ = event?.values?.get(2)
            val accelString =
                "${"%.2f".format(accelX)} | ${"%.2f".format(accelY)} | ${"%.2f".format(accelZ)}"
            accelValue.setText(accelString)
        }

        sensorService.gyroscopeData.observe(this) { event ->
            val gyroX = event?.values?.get(0)
            val gyroY = event?.values?.get(1)
            val gyroZ = event?.values?.get(2)
            val gyroString =
                "${"%.2f".format(gyroX)} | ${"%.2f".format(gyroY)} | ${"%.2f".format(gyroZ)}"
            gyroValue.setText(gyroString)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        if (isSensorServiceBound) {
            unbindService(sensorServiceConnection)
        }
    }

}