package cn.ggband.loglib.bean

/**
 * 日志类型
 */
enum class LogType {
    DEF_LOG,  //普通日志
    CASH_LOG //异常日志
}

fun LogType.value():Int{
    return  if(this==LogType.DEF_LOG) 0 else 1
}