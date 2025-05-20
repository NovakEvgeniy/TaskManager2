package com.taskmanager2.novak.web2.controller;

/*
 Основные особенности контроллера:
Гибкая архитектура:
Совмещает MVC и REST подходы
Имеет методы как для отображения страниц, так и для API
Безопасность:
Все методы защищены аннотациями @PreAuthorize
Разные уровни доступа для разных ролей
Отдельный метод для проверки роли текущего пользователя
Обработка ошибок:
Единообразная обработка исключений
Четкие сообщения об ошибках
Использование ResponseStatusException для MVC методов
Полный CRUD:
Создание, чтение, обновление и удаление задач
Дополнительный метод для фильтрации по статусу
Работа с параметрами:
Прием параметров через @RequestParam
Работа с path переменными через @PathVariable
Использование DTO для передачи данных
Контроллер обеспечивает полный функционал для работы с задачами с учетом требований безопасности и удобства использования*/

import com.taskmanager2.novak.common2.dto.TaskDto;
import com.taskmanager2.novak.service2.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Controller // Аннотация, объявляющая класс как Spring MVC Controller
public class TaskController {

    // Сервис для работы с задачами
    private final TaskService taskService;

    // Конструктор с внедрением зависимости TaskService
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * Отображение страницы со списком задач
     * @param model Объект для передачи данных в представление
     * @return Имя шаблона страницы TaskView
     * @throws ResponseStatusException В случае ошибки при получении задач
     */
    @GetMapping("/tasks")
    @PreAuthorize("hasAnyRole('DIRECTOR', 'ECONOMIST', 'ACCOUNTANT', 'VISITOR', 'ADMIN')")
    public String showTasks(Model model) {
        try {
            // Добавление списка задач в модель для отображения
            model.addAttribute("tasks", taskService.getAllTasks());
            return "TaskView";
        } catch (Exception e) {
            // Генерация исключения с HTTP статусом 500 при ошибке
            throw new ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Error fetching tasks",
                e
            );
        }
    }

    /**
     * REST endpoint для получения всех задач
     * @return ResponseEntity со списком задач или сообщением об ошибке
     */
    @GetMapping("/api/tasks")
    @ResponseBody // Указывает, что возвращаемый объект должен быть записан в тело HTTP-ответа
    @PreAuthorize("hasAnyRole('DIRECTOR', 'ECONOMIST', 'ACCOUNTANT', 'VISITOR', 'ADMIN')")
    public ResponseEntity<?> getAllTasks() {
        try {
            return ResponseEntity.ok(taskService.getAllTasks());
        } catch (Exception e) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error fetching tasks: " + e.getMessage());
        }
    }

    /**
     * Создание новой задачи
     * @param nameTask Название задачи
     * @param statusTask Статус задачи
     * @return ResponseEntity с созданной задачей или сообщением об ошибке
     */
    @PostMapping("/api/tasks")
    @ResponseBody
    @PreAuthorize("hasAnyRole('DIRECTOR', 'ADMIN')") // Только для директора и администратора
    public ResponseEntity<?> addTask(
        @RequestParam String nameTask,
        @RequestParam String statusTask) {
        
        try {
            TaskDto taskDto = new TaskDto();
            taskDto.setNameTask(nameTask);
            taskDto.setStatusTask(statusTask);
            return ResponseEntity.ok(taskService.createTask(taskDto));
        } catch (Exception e) {
            return ResponseEntity
                .badRequest()
                .body("Error creating task: " + e.getMessage());
        }
    }

    /**
     * Обновление существующей задачи
     * @param id ID задачи для обновления
     * @param nameTask Новое название задачи
     * @param statusTask Новый статус задачи
     * @return ResponseEntity с обновленной задачей или сообщением об ошибке
     */
    @PutMapping("/api/tasks/{id}")
    @ResponseBody
    @PreAuthorize("hasAnyRole('DIRECTOR', 'ADMIN')") // Только для директора и администратора
    public ResponseEntity<?> updateTask(
        @PathVariable Long id,
        @RequestParam String nameTask,
        @RequestParam String statusTask) {
        
        try {
            TaskDto taskDto = new TaskDto();
            taskDto.setNameTask(nameTask);
            taskDto.setStatusTask(statusTask);
            return ResponseEntity.ok(taskService.updateTask(id, taskDto));
        } catch (Exception e) {
            return ResponseEntity
                .badRequest()
                .body("Error updating task: " + e.getMessage());
        }
    }

    /**
     * Удаление задачи
     * @param id ID задачи для удаления
     * @return ResponseEntity с сообщением о результате операции
     */
    @DeleteMapping("/api/tasks/{id}")
    @ResponseBody
    @PreAuthorize("hasAnyRole('DIRECTOR', 'ECONOMIST', 'ADMIN')") // Для директора, экономиста и администратора
    public ResponseEntity<?> deleteTask(@PathVariable Long id) {
        try {
            taskService.deleteTask(id);
            return ResponseEntity.ok("Task deleted successfully");
        } catch (Exception e) {
            return ResponseEntity
                .badRequest()
                .body("Error deleting task: " + e.getMessage());
        }
    }

    /**
     * Фильтрация задач по статусу
     * @param status Статус для фильтрации
     * @return ResponseEntity с отфильтрованным списком задач или сообщением об ошибке
     */
    @GetMapping("/api/tasks/filter")
    @ResponseBody
    @PreAuthorize("hasAnyRole('DIRECTOR', 'ECONOMIST', 'ACCOUNTANT', 'ADMIN')")
    public ResponseEntity<?> filterTasks(@RequestParam String status) {
        try {
            return ResponseEntity.ok(taskService.getTasksByStatus(status));
        } catch (Exception e) {
            return ResponseEntity
                .badRequest()
                .body("Error filtering tasks: " + e.getMessage());
        }
    }

    /**
     * Получение роли текущего пользователя
     * @param authentication Объект аутентификации Spring Security
     * @return ResponseEntity с ролью пользователя или сообщением об ошибке
     */
    @GetMapping("/api/check-role")
    @ResponseBody
    public ResponseEntity<String> getCurrentUserRole(Authentication authentication) {
        try {
            // Получение роли пользователя из контекста безопасности
            String role = authentication.getAuthorities().iterator().next()
                    .getAuthority().replace("ROLE_", "");
            return ResponseEntity.ok(role);
        } catch (Exception e) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error getting user role");
        }
    }
}