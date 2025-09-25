<h1>Система управления банковскими картами</h1>

<p>Полнофункциональная система управления банковскими картами на базе Spring Boot и PostgreSQL, с поддержкой аутентификации, ролевого управления и переводов между картами.</p>

<h2>Статус проекта</h2>
<p><strong>Готово к использованию</strong> – все ключевые функции реализованы и покрыты тестами.</p>

<h2>Структура проекта</h2>
<pre>
card-management-system/
├── example/                    
│   ├── src/main/java/com/example/bankcards/
│   │   ├── config/            
│   │   ├── controller/        
│   │   ├── dto/               
│   │   ├── entity/            
│   │   ├── exception/         
│   │   ├── repository/        
│   │   ├── security/          
│   │   ├── service/           
│   │   └── util/              
│   ├── src/main/resources/
│   │   ├── db/migration/      
│   │   └── application.yml    
│   └── pom.xml               
├── docker-compose.yml         
├── Dockerfile                
├── .env.docker               
└── pom.xml                   
</pre>

<h2>Технологии</h2>
<ul>
    <li><strong>Backend:</strong> Spring Boot 3.3.4, Spring Security, Spring Data JPA</li>
    <li><strong>База данных:</strong> PostgreSQL 15</li>
    <li><strong>Миграции:</strong> Liquibase</li>
    <li><strong>Аутентификация:</strong> JWT (JSON Web Tokens)</li>
    <li><strong>Документация:</strong> OpenAPI 3 / Swagger</li>
    <li><strong>Контейнеризация:</strong> Docker, Docker Compose</li>
</ul>

<h2>Требования</h2>
<ul>
    <li>Docker и Docker Compose</li>
    <li>Java 17</li>
    <li>Maven 3.9+</li>
</ul>

<h2>Быстрый старт</h2>

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
JWT_SECRET_KEY=ваш-секретный-jwt-ключ
ENCRYPTION_SECRET_KEY=ваш-16символьный-ключ
SPRING_PROFILES_ACTIVE=dev
SERVER_PORT=8080</code></pre>
    </li>
</ol>

<p><strong>Важно:</strong> <code>ENCRYPTION_SECRET_KEY</code> должен быть ровно 16 символов.</p>

<h3>Запуск через Docker</h3>
<pre><code>docker-compose up --build</code></pre>

<h3>Доступ к приложению</h3>
<ul>
    <li>API: <a href="http://localhost:8080">http://localhost:8080</a></li>
    <li>Swagger UI: <a href="http://localhost:8080/swagger-ui.html">http://localhost:8080/swagger-ui.html</a></li>
    <li>Документация API: <a href="http://localhost:8080/api-docs">http://localhost:8080/api-docs</a></li>
</ul>

<h2>API эндпоинты</h2>

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

<h3>Управление пользователями (администраторы)</h3>
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

<h2>Возможности</h2>
<ul>
    <li>Аутентификация на основе JWT</li>
    <li>Ролевое управление доступом (USER, ADMIN)</li>
    <li>CRUD операции с картами</li>
    <li>Переводы денег между картами</li>
    <li>База данных PostgreSQL с миграциями Liquibase</li>
    <li>Контейнеризация Docker</li>
    <li>Документация API через Swagger</li>
    <li>Шифрование и маскировка данных</li>
</ul>

<h2>Схема базы данных</h2>
<ul>
    <li><strong>users</strong> - Учетные записи пользователей</li>
    <li><strong>roles</strong> - Роли пользователей</li>
    <li><strong>user_roles</strong> - Связи пользователь-роль</li>
    <li><strong>cards</strong> - Банковские карты с зашифрованными данными</li>
</ul>

<h2>Разработка</h2>

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

<h2>Покрытие тестами</h2>
<ul>
    <li>AuthControllerTest</li>
    <li>CardControllerTest</li>
    <li>TransferControllerTest</li>
    <li>UserControllerTest</li>
    <li>CardServiceTest</li>
    <li>UserServiceTest</li>
    <li>EncryptionUnitTest</li>
</ul>

<h2>Примеры использования</h2>

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


