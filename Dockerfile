FROM eclipse-temurin:23-jre

ADD target/chatservice-*.jar chatservice.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","chatservice.jar"]