# Étape 1 : Construction de l'application (Build)
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copier le pom.xml et télécharger les dépendances en cache
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copier les sources et compiler le fichier JAR
COPY src ./src
ENV MAVEN_OPTS="-Dfile.encoding=UTF-8"
RUN mvn clean package -DskipTests

# Étape 2 : Exécution de l'application (Runtime)
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copier le JAR généré à l'étape précédente
COPY --from=build /app/target/*.jar app.jar

# Exposer le port par défaut de Spring Boot
EXPOSE 8080

# Lancer l'application
ENTRYPOINT ["java", "-jar", "app.jar"]
