// Пакет, содержащий сущности для работы с базой данных
package com.taskmanager2.novak.persistence2.entity;

// Импорт аннотаций JPA для работы с базой данных
import jakarta.persistence.*;

/**
 * Сущность для представления пользователя в базе данных.
 * Используется JPA для отображения этой сущности на таблицу в базе данных.
 * @Entity: Это аннотация JPA, указывающая, что класс является сущностью, и будет отображаться на таблицу в базе данных.
@Table(name = "users"): Определяет, что эта сущность будет храниться в таблице с именем users в базе данных.
@Id и @GeneratedValue: Указывают, что поле id будет являться уникальным идентификатором (первичным ключом),
 и его значения будут автоматически генерироваться.
@Column(nullable = false): Указывает, что столбец не может быть пустым.
@Column(unique = true): Указывает, что значение в столбце должно быть уникальным.
Метод setRole: В нем добавляется префикс ROLE_ к роли, чтобы она соответствовала стандарту, используемому в Spring Security,
 где роли обычно имеют вид ROLE_USER, ROLE_ADMIN и т.д.
Этот класс представляет сущность пользователя для хранения в базе данных, и будет использоваться для аутентификации
 и авторизации в приложении.
 */
@Entity
@Table(name = "users")  // Указывает на таблицу "users" в базе данных
public class UserEntity {

    /**
     * Уникальный идентификатор пользователя.
     * Аннотация @Id помечает поле как первичный ключ.
     * @GeneratedValue(strategy = GenerationType.IDENTITY) указывает на автогенерацию значения ключа.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Имя пользователя (логин).
     * @Column(nullable = false, unique = true) означает, что поле не может быть пустым, и значение должно быть уникальным в таблице.
     */
    @Column(nullable = false, unique = true)
    private String username;

    /**
     * Пароль пользователя.
     * @Column(nullable = false) означает, что поле не может быть пустым.
     */
    @Column(nullable = false)
    private String password;

    /**
     * Роль пользователя.
     * @Column(nullable = false) означает, что поле не может быть пустым.
     */
    @Column(nullable = false)
    private String role;

    // Геттеры и сеттеры (методы доступа к полям класса)

    public Long getId() {
        return id;  // Возвращает уникальный идентификатор пользователя
    }

    public String getUsername() {
        return username;  // Возвращает имя пользователя (логин)
    }

    public String getPassword() {
        return password;  // Возвращает пароль пользователя
    }

    public String getRole() {
        return role;  // Возвращает роль пользователя
    }

    public void setId(Long id) {
        this.id = id;  // Устанавливает уникальный идентификатор пользователя
    }

    public void setUsername(String username) {
        this.username = username;  // Устанавливает имя пользователя (логин)
    }

    public void setPassword(String password) {
        this.password = password;  // Устанавливает пароль пользователя
    }

    
    public void setRole(String role) {
        this.role = role;  
    }
}
