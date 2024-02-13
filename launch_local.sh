#!/bin/bash

docker-compose -f docker-compose.yml down
docker-compose -f docker-compose.yml up --build -d

# Copia el archivo desde el contenedor a tu máquina local
docker cp jenkins:/var/jenkins_home/secrets/initialAdminPassword src/main/resources/jenkins/initialAdminPassword

