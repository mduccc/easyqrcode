package com.indieteam.qrcode.ui.camera

import android.hardware.camera2.CameraDevice
import android.view.Surface
import android.widget.RelativeLayout
import com.indieteam.qrcode.process.callback.CameraPreviewSessionCallback
import com.indieteam.qrcode.ui.activity.MainActivity
import com.indieteam.qrcode.ui.draw.DrawOnPrevew
import kotlinx.android.synthetic.main.activity_main.*

class CameraPreview(val activity: MainActivity){

    fun init(){
        activity.runOnUiThread {
            val layoutParam = RelativeLayout.LayoutParams(activity.widthPixels, (activity.widthPixels * activity.camOutputSizeWidth) / activity.camOutputSizeHeight)
            activity.mytextureView.layoutParams = layoutParam
            activity.mytextureView.y = (activity.heightPixels / 100f) * 50f - ((activity.widthPixels * activity.camOutputSizeWidth) / activity.camOutputSizeHeight) / 2f
            activity.drawOnPrevew = DrawOnPrevew(activity)
            activity.rl_main_act.addView(activity.drawOnPrevew)
        }

        val texture = activity.mytextureView.surfaceTexture
        //resolution will show on preview
        texture.setDefaultBufferSize(activity.previewWidth, activity.previewHeight)

        val outputSurface = ArrayList<Surface>(2)
        outputSurface.apply { add(activity.imageReader.surface); add(Surface(texture)) }

        try {
            //create request capture
            activity.captureRequestForPreview = activity.mCamera.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
            //output to drawOnPrevew image on
            activity.captureRequestForPreview.apply { addTarget(Surface(texture)); addTarget(activity.imageReader.surface) }
            // after request capture, start capture session
            activity.mCamera.createCaptureSession(outputSurface, CameraPreviewSessionCallback(activity), null)
        }catch (e: Exception){ }
    }


}