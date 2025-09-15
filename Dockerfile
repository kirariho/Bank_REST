# Stage 1: Build
FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /workspace

# Копируем pom файлы для кэширования зависимостей
COPY pom.xml .
COPY example/pom.xml example/

# Скачиваем зависимости (будет закэшировано)
RUN mvn dependency:go-offline -f example/pom.xml

# Копируем исходный код
COPY example/src example/src

# Сборка из корневого pom.xml для правильной работы с модулями
RUN mvn clean package -DskipTests

# Stage 2: Run
FROM eclipse-temurin:17-jre-jammy

# Создаем пользователя для безопасности
RUN groupadd -r appuser && useradd -r -g appuser appuser

WORKDIR /app

# Копируем JAR файл
COPY --from=build /workspace/example/target/example-1.0-SNAPSHOT.jar /app/app.jar

# Меняем владельца файлов
RUN chown appuser:appuser /app/app.jar

# Переключаемся на пользователя
USER appuser

# Открываем порт
EXPOSE 8080

# Команда запуска с JVM настройками
ENTRYPOINT ["java", "-Xmx512m", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app/app.jar"]