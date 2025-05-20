package com.taskmanager2.novak.service2.service;

/*Ключевые особенности сервиса:
Логирование - все важные операции логируются
Валидация:
Проверка уникальности имени пользователя
Валидация ролей (запрет на регистрацию ADMIN через публичный endpoint)
Валидация входных данных через @Valid
Безопасность:
Хеширование паролей перед сохранением
Отдельный защищенный метод для регистрации администраторов
Транзакционность - ключевые методы помечены @Transactional
Разделение прав - обычные пользователи и администраторы регистрируются разными методами
Сервис обеспечивает полный цикл работы с пользователями: создание, получение, удаление,
с соблюдением всех необходимых проверок безопасности.
 * */

import com.taskmanager2.novak.common2.dto.UserDto;
import com.taskmanager2.novak.common2.enums.RoleType;
import com.taskmanager2.novak.persistence2.entity.UserEntity;
import com.taskmanager2.novak.persistence2.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.Valid;
import java.util.List;

@Service // Помечает класс как сервисный компонент Spring
@Validated // Включает поддержку валидации для методов класса
public class UserService {
    
    // Логгер для записи событий
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    
    // Репозиторий для работы с пользователями в БД
    private final UserRepository userRepository;
    // Кодировщик паролей
    private final PasswordEncoder passwordEncoder;

    // Конструктор с внедрением зависимостей
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Получение списка всех пользователей
     * @return список пользователей
     */
    public List<UserEntity> getAllUsers() {
        logger.info("Fetching all users");
        return userRepository.findAll();
    }

    /**
     * Удаление пользователя по ID
     * @param id идентификатор пользователя
     * @throws RuntimeException если пользователь не найден
     */
    @Transactional // Аннотация для управления транзакциями
    public void deleteUser(Long id) {
        logger.info("Deleting user with id: {}", id);
        
        // Проверка существования пользователя
        if (!userRepository.existsById(id)) {
            logger.error("User not found for deletion with id: {}", id);
            throw new RuntimeException("User not found with id: " + id);
        }
        
        userRepository.deleteById(id);
    }

    /**
     * Регистрация нового пользователя
     * @param userDto DTO с данными пользователя
     * @return сохраненный пользователь
     * @throws RuntimeException если имя пользователя уже существует или роль недопустима
     */
    @Transactional
    public UserEntity registerUser(@Valid UserDto userDto) {
        logger.info("Registering new user: {}", userDto.getUsername());
        
        // Проверка уникальности имени пользователя
        if (userRepository.findByUsername(userDto.getUsername()).isPresent()) {
            logger.error("Username already exists: {}", userDto.getUsername());
            throw new RuntimeException("Username already exists");
        }

        // Валидация роли пользователя
        validateUserRole(userDto.getRole());

        // Создание и сохранение нового пользователя
        UserEntity user = new UserEntity();
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword())); // Хеширование пароля
        user.setRole("ROLE_" + userDto.getRole().name()); // Добавление префикса ROLE_ для Spring Security
        
        return userRepository.save(user);
    }

    /**
     * Проверка существования пользователя по имени
     * @param username имя пользователя
     * @return true если пользователь существует
     */
    public boolean usernameExists(String username) {
        return userRepository.existsByUsername(username);
    }

    /**
     * Валидация роли пользователя
     * @param role роль для проверки
     * @throws RuntimeException если роль null или попытка регистрации ADMIN
     */
    private void validateUserRole(RoleType role) {
        if (role == null) {
            throw new RuntimeException("Role cannot be null");
        }
        
        // Запрет на регистрацию администраторов через обычный endpoint
        if (role == RoleType.ADMIN) {
            throw new RuntimeException("Admin registration is not allowed through this endpoint");
        }
    }

    /**
     * Защищенный метод для регистрации администратора
     * @param username имя администратора
     * @param password пароль администратора
     * @return сохраненный администратор
     * @throws RuntimeException если имя администратора уже существует
     */
    @Transactional
    protected UserEntity registerAdminUser(String username, String password) {
        // Проверка уникальности имени администратора
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Admin username already exists");
        }

        // Создание и сохранение администратора
        UserEntity admin = new UserEntity();
        admin.setUsername(username);
        admin.setPassword(passwordEncoder.encode(password)); // Хеширование пароля
        admin.setRole("ROLE_ADMIN"); // Установка роли администратора
        
        return userRepository.save(admin);
    }
}