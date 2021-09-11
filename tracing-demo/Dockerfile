FROM adoptopenjdk/openjdk11:alpine
RUN addgroup -S eshop && adduser -S eshop -G eshop
USER eshop:eshop
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} eshop.jar
EXPOSE 8080
CMD ["java","-jar","/eshop.jar"]
