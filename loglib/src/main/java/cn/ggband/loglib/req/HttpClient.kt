package cn.ggband.loglib.req


import cn.ggband.loglib.utils.CommUtils
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
     * 发送请求
     * @param urlStr url
     * @param data  数据包 data为json字符串时Body为JSON;否则为Form
     * @param headers HEAD
     * @param reqMethod 请求方法
     */
    fun req(
        urlStr: String,
        data: String?,
        headers: Map<String, String>? = null,
        reqMethod: ReqMethod = ReqMethod.POST
    ): String {
        var res = ""
        var conn: HttpURLConnection? = null
        try {
            val url = URL(urlStr)
            conn = url.openConnection() as HttpURLConnection
            conn.run {
                connectTimeout = 8000
                requestMethod = if (reqMethod == ReqMethod.POST) "POST" else "GET"
                headers?.forEach {
                    conn?.setRequestProperty(it.key, it.value)
                }
                setRequestProperty("Charset", "UTF-8")
                if (CommUtils.isJson(data)) {
                    setRequestProperty("Content-Type", "application/json; charset=UTF-8")
                    setRequestProperty("accept", "application/json")
                }
                instanceFollowRedirects = false
                if (!data.isNullOrEmpty()) {
                    val reqBytes = data.toByteArray(StandardCharsets.UTF_8)
                    setRequestProperty("Content-Length", reqBytes.toString())
                    val outStream = outputStream
                    outStream.write(reqBytes)
                }
                connect()
                val bufReader =
                    BufferedReader(InputStreamReader(inputStream, StandardCharsets.UTF_8))
                val backData = StringBuilder()
                var line: String? = ""
                while (bufReader.readLine().also { line = it } != null) backData.append(line)
                    .append(
                        "\r\n"
                    )
                res = backData.toString()
            }

        } catch (e: Throwable) {
            res = e.toString()
        } finally {
            conn?.let {
                it.disconnect()
                conn = null
            }
        }

        return res
    }

    /**
     * post请求
     * @param urlStr url
     * @param reqPair 请求参数
     * @param headers header
     * @param reqType 请求包格式
     */
    fun reqPost(
        urlStr: String,
        reqPair: Map<String, Any>?,
        headers: Map<String, String>? = null,
        reqType: ReqType = ReqType.JSON
    ): String {
        return req(urlStr, buildReqData(reqType, reqPair), headers)
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
     * 表单上传
     * @param urlStr  url地址
     * @param textMap 附带信息
     * @param fileMap 文件列表
     * @return 返回json的报文 如果失败，则为空
     */
    fun postFileByForm(
        urlStr: String, textMap: Map<String, Any>?,
        fileMap: Map<String, String>?,
        headers: Map<String, String>? = null
    ): String {
        var res = ""
        var conn: HttpURLConnection? = null
        //boundary就是request头和上传文件内容的分隔符
        val BOUNDARY = "---------------------------"
        try {
            val url = URL(urlStr)
            conn = url.openConnection() as HttpURLConnection
            conn.run {
                connectTimeout = 5000
                readTimeout = 30000
                doOutput = true
                doInput = true
                useCaches = false
                requestMethod = "POST"
                headers?.forEach {
                    conn?.setRequestProperty(it.key, it.value)
                }
                setRequestProperty("Connection", "Keep-Alive")
                setRequestProperty(
                    "User-Agent",
                    "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)"
                )
                setRequestProperty(
                    "Content-Type",
                    "multipart/form-data; boundary=$BOUNDARY"
                )
                val outputStream: OutputStream = DataOutputStream(outputStream)
                // text
                textMap?.entries?.forEach {
                    val strBuf = StringBuffer()
                    strBuf.append("\r\n").append("--").append(BOUNDARY).append(
                        "\r\n"
                    )
                    strBuf.append(
                        "Content-Disposition: form-data; name=\""
                                + it.key + "\"\r\n\r\n"
                    )
                    strBuf.append(it.value)

                    outputStream.write(strBuf.toString().toByteArray())
                }
                // file
                fileMap?.entries?.forEach {
                    val file = File(it.value)
                    val filename: String = file.name
                    val strBuf = StringBuffer()
                    strBuf.append("\r\n").append("--").append(BOUNDARY).append(
                        "\r\n"
                    )
                    strBuf.append(
                        "Content-Disposition: form-data; name=\""
                                + it.key + "\"; filename=\"" + filename
                                + "\"\r\n"
                    )
                    strBuf.append("Content-Type:application/octet-stream\r\n\r\n")
                    outputStream.write(strBuf.toString().toByteArray())
                    val dataInput = DataInputStream(
                        FileInputStream(file)
                    )
                    var bytes = 0
                    val bufferOut = ByteArray(1024)
                    while (dataInput.read(bufferOut).also { bytes = it } != -1) {
                        outputStream.write(bufferOut, 0, bytes)
                    }
                    dataInput.close()
                }

                val endData = "\r\n--$BOUNDARY--\r\n".toByteArray()
                outputStream.write(endData)
                outputStream.flush()
                outputStream.close()
                // 读取返回数据
                val strBuf = StringBuffer()
                var reader: BufferedReader? = BufferedReader(
                    InputStreamReader(inputStream, "UTF-8")
                )
                var line: String? = null
                while (reader!!.readLine().also { line = it } != null) {
                    strBuf.append(line).append("\n")
                }
                res = strBuf.toString()
                reader.close()
                reader = null
            }
        } catch (e: Exception) { //日志处理
            res = ""
        } finally {
            conn?.let {
                it.disconnect()
                conn = null
            }
        }
        return res
    }
}