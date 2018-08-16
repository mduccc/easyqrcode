package com.indieteam.qrcode.ui.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.indieteam.qrcode.R
import com.indieteam.qrcode.process.mlkit.barcodes.ParseResult
import com.indieteam.qrcode.ui.update.UpdateResultFragment

class ResultFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val params = arguments
        var result = params?.getString("result")

        result =  ParseResult().run(result!!)
        UpdateResultFragment(this).update(result)
    }

}
