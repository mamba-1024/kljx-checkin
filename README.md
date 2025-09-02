
## 打卡项目后台系统

## 将jar包上传到服务器上
```
scp target/hzjy-checkIn-0.0.1-SNAPSHOT.jar root@8.140.247.255:/var
```

## 启动 Java 服务
```
java -jar hzjy-checkIn-0.0.1-SNAPSHOT.jar &
```


## 关闭 Java 进程
```
// 获取端口号
ps -aux | grep java

// kill 进程
kill -9 port
```