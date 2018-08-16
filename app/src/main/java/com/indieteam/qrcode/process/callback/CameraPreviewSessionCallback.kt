package com.indieteam.qrcode.process.callback

import android.hardware.camera2.CameraCaptureSession
import android.widget.Toast
import com.indieteam.qrcode.ui.activity.MainActivity

class CameraPreviewSessionCallback(val activity: MainActivity): CameraCaptureSession.StateCallback(){
    override fun onConfigured(session: CameraCaptureSession?) {
        //setRepeatingRequest(): repeating capture to update mytextureView
        session!!.setRepeatingRequest(activity.captureRequestForPreview.build(), null, activity.mBackgroundHandler)
        activity.cameraCaptureSessionForPreview = session
    }

    override fun onConfigureFailed(session: CameraCaptureSession?) {
        Toast.makeText(activity, "sessionCallback Fase", Toast.LENGTH_LONG).show()
    }
}
