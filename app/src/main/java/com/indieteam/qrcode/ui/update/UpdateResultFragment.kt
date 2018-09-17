package com.indieteam.qrcode.ui.update

import com.indieteam.qrcode.ui.fragment.ResultFragment
import kotlinx.android.synthetic.main.fragment_result.*

class UpdateResultFragment(private val fragment: ResultFragment){

    fun update(result: String){ fragment.text_view_result.text = result }

}