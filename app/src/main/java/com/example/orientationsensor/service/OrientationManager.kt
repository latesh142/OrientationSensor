package com.example.orientationsensor.service

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import com.example.orientationsensor.OrientationSensor
import com.example.orientationsensor.model.SensorModel
import com.example.orientationsensor.util.AppConstants

class OrientationManager(val mContext: Context): ServiceConnection, SensorModel.Listener {
    var connected = false
    var orientationSensor: OrientationSensor?= null

    /**
     * it will start the sensor to calculate orientation
     */
    private fun startSensor(){
        SensorModel.Build(mContext, this)
    }

    /**
     * it will start the binder service to bind the data and send in background
     */
    fun connectToRemoteService() {
        Intent(mContext, OrientationService::class.java).also { intent ->
            mContext.bindService(intent, this, Context.BIND_AUTO_CREATE)
        }
    }

    /**
     * unbind the service to avoid unnecessary memory usage
     */
    fun disconnectToRemoteService() {
        if(connected){
            mContext?.unbindService(this)
        }
    }

    /**
     * after service connection orientation sensor detector will get started from here
     */
    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        orientationSensor = OrientationSensor.Stub.asInterface(service)
        connected = true
        startSensor()
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        orientationSensor = null
        connected = false
    }

    override fun orientationValues(pitch: String, roll: String) {
        orientationSensor?.setOrientationValues(pitch, roll)
    }
}