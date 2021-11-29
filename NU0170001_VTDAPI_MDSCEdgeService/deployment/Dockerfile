FROM #{image-base-java11}#
ENV TZ=America/Bogota
COPY *.jar app.jar
USER root
RUN mkdir -p /usr/share/zoneinfo/America
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
COPY Bogota /usr/share/zoneinfo/America/
ENV JAVA_OPTS=" -Xshareclasses:name=cacheapp,cacheDir=/cache,nonfatal -XX:+UseContainerSupport -XX:MaxRAMPercentage=70"
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar app.jar" ]