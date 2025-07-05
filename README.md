# Auth Service
Безопасный микросервис аутентификации, реализованный на `Kotlin`
с использованием `Ktor`.

---

### Стек технологий
`Kotlin`, `Ktor`, `Redis`, `PostgreSQL`, `Exposed`, `JWT`, `SMTP`

---

### Функциональность
- Регистрация по email с подтверждением кода 
- Авторизация по паролю или коду
- Обновление **access** и **refresh** токенов
- Безопасный выход (отзыв токенов)
- Валидация email, пароля, даты рождения

---

### Архитектура DDD
- `application` — UseCases
- `domain` — Интерфейсы, сущности
- `infrastructure` — JWT, Redis, SMTP, DAO
- `presentation` — HTTP-маршруты и DTO

---

### Эндпоинты
| Метод | URL                       | Описание                                                   |
|-------|---------------------------|------------------------------------------------------------|
| POST  | `/auth/register`          | Отправка кода регистрации на email                         |
| POST  | `/auth/register/verify`   | Проверка кода -> выдача **register** токена                |
| POST  | `/auth/register/complete` | Завершение регистрации через **register** токен            |
| POST  | `/auth/login/code`        | Отправка кода авторизации на email                         |
| POST  | `/auth/login/verify`      | Проверка кода -> выдача **access** и **refresh** токенов   |
| POST  | `/auth/login/password`    | Проверка пароля -> выдача **access** и **refresh** токенов |
| POST  | `/auth/refresh`           | Обновление **access** и **refresh** токенов                |
| POST  | `/auth/logout`            | Отзыв **access** и **refresh** токенов                     |

---

### Как реализована регистрация
1. #### `POST /auth/register`
    ```json
    {
      "email": "user@example.com"
    }
    ```
    - Валидация email
    - Проверка количества запросов `email:request-count:user@example.com` (ограничение: 5 запросов в 10 минут)
    - Создание проверочного кода и добавление записей в `Redis`
        ```
        key: email:confirm:user@example.com
        value: 123456
        ttl: 10 минут
        ```
    - Добавление записи о количестве запросов в `Redis` (если создана -> `value += 1`)
        ```
        key: email:request-count:user@example.com
        value: 1
        ttl: 10 минут
        ```
    - Отправка письма на **email** через `SMTP`
    ##### Ответ: `200 OK` (письмо с кодом отправлено)
2. #### `POST /auth/register/verify`
    ```json
    {
      "email": "user@example.com",
      "code": "123456"
    }
    ```
    - Валидация email
    - Проверка количества попыток отправки кода `email:verify-attempts:user@example.com` (ограничение: 5 попыток в 10 минут)
    - Создание записи количества попыток отправки кода в `Redis` (если создана -> `value += 1`)
        ```
        key: email:verify-attempts:user@example.com
        value: 1
        ttl: 10 минут
        ```
    - Сравнение кода со значением записи `email:confirm:user@example.com`
    - Если код неверный: Ошибка `400 Неверный код подтверждения`
    - Создание токена регистрации и добавление записи в `Redis`
        ```
        key: register:token:360a1329-aa10-46a5-87d6-b7273e4efb75
        value: user@example.com
        ttl: 10 минут
        ```
    - Удаление записей `email:confirm:user@example.com`, `email:request-count:user@example.com`, `email:verify-attempts:user@example.com`
    ##### Ответ:
    ```json
    {
      "token": "360a1329-aa10-46a5-87d6-b7273e4efb75"
    }
    ```
3. #### `POST /auth/register/complete`
    ```json
    {
      "token": "360a1329-aa10-46a5-87d6-b7273e4efb75",
      "fullName": "Ivan Ivanov",
      "birthday": "2002-02-20",
      "password": "mysecretpass"
    }
    ```
    - Проверка токена регистрации
    - Валидация даты и пароля
    - Сохранение пользователя в `PostgreSQL`
    ##### Ответ:
    ```json
    {
      "id": "326a3eb7-1505-438c-b712-5411d9bfc9c6",
      "email": "user@example.com",
      "fullName": "Ivan Ivanov",
      "birthday": "2002-02-20"
    }
    ```

---

### Как реализована авторизация (по коду)
1. #### `POST /auth/login/code`
    ```json
    {
    "email": "user@example.com"
    }
    ```
    - Валидация email
    - Проверка количества запросов `email:request-count:user@example.com` (ограничение: 5 запросов в 10 минут)
    - Создание проверочного кода и добавление записей в `Redis`
        ```
        key: email:confirm:user@example.com
        value: 123456
        ttl: 10 минут
        ```
    - Добавление записи о количестве запросов в `Redis` (если создана -> `value += 1`)
        ```
        key: email:request-count:user@example.com
        value: 1
        ttl: 10 минут
        ```
    - Отправка письма на **email** через `SMTP`
    ##### Ответ: `200 OK` (письмо с кодом отправлено)
2. #### `POST /auth/login/verify`
    ```json
    {
      "email": "user@example.com",
      "code": "123456"
    }
    ```
    - Валидация email
    - Проверка количества попыток отправки кода `email:verify-attempts:user@example.com` (ограничение: 5 попыток в 10 минут)
    - Создание записи количества попыток отправки кода в `Redis` (если создана -> `value += 1`)
        ```
        key: email:verify-attempts:user@example.com
        value: 1
        ttl: 10 минут
        ```
    - Сравнение кода со значением записи `email:confirm:user@example.com`
    - Если код неверный: Ошибка `400 Неверный код подтверждения`
    - Создание **access** и **refresh** токенов
    - Создание записи **refresh** токена в `Redis`
        ```
        key: refresh:eyJ...wI4
        value: 1
        ttl: 7 дней
        ```
    - Удаление записей `email:confirm:user@example.com`, `email:request-count:user@example.com`, `email:verify-attempts:user@example.com`
    ##### Ответ:
    ```json
    {
      "accessToken": "eyJ...FPI",
      "refreshToken": "eyJ...wI4"
    }
    ```

---

### Как реализована авторизация (по паролю)
1. #### `POST /auth/login/password`
    ```json
    {
      "email": "user@example.com",
      "password": "mysecretpass"
    }
    ```
    - Валидация email
    - Проверка количества попыток отправки пароля `email:login-failures:user@example.com` (ограничение: 5 попыток в 5 минут)
    - Создание записи количества попыток отправки пароля в `Redis` (если создана -> `value += 1`)
        ```
        key: email:login-failures:user@example.com
        value: 1
        ttl: 5 минут
        ```
    - Проверка пароля
    - Если пароль неверный: Ошибка `400 Неверный пароль`
    - Создание **access** и **refresh** токенов
    - Создание записи **refresh** токена в `Redis`
        ```
        key: refresh:eyJ...wI4
        value: 1
        ttl: 7 дней
        ```
    - Удаление записи `email:login-failures:user@example.com`
    ##### Ответ:
    ```json
    {
      "accessToken": "eyJ...FPI",
      "refreshToken": "eyJ...wI4"
    }
    ```

---

### Как реализовано обновление **access**, **refresh** токенов
1. #### `POST /auth/refresh`
    ```json
    {
      "refreshToken": "eyJ...wI4"
    }
    ```
    - Проверка **refresh** токена
    - Создание новых **access** и **refresh** токенов
    - Создание записи **refresh** токена в `Redis`
        ```
        key: refresh:eyJ...4iI
        value: 1
        ttl: 7 дней
        ```
    - Удаление записи старого **refresh** токена `refresh:eyJ...wI4`
    ##### Ответ:
    ```json
    {
      "accessToken": "eyJ...GxA",
      "refreshToken": "eyJ...4iI"
    }
    ```

---

### Как реализован выход
1. #### `POST /auth/logout`
    `Authorization: Bearer eyJ...GxA`
    ```json
    {
      "refreshToken": "eyJ...4iI"
    }
    ```
    - Проверка **access**, **refresh** токенов
    - Добавление **access** токена в blacklist **Redis**
        ```
        key: blacklist:eyJ...GxA 
        value: 1
        ttl: до истечения access
       ```
    - Удаление записи `refresh:eyJ...4iI` 

---   

### Структура базы данных
##### Таблица `users`
```sql
CREATE TABLE users (
    id UUID PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password TEXT NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    birthday DATE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE OR REPLACE FUNCTION update_updated_at() 
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER set_updated_at
    BEFORE UPDATE ON users
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at();
```

---
### Переменные окружения

```dotenv
### AUTH SERVICE ###
AUTH_SERVICE_PORT=8080            # Порт, на котором запускается Ktor-сервер

### POSTGRES ###
POSTGRES_USER=your_user           # Пользователь базы данных
POSTGRES_PASSWORD=your_password   # Пароль
POSTGRES_USERS_HOST=localhost     # Хост PostgreSQL (например: postgres или 127.0.0.1)
POSTGRES_USERS_DB=users           # Название БД

### REDIS ###
REDIS_HOST=localhost              # Хост Redis (например: redis или 127.0.0.1)
REDIS_PORT=6379                   # Порт Redis

### MAIL (SMTP) ###
SMTP_LOGIN=smtp@example.com       # SMTP логин
SMTP_PASSWORD=your_smtp_password  # SMTP пароль
SMTP_HOST=smtp.example.com        # SMTP сервер
SMTP_PORT=587                     # SMTP порт
MAIL_USER=noreply@example.com     # Отправитель
TTL_VERIFICATION_CODE=600         # TTL проверочного кода (в секундах)

### JWT ###
JWT_SECRET=your_jwt_secret        # Секретный ключ
JWT_AUDIENCE=auth-users           # Аудитория токена
JWT_ISSUER=auth-service           # Имя или URL сервиса
TTL_ACCESS_TOKEN=3600             # Время жизни access токена (в секундах)
TTL_REFRESH_TOKEN=604800          # Время жизни refresh токена (в секундах)
```
