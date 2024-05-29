# CRUD de Spaceships

Este proyecto es un CRUD (Create, Read, Update, Delete) de naves espaciales desarrollado en Java utilizando el framework Spring Boot. Permite gestionar una colección de naves espaciales, con operaciones como consultar, crear, actualizar y eliminar.

## Funcionalidades

El proyecto cuenta con las siguientes funcionalidades:

- Consultar todas las naves espaciales.
- Consultar una única nave espacial por su ID.
- Consultar naves espaciales que contienen cierto texto en su nombre.
- Crear una nueva nave espacial.
- Modificar una nave espacial existente.
- Eliminar una nave espacial.

Además, incluye las siguientes características adicionales:

- Uso de base de datos H2 en memoria para almacenar las naves espaciales.
- Documentación de la API con Swagger/OpenAPI.
- Gestión centralizada de excepciones.
- Uso de la caché básica de Spring Boot para mejorar el rendimiento.
- Un aspecto para registrar un mensaje cuando se solicita una nave con un ID negativo.
- Pruebas unitarias y de integración.
- Uso de Liquibase para el mantenimiento de scripts DDL de la base de datos.
- Integración con RabbitMQ para mensajería asíncrona, con almacenamiento de mensajes en la base de datos.

## Tecnologías Utilizadas

El proyecto utiliza las siguientes tecnologías:

- Java
- Spring Boot
- Maven
- H2 Database (Base de datos en memoria)
- Spring Data JPA (Acceso a datos)
- Springdoc OpenAPI (Documentación de la API)
- JUnit y Mockito (Pruebas unitarias e integración)
- Lombok (Reducir la verbosidad del código)
- Liquibase (Gestión de versiones de la base de datos)
- Spring Cache (Caché básica)
- RabbitMQ (Mensajería asíncrona)

## Configuración y Uso

1. Clona este repositorio.
2. Importa el proyecto en tu IDE favorito.
3. Ejecuta la aplicación.
4. Accede a la documentación de la API desde `http://localhost:8080/swagger-ui/index.html`.

## Credenciales de Seguridad

Las credenciales de usuario para acceder a la aplicación son las siguientes:

- **Usuario estándar:**
   - **Username:** `user`
   - **Password:** `password`
   - **Roles:** `USER`

- **Administrador:**
   - **Username:** `admin`
   - **Password:** `adminpassword`
   - **Roles:** `ADMIN`

## Uso con Docker

1. Tener Docker instalado y funcionando.
2. Construye la imagen Docker ejecutando el siguiente comando en la raíz del proyecto:
   ```bash
   docker build -t spaceships .
    ```
3. Ejecuta el contenedor Docker con el siguiente comando:
    ```bash
    docker run -p 8080:8080 spaceships
    ```
4. Accede a la documentación de la API desde `http://localhost:8080/swagger-ui/index.html`.
5. Para detener el contenedor, ejecuta el siguiente comando:
    ```bash
    docker stop <CONTAINER_ID>
    ```
    Puedes obtener el CONTAINER_ID ejecutando el comando `docker ps`.

## Configuración de RabbitMQ

Para ejecutar RabbitMQ en un contenedor Docker separado, sigue estos pasos:

1. Inicia RabbitMQ usando Docker:
   ```bash
   docker run -d --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:management
    ```
2. Accede a la consola de administración de RabbitMQ desde `http://localhost:15672` (usuario: guest, contraseña: guest).
3. Inicia la aplicación y realiza operaciones de crear, actualizar o eliminar naves espaciales.
4. Verifica que los mensajes se envían y reciben correctamente en la consola de RabbitMQ. 
5. Para detener RabbitMQ, ejecuta el siguiente comando:
    ```bash
    docker stop rabbitmq
    ```
    Puedes eliminar el contenedor RabbitMQ ejecutando el comando `docker rm rabbitmq`.

## Almacenamiento de Mensajes de RabbitMQ

Los mensajes recibidos de RabbitMQ se procesan y se almacenan en la base de datos H2 en memoria. Cada mensaje se guarda en la tabla MESSAGE_LOG para mantener un registro de todos los mensajes recibidos.

## Acceso a la Consola de H2

Para acceder a la consola de H2, sigue estos pasos:

1. Accede a `http://localhost:8080/h2-console`.
2. Ingresa la siguiente información:
   - **Driver Class:** `org.h2.Driver`
   - **JDBC URL:** `jdbc:h2:mem:testdb`
   - **User Name:** `sa`
   - **Password:** `password`
3. Haz clic en el botón `Connect`.
4. Puedes consultar las tablas de la base de datos y ejecutar consultas SQL.

## Autores

- [Emanuel flores](https://github.com/emaflores) - Desarrollador principal


