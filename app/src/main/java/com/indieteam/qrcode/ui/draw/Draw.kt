package com.indieteam.qrcode.ui.draw

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.view.View
import com.indieteam.qrcode.ui.activity.MainActivity


class Draw(val activity: MainActivity): View(activity) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    var drawIt = "  "

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawSquare(canvas!!)
    }

    fun drawSquare(canvas: Canvas){
        paint.color = Color.rgb(0, 230, 0)
        paint.strokeWidth = 5f
        paint.style  = Paint.Style.STROKE
        val left = (activity.widthPixels/100f)*5f
        val top = (activity.heightPixels/100f)*50f - ((activity.widthPixels*activity.camOutputSizeWidth)/activity.camOutputSizeHeight)/2 + (activity.heightPixels/100f)*2f
        val right = (activity.widthPixels/100f)*95f
        val bottom = top + ((activity.widthPixels*activity.camOutputSizeWidth)/activity.camOutputSizeHeight) - (activity.heightPixels/100f)*4f
        canvas.drawRect(left, top, right, bottom, paint)
    }

    private fun drawBarCode(canvas: Canvas){
        postInvalidateOnAnimation()
        paint.let {
            it.color = Color.parseColor("#fdc51162")
            it.textSize = 30f
            it.typeface = Typeface.DEFAULT_BOLD
        }
        canvas.drawText(drawIt, 0f, 0f, paint)
    }

    fun drawAgain(){
        postInvalidateOnAnimation()
    }
}