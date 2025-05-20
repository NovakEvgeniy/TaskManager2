Вот готовая разметка Markdown для вашего README.md:

```markdown
# TaskManager2

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-F2F4F9?style=for-the-badge&logo=spring-boot)
![MySQL](https://img.shields.io/badge/mysql-%2300f.svg?style=for-the-badge&logo=mysql&logoColor=white)
![GitHub](https://img.shields.io/badge/github-%23121011.svg?style=for-the-badge&logo=github&logoColor=white)

Многомодульное Spring Boot учебное приложение для управления задачами CRUD с системой ролевого доступа.

## 📋 О проекте

TaskManager2 - это мульти-модульное веб-приложение для управления задачами CRUD с различными уровнями доступа для разных ролей пользователей:
- Администратор (полный доступ)
- Директор (расширенные права)
- Экономист (ограниченные права)
- Бухгалтер (ограниченные права)
- Посетитель (базовые права)

## 🌟 Особенности

- Многомодульная архитектура (6 модулей)
- Аутентификация и авторизация с Spring Security
- REST API для управления задачами
- Ролевая модель доступа
- Интерактивный веб-интерфейс
- Интеграция с MySQL
- Валидация данных
- Логирование операций

## 🛠 Технологии

- **Backend**: Java 17, Spring Boot 3.2.0
- **Frontend**: Thymeleaf, JavaScript, Bootstrap
- **База данных**: MySQL 8.0
- **Безопасность**: Spring Security
- **Сборка**: Maven
- **Система контроля версий**: Git/GitHub

## 🏗 Архитектура

Это приложение относится к гибридному типу SSR + AJAX (CSR)

Проект разделен на 6 модулей:
1. **app2** - Главный модуль приложения
2. **common2** - Общие DTO и перечисления
3. **persistence2** - Работа с базой данных (JPA)
4. **security2** - Конфигурация безопасности
5. **service2** - Бизнес-логика
6. **web2** - Веб-слой и контроллеры

## Связь с БД

Тип связи с БД — ORM с использованием Spring Data JPA и Hibernate


## 🚀 Запуск проекта

### Проект выполнен в версиях
- Java 20 JDK
- MySQL 8.0+
- Maven 3.8+

### Установка
1. Клонируйте репозиторий:
   ```bash
   git clone https://github.com/NovakEvgeniy/TaskManager2.git
   ```

2. Настройте базу данных:
   - Создайте БД `taskmanager2` в MySQL. Остальное создаст фреймворк Hibernate.
   - В файле `app2/src/main/resources/application.properties` настройте доступ:
     ```properties
     # Имя пользователя для доступа к вашей БД
     spring.datasource.username=ваш_username
     # Пароль для подключения к вашей БД 
     spring.datasource.password=ваш_password
     ```

3. Соберите проект:
   ```bash
   mvn clean install
   ```

4. Запустите приложение:
   ```bash
   cd app2
   mvn spring-boot:run
   ```
   (или через вашу среду разработки)

5. Приложение будет доступно по адресу: `http://localhost:8080`

## 🔐 Тестовые пользователи (in-memory)

| Роль          | Логин      | Пароль    |
|---------------|-----------|-----------|
| Администратор | admin     | admin     |
| Директор      | director  | director  |
| Экономист     | economist | economist |
| Бухгалтер     | accountant| accountant|

> Посетитель регистрируется самостоятельно (например, логин: visitor1, пароль: visitor1)

## 📄 Лицензия

Проект не лицензирован

## ✉️ Контакты

Автор: Новак Евгений  
Email: [novakevgeniy1953@gmail.com](mailto:novakevgeniy1953@gmail.com)  
GitHub: [https://github.com/NovakEvgeniy/TaskManager2](https://github.com/NovakEvgeniy/TaskManager2)
```
