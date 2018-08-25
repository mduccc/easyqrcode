package com.indieteam.qrcode.process.mlkit.barcodes

import android.media.Image
import android.util.Log
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.indieteam.qrcode.ui.activity.MainActivity

class BarCode {
    //configure
    private val option = FirebaseVisionBarcodeDetectorOptions.Builder()
            .setBarcodeFormats(FirebaseVisionBarcode.FORMAT_QR_CODE, FirebaseVisionBarcode.FORMAT_AZTEC)
            .build()
    private var result = ""

    fun run(context: MainActivity, imageYUV: Image, rotation: Int){
        context.checkMlCallback = 0
        result = ""
        var image = FirebaseVisionImage.fromMediaImage(imageYUV, rotation)
        val detector = FirebaseVision.getInstance()
                .visionBarcodeDetector

        detector.detectInImage(image)
                .addOnSuccessListener {
                    for (i in it){
                        val valueType = i.valueType
                        when(valueType){
                            FirebaseVisionBarcode.TYPE_TEXT -> {
                                //Log.d("Barcode", i.displayValue.toString())
                                result += i.displayValue.toString()
                            }
                            FirebaseVisionBarcode.FORMAT_QR_CODE -> {
                                //Log.d("Barcode", i.displayValue.toString())
                                result += i.displayValue.toString()
                            }
                            else -> {
                                //Log.d("Barcode", i.displayValue.toString())
                                result += i.displayValue.toString()
                            }
                        }
                    }
                    if(result != "") {
                        Log.d("Barcode", result)
                        context.done(result)
                        context.checkMlCallback = 0
                    }
                    context.checkMlCallback = 1
                    imageYUV.close()
                }
                .addOnFailureListener {
                    context.checkMlCallback = 1
                    Log.d("err", "run addOnFailureListener")
                }
        image?.let { image = null }

    }
}