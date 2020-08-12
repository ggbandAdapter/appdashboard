package cn.ggband.loglib.req


import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets


/**
 * Http
 */
object HttpClient {

    /**
     * post请求
     * @param urlStr url
     * @param reqPair 请求参数
     * @param headers header
     * @param reqType 请求包格式
     */
    @Throws
    fun reqPost(
        urlStr: String,
        reqPair: Map<String, Any>?,
        headers: Map<String, String>? = null,
        reqType: ReqType = ReqType.JSON
    ): String {
        val url = URL(urlStr)
        val conn = url.openConnection() as HttpURLConnection
        conn.connectTimeout = 8000
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

    /**
     *
     * @param urlStr  url地址
     * @param textMap 附带信息
     * @param fileMap 文件列表
     * @return 返回json的报文 如果失败，则为空
     */
    fun postFileByForm(
        urlStr: String, textMap: Map<String, Any>?,
        fileMap: Map<String?, String?>?
    ): String? {
        var res = ""
        var conn: HttpURLConnection? = null
        val BOUNDARY =
            "---------------------------" //boundary就是request头和上传文件内容的分隔符
        try {
            val url = URL(urlStr)
            conn = url.openConnection() as HttpURLConnection
            conn.connectTimeout = 5000
            conn.readTimeout = 30000
            conn.doOutput = true
            conn.doInput = true
            conn.useCaches = false
            conn.requestMethod = "POST"
            conn.setRequestProperty("Connection", "Keep-Alive")
            conn.setRequestProperty(
                "User-Agent",
                "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)"
            )
            conn.setRequestProperty(
                "Content-Type",
                "multipart/form-data; boundary=$BOUNDARY"
            )
            val out: OutputStream = DataOutputStream(conn.outputStream)
            // text
            if (textMap != null) {
                val strBuf = StringBuffer()
                val iter: Iterator<*> = textMap.entries.iterator()
                while (iter.hasNext()) {
                    val entry =
                        iter.next() as Map.Entry<*, *>
                    val inputName = entry.key as String
                    val inputValue = entry.value as String? ?: continue
                    strBuf.append("\r\n").append("--").append(BOUNDARY).append(
                        "\r\n"
                    )
                    strBuf.append(
                        "Content-Disposition: form-data; name=\""
                                + inputName + "\"\r\n\r\n"
                    )
                    strBuf.append(inputValue)
                }
                out.write(strBuf.toString().toByteArray())
            }
            // file
            if (fileMap != null) {
                val iter: Iterator<*> = fileMap.entries.iterator()
                while (iter.hasNext()) {
                    val entry =
                        iter.next() as Map.Entry<*, *>
                    val inputName = entry.key as String
                    val inputValue = entry.value as String? ?: continue
                    val file = File(inputValue)
                    val filename: String = file.getName()
                    var contentType: String = ""
                    if (contentType == null || contentType == "") {
                        contentType = "application/octet-stream"
                    }
                    val strBuf = StringBuffer()
                    strBuf.append("\r\n").append("--").append(BOUNDARY).append(
                        "\r\n"
                    )
                    strBuf.append(
                        "Content-Disposition: form-data; name=\""
                                + inputName + "\"; filename=\"" + filename
                                + "\"\r\n"
                    )
                    strBuf.append("Content-Type:$contentType\r\n\r\n")
                    out.write(strBuf.toString().toByteArray())
                    val `in` = DataInputStream(
                        FileInputStream(file)
                    )
                    var bytes = 0
                    val bufferOut = ByteArray(1024)
                    while (`in`.read(bufferOut).also({ bytes = it }) != -1) {
                        out.write(bufferOut, 0, bytes)
                    }
                    `in`.close()
                }
            }
            val endData = "\r\n--$BOUNDARY--\r\n".toByteArray()
            out.write(endData)
            out.flush()
            out.close()
            // 读取返回数据
            val strBuf = StringBuffer()
            var reader: BufferedReader? = BufferedReader(
                InputStreamReader(
                    conn.inputStream
                )
            )
            var line: String? = null
            while (reader!!.readLine().also { line = it } != null) {
                strBuf.append(line).append("\n")
            }
            res = strBuf.toString()
            reader.close()
            reader = null
        } catch (e: Exception) { //日志处理
            res = ""
        } finally {
            if (conn != null) {
                conn.disconnect()
                conn = null
            }
        }
        return res
    }
}