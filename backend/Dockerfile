# Multi-stage build para optimizar el tamaño de la imagen
FROM eclipse-temurin:21-jdk-alpine AS builder

# Instalar Gradle
RUN apk add --no-cache gradle

# Establecer directorio de trabajo
WORKDIR /app

# Copiar archivos de configuración de Gradle
COPY build.gradle.kts settings.gradle.kts ./
COPY gradle/ gradle/

# Descargar dependencias (capa de caché)
RUN gradle dependencies --no-daemon

# Copiar código fuente
COPY src/ src/

# Construir la aplicación
RUN gradle build --no-daemon -x test

# Imagen de producción
FROM eclipse-temurin:21-jre-alpine

# Instalar dependencias del sistema
RUN apk add --no-cache curl

# Crear usuario no-root para seguridad
RUN addgroup -g 1001 -S appgroup && \
    adduser -u 1001 -S appuser -G appgroup

# Establecer directorio de trabajo
WORKDIR /app

# Copiar JAR construido desde la etapa anterior
COPY --from=builder /app/build/libs/*.jar app.jar

# Cambiar propiedad del archivo al usuario de la aplicación
RUN chown appuser:appgroup app.jar

# Cambiar al usuario no-root
USER appuser

# Exponer puerto
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
