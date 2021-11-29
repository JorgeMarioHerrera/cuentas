FROM #{image-base-java8}#
ENV TZ=America/Bogota
COPY --chown=1001:0 *.jar app.jar
USER root
RUN mkdir -p /usr/share/zoneinfo/America
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
COPY Bogota /usr/share/zoneinfo/America/
COPY --chown=1001:0 CARTA_BIENVENIDA_CUENTA_AHORROS.pdf /home/appuser/pdf/resources/CARTA_BIENVENIDA_CUENTA_AHORROS.pdf
COPY --chown=1001:0 OpenSans_bold.ttf /home/appuser/pdf/resources/OpenSans_bold.ttf
ENV JAVA_OPTS=" -Xshareclasses:name=cacheapp,cacheDir=/cache,nonfatal -XX:+UseContainerSupport -XX:MaxRAMPercentage=70"
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar app.jar" ]