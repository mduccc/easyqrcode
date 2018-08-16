package com.indieteam.qrcode.ui.camera

import android.graphics.Rect
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CaptureRequest
import android.view.Surface
import android.widget.RelativeLayout
import com.indieteam.qrcode.process.callback.CameraPreviewSessionCallback
import com.indieteam.qrcode.ui.activity.MainActivity
import com.indieteam.qrcode.ui.draw.Draw
import kotlinx.android.synthetic.main.activity_main.*

class CameraPreview(val activity: MainActivity){

    fun init(){
        activity.runOnUiThread {
            val layoutParam = RelativeLayout.LayoutParams(activity.widthPixels, (activity.widthPixels * activity.camOutputSizeWidth) / activity.camOutputSizeHeight)
            activity.mytextureView.layoutParams = layoutParam
            activity.mytextureView.y = (activity.heightPixels / 100f) * 50f - ((activity.widthPixels * activity.camOutputSizeWidth) / activity.camOutputSizeHeight) / 2f
            activity.draw = Draw(activity)
            activity.rl_main_act.addView(activity.draw)
        }

        val texture = activity.mytextureView.surfaceTexture
        //resolution will show on preview
        texture.setDefaultBufferSize(activity.camOutputSizeWidth, activity.camOutputSizeHeight)

        val outputSurface = ArrayList<Surface>(2)
        outputSurface.let {
            it.add(activity.imageReader.surface)
            it.add(Surface(texture))
        }

        try {
            //create request capture
            activity.captureRequestForPreview = activity.mCamera.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
            //output to draw image on
            activity.captureRequestForPreview.let {

                //val cropRect = Rect(0, 0, activity.camOutputSizeWidth, activity.camOutputSizeWidth)
                //it.set(CaptureRequest.CONTROL_AE_TARGET_FPS_RANGE, Range(0, 30))
                //it.set(CaptureRequest.SCALER_CROP_REGION, cropRect)
                it.addTarget(Surface(texture))
                it.addTarget(activity.imageReader.surface)
            }
            // after request capture, start capture session
            activity.mCamera.createCaptureSession(outputSurface, CameraPreviewSessionCallback(activity), null)
        }catch (e: Exception){ }
    }


}