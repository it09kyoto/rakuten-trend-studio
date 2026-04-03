# 使用 JDK 17
FROM openjdk:17-jdk-slim
# 将本地的 jar 包复制到容器中
COPY target/*.jar app.jar
# 运行程序
ENTRYPOINT ["java","-jar","/app.jar"]
