FROM artifactory.apps.bancolombia.com/devops/openjdk11-openj9:x86_64-alpine-jdk-11.0.3_7_openj9-0.14.3-slim
COPY *.jar app.jar
ENV TZ America/Bogota
ENV JAVA_OPTS="-Dfile.encoding=UTF8 -XX:+UseContainerSupport -XX:MaxRAMPercentage=75 -Djava.security.egd=file:/dev/./urandom"
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar /app.jar"]
