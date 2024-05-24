# Establece la imagen base
FROM openjdk:21-slim

# Establece el directorio de trabajo
WORKDIR /app

# Copia los archivos necesarios para construir el proyecto
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
COPY src ./src

# Construye la aplicación
RUN ./mvnw package -DskipTests

# Asegura que el JAR se haya construido
RUN ls -l target/

# Comando para ejecutar la aplicación
CMD ["java", "-jar", "target/spaceships-0.0.1-SNAPSHOT.jar"]