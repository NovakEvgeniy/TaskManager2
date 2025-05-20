package com.taskmanager2.novak.web2.controller;
/*Этот контроллер прост и отвечает за отображение страницы входа в систему и редирект на неё при посещении главной страницы.
 * */
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller // Обозначает, что этот класс является контроллером Spring MVC
public class LoginController {

    // Обработчик для отображения страницы логина
    @GetMapping("/login") // Обрабатывает HTTP GET запросы по URL "/login"
    public String showLoginPage() {
        return "login"; // Возвращает имя шаблона "login", который должен быть в папке templates
                        // В данном случае должен быть файл "login.html" в директории templates
    }

    // Обработчик для перенаправления с главной страницы на страницу логина
    @GetMapping("/") // Обрабатывает HTTP GET запросы по URL "/"
    public String redirectToLogin() {
        return "redirect:/login"; // Перенаправляет пользователя на страницу логина
    }
}
