package cn.ggband.loglib.req

import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets

/**
 * Http
 */
object HttpClient {

    /**
     * post请求
     * @param strUrl url
     * @param reqPair 请求参数
     * @param headers header
     * @param reqType 请求包格式
     */
    @Throws
    fun reqPost(
        strUrl: String,
        reqPair: Map<String, Any>?,
        headers: Map<String, String>? = null,
        reqType: ReqType = ReqType.JSON
    ): String {
        val url = URL(strUrl)
        val conn = url.openConnection() as HttpURLConnection
        conn.connectTimeout = 3000
        conn.requestMethod = "POST"
        headers?.forEach {
            conn.setRequestProperty(it.key, it.value)
        }
        conn.setRequestProperty("Charset", "UTF-8")
        if (reqType == ReqType.JSON) {
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8")
            conn.setRequestProperty("accept", "application/json")
        }
        conn.instanceFollowRedirects = false
        val reqStr = buildReqData(reqType, reqPair)
        if (reqStr.isNotEmpty()) {
            val reqBytes = reqStr.toByteArray(StandardCharsets.UTF_8)
            conn.setRequestProperty("Content-Length", reqBytes.toString())
            val outStream = conn.outputStream
            outStream.write(reqBytes)
        }
        conn.connect()
        val bufReader =
            BufferedReader(InputStreamReader(conn.inputStream, StandardCharsets.UTF_8))
        val backData = StringBuilder()
        var line: String? = ""
        while (bufReader.readLine().also { line = it } != null) backData.append(line).append(
            "\r\n"
        )
        return backData.toString()
    }

    /**
     * 构建请求参数
     * @param reqType 请求包格式
     * @param reqPair 请求参数
     */
    private fun buildReqData(reqType: ReqType = ReqType.JSON, reqPair: Map<String, Any>?): String {
        return if (reqType == ReqType.JSON) {
            if (reqPair.isNullOrEmpty()) "" else JSONObject(reqPair.toMap()).toString()
        } else {
            val reqBuilder = StringBuffer()
            reqPair?.entries?.forEachIndexed { index, entry ->
                reqBuilder.append("${entry.key}=${entry.value}")
                if (index != reqPair.size)
                    reqBuilder.append("&")
            }
            reqBuilder.toString()
        }
    }
}