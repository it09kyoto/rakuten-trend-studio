# 使用更稳定的 Eclipse Temurin JDK 17 (这是目前最推荐的开源 JDK)
FROM eclipse-temurin:17-jdk-alpine

# 将本地的 jar 包复制到容器中
COPY target/*.jar app.jar

# 运行程序
ENTRYPOINT ["java","-jar","/app.jar"]
