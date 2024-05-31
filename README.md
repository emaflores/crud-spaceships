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

## Uso con Docker Compose

1. Tener Docker y Docker Compose instalados en tu máquina.
2. Construye la imagen Docker ejecutando el siguiente comando en la raíz del proyecto:
   ```bash
   docker build -t spaceships .
    ```
3. Ejecuta el contenedor Docker con el siguiente comando:
    ```bash
    docker-compose up
    ```
4. Accede a la documentación de la API desde `http://localhost:8080/swagger-ui/index.html`.
5. Para detener el contenedor, ejecuta el siguiente comando:
    ```bash
    docker-compose down
    ```

## Almacenamiento de Mensajes de RabbitMQ

Los mensajes recibidos de RabbitMQ se procesan y se almacenan en la base de datos H2 en memoria. Cada mensaje se guarda en la tabla MESSAGE_LOG para mantener un registro de todos los mensajes recibidos.

## Acceso a la Consola de H2

Para acceder a la consola de H2, sigue estos pasos:

1. Accede a `http://localhost:8081/`.
2. Ingresa la siguiente información:
   - **Driver Class:** `org.h2.Driver`
   - **JDBC URL:** `jdbc:h2:tcp://h2:1521/mem:testdb`
   - **User Name:** `sa`
   - **Password:** `password`
3. Haz clic en el botón `Connect`.
4. Puedes consultar las tablas de la base de datos y ejecutar consultas SQL.

## Report de Cobertura de Pruebas con JaCoCo

Se ha configurado el plugin JaCoCo para generar un report de cobertura de pruebas. Para generar el report, ejecuta el siguiente comando en la raíz del proyecto:

```bash
mvn clean test
```

El report se generará en la siguiente ruta: `target/site/jacoco/index.html`.

## Uso de Testcontainers para RabbitMQ

Los tests no fallarán debido a la configuración de RabbitMQ, ya que se utiliza Testcontainers para levantar instancias de RabbitMQ durante la ejecución de las pruebas.

## Autores

- [Emanuel flores](https://github.com/emaflores) - Desarrollador principal


