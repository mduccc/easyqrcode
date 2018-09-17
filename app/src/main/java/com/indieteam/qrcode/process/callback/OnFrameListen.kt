package com.indieteam.qrcode.process.callback

import android.media.ImageReader
import com.indieteam.qrcode.device.Rotation
import com.indieteam.qrcode.ui.activity.MainActivity

class OnFrameListen(val activity: MainActivity): ImageReader.OnImageAvailableListener{

    override fun onImageAvailable(reader: ImageReader) {
        activity.j++
        activity.imageOnFrame = reader.acquireNextImage()
        if(activity.checkMlCallback == 1) {
            activity.barCode.run(activity, activity.imageOnFrame, Rotation(activity).get(activity.useCamera))
            activity.j = 0
        }
        activity.imageOnFrame.close()
    }

}