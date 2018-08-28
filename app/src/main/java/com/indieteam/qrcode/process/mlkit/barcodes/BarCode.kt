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
                            FirebaseVisionBarcode.TYPE_CALENDAR_EVENT -> {
                                result += "EVENT: " +
                                        "\n\n Status: \n" +
                                        i.calendarEvent?.status.toString() +
                                        "\n\n Summary: \n" +
                                        i.calendarEvent?.summary.toString() +
                                        "\n\n Description: \n" +
                                        i.calendarEvent?.description.toString() +
                                        "\n\n Start: \n" +
                                        i.calendarEvent?.start?.hours +
                                        ":" + i.calendarEvent?.start?.minutes +
                                        i.calendarEvent?.start?.day +
                                        "/" + i.calendarEvent?.start?.month +
                                        "/" + i.calendarEvent?.start?.year +
                                        "\n\n End: \n" +
                                        i.calendarEvent?.end?.hours +
                                        ":" + i.calendarEvent?.end?.minutes +
                                        i.calendarEvent?.end?.day +
                                        "/" + i.calendarEvent?.end?.month +
                                        "/" + i.calendarEvent?.end?.year +
                                        "\n\n Location: \n" +
                                        i.calendarEvent?.location
                            }
                            FirebaseVisionBarcode.FORMAT_QR_CODE ->{
                                result += "CODE: \n\n" + i.displayValue.toString()
                            }
                            FirebaseVisionBarcode.FORMAT_UNKNOWN ->{
                                result += "RESULT: \n\n" + i.displayValue.toString()
                            }
                            FirebaseVisionBarcode.TYPE_CONTACT_INFO ->{
                                result += "CONTACTS:" +
                                        "\n\n Name: \n" + i.contactInfo?.name?.last +
                                        " " + i.contactInfo?.name?.first +
                                        "\n\n Phone: \n"
                                i.contactInfo?.let {
                                    it.phones?.let {
                                        for (phone in it) {
                                            result += phone.number + "\n"
                                        }
                                    }
                                }
                                result += "\n Email: \n"
                                i.contactInfo?.let {
                                    it.emails?.let {
                                        for (email in it) {
                                            result += email.address + "\n"
                                        }
                                    }
                                }
                                result += "\n Addresses: \n"
                                i.contactInfo?.let {
                                    it.addresses?.let {
                                        for (addresses in it){
                                            for(addressesLines in addresses.addressLines){
                                                result += addressesLines + "\n"
                                            }
                                        }
                                    }
                                }

                                result += "\n Title: \n" + i.contactInfo?.title +
                                        "\n\n  Organization: \n" + i.contactInfo?.organization + "" +
                                        "\n\n Website: \n"
                                i.contactInfo?.let {
                                    it.urls?.let {
                                        for(url in it){
                                            result += url
                                        }
                                    }
                                }
                            }
                            FirebaseVisionBarcode.TYPE_PHONE ->{
                                result += "PHONE: " +
                                        "\n\n Number: " + i.phone?.number
                            }
                            FirebaseVisionBarcode.TYPE_EMAIL ->{
                                result += "EMAIL: " +
                                        "\n\n Address: \n" +
                                        i.email?.address +
                                        "\n\n Subject: \n" +
                                        i.email?.subject +
                                        "\n\n Body: \n" +
                                        i.email?.body
                            }
                            FirebaseVisionBarcode.TYPE_WIFI ->{
                                result += "WIFI: " +
                                        "\n\n SSID: " + i.wifi?.ssid +
                                        "\n Password: " + i.wifi?.password +
                                        "\n Encryption Type: " + i.wifi?.encryptionType
                            }
                            FirebaseVisionBarcode.TYPE_SMS ->{
                                result += "SMS: " +
                                        "\n\n Phone number: \n" + i.sms?.phoneNumber +
                                        "\n\n Message: \n" + i.sms?.message

                            }
                            FirebaseVisionBarcode.TYPE_TEXT ->{
                                result += "TEXT: \n\n" + i.displayValue
                            }
                            FirebaseVisionBarcode.TYPE_URL ->{
                                result += "URL: \n\n" + i.displayValue
                            }
                            FirebaseVisionBarcode.TYPE_GEO ->{
                                result += "GEOLOCATION: " +
                                        "\n\n Lat: " + i.geoPoint?.lat +
                                        "\n Lng: " + i.geoPoint?.lng
                            }
                            else -> {
                                result += "RESULT: \n\n" + i.displayValue
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