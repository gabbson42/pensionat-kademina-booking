FROM eclipse-temurin:25-jre

WORKDIR /app

COPY target/*.jar app.jar

#Endast Dokumentation
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]