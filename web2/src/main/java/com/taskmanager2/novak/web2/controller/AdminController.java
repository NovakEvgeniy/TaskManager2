package com.taskmanager2.novak.web2.controller;
/*
 Ключевые особенности контроллера:
Двойная функциональность:
Работает как традиционный MVC-контроллер (возвращает имена шаблонов)
И как REST-контроллер (через аннотации @ResponseBody)
Безопасность:
Критические методы защищены @PreAuthorize
Доступ к API только для роли ADMIN
Обработка ошибок:
Все методы имеют try-catch блоки
Возвращают понятные сообщения об ошибках
Организация endpoints:
/admin/* - для страниц админ-панели
/api/users/* - для REST API работы с пользователями
Работа с данными:
Использует UserService для бизнес-логики
Передает данные в представления через Model
Контроллер обеспечивает как отображение административных страниц, так и REST API для управления пользователями,
 с соблюдением требований безопасности.*/

import com.taskmanager2.novak.service2.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller // Помечает класс как Spring MVC Controller
public class AdminController {

    // Сервис для работы с пользователями
    private final UserService userService;

    // Конструктор с внедрением зависимости UserService
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Обработчик GET запроса для отображения главной страницы админки
     * @return имя шаблона страницы admin.html
     */
    @GetMapping("/admin")
    public String showAdminPage() {
        return "admin";
    }

    /**
     * Обработчик GET запроса для отображения страницы задач в админке
     * @return имя шаблона страницы taskView.html
     */
    @GetMapping("/admin/tasks")
    public String showAdminTasksView() {
        return "taskView";
    }

    /**
     * Обработчик GET запроса для отображения страницы пользователей в админке
     * @param model объект для передачи данных в представление
     * @return имя шаблона страницы userView.html
     */
    @GetMapping("/admin/users")
    public String showAdminUsersView(Model model) {
        // Добавляем список всех пользователей в модель
        model.addAttribute("users", userService.getAllUsers());
        return "userView";
    }

    /**
     * REST endpoint для получения списка всех пользователей (только для ADMIN)
     * @return ResponseEntity со списком пользователей или сообщением об ошибке
     */
    @GetMapping("/api/users")
    @ResponseBody // Указывает, что возвращаемый объект должен быть преобразован в тело ответа
    @PreAuthorize("hasRole('ADMIN')") // Разрешает доступ только пользователям с ролью ADMIN
    public ResponseEntity<?> getAllUsers() {
        try {
            // Возвращаем список пользователей с HTTP статусом 200 OK
            return ResponseEntity.ok(userService.getAllUsers());
        } catch (Exception e) {
            // В случае ошибки возвращаем HTTP статус 500 с сообщением об ошибке
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error fetching users: " + e.getMessage());
        }
    }

    /**
     * REST endpoint для удаления пользователя по ID (только для ADMIN)
     * @param id ID пользователя для удаления
     * @return ResponseEntity с сообщением о результате операции
     */
    @DeleteMapping("/api/users/{id}")
    @ResponseBody
    @PreAuthorize("hasRole('ADMIN')") // Разрешает доступ только пользователям с ролью ADMIN
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            // Удаляем пользователя
            userService.deleteUser(id);
            // Возвращаем сообщение об успешном удалении
            return ResponseEntity.ok("User deleted successfully");
        } catch (Exception e) {
            // В случае ошибки возвращаем HTTP статус 400 с сообщением об ошибке
            return ResponseEntity.badRequest().body("Error deleting user: " + e.getMessage());
        }
    }
}