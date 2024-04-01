#https://hub.docker.com/r/arm32v7/eclipse-temurin/  este es un contenedor para la arquitectura ARM de la raspbery, almenos para la PI 3 b+

FROM arm32v7/eclipse-temurin:latest
EXPOSE 8081
RUN mkdir /opt/app
ADD target/SmartdboxWebStarken-0.0.1-SNAPSHOT.jar /opt/app/SmartdboxWebStarken-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/opt/app/SmartdboxWebStarken-0.0.1-SNAPSHOT.jar"]
