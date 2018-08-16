package com.indieteam.qrcode.device

import android.content.Context
import android.hardware.camera2.CameraManager

class GetListCameraDevice(context: Context){

    private val manager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager

    fun getList(): List<String> {
        var list = listOf<String>()
        for (i in manager.cameraIdList){
            list += i
        }
        return list
    }

}

