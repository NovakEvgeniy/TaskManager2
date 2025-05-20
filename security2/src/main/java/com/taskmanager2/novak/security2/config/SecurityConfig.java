package com.taskmanager2.novak.security2.config;

/*Основные моменты конфигурации:
PasswordEncoder - используется BCrypt для хеширования паролей
SecurityFilterChain - определяет правила доступа к URL и настройки аутентификации
AuthenticationSuccessHandler - определяет логику перенаправления после успешного входа
UserDetailsService - комбинированный сервис, который сначала проверяет пользователей в памяти, затем в базе данных
Роли и доступ - настроены различные уровни доступа для разных ролей пользователей
Конфигурация обеспечивает гибридный подход к аутентификации, используя как предопределенных пользователей в памяти,
 так и пользователей из базы данных.
  */



import com.taskmanager2.novak.persistence2.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration // Указывает, что класс содержит конфигурацию Spring
@EnableWebSecurity // Включает настройки безопасности Spring Security
public class SecurityConfig {

    private final UserRepository userRepository;

    // Конструктор с внедрением зависимости UserRepository
    public SecurityConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Бин для кодирования паролей с использованием BCrypt
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Основная конфигурация безопасности
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // Разрешаем доступ всем к следующим URL
                .requestMatchers("/", "/login", "/register", "/css/**", "/js/**").permitAll()
                // Доступ к админским URL только для роли ADMIN
                .requestMatchers("/admin/**").hasRole("ADMIN")
                // Доступ к задачам для нескольких ролей
                .requestMatchers("/tasks", "/api/tasks").hasAnyRole("DIRECTOR", "ECONOMIST", "ACCOUNTANT", "VISITOR", "ADMIN")
                // POST запросы к задачам только для директора и админа
                .requestMatchers(HttpMethod.POST, "/api/tasks").hasAnyRole("DIRECTOR", "ADMIN")
                // PUT запросы к задачам только для директора и админа
                .requestMatchers(HttpMethod.PUT, "/api/tasks/**").hasAnyRole("DIRECTOR", "ADMIN")
                // DELETE запросы к задачам для директора, экономиста и админа
                .requestMatchers(HttpMethod.DELETE, "/api/tasks/**").hasAnyRole("DIRECTOR", "ECONOMIST", "ADMIN")
                // Фильтрация задач для директора, экономиста, бухгалтера и админа
                .requestMatchers("/api/tasks/filter").hasAnyRole("DIRECTOR", "ECONOMIST", "ACCOUNTANT", "ADMIN")
                // Все остальные запросы требуют аутентификации
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                // Настраиваем форму входа
                .loginPage("/login") // Страница входа
                .successHandler(authenticationSuccessHandler()) // Обработчик успешного входа
                .permitAll() // Разрешаем доступ всем к странице входа
            )
            .logout(logout -> logout
                // Настраиваем выход
                .logoutSuccessUrl("/login?logout") // URL после выхода
                .permitAll() // Разрешаем доступ всем к выходу
            )
            .csrf(csrf -> csrf.disable()); // Отключаем CSRF защиту (не рекомендуется для продакшена)

        return http.build();
    }

    // Обработчик успешной аутентификации
    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(
                HttpServletRequest request,
                HttpServletResponse response,
                Authentication authentication) {
                
                try {
                    // Перенаправляем пользователя в зависимости от его роли
                    if (authentication.getAuthorities().stream()
                        .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))) {
                        response.sendRedirect("/admin"); // Админа перенаправляем в админку
                    } else {
                        response.sendRedirect("/tasks"); // Остальных - к задачам
                    }
                } catch (Exception e) {
                    throw new RuntimeException("Redirect failed", e);
                }
            }
        };
    }

    // Сервис для загрузки данных пользователей
    @Bean
    public UserDetailsService userDetailsService() {
        // Создаем тестовых пользователей в памяти
        UserDetails admin = User.builder()
            .username("admin")
            .password(passwordEncoder().encode("admin")) // Кодируем пароль
            .roles("ADMIN")
            .build();

        UserDetails director = User.builder()
            .username("director")
            .password(passwordEncoder().encode("director"))
            .roles("DIRECTOR")
            .build();

        UserDetails economist = User.builder()
            .username("economist")
            .password(passwordEncoder().encode("economist"))
            .roles("ECONOMIST")
            .build();

        UserDetails accountant = User.builder()
            .username("accountant")
            .password(passwordEncoder().encode("accountant"))
            .roles("ACCOUNTANT")
            .build();

        // Менеджер пользователей в памяти
        InMemoryUserDetailsManager inMemoryManager = 
            new InMemoryUserDetailsManager(admin, director, economist, accountant);

        // Возвращаем UserDetailsService, который сначала проверяет память, потом базу данных
        return username -> {
            try {
                // Пытаемся найти пользователя в памяти
                return inMemoryManager.loadUserByUsername(username);
            } catch (UsernameNotFoundException e) {
                // Если не найден в памяти, ищем в базе данных
                return userRepository.findByUsername(username)
                    .map(user -> User.builder()
                        .username(user.getUsername())
                        .password(user.getPassword())
                        .roles(user.getRole().replace("ROLE_", "")) // Удаляем префикс ROLE_
                        .build())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            }
        };
    }
}