package com.indieteam.qrcode.process.callback

import android.hardware.camera2.CameraDevice
import android.widget.Toast
import com.indieteam.qrcode.ui.activity.MainActivity
import com.indieteam.qrcode.ui.camera.CameraPreview

class CameraOpenCallback(val activity: MainActivity): CameraDevice.StateCallback(){

    override fun onOpened(camera: CameraDevice?) {
        activity.cameraOpen = 1
        activity.mCamera = camera!!
        activity.getSizes()
        activity.cameraPreview = CameraPreview(activity)
        activity.cameraPreview.init()
    }

    override fun onDisconnected(camera: CameraDevice?) { activity.mCamera.close(); activity.cameraOpen = 0 }

    override fun onError(camera: CameraDevice?, error: Int) {
        activity.runOnUiThread {
            Toast.makeText(activity, "Err", Toast.LENGTH_LONG).show()
        }
        activity.mCamera.close()
        activity.cameraOpen = 0
    }

}