FROM openjdk:17-jdk-slim
RUN apt-get update && apt-get install -y socat
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 80
CMD ["java", "-jar", "app.jar", "--server.port=8080"]
ENTRYPOINT ["sh", "-c", "java -jar app.jar --server.port=8080 & socat TCP-LISTEN:80,fork TCP:127.0.0.1:8080"]
