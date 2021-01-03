package com.example.mediaplayer.network

import android.text.TextUtils
import org.apache.http.NameValuePair
import java.io.BufferedWriter
import java.io.IOException
import java.io.OutputStream
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

class UrlConnManager {
    companion object {
        fun getHttpURLConnection(url: String): HttpURLConnection? {
            var httpURLConnection: HttpURLConnection? = null
            try {
                val url = URL(url)
                httpURLConnection = url.openConnection() as HttpURLConnection
                with(httpURLConnection) {
                    connectTimeout = 15000
                    readTimeout = 15000
                    requestMethod = "POST"
                    setRequestProperty("Connection", "Keep-Alive")
                    doInput = true
                    doOutput = true
                }
            } catch (e: IOException) {

            }
            return httpURLConnection
        }

        fun postParams(output: OutputStream, parasList: List<NameValuePair>) {
            var stringBuilder: StringBuilder = java.lang.StringBuilder()
            parasList.forEach {
                if (!TextUtils.isEmpty(stringBuilder)) {
                    stringBuilder.append("&")
                }
                stringBuilder.append(URLEncoder.encode(it.name, "UTF-8"))
                stringBuilder.append("=")
                stringBuilder.append(it.value, "UTF-8")
            }
            val writer = BufferedWriter(OutputStreamWriter(output, "UTF-8"))
            writer.write(stringBuilder.toString())
            writer.flush()
            writer.close()
        }
    }
}