// Пакет, в котором находится DTO-класс для задач
package com.taskmanager2.novak.common2.dto;

// Импорт перечисления TaskStatus (не используется прямо, возможно — планируется)
import com.taskmanager2.novak.common2.enums.TaskStatus;

// Импорты аннотаций валидации Jakarta (бывшая javax.validation)
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO (Data Transfer Object) для передачи информации о задаче между слоями приложения.
 * Используется, например, в контроллерах и сервисах.
 */
public class TaskDto {

    /**
     * Название задачи.
     * 
     * @NotBlank — поле не может быть пустым или содержать только пробелы.
     * @Size(max = 100) — ограничение длины названия до 100 символов.
     */
    @NotBlank(message = "Task name cannot be blank")
    @Size(max = 100, message = "Task name cannot exceed 100 characters")
    private String nameTask;

    /**
     * Статус задачи (например: TODO, IN_PROGRESS, DONE).
     * 
     * @NotNull — статус не должен быть null.
     * Здесь тип — String, но можно было бы использовать enum TaskStatus.
     */
    @NotNull(message = "Task status cannot be null")
    private String statusTask;

    // Геттеры и сеттеры — используются для доступа к полям объекта

    public String getNameTask() {
        return nameTask;
    }

    public void setNameTask(String nameTask) {
        this.nameTask = nameTask;
    }

    public String getStatusTask() {
        return statusTask;
    }

    public void setStatusTask(String statusTask) {
        this.statusTask = statusTask;
    }
}
