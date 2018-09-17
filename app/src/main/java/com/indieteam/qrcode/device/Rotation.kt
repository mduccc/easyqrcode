package com.indieteam.qrcode.device

import android.content.Context
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata
import com.indieteam.qrcode.ui.activity.MainActivity

class Rotation(val activity: MainActivity){

    fun get(camID: String): Int{
        val deviceRotation = activity.windowManager.defaultDisplay.rotation
        var rotationCompensation = MainActivity.ORIENTATIONS.get(deviceRotation)
        val sensorOrientation = activity.manager.getCameraCharacteristics(camID)
                .get(CameraCharacteristics.SENSOR_ORIENTATION)
        rotationCompensation = (rotationCompensation + sensorOrientation + 270) / 360
        //Log.d("rotateeeee", rotationCompensation.toString())
        return when(rotationCompensation){
            0 -> FirebaseVisionImageMetadata.ROTATION_0
            90 -> FirebaseVisionImageMetadata.ROTATION_90
            180 -> FirebaseVisionImageMetadata.ROTATION_180
            270 -> FirebaseVisionImageMetadata.ROTATION_270
            else -> FirebaseVisionImageMetadata.ROTATION_90
        }
    }

}