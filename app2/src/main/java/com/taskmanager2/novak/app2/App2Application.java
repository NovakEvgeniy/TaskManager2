// Пакет, в котором находится основной класс приложения
package com.taskmanager2.novak.app2;

// Импорты необходимых Spring Boot аннотаций и классов
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/*
 * Главный класс Spring Boot приложения.
 * 
 * Аннотация @SpringBootApplication включает в себя:
 * - @Configuration: обозначает, что класс содержит конфигурационные бины
 * - @ComponentScan: автоматически сканирует компоненты, сервисы, контроллеры и т.д.
 * - @EnableAutoConfiguration: включает автоматическую настройку Spring по зависимостям
 * 
 * В данном случае используется scanBasePackages, чтобы указать корневой пакет проекта,
 * включая все модули: web2, service2, persistence2 и др.
 */
@SpringBootApplication(scanBasePackages = "com.taskmanager2.novak")

// Указывает, где искать интерфейсы репозиториев JPA
@EnableJpaRepositories(basePackages = "com.taskmanager2.novak.persistence2.repository")

// Указывает, где искать JPA-сущности (Entity-классы)
@EntityScan(basePackages = "com.taskmanager2.novak.persistence2.entity")

public class App2Application {

    /**
     * Метод main — точка входа в приложение.
     * Вызывает SpringApplication.run, который запускает Spring Boot.
     */
    public static void main(String[] args) {
        SpringApplication.run(App2Application.class, args);
    }
}
