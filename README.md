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
   docker run -p 8081:8081 jenkins_service_api:v0.0.1

4. Acceder a la API Swagger:

   ```bash
   http://localhost:8081/seido/swagger-ui.html
   
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
   
   Para obtener el usuario y la contraseña que disponemos de la imagen de docker:
   
   ```bash
   docker cp jenkins:/var/jenkins_home/secrets/initialAdminPassword src/main/resources/jenkins/initialAdminPassword
   
   El usuario lo disponemos en application.yml:
   jenkins.url: http://localhost:8080
   jenkins.username: admin
   jenkins.password: (el contenido del fichero initialAdminPassword)
   
6. Postgres lo disponemos dockerizado también para la persistencia exigida:

   ```bash
   datasource.url: jdbc:postgresql://jenkinsmicroservice-postgres-1:5432/seido_manager
   datasource.username: admin_db
   datasource.password: admin_12345678
   
7. Las demás configuraciones y propiedades son externa y configurables

### Instrucciones para despliegue en forma Devops

Estando en la ruta del proyecto que nos hayamos descargado lanzamos este script sh:

    ```bash
      $ ./launch_local.sh

Este script levanta teniendo el docker desktop que he utilizado levantado, las siguientes imagenes:
- El servicio jenkins-service-api expuesto al puerto 8081
- El servicio jenkins expuesto al puerto 8080 y 50000
- El servicio postgres expuesto al puerto 5432
- El servicio pgadmin expuesto al puerto 15432

### Cosas a tener en cuenta:

No se ha podido completar los test a causa de fuerza mayor y falta de tiempo.
Se ha realizado pruebas integradas levantando el docker, teniendo postgres, jenkins dockerizados
y ver que se creaba un build en jenkins indicandole los parametros exigidos de entrada.