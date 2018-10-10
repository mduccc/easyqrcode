package com.indieteam.qrcode.ui.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.indieteam.qrcode.R
import kotlinx.android.synthetic.main.activity_gen_qr.*
import net.glxn.qrgen.android.QRCode
import net.glxn.qrgen.core.image.ImageType

class GenQRActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gen_qr)
        img_qr.scaleType = ImageView.ScaleType.CENTER
        img_qr.setImageDrawable(resources.getDrawable(R.drawable.ic_qr_blank_64dp))
        make_qr.setOnClickListener {
            if (text_qr.text.toString().isNotBlank()) {
                val qrBitmap = QRCode.from(text_qr.text.toString().trim()).to(ImageType.PNG).withSize(1300, 1300).bitmap()
                img_qr.scaleType = ImageView.ScaleType.CENTER_INSIDE
                img_qr.setImageBitmap(qrBitmap)
            }else {
                img_qr.scaleType = ImageView.ScaleType.CENTER
                img_qr.setImageDrawable(resources.getDrawable(R.drawable.ic_qr_blank_64dp))
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}
