// OrientationSensor.aidl
package com.example.orientationsensor;

interface OrientationSensor {
// this will return the values calculated by orientation sensor
    void setOrientationValues(String pitch, String roll);
}