FROM openjdk:11
VOLUME /tmp
EXPOSE 8888
ADD ./target/eureka-server-1.0.0.jar eureka-server-1.0.0.jar
ENTRYPOINT ["java","-jar","/eureka-server-1.0.0.jar"]