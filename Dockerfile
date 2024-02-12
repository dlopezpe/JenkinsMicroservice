FROM jenkins/jenkins:lts-jdk11
USER root
RUN apt-get update && apt-get install -y lsb-release
RUN curl -fsSLo /usr/share/keyrings/docker-archive-keyring.asc \
  https://download.docker.com/linux/debian/gpg
RUN echo "deb [arch=$(dpkg --print-architecture) \
  signed-by=/usr/share/keyrings/docker-archive-keyring.asc] \
  https://download.docker.com/linux/debian \
  $(lsb_release -cs) stable" > /etc/apt/sources.list.d/docker.list
RUN apt-get update && apt-get install -y docker-ce-cli
USER jenkins

# Fase de construcción
FROM maven:3.8.4-openjdk-11 AS build

# Establecer el directorio de trabajo
WORKDIR /app

# Copiar el archivo POM y descargar las dependencias
COPY pom.xml .
RUN mvn dependency:go-offline

# Copiar el resto del código fuente
COPY src ./src

# Empaquetar la aplicación
RUN mvn package

# Fase de ejecución
FROM openjdk:11-jre-slim

# Establecer el directorio de trabajo
WORKDIR /app

# Copiar el JAR construido desde la fase de construcción
COPY --from=build /app/target/jenkins-service-api-0.0.1-SNAPSHOT.jar /app/jenkins-service-api-0.0.1-SNAPSHOT.jar
COPY application.yml /app/application.yml

# Exponer el puerto en el que la aplicación se ejecutará
EXPOSE 8081

# Nombre de la imagen y etiqueta
LABEL image.name="jenkins_service_api" \
      image.tag="v0.0.1"

# Comando para ejecutar la aplicación cuando se inicie el contenedor
CMD ["java", "-jar", "jenkins-service-api-0.0.1-SNAPSHOT.jar" , "--spring.config.name=application"]
