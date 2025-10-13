# 1. Imagen base con Java
FROM eclipse-temurin:17-jdk-alpine

# 2. Argumento para el JAR generado
ARG JAR_FILE=target/pruebacompralinktic-0.0.1-SNAPSHOT.jar

# 3. Copiar el JAR al contenedor
COPY ${JAR_FILE} app.jar

# 4. Comando para ejecutar la app
ENTRYPOINT ["java","-jar","/app.jar"]