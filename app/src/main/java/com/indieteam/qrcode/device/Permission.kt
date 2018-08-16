package com.indieteam.qrcode.device

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat.requestPermissions
import android.support.v4.content.ContextCompat

const val PERMISSIONS_REQUEST_CAMERA = 1
const val PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1

class Permission(private val activity: Activity){

    fun getPermissionCam(): Boolean{
        // Permission is not granted
        var bol = false
        if(ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(activity,
                        arrayOf(Manifest.permission.CAMERA),
                    PERMISSIONS_REQUEST_CAMERA)
        }else{
            //Toast.makeText(activity, "permission granded", Toast.LENGTH_LONG).show()
            bol = true
        }
        return bol
    }

    fun getPermissionStorage(): Boolean{
        var bol = false
        if(ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            requestPermissions(activity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE)
        }else{
            bol = true
        }
        return bol
    }
}