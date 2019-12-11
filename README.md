# Componentization

这是由一个模块化和组件化架构搭建的项目，并实现了几个常见的功能，包含有如下内容：

- Crash日志的捕获和保存。
- 两种热修复方案实现：[阿里AndFix](https://github.com/alibaba/AndFix)和[腾讯Tinker](https://github.com/Tencent/tinker)。
- 自定义插件化，加载未安装的apk文件。
- 通过ioc注解，实现仿写一个ButterKnife。 
- 使用jni/ndk对图片进行哈夫曼压缩。

## 使用

### Crash获取

```java
//获取上次闪退日志
ExceptionCrashManager.getInstance().getCrashFile();
```
### AndFix(只支持2.3到7.0）

```java
//加载.apatch文件
AndFixPatchManager.getInstance().addPatch( andFixPath );
```

### Tinker

```java
//加载.apk文件
TinkerManager.loadPatch( tinkerFixPath );
```

### 插件加载

```java
//获取插件
 PluginInfo pluginInfo = PluginManager.getInstance( getApplicationContext() ).loadApk( bundlePath );
        try {
            //加载插件里的BundleTest类
            Class<?> aClass = pluginInfo.mClassLoader.loadClass( "com.hechuangwu.bundle.BundleTest" );
            if (aClass != null) {
                Object instance = aClass.newInstance();
                //执行test方法
                Method method = aClass.getMethod( "test" );
                method.invoke( instance );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
```

### IOC注解

```java
//绑定
ButterKnife.bind( this );
```

### IOC注解

```java
//图片压缩
ImageCompress.compress( srcPath, dstPathHF );
```








