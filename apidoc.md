# appdashboard

[app 日志 || 异常 || 版本管理平台](http://www.appdashboard.cn/)

## appdashboard API 接口文档

### 注意：
  + 如果app是android平台，建议你直接接入[android sdk](https://github.com/ggbandAdapter/appdashboard).
  + 所有接口都需要添加** appId ** header参数.
  + baseurl为 *** http://www.appdashboard.cn/***

### api 使用
  #### 1.检测新版本

  + ** url** :  *** /app/version/check ***  ---- POST ----- application/json

  + Parameters:
 - [x] ** softVersion ** integer 软件版本 0:Alpha(内测);1:Beta(公测);2:Release（发布）
 - [x] ** versionCode ** integer 当前版本号

  + Result：
 - [x] ** appName ** string app名称
 - [x] ** createTime ** long 创建时间
 - [x] ** downUrl ** string 下载地址
 - [x] ** softVersion ** integer 软件版本 0:Alpha(内测);1:Beta(公测);2:Release（发布
 - [x] ** versionCode ** integer 版本号
 - [x] ** versionName ** string 版本名称
 - [x] ** isForce ** integer 是否强制更新 1：强制更新，0：非强制更新
 - [x] ** versionTips ** string 版本更新内容
 - [x] ** remarks ** string 备注
----
