# Usa una imagen base de Java
FROM eclipse-temurin:17-jre

# Crea un directorio para la app
WORKDIR /app

# Copia el JAR generado al contenedor
COPY target/*.jar app.jar

# Expone el puerto (ajusta si tu app usa otro)
EXPOSE 8080

# Comando para ejecutar la app
ENTRYPOINT ["java", "-jar", "app.jar"]