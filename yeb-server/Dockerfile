FROM java:8
EXPOSE 8081

VOLUME /tmp
ADD yeb-server-2.6.2.jar  /app.jar
RUN bash -c 'touch /app.jar'
ENTRYPOINT ["java","-jar","/app.jar"]
