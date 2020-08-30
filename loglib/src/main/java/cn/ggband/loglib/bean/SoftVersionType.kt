package cn.ggband.loglib.bean

/**
 * 软件版本；0:Alpha(内测);1:Beta(公测);2:Release（发布)
 */
enum class SoftVersionType {
    Alpha,
    Beta,
    Release
}

fun SoftVersionType.value(): Int {
    return when (this) {
        SoftVersionType.Alpha -> 0
        SoftVersionType.Beta -> 1
        else -> 2
    }
}