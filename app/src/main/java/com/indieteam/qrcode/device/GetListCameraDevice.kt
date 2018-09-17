package com.indieteam.qrcode.device

import android.content.Context
import android.hardware.camera2.CameraManager

class GetListCameraDevice(context: Context){

    private val manager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    private var list = listOf<String>()

    fun getList(): List<String> { for (i in manager.cameraIdList){ list += i }; return list }

}

