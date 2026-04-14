# 第一階段：編譯程式碼 (使用 Maven)
FROM maven:3.8.4-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

# 第二階段：執行程式碼 (使用輕量級 JDK)
FROM openjdk:17-jdk-slim
COPY --from=build /target/*.jar app.jar

# 設定記憶體限制，防止免費版崩潰
ENV JAVA_OPTS="-Xmx300m -Xss512k"

# 啟動指令
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app.jar"]
