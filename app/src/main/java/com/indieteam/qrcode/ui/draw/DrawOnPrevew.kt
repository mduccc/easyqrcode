package com.indieteam.qrcode.ui.draw

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.View
import com.indieteam.qrcode.ui.activity.MainActivity


class DrawOnPrevew(val activity: MainActivity): View(activity) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawSquare(canvas!!)
    }

    fun drawSquare(canvas: Canvas){
        paint.apply {
            color = Color.rgb(255, 255, 255)
            strokeWidth = 5f
            style  = Paint.Style.STROKE
        }
        val left = (activity.widthPixels/100f)*5f
        val top = (activity.heightPixels/100f)*50f - ((activity.widthPixels*activity.camOutputSizeWidth)/activity.camOutputSizeHeight)/2 + (activity.heightPixels/100f)*2f
        val right = (activity.widthPixels/100f)*95f
        val bottom = top + ((activity.widthPixels*activity.camOutputSizeWidth)/activity.camOutputSizeHeight) - (activity.heightPixels/100f)*4f
        canvas.drawRect(left, top, right, bottom, paint)
    }

}