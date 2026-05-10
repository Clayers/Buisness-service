# Business-service

Основной сервис бизнес-логики проекта. Обеспечивает работу с базой данных через `dto-lib`, реализует алгоритмические задачи, предоставляет REST API (внутренний, не публичный). **Все запросы из `MenuApiProject` (сервис-фасад с Swagger UI) летят именно в этот сервис, где выполняется реальная бизнес-логика.**

## 🎯 Назначение

Реализация всей бизнес-логики приложения. Взаимодействие с `dto-lib` (JPA-сущности, DAO). Предоставление внутреннего API для вызовов из `MenuApiProject`. `Business-service` не имеет собственного Swagger UI – он работает как бэкенд для фасадного сервиса. Swagger UI находится в `MenuApiProject`, а все запросы по нажатию кнопок в меню отправляются в `Business-service`.

## 🔄 Архитектура взаимодействия

1. **MenuApiProject** (сервис-фасад) – содержит Swagger UI, который выступает в роли интерактивного меню.
2. **Business-service** (сервис бизнес-логики) – реально выполняет операции с БД и логические задачи.
3. **dto-lib** (общая библиотека) – содержит JPA-сущности, DAO, NSI-справочники.

При нажатии кнопки `Try it out → Execute` в Swagger `MenuApiProject` отправляет HTTP-запрос в `Business-service` (через RestTemplate, Feign Client или WebClient). `Business-service` обрабатывает запрос, выполняет бизнес-логику, работает с БД через `dto-lib`. Пользователь видит результат в Swagger UI (чаще всего в консоли ).

## 🧩 Меню (группы эндпоинтов, которые вызывает MenuApiProject)

| Группа | Назначение |
|--------|-------------|
| DB Operations | CRUD операции с пользователями, покупками, товарами |
| Logic Tasks | Алгоритмические задачи (палиндром, факториал, сортировка) |
| Kafka Publisher | Отправка сообщений в Kafka (в разработке) |

## 🚀 Быстрый старт

**Важно: перед запуском `Business-service` убедись, что запущен `MenuApiProject` (сервис-фасад с Swagger UI).** Клонировать репозиторий: `git clone <your-repo-url> cd Business-service`. Собрать проект: `mvn clean install` (предварительно должна быть собрана `dto-lib`). Запустить приложение: `mvn spring-boot:run`. Проверить, что сервис доступен (обычно на порту 8081, чтобы не конфликтовать с MenuApiProject на порту 8080). После запуска `MenuApiProject` будет отправлять запросы на `http://localhost:8081/api/...`.

## 📁 Структура проекта

src/main/java/com/example/business/ – controller/ (REST контроллеры для вызовов из MenuApiProject), service/ (бизнес-логика), mapper/ (мапперы Entity -> DTO), kafka/ (продюсер/консюмер планируется), client/ (Feign клиент для вызовов между сервисами – опционально), config/ (конфигурации). src/main/resources/ – application.properties (настройки порта, БД, Kafka).

## 🗄️ Зависимости

dto-lib (JPA-сущности и DAO для работы с БД), Java 17, Spring Boot 3.2.5, Spring Web, Spring Cloud OpenFeign (для вызовов между сервисами – опционально), Kafka (планируется).

## 🔄 Взаимодействие с dto-lib

Business-service использует dto-lib как Maven-зависимость: `com.example:dto-lib:0.0.1-SNAPSHOT`. Через PublicDao и NsiDao выполняются все операции с БД. Справочники (profession, company, product_type) автоматически заполняются при старте через инициализатор в dto-lib.

## 🧠 Примеры бизнес-логики

Создание пользователя с привязкой мест работы. Покупка товара (списание со счёта, уменьшение остатка, запись в историю). Пополнение счёта (зарплата, депозит). Расчёт статистики (сумма покупок, остаток на складе). Алгоритмические задачи: палиндром, факториал, сортировка.

## 🛠️ Настройка

В application.properties: spring.application.name=Business-service, server.port=8081 (чтобы не конфликтовать с MenuApiProject на 8080), spring.datasource.url=jdbc:postgresql://localhost:5432/mydb, spring.datasource.username=postgres, spring.datasource.password=root, spring.jpa.hibernate.ddl-auto=update, spring.jpa.properties.hibernate.hbm2ddl.create_namespaces=true.
Нужно еще проверить ваш порт бд, если отличается поменять на свой (нужно для генерации схем бд)

## 🧱 Почему разделение на два сервиса?

**MenuApiProject** – фасад с Swagger UI, имитирует пользовательское меню. **Business-service** – скрытая бизнес-логика, не имеет UI. Это позволяет: менять бизнес-логику без перегенерации Swagger, масштабировать сервисы независимо, использовать разные языки/технологии для разных слоёв, соблюдать принципы микросервисной архитектуры. Пользователь (разработчик) взаимодействует только с MenuApiProject, не зная о существовании Business-service, но все данные реально обрабатываются в нём.
