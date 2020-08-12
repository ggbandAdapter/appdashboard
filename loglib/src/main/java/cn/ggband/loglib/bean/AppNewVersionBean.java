package cn.ggband.loglib.bean;

/**
 * APP新版本信息
 */
public class AppNewVersionBean {

    private boolean hashNewVersion;
    private VersionBean version;

    public boolean isHashNewVersion() {
        return hashNewVersion;
    }

    public void setHashNewVersion(boolean hashNewVersion) {
        this.hashNewVersion = hashNewVersion;
    }

    public VersionBean getVersion() {
        return version;
    }

    public void setVersion(VersionBean version) {
        this.version = version;
    }

    public static class VersionBean {
        private String appName;
        private String versionName;
        private int versionCode;
        private String packageName;
        private String downUrl;
        private int isForce;
        private int softVersion;
        private String versionTips;
        private int enable;
        private String remarks;
        private int fileSize;
        private long createTime;
        private long updateTime;

        public String getAppName() {
            return appName;
        }

        public void setAppName(String appName) {
            this.appName = appName;
        }

        public String getVersionName() {
            return versionName;
        }

        public void setVersionName(String versionName) {
            this.versionName = versionName;
        }

        public int getVersionCode() {
            return versionCode;
        }

        public void setVersionCode(int versionCode) {
            this.versionCode = versionCode;
        }

        public String getPackageName() {
            return packageName;
        }

        public void setPackageName(String packageName) {
            this.packageName = packageName;
        }

        public String getDownUrl() {
            return downUrl;
        }

        public void setDownUrl(String downUrl) {
            this.downUrl = downUrl;
        }

        public int getIsForce() {
            return isForce;
        }

        public void setIsForce(int isForce) {
            this.isForce = isForce;
        }

        public int getSoftVersion() {
            return softVersion;
        }

        public void setSoftVersion(int softVersion) {
            this.softVersion = softVersion;
        }

        public String getVersionTips() {
            return versionTips;
        }

        public void setVersionTips(String versionTips) {
            this.versionTips = versionTips;
        }

        public int getEnable() {
            return enable;
        }

        public void setEnable(int enable) {
            this.enable = enable;
        }

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }

        public int getFileSize() {
            return fileSize;
        }

        public void setFileSize(int fileSize) {
            this.fileSize = fileSize;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public long getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(long updateTime) {
            this.updateTime = updateTime;
        }

        @Override
        public String toString() {
            return "VersionBean{" +
                    "appName='" + appName + '\'' +
                    ", versionName='" + versionName + '\'' +
                    ", versionCode=" + versionCode +
                    ", packageName='" + packageName + '\'' +
                    ", downUrl='" + downUrl + '\'' +
                    ", isForce=" + isForce +
                    ", softVersion=" + softVersion +
                    ", versionTips='" + versionTips + '\'' +
                    ", enable=" + enable +
                    ", remarks='" + remarks + '\'' +
                    ", fileSize=" + fileSize +
                    ", createTime=" + createTime +
                    ", updateTime=" + updateTime +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "AppNewVersionBean{" +
                "hashNewVersion=" + hashNewVersion +
                ", version=" + version +
                '}';
    }
}
