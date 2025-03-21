FROM eclipse-temurin:21-jre

# Configuramos la zona horaria a Bogotá
ENV TZ=America/Bogota
RUN apt-get update && apt-get install -y tzdata && rm -rf /var/lib/apt/lists/*
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# Definimos el directorio de trabajo dentro del contenedor
WORKDIR /papaGroups

# Copiamos el archivo JAR generado en la carpeta target
COPY target/papagroups-0.0.1-SNAPSHOT.jar app.jar

# Exponemos el puerto en el que corre la aplicación
EXPOSE 8080

# Definimos el comando de entrada para ejecutar la aplicación
CMD ["java", "-jar", "app.jar"]
