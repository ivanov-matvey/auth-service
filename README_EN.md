# Auth Service
A secure authentication microservice implemented in `Kotlin` using `Ktor`.

Русская версия: [README.md](README.md)

---

### Стек технологий
`Kotlin`, `Ktor`, `Redis`, `PostgreSQL`, `Exposed`, `JWT`, `SMTP`

---

### Functionality
- Registration by email with code confirmation
- Authorization by password or code
- Update **access** and **refresh** tokens
- Safe exit (token revocation)
- Validation of email, password, date of birth

---

### DDD Architecture
- `application` - UseCases
- `domain` - Interfaces, entities
- `infrastructure` - JWT, Redis, SMTP, DAO
- `presentation` - HTTP routes and DTOs

---

### Эндпоинты
| Method | URL                       | Description                                                      |
|--------|---------------------------|------------------------------------------------------------------|
| POST   | `/auth/register`          | Sending registration code to email                               |
| POST   | `/auth/register/verify`   | Code verification -> issue **register** token                    |
| POST   | `/auth/register/complete` | Completion of registration via **register** token                |
| POST   | `/auth/login/code`        | Sending authorization code to email                              |
| POST   | `/auth/login/verify`      | Code verification -> issue **access** and **refresh** tokens     |
| POST   | `/auth/login/password`    | Password verification -> issue **access** and **refresh** tokens |
| POST   | `/auth/refresh`           | Update **access** and **refresh** tokens                         |
| POST   | `/auth/logout`            | Revocation of **access** and **refresh** tokens                  |

---

### Registration
1. #### `POST /auth/register`
    ```json
    {
      "email": "user@example.com"
    }
    ```
    - Email validation
    - Checking the number of `email:request-count:user@example.com` requests (limit: 5 requests per 10 minutes)
    - Create validation code and add records to `Redis`.
        ```
        key: email:confirm:user@example.com
        value: 123456
        ttl: 10 minutes
        ```
    - Adding a record of the number of requests to `Redis` (if created -> `value += 1`)
        ```
        key: email:request-count:user@example.com
        value: 1
        ttl: 10 minutes
        ```
    - Sending email via `SMTP`
    ##### Response: `200 OK` (code email sent)
2. #### `POST /auth/register/verify`
    ```json
    {
      "email": "user@example.com",
      "code": "123456"
    }
    ```
    - Email validation
    - Checking the number of attempts to send code `email:verify-attempts:user@example.com` (limit: 5 attempts per 10 minutes)
    - Create record of number of attempts to send code in `Redis` (if created -> `value += 1`)
        ```
        key: email:verify-attempts:user@example.com
        value: 1
        ttl: 10 minutes
        ```
    - Code comparison with `email:confirm:user@example.com` record value
    - If the code is incorrect: Error `400 Invalid confirmation code`
    - Creating registration token and adding record to `Redis`
        ```
        key: register:token:360a1329-aa10-46a5-87d6-b7273e4efb75
        value: user@example.com
        ttl: 10 minutes
        ```
    - Deleting entries `email:confirm:user@example.com`, `email:request-count:user@example.com`, `email:verify-attempts:user@example.com`
    ##### Response:
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
    - Registration token validation
    - Date and password validation
    - Saving the user to `PostgreSQL`.
    ##### Response:
    ```json
    {
      "id": "326a3eb7-1505-438c-b712-5411d9bfc9c6",
      "email": "user@example.com",
      "fullName": "Ivan Ivanov",
      "birthday": "2002-02-20"
    }
    ```

---

### Authorization by code
1. #### `POST /auth/login/code`
    ```json
    {
    "email": "user@example.com"
    }
    ```
    - Email validation
    - Checking the number of `email:request-count:user@example.com` requests (limit: 5 requests per 10 minutes)
    - Create validation code and add records to `Redis`.
        ```
        key: email:confirm:user@example.com
        value: 123456
        ttl: 10 minutes
        ```
    - Adding a record of the number of requests to `Redis` (if created -> `value += 1`)
        ```
        key: email:request-count:user@example.com
        value: 1
        ttl: 10 minutes
        ```
    - Sending email via `SMTP`
    ##### Response: `200 OK` (code email sent)
2. #### `POST /auth/login/verify`
    ```json
    {
      "email": "user@example.com",
      "code": "123456"
    }
    ```
    - Email validation
    - Checking the number of attempts to send code `email:verify-attempts:user@example.com` (limit: 5 attempts per 10 minutes)
    - Create record of number of attempts to send code in `Redis` (if created -> `value += 1`)
        ```
        key: email:verify-attempts:user@example.com
        value: 1
        ttl: 10 minutes
        ```
    - Code comparison with `email:confirm:user@example.com` record value
    - If the code is incorrect: Error `400 Invalid validation code`
    - Creating **access** and **refresh** tokens
    - Creating **refresh** token record in `Redis`
        ```
        key: refresh:eyJ...wI4
        value: 1
        ttl: 7 days
        ```
    - Deleting entries `email:confirm:user@example.com`, `email:request-count:user@example.com`, `email:verify-attempts:user@example.com`
    ##### Response:
    ```json
    {
      "accessToken": "eyJ...FPI",
      "refreshToken": "eyJ...wI4"
    }
    ```

---

### Authorization by password
1. #### `POST /auth/login/password`
    ```json
    {
      "email": "user@example.com",
      "password": "mysecretpass"
    }
    ```
    - Email validation
    - Checking the number of attempts to send password `email:login-failures:user@example.com` (limit: 5 attempts per 5 minutes)
    - Create record of number of attempts to send password in `Redis` (if created -> `value += 1`)
        ```
        key: email:login-failures:user@example.com
        value: 1
        ttl: 5 minutes
        ```
    - Password Check
    - If the password is incorrect: Error `400 Invalid password`.
    - Creating **access** and **refresh** tokens
    - Creating **refresh** token record in `Redis`
        ```
        key: refresh:eyJ...wI4
        value: 1
        ttl: 7 days
        ```
    - Deleting the `email:login-failures:user@example.com` record
    ##### Response:
    ```json
    {
      "accessToken": "eyJ...FPI",
      "refreshToken": "eyJ...wI4"
    }
    ```

---

### Refresh **access**, **refresh** tokens
1. #### `POST /auth/refresh`
    ```json
    {
      "refreshToken": "eyJ...wI4"
    }
    ```
    - Verification of **refresh** token
    - Creating new **access** and **refresh** tokens
    - Creating a **refresh** token record in `Redis`
        ```
        key: refresh:eyJ...4iI
        value: 1
        ttl: 7 days
        ```
    - Deleting the old **refresh** token record `refresh:eyJ...wI4`.
    ##### Response:
    ```json
    {
      "accessToken": "eyJ...GxA",
      "refreshToken": "eyJ...4iI"
    }
    ```

---

### Logout
1. #### `POST /auth/logout`
    `Authorization: Bearer eyJ...GxA`
    ```json
    {
      "refreshToken": "eyJ...4iI"
    }
    ```
    - Check **access**, **refresh** tokens
    - Add **access** token to **Redis** blacklist
        ```
        key: blacklist:eyJ...GxA 
        value: 1
        ttl: до истечения access
       ```
    - Deleting a `refresh:eyJ...4iI` record

---

### Quick Start
- Clone repository
```bash
git clone https://github.com/ivanov-matvey/auth-service
cd auth-service
```
- Build project
````bash
./gradlew build
````
- Start services via Docker Compose
````bash
docker-compose up -d
```
