// Пакет, содержащий DTO-классы
package com.taskmanager2.novak.common2.dto;

// Импорт перечисления ролей пользователя
import com.taskmanager2.novak.common2.enums.RoleType;

// Импорт аннотаций для валидации
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO (Data Transfer Object) для передачи информации о пользователе между слоями приложения.
 * Используется, например, при регистрации, входе или управлении пользователями в админке.
 */
public class UserDto {

    // Уникальный идентификатор пользователя (может быть null при создании нового)
    private Long id;

    /**
     * Имя пользователя.
     * @NotBlank — строка не должна быть пустой или содержать только пробелы.
     * @Size — минимальная длина 3 символа, максимальная — 20.
     */
    @NotBlank(message = "Username cannot be blank")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    private String username;

    /**
     * Пароль пользователя.
     * @NotBlank — не может быть пустым.
     * @Size — минимум 6 символов.
     * В реальном приложении не стоит возвращать пароль в ответе, даже в DTO.
     */
    @NotBlank(message = "Password cannot be blank")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;

    /**
     * Роль пользователя (например, ADMIN, USER).
     * Используется перечисление RoleType для ограничения значений.
     * @NotNull — роль обязательно должна быть указана.
     */
    @NotNull(message = "Role cannot be null")
    private RoleType role;

    // Геттеры и сеттеры (методы доступа к полям класса)

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public RoleType getRole() {
        return role;
    }

    public void setRole(RoleType role) {
        this.role = role;
    }
}
