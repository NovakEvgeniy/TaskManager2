package com.taskmanager2.novak.web2.controller;

import com.taskmanager2.novak.common2.dto.UserDto; // Импорт DTO для пользователя
import com.taskmanager2.novak.common2.enums.RoleType; // Импорт Enum для ролей пользователей
import com.taskmanager2.novak.service2.service.UserService; // Импорт сервиса для работы с пользователями
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller // Обозначает класс как контроллер Spring MVC
public class RegistrationController {
    
    private final UserService userService; // Сервис для регистрации пользователей

    // Конструктор для внедрения зависимости UserService
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    // Обработчик GET-запросов для отображения формы регистрации
    @GetMapping("/register") // Обрабатывает запросы по URL "/register"
    public String showRegistrationForm() {
        return "register"; // Возвращает имя шаблона "register", который должен быть в папке templates
                            // В данном случае должен быть файл "register.html" в директории templates
    }

    // Обработчик POST-запросов для регистрации нового пользователя
    @PostMapping("/register") // Обрабатывает POST-запросы по URL "/register"
    public String registerUser(
        @RequestParam String username, // Параметр для имени пользователя
        @RequestParam String password  // Параметр для пароля пользователя
    ) {
        // Создаем новый объект UserDto и заполняем его данными пользователя
        UserDto userDto = new UserDto();
        userDto.setUsername(username); // Устанавливаем имя пользователя
        userDto.setPassword(password); // Устанавливаем пароль пользователя
        userDto.setRole(RoleType.VISITOR); // Устанавливаем роль пользователя как "VISITOR"
        
        try {
            // Попытка зарегистрировать пользователя через сервис
            userService.registerUser(userDto);
            return "redirect:/login?registered"; // Перенаправляем на страницу логина с параметром "registered"
        } catch (Exception e) {
            // В случае ошибки при регистрации перенаправляем на страницу регистрации с сообщением об ошибке
            return "redirect:/register?error=" + e.getMessage(); 
        }
    }
}
