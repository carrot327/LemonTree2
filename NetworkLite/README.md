精简版网络库说明文档
==========

## 使用方法

* Android Studio

    ```
    compile 'com.networklite:networklite:0.2.12'
    ```

## 配置OkHttpClient

默认情况下，将直接使用默认的配置生成OkHttpClient，若需要自定义，可以在Application中调用`NetworkLiteHelper.initializeClient `方法进行配置。

```java
OkHttpClient okHttpClient = new OkHttpClient.Builder()
        .connectTimeout(10000L, TimeUnit.MILLISECONDS)
        .readTimeout(10000L, TimeUnit.MILLISECONDS)
        .writeTimeout(10000L, TimeUnit.MILLISECONDS)
        //其他配置
        .build();
NetworkLiteHelper.initializeClient(okHttpClient);
```
## 查看请求日志

初始化OkHttpClient时，通过设置拦截器实现查看请求日志，框架中提供了一个类`HttpLoggerInterceptor`，也可以自行实现一个Interceptor。

```
OkHttpClient okHttpClient = new OkHttpClient.Builder()
        .addInterceptor(new HttpLoggerInterceptor(new HttpLoggerInterceptor.Callback() {
            //是否开启日志输出
            @Override
            public boolean getLogOutputState() {
                return true;
            }

            //日志输出的Tag
            @Override
            public String generateLogTag() {
                return "okhttp";
            }
        }))
        //其他配置
        .build();
NetworkLiteHelper.initializeClient(okHttpClient);
```

## 请求参数签名

初始化OkHttpClient时，通过设置拦截器实现请求参数签名，框架中提供了一个类`ParamsSignAuthInterceptor `，也可以自行实现一个Interceptor。

```
OkHttpClient okHttpClient = new OkHttpClient.Builder()
        .addInterceptor(new ParamsSignAuthInterceptor(
            new ParamsSignAuthInterceptor.Callback() {
                //正则匹配规则列表
                //只要请求地址符合列表中任意一个规则，就会被过滤，不进行参数签名
                @Override
                public List<String> getFilterList() {
                    return filterList;
                }

                //本地与服务器时差
                @Override
                public long getTimeOffset() {
                    return 0;
                }

                //应用id
                @Override
                public String getAppId() {
                    return "nono";
                }

                //md5
                @Override
                public String getMD5String(String s) {
                    return MD5Utils.getMD5String(s);
                }

                //是否开启日志输出
                @Override
                public boolean getLogOutputState() {
                    return true;
                }

                //日志输出的Tag
                @Override
                public String generateLogTag() {
                    return "okhttpParamsSignAuth";
                }
            }))
        //其他配置
        .build();
NetworkLiteHelper.initializeClient(okHttpClient);
```

## 请求头信息

初始化OkHttpClient时，通过设置拦截器实现请求头信息配置，框架中提供了一个类`HeaderInterceptor`，也可以自行实现一个Interceptor。

```
HeaderInterceptor headerInterceptor = new HeaderInterceptor(new HeaderInterceptor.Callback() {
            //是否开启日志输出
            @Override
            public boolean getLogOutputState() {
                return true;
            }

            //日志输出的Tag
            @Override
            public String generateLogTag() {
                return "okhttp";
            }
        });
Map<String, String> defaultHeader = new HashMap<String, String>();
defaultHeader.put("key1", "value1");
//添加头信息：key正则匹配规则，value头信息键值对
//只要请求地址匹配正则匹配规则，就会将对应数据添加到头信息中（可同时匹配多个）
headerInterceptor.addHeader(".*", defaultHeader);
OkHttpClient okHttpClient = new OkHttpClient.Builder()
        .addNetworkInterceptor(headerInterceptor)
        //其他配置
        .build();
NetworkLiteHelper.initializeClient(okHttpClient);
```

## Cookie（包含Session）

初始化OkHttpClient时，通过cookieJar方法配置。

```
//PersistentCookieJar需自行实现或添加依赖库
PersistentCookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(context));
OkHttpClient okHttpClient = new OkHttpClient.Builder()
          .cookieJar(cookieJar)
          //其他配置
         .build();
NetworkLiteHelper.initializeClient(okHttpClient);
```

## HTTPS

初始化OkHttpClient时，通过sslSocketFactory方法配置，框架中提供了一个类`HttpsHelper`

* 设置可访问所有的HTTPS网站

```
OkHttpClient okHttpClient = new OkHttpClient.Builder()
        .sslSocketFactory(HttpsHelper.getSslSocketFactory(null))
        //其他配置
        .build();
NetworkLiteHelper.initializeClient(okHttpClient);
```

* 设置具体的证书

```
InputStream[] iss = new InputStream[2];
iss[0] = 证书1的InputStream;
iss[1] = 证书2的InputStream;
OkHttpClient okHttpClient = new OkHttpClient.Builder()
        .sslSocketFactory(HttpsHelper.getSslSocketFactory(iss))
        .hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        })
        //其他配置
        .build();
NetworkLiteHelper.initializeClient(okHttpClient);
```

* 双向认证

```
HttpsHelper.getSslSocketFactory(
    证书的InputStream数组,
    本地证书的InputStream,
    本地证书的密码)
```

框架中只提供了几个封装方法，也可以自行实现`SSLSocketFactory`，传入sslSocketFactory方法即可。

## 其他用法示例

### GET请求

```java
String url = "http://www.baidu.com/";
NetworkLiteHelper
    .get()
    .url(url)
    .build()
    .execute(new StringCallback() {
        @Override
        public void onFailure(Call call, Exception e, int i) {
            e("onFailure...e=" + e.getMessage());
        }

        @Override
        public void onSuccess(Call call, String s, int i) {
            i("onSuccess...s=" + s);
        }
    });
```

### POST表单

```java
NetworkLiteHelper
    .postForm()
    .addParam("username", "testuser")
    .addParam("password", "123456")
    .url(url)
    .build()
    .execute(new StringCallback() {
        @Override
        public void onFailure(Call call, Exception e, int i) {
            e("onFailure...e=" + e.getMessage());
        }

        @Override
        public void onSuccess(Call call, String s, int i) {
            i("onSuccess...s=" + s);
        }
    });
```

### POST JSON

```java
NetworkLiteHelper
    .postJson()
    .url(url)
    .content(new Gson().toJson(bean))
    .build()
    .execute(new StringCallback() {
        @Override
        public void onFailure(Call call, Exception e, int i) {
            e("onFailure...e=" + e.getMessage());
        }

        @Override
        public void onSuccess(Call call, String s, int i) {
            i("onSuccess...s=" + s);
        }
    });
```

或

```java
NetworkLiteHelper
    .postString()
    .url(url)
    .mediaType(MediaType.parse("application/json; charset=utf-8"))
    .content(new Gson().toJson(bean))
    .build()
    .execute(new StringCallback() {
        @Override
        public void onFailure(Call call, Exception e, int i) {
            e("onFailure...e=" + e.getMessage());
        }

        @Override
        public void onSuccess(Call call, String s, int i) {
            i("onSuccess...s=" + s);
        }
    });
```

提交JSON字符串到服务器端

`postJson`相当于`postString`后自动调用`.mediaType(MediaType.parse("application/json; charset=utf-8"))`

### POST表单上传文件

```java
NetworkLiteHelper
    .postForm()
    .addFile("mFile", "test_image_01.jpg", file)
    .addFile("mFile", "test_text_01.txt", file2)
    .setParams(params)
    .headers(headers)
    .url(url)
    .build()
    .execute(new StringCallback() {
        @Override
        public void onFailure(Call call, Exception e, int i) {
            e("onFailure...e=" + e.getMessage());
        }

        @Override
        public void onSuccess(Call call, String s, int i) {
            i("onSuccess...s=" + s);
        }
    });
```

支持单个多个文件，`addFile`的第一个参数为文件的key，即表单中`<input type="file" name="mFile"/>`的name属性。

### 自定义CallBack

目前内部包含`StringCallBack`、`FileCallBack`、`BitmapCallback`和`GenericCallback`，可以根据自己的需求自定义Callback，例如希望回调User对象：

```java
public abstract class UserCallback extends Callback<User> {

    @Override
    public User parseResponse(Response response, int i) throws Exception {
        String text = response.body().string();
        User user = new Gson().fromJson(text, User.class);
        return user;
    }
}
NetworkLiteHelper
    .postForm()
    .addParam("username", "testuser")
    .addParam("password", "123456")
    .url(url)
    .build()
    .execute(new UserCallback() {
        @Override
        public void onFailure(Call call, Exception e, int i) {
            mTv.setText("onFailure:" + e.getMessage());
        }

        @Override
        public void onSuccess(Call call, User user, int i) {
            mTv.setText("onSuccess:" + user.username);
        }
    });
```

通过`parseResponse`对回调的response进行解析，该方法运行在子线程，所以可以进行任何耗时操作。

也可以使用`GenericCallback`

```java
NetworkLiteHelper
    .postForm()
    .addParam("username", "testuser")
    .addParam("password", "123456")
    .url(url)
    .build()
    .execute(new GenericCallback<User>() {
        @Override
        public void onFailure(Call call, Exception e, int i) {
            e("onFailure...e=" + e.getMessage());
        }

        @Override
        public void onSuccess(Call call, User user, int i) {
            i("onSuccess...user=" + user);
        }
    });
```

### 下载文件

```java
String url = "https://github.com/square/okhttp/archive/parent-3.8.1.zip";
NetworkLiteHelper
    .get()
    .url(url)
    .build()
    .execute(new FileCallback(Environment.getExternalStorageDirectory().getAbsolutePath(), "okhttp-3.8.1.zip") {
        @Override
        public void onFailure(Call call, Exception e, int i) {
            e("onFailure...e=" + e.getMessage());
        }

        @Override
        public void onSuccess(Call call, File file, int i) {
            i("onSuccess...file=" + file);
        }

        @Override
        public void onProgressUpdate(float progress, long total, int id) {
            i("onProgressUpdate...total=" + total + ",id=" + id + ",progress=" + progress);
        }
    });
```

下载文件可以使用`FileCallback`，需要传入保存文件的目录以及文件名。

### 显示图片

```java
NetworkLiteHelper
    .get()
    .url(url)
    .build()
    .execute(new BitmapCallback() {
        @Override
        public void onFailure(Call call, Exception e, int i) {
            e("onFailure...e=" + e.getMessage());
        }

        @Override
        public void onSuccess(Call call, Bitmap bitmap, int i) {
            i("onSuccess...bitmap=" + bitmap);
            iv.setImageBitmap(bitmap);
        }
    });
```

显示图片可使用`BitmapCallback`。


### 上传下载的进度显示

```java
new Callback<T>() {
    //...
    @Override
    public void onProgressUpdate(float progress, long total, int id) {
        //progress: 0f ~ 1f
    }
}
```

Callback回调中有`onProgressUpdate`方法。

### 同步的请求

```
Response response = NetworkLiteHelper
                        .get()
                        .url(url)
                        .build()
                        .execute();
```

execute方法不传入callback即为同步的请求，返回Response。

### 取消单个请求

```java
RequestCall call = NetworkLiteHelper
                        .get()
                        .url(url)
                        .build()
call.cancel();
```

### 根据Tag取消请求

build方法创建RequestCall之前，可使用tag方法设置请求对应的Tag，设置后可通过`NetworkLiteHelper.cancelByTag(tag)`取消请求。

例如：

```
NetworkLiteHelper
    .get()
    .url(url)
    .tag(this)
    .build()

@Override
protected void onDestroy() {
    super.onDestroy();
    //取消以Activity.this作为tag的请求
    NetworkLiteHelper.cancelByTag(this);
}
```
当前Activity页面所有的请求都以Activity对象作为tag，可以在onDestory里面统一取消。

## 混淆

```
#okhttp
-dontwarn okhttp3.**
-keep class okhttp3.**{*;}

#okio
-dontwarn okio.**
-keep class okio.**{*;}
```
