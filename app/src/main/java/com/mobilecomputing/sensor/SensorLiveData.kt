package com.mobilecomputing.sensor

import androidx.lifecycle.LiveData
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import java.util.Date


    class SensorLiveData(context: Context, sensorType: Int, delay: Int) :
        LiveData<SensorLiveData.Event?>() {
        private val sensorManager: SensorManager
        private val sensor: Sensor?
        private val delay: Int
        override fun onActive() {
            super.onActive()
            sensorManager.registerListener(listener, sensor, delay)
        }

        override fun onInactive() {
            sensorManager.unregisterListener(listener)
            super.onInactive()
        }

        private val listener: SensorEventListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                setValue(Event(event))
            }

            override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
                // unused
            }
        }

        init {
            sensorManager = context.applicationContext
                .getSystemService(Context.SENSOR_SERVICE) as SensorManager
            sensor = sensorManager.getDefaultSensor(sensorType)
            this.delay = delay
            checkNotNull(sensor) { "Cannot obtain the requested sensor" }
        }

        class Event(event: SensorEvent) {
            val date = Date()
            val values: FloatArray

            init {
                values = FloatArray(event.values.size)
                System.arraycopy(event.values, 0, values, 0, event.values.size)
            }
        }
    }