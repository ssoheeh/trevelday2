package com.example.travelday_2

import android.os.AsyncTask
import android.util.Log
import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.JsonParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class Task {
    private val clientKey = "UqssreWmwfkh6FaewreJVqqVFkqhGGGWFagaLj97"

    suspend fun doInBackground(vararg params: String?): Double {
        val from = params[0]
        val to = params[1]
        var currencyRate = 0.0

        val url = try {
            URL(
                "https://api.freecurrencyapi.com/v1/latest?apikey="
                        + clientKey
                        + "&currencies="
                        + from
                        + "," + to
            )
        } catch (e: IOException) {
            e.printStackTrace()
            return currencyRate
        }

        return withContext(Dispatchers.IO) {
            try {
                val conn = url.openConnection() as HttpURLConnection
                conn.setRequestProperty(
                    "Content-Type",
                    "application/x-www-form-urlencoded;charset=UTF-8"
                )

                if (conn.responseCode == HttpURLConnection.HTTP_OK) {
                    val tmp = InputStreamReader(conn.inputStream, "UTF-8")
                    val reader = BufferedReader(tmp)
                    val buffer = StringBuffer()
                    var str: String?
                    while (reader.readLine().also { str = it } != null) {
                        buffer.append(str)
                    }
                    val receiveMsg = buffer.toString()
                    Log.e("receiveMsg : ", receiveMsg)
                    reader.close()

                    val jsonParser = JsonParser()
                    val obj = jsonParser.parse(receiveMsg)
                    val jsonObj = obj as JsonObject
                    val curobj = jsonObj.get("data") as JsonObject
                    Log.e("parse", "$from ${curobj.get(from).toString()}")
                    Log.e("parse", "$to ${curobj.get(to).toString()}")
                    val a = curobj.get(from).toString()
                    val b = curobj.get(to).toString()
                    currencyRate = b.toDouble() / a.toDouble()
                    Log.e("parse", "currencyRate $currencyRate")
                } else {
                    Log.e("통신 결과", conn.responseCode.toString() + "에러")
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }

            currencyRate
        }
    }

    suspend fun executeAsync(vararg params: String?): Double {
        return doInBackground(*params)
    }
}