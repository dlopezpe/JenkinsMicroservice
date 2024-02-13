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
   http://localhost:8082/seido/swagger-ui.html
   
5. Para Jenkins:
   ```bash
   http://localhost:8080/
   
Y dentro del contenedor de jenkins podremos ver la contraseña que nos genera, por ejemplo:

2024-02-13 01:13:20 Jenkins initial setup is required. An admin user has been created and a password generated.
2024-02-13 01:13:20 Please use the following password to proceed to installation:
2024-02-13 01:13:20
2024-02-13 01:13:20 547e12245a4f4780a439a21ec5841384
..

O también en el fichero 

   ```bash
   /var/jenkins_home/secrets/initialAdminPassword
6. 

### Instrucciones para despliegue en forma Devops

Estando en la ruta del proyecto que nos hayamos descargado lanzamos este script sh:

 ```bash
   $ ./launch_local.sh