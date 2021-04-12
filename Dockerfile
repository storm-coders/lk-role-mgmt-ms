FROM openjdk:11-jre-slim-buster
VOLUME /tmp
COPY run.sh .
COPY build/libs/*.jar app.jar
RUN chmod 777 run.sh
ENTRYPOINT ["sh", "./run.sh"]