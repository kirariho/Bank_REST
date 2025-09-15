<h1>🚀 Система управления банковскими картами</h1>

<p>Система управления банковскими картами, построенная на Spring Boot и PostgreSQL.</p>

<h2>📊 Статус проекта</h2>
<p>🚧 <strong>В разработке</strong> - Основная функциональность реализована, тесты и обработка ошибок в процессе доработки.</p>

<h2>📁 Структура проекта</h2>
<pre>
card-management-system/
├── example/                    # Основной модуль приложения
│   ├── src/main/java/com/example/bankcards/
│   │   ├── config/            # Конфигурация Spring
│   │   ├── controller/        # REST контроллеры
│   │   ├── dto/               # Объекты передачи данных
│   │   ├── entity/            # JPA сущности
│   │   ├── exception/         # Пользовательские исключения
│   │   ├── repository/        # JPA репозитории
│   │   ├── security/          # Безопасность и JWT
│   │   ├── service/           # Бизнес-логика
│   │   └── util/              # Утилиты
│   ├── src/main/resources/
│   │   ├── db/migration/      # Миграции Liquibase
│   │   └── application.yml    # Конфигурация
│   └── pom.xml               # Зависимости модуля
├── docker-compose.yml         # Docker композиция
├── Dockerfile                # Контейнер приложения
├── .env.docker               # Переменные окружения
└── pom.xml                   # Родительский POM
</pre>

<h2>🔧 Технологии</h2>
<ul>
    <li><strong>Backend:</strong> Spring Boot 3.3.4, Spring Security, Spring Data JPA</li>
    <li><strong>База данных:</strong> PostgreSQL 15</li>
    <li><strong>Миграции:</strong> Liquibase</li>
    <li><strong>Аутентификация:</strong> JWT (JSON Web Tokens)</li>
    <li><strong>Документация:</strong> OpenAPI 3 / Swagger</li>
    <li><strong>Контейнеризация:</strong> Docker, Docker Compose</li>
</ul>

<h2>🔹 Требования</h2>
<ul>
    <li>Docker и Docker Compose</li>
    <li>Java 17 (для локальной разработки)</li>
    <li>Maven 3.9+ (для локальной разработки)</li>
</ul>

<h2>🔹 Быстрый старт</h2>

<h3>Настройка окружения</h3>
<ol>
    <li>Клонируйте репозиторий</li>
    <li>Скопируйте шаблон переменных окружения:
        <pre><code>cp .env.docker.example .env.docker</code></pre>
    </li>
    <li>Обновите <code>.env.docker</code> своими значениями:
        <pre><code>DB_URL=jdbc:postgresql://db:5432/bankdb
DB_USERNAME=postgres
DB_PASSWORD=postgres
JWT_SECRET_KEY=ваш-секретный-jwt-ключ-должен-быть-достаточно-длинным
ENCRYPTION_SECRET_KEY=ваш-16символьн-ключ
SPRING_PROFILES_ACTIVE=dev
SERVER_PORT=8080</code></pre>
    </li>
</ol>

<p>⚠️ <strong>Важно:</strong> <code>ENCRYPTION_SECRET_KEY</code> должен быть ровно 16 символов.</p>

<h3>Запуск через Docker</h3>
<pre><code>docker-compose up --build</code></pre>

<h3>Доступ к приложению</h3>
<ul>
    <li>API: <a href="http://localhost:8080">http://localhost:8080</a></li>
    <li>Swagger UI: <a href="http://localhost:8080/swagger-ui.html">http://localhost:8080/swagger-ui.html</a></li>
    <li>Документация API: <a href="http://localhost:8080/api-docs">http://localhost:8080/api-docs</a></li>
</ul>

<h2>🔹 API эндпоинты</h2>

<h3>Аутентификация</h3>
<table border="1" cellpadding="5" cellspacing="0">
    <thead>
        <tr>
            <th>Метод</th>
            <th>Эндпоинт</th>
            <th>Описание</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td>POST</td>
            <td>/api/auth/login</td>
            <td>Вход пользователя</td>
        </tr>
        <tr>
            <td>POST</td>
            <td>/api/auth/register</td>
            <td>Регистрация пользователя</td>
        </tr>
    </tbody>
</table>

<h3>Управление пользователями (только для администраторов)</h3>
<table border="1" cellpadding="5" cellspacing="0">
    <thead>
        <tr>
            <th>Метод</th>
            <th>Эндпоинт</th>
            <th>Описание</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td>POST</td>
            <td>/api/users</td>
            <td>Создать пользователя</td>
        </tr>
        <tr>
            <td>GET</td>
            <td>/api/users</td>
            <td>Получить всех пользователей</td>
        </tr>
    </tbody>
</table>

<h3>Управление картами</h3>
<table border="1" cellpadding="5" cellspacing="0">
    <thead>
        <tr>
            <th>Метод</th>
            <th>Эндпоинт</th>
            <th>Описание</th>
            <th>Доступ</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td>POST</td>
            <td>/api/cards</td>
            <td>Создать карту</td>
            <td>Администратор</td>
        </tr>
        <tr>
            <td>GET</td>
            <td>/api/cards/user/{ownerId}</td>
            <td>Получить карты пользователя</td>
            <td>Пользователь/Администратор</td>
        </tr>
        <tr>
            <td>PUT</td>
            <td>/api/cards/{cardId}/balance</td>
            <td>Обновить баланс</td>
            <td>Администратор</td>
        </tr>
        <tr>
            <td>DELETE</td>
            <td>/api/cards/{cardId}</td>
            <td>Удалить карту</td>
            <td>Администратор</td>
        </tr>
        <tr>
            <td>POST</td>
            <td>/api/transfers</td>
            <td>Перевести деньги</td>
            <td>Пользователь/Администратор</td>
        </tr>
    </tbody>
</table>

<h2>🔹 Возможности</h2>
<ul>
    <li>✅ Аутентификация на основе JWT</li>
    <li>✅ Ролевое управление доступом (USER, ADMIN)</li>
    <li>✅ CRUD операции с картами</li>
    <li>✅ Переводы денег между картами</li>
    <li>✅ База данных PostgreSQL с миграциями Liquibase</li>
    <li>✅ Контейнеризация Docker</li>
    <li>✅ Документация API через Swagger</li>
    <li>✅ Шифрование и маскировка данных</li>
</ul>

<h2>🔹 Схема базы данных</h2>
<ul>
    <li><strong>users</strong> - Учетные записи пользователей</li>
    <li><strong>roles</strong> - Роли пользователей (USER, ADMIN)</li>
    <li><strong>user_roles</strong> - Связи пользователь-роль</li>
    <li><strong>cards</strong> - Банковские карты с зашифрованными данными</li>
</ul>

<h2>🔹 Разработка</h2>

<h3>Локальная разработка</h3>
<ol>
    <li>Запустите PostgreSQL:
        <pre><code>docker run -d --name postgres -p 5432:5432 \
  -e POSTGRES_PASSWORD=postgres \
  -e POSTGRES_DB=bankdb postgres:15</code></pre>
    </li>
    <li>Установите переменные окружения</li>
    <li>Запустите приложение:
        <pre><code>mvn spring-boot:run -f example/pom.xml</code></pre>
    </li>
</ol>

<h3>Запуск тестов</h3>
<pre><code>mvn clean test -f example/pom.xml</code></pre>

<h2>🔹 Статус разработки</h2>

<h3>✅ Завершено</h3>
<ul>
    <li>Основная структура приложения</li>
    <li>Аутентификация и авторизация</li>
    <li>Базовые CRUD операции</li>
    <li>Контейнеризация Docker</li>
    <li>Миграции базы данных</li>
</ul>

<h3>🚧 В процессе</h3>
<ul>
    <li>Полное покрытие тестами</li>
    <li>Улучшенная обработка ошибок</li>
    <li>Доработка валидации входных данных</li>
    <li>Доработка документации API</li>
</ul>

<h3>📋 Известные проблемы</h3>
<ul>
    <li>Некоторые тесты не полностью реализованы</li>
    <li>Нужна стандартизация ответов об ошибках</li>
    <li>Требует доработки обработка параметров запросов</li>
</ul>

<h2>🔹 Участие в разработке</h2>
<p>Текущие приоритеты разработки:</p>
<ol>
    <li>Завершить модульные и интеграционные тесты</li>
    <li>Улучшить обработку ошибок и валидацию</li>
    <li>Доработать документацию API</li>
    <li>Добавить логирование и мониторинг</li>
</ol>

<h2>🔹 Примеры использования</h2>

<h3>Аутентификация</h3>
<pre><code>POST /api/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin123"
}</code></pre>

<h3>Создание карты</h3>
<pre><code>POST /api/cards?ownerId=1&rawCardNumber=2202222234565555&expiryDateStr=2025-12-31&initialBalance=1000
Authorization: Bearer {jwt-token}</code></pre>

