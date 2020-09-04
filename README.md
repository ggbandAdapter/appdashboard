# appdashboard

[app 日志 || 异常 || 版本管理平台](http://www.appdashboard.cn/)

[![Release](https://jitpack.io/v/ggbandAdapter/appdashboard.svg)](https://jitpack.io/#ggbandAdapter/appdashboard)
[![Platform](https://img.shields.io/badge/platform-android-green.svg)](http://developer.android.com/index.html)
![SDK](https://img.shields.io/badge/SDK-15%2B-green.svg)

### 注意：
  + 如果app不是Android平台，请通过[API接入](https://github.com/ggbandAdapter/appdashboard/apidoc.md).

## 使用方法

1. 配置JitPack相关的配置信息
 + 在项目根目录build.gradle 文件中加入如下配置:
```javascript
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

 + 引入依赖:
```javascript
	dependencies {
		implementation 'com.github.ggbandAdapter:appdashboard:v1.0.0'
	}
```
## sdk api使用
 + 初始化:
```javascript
 	AppdashboardKit.init(app, logTagCmd, appId)
```
    + 参数定义

	|    参数名     |      定义   |          例子            |
	|     app       | Application |          app            |
	|   logTagCmd  |   log cmd   | logcat tag1:D tag2:I *:S |
	|    appId     |  平台APPID  |         kJOxWfRd         |
  + 检测新版本
```javascript
   val result = AppdashboardKit.apiHelper.checkNewVersion()
```
  + 上传日志文件
```javascript
     val result = AppdashboardKit.apiHelper.upLogFile()
```
  + 上传异常信息
```javascript
     val result = AppdashboardKit.apiHelper.upLoadCashLogs()
```
## 更多请看 [example](https://github.com/ggbandAdapter/appdashboard/tree/master/app)
