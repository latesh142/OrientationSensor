package com.example.orientationsensor.model

import android.app.Activity
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.widget.TextView
import com.example.orientationsensor.R

object SensorModel: SensorEventListener {
    lateinit var sensorManager: SensorManager
    lateinit var sensor: Sensor
    var FROM_RADS_TO_DEGS = -57
    lateinit var mListener: Listener


    //build the primary variables to initialize the sensors
    fun Build(context: Context, listener: Listener){
        sensorManager = context.getSystemService(Activity.SENSOR_SERVICE) as SensorManager
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
        sensorManager.registerListener(this, sensor, 8000)
        mListener = listener
    }

    //will send the data detected from sensor in proper format
    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor === sensor) {
            if (event.values.size > 4) {
                val truncatedRotationVector = FloatArray(4)
                System.arraycopy(event.values, 0, truncatedRotationVector, 0, 4)
                update(truncatedRotationVector)
            } else {
                update(event.values)
            }
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }

    //this method convert the orientation value to pitch and roll @source:- Internet
    private fun update(vectors: FloatArray) {
        val rotationMatrix = FloatArray(9)
        SensorManager.getRotationMatrixFromVector(rotationMatrix, vectors)
        val worldAxisX = SensorManager.AXIS_X
        val worldAxisZ = SensorManager.AXIS_Z
        val adjustedRotationMatrix = FloatArray(9)
        SensorManager.remapCoordinateSystem(
            rotationMatrix,
            worldAxisX,
            worldAxisZ,
            adjustedRotationMatrix
        )
        val orientation = FloatArray(3)
        SensorManager.getOrientation(adjustedRotationMatrix, orientation)
        val pitch: Float = orientation[1] * FROM_RADS_TO_DEGS
        val roll: Float = orientation[2] * FROM_RADS_TO_DEGS
        mListener.orientationValues(pitch.toString(), roll.toString())
    }

    interface Listener{
        fun orientationValues(pitch: String, roll: String)
    }
}