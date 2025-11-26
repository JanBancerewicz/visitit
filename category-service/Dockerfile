FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /workspace
# kopiujemy całe repo, żeby był POM rodzica
COPY . .
# budujemy tylko moduł i zależności, bez testów
RUN mvn -q -DskipTests -pl category-service -am package

FROM eclipse-temurin:21-jre
WORKDIR /app
# artefakt powstaje w target modułu
COPY --from=build /workspace/category-service/target/*.jar app.jar
EXPOSE 8080
ENV JAVA_OPTS=""
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar app.jar"]
