# 第一阶段：编译环境
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
# 将所有源代码复制进去
COPY . .
# 在云端执行打包命令
RUN mvn clean package -DskipTests

# 第二阶段：运行环境
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
# 从编译阶段把生成的 jar 包拿过来
COPY --from=build /app/target/*.jar app.jar
# 乐天项目通常使用 8080 端口
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
