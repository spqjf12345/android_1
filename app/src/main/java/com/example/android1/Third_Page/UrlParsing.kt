package com.example.android1.Third_Page

import android.content.Context
import android.util.Log
import org.json.JSONObject
import org.jsoup.Jsoup
import org.w3c.dom.Element
import java.lang.Exception

class UrlParsing (var baseUrl: String, var context: Context): Runnable{
    @Synchronized
    override fun run() {
        try{
            var doc = Jsoup.connect(baseUrl).ignoreContentType(true).get()
            var elements = doc.select("body")
            //Log.d("JSON", doc.toString())
            var result = elements.toString()
            result = result.replace("<body>","")
            result = result.replace("</body>","")
            Log.d("JSON", result)
            var jsonobj: JSONObject =  JSONObject(result)
            Log.d("JSON", jsonobj.toString())
        }catch (e:Exception){
            Log.d("TTT",e.toString())
        }
    }

}