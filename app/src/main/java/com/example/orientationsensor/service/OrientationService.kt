package com.example.orientationsensor.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import com.example.orientationsensor.OrientationSensor
import com.example.orientationsensor.util.AppConstants

class OrientationService: Service() {
    lateinit var context: Context

    /**
     * orientation connector (binder)
     */
    private val binder = object: OrientationSensor.Stub(){
        override fun setOrientationValues(pitch: String?, roll: String?) {
            sendOrientationData(pitch, roll)
        }
    }

    /**
     * send data to registered receiver activity
     */
    private fun sendOrientationData(pitch: String?, roll: String?){
        val broadcastIntent = Intent()
        broadcastIntent.action = AppConstants.ACTION_ORIENTATION
        broadcastIntent.putExtra(AppConstants.PITCH_VALUE, pitch)
        broadcastIntent.putExtra(AppConstants.ROLL_VALUE, roll)
        sendBroadcast(broadcastIntent)
    }

    override fun onCreate() {
        super.onCreate()
        context = this
    }

    override fun onBind(p0: Intent?): IBinder? {
        return binder
    }
}