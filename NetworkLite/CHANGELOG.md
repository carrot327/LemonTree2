更新日志
==========

###### 0.2.13
```
1.当网络库初始化时若无证书入参，则标记为测试环境，不验证https证书
```

###### 0.2.12
```
1.修改https证书验证逻辑为当预埋证书内域名与接口请求匹配后进行证书验证，不匹配则使用okhttp默认证书验证
2.HttpLoggerInterceptor中的decode方法取消错误日志输出
```

###### 0.2.11
```
1.ParamsSignAuthInterceptor中兼容POST请求无参数的情况
2.PostStringRequest中兼容bodyContent为null的情况
```

###### 0.2.10
```
1.ParamsSignAuthInterceptor中每次都通过回调重新获取filterList和timeOffset
```

###### 0.2.9
```
1.NetworkLite结构调整，可选择统一管理或独立使用
```

###### 0.2.8
```
1.ParamsSignAuthInterceptor增加对数组包含数组和数组包含空值情况的处理
2.ParamsSignAuthInterceptor增加对POST请求无参数的兼容处理
3.ParamsSignAuthInterceptor修复POST请求form表单提交时参数签名错误的问题
```

###### 0.2.7
```
1.修复ParamsSignAuthInterceptor因Get请求参数没有URLEncoder.encode造成的参数签名错误的问题
```

###### 0.2.6
```
1.修复ParamsSignAuthInterceptor因并发造成的参数签名错误的问题
```

###### 0.2.5
```
1.修复HttpLoggerInterceptor中因URLDecoder.decode异常导致崩溃的问题
```

###### 0.2.4
```
1.Post表单请求（没有上传文件）不支持进度回调callback.onProgressUpdate方法；Post表单请求（有上传文件）仍支持进度回调callback.onProgressUpdate方法
2.POST Multipart请求不做参数签名
3.ParamsSignAuthInterceptor去除响应结果日志输出
4.代码优化
```

###### 0.2.3
```
1.HeaderInterceptor增加入参HeaderInterceptor.Callback，用于控制日志是否输出和日志Tag
2.HttpLoggerInterceptor增加入参HttpLoggerInterceptor.Callback，用于控制日志是否输出和日志Tag；当不输出日志时，拦截器直接返回结果不再进行日志拼接等操作，提高运行效率
3.ParamsSignAuthInterceptor.Callback增加getFilterList方法，只要符合列表中任意一个正则表达式，就会被禁止使用参数签名验证功能
4.NetworkLiteHelper增加replaceClient方法，可替换NetworkLiteHelper单例中保存的OkHttpClient对象
```

###### 0.2.2
```
1.HttpsHelper优化
```

###### 0.2.1
```
1.参数签名验证拦截器优化：参数为数组时，按序号对应字符串排序后生产签名
```

###### 0.2.0
```
1.增加请求头拦截器
2.增加参数签名验证拦截器
```

###### 0.1.0
```
初始版本
```