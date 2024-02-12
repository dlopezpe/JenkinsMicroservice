# Create Build Microservice

Este microservicio se comunica con otra API o directamente con Jenkins para lanzar una CI. La metadata del build se persiste.
Cuando ejecuta la imagen de la ventana acoplable, debe ser posible visitarla en un navegador donde se encuentre la imagen principal.
La página debe ser una interfaz de usuario Swagger.
En la página Swagger, debería haber un controlador de servicio que agregue una nueva compilación con esto
estructura:
– ID de compilación (número)
– nombre (cadena - solo letras)
– pathRepo (cadena con barras (\))
– versión (M.m.f)
Este servicio tiene que validar el build bean y llamar a otro servicio Rest.
Esta aplicación debe crearse con SpringBoot.
Datos:
- Servicio de descanso de punto final: jenkins.localhost.com
  Prima:
- Algunas pruebas unitarias
- Prueba para validar la API creada.
- Persiste las construcciones históricas con JPA.

### Instrucciones


1. Tener operativo Jenkins, en este caso hemos obtado por dockerizado
   ```bash
   https://localhost:8080

2. Construir la imagen de Docker:

   ```bash
   docker build -t jenkins_service_api:v0.0.1 .

3. Ejecutar el contenedor:

   ```bash
   docker run -p 8080:8080 jenkins_service_api:v0.0.1

4. Acceder a la API Swagger:

   ```bash
   http://localhost:8080/seido/swagger-ui.html

### Instrucciones para despliegue en forma Devops

Estando en la ruta del proyecto que nos hayamos descargado lanzamos este script sh:

 ```bash
   $ ./launch_local.sh