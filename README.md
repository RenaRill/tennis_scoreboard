# Проект "Табло теннисного матча"

Веб-приложение для ведения счёта теннисных матчей. Матч создаётся для двух игроков, очки начисляются по ходу игры, а
завершённые матчи сохраняются в базе данных и доступны с поиском по имени игрока.

**Деплой:** http://87.120.244.158:8080/

## Технологии

- Java 17;
- Maven;
- Spring MVC;
- Spring ORM;
- Hibernate;
- PostgreSQL;
- HikariCP;
- Hibernate Validator;
- Jackson;
- Logback;
- Lombok;
- JUnit 5;
- Tomcat 11.

## API

| Метод  | Путь                    | Что делает                |
|--------|-------------------------|---------------------------|
| `POST` | `/matches`              | создать матч              |
| `GET`  | `/matches/{uuid}`       | получить текущий счёт     |
| `POST` | `/matches/{uuid}/point` | начислить очко игроку     |
| `GET`  | `/matches?page=1&name=` | список завершённых матчей |

## Локальный запуск

Требования: JDK 17+, Maven, PostgreSQL, Tomcat 11.

1. Создать базу и выполнить скрипт `src/main/resources/db.sql`:

```bash
createdb tennis_scoreboard_db
psql -d tennis_scoreboard_db -f src/main/resources/db.sql
```

2. Скопировать шаблон настроек и указать в нём данные для подключения к БД:

```bash
cp src/main/resources/hibernate.properties.origin src/main/resources/hibernate.properties
```

3. Собрать проект:

```bash
mvn clean package
```

4. Запустить приложение в Tomcat.
5. Открыть в браузере `http://localhost:8080/`.