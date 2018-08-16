package com.indieteam.qrcode.process.mlkit.barcodes

class ParseResult{

    fun run(result: String): String{
        val newResult = "Result:\n\n$result"
        return newResult
    }

}