package cn.ggband.loglib.db.tb;

/**
 * 异常表
 */
public class TbCash {
    //主键ID
    private Integer id;
    //app 版本号
    private int versionCode;
    //app版本
    private String versionName;
    //软件版本；0:Alpha(内测);1:Beta(公测);2:Release（发布)
    private int softVersion;

    private String appName;
    //异常标识
    private String cashTag;
    //异常名称
    private String cashName;
    //异常详情
    private String cashDetail;
    //异常时间
    private String cashTime;
    //是否已经上报
    private int isReported;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public int getSoftVersion() {
        return softVersion;
    }

    public void setSoftVersion(int softVersion) {
        this.softVersion = softVersion;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getCashTag() {
        return cashTag;
    }

    public void setCashTag(String cashTag) {
        this.cashTag = cashTag;
    }

    public String getCashName() {
        return cashName;
    }

    public void setCashName(String cashName) {
        this.cashName = cashName;
    }

    public String getCashDetail() {
        return cashDetail;
    }

    public void setCashDetail(String cashDetail) {
        this.cashDetail = cashDetail;
    }

    public String getCashTime() {
        return cashTime;
    }

    public void setCashTime(String cashTime) {
        this.cashTime = cashTime;
    }

    public int getIsReported() {
        return isReported;
    }

    public void setIsReported(int isReported) {
        this.isReported = isReported;
    }

    @Override
    public String toString() {
        return "TbCash{" +
                "id=" + id +
                ", versionCode=" + versionCode +
                ", versionName='" + versionName + '\'' +
                ", softVersion=" + softVersion +
                ", appName='" + appName + '\'' +
                ", cashTag='" + cashTag + '\'' +
                ", cashName='" + cashName + '\'' +
                ", cashDetail='" + cashDetail + '\'' +
                ", cashTime=" + cashTime +
                ", isReported=" + isReported +
                '}';
    }
}
