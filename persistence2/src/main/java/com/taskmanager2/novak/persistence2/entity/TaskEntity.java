// Пакет, содержащий сущности для работы с базой данных
package com.taskmanager2.novak.persistence2.entity;

// Импорт перечисления для статусов задач
import com.taskmanager2.novak.common2.enums.TaskStatus;

// Импорт аннотаций JPA для работы с базой данных
import jakarta.persistence.*;

/**
 * Сущность для представления задачи в базе данных.
 * Используется JPA для отображения этой сущности на таблицу в базе данных.
 * @Entity: Это аннотация JPA, указывающая, что класс является сущностью, и будет отображаться на таблицу в базе данных.
@Table(name = "tasks"): Определяет, что эта сущность будет храниться в таблице с именем tasks в базе данных.
@Id и @GeneratedValue: Указывают, что поле id будет являться уникальным идентификатором (первичным ключом),
 и его значения будут автоматически генерироваться.
@Column: Указывает имя столбца в базе данных и дополнительные параметры, такие как обязательность значения (nullable = false).
@Enumerated(EnumType.STRING): Указывает, что перечисление TaskStatus будет сохранено в базе данных как строка (например,
 TO_DO, IN_PROGRESS, DONE).
 */
@Entity
@Table(name = "tasks")  // Указывает на таблицу "tasks" в базе данных
public class TaskEntity {

    /**
     * Уникальный идентификатор задачи.
     * Аннотация @Id помечает поле как первичный ключ,
     * @GeneratedValue(strategy = GenerationType.IDENTITY) указывает на автогенерацию значения ключа.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Название задачи.
     * @Column(name = "name_task", nullable = false) указывает на столбец в таблице, который не может быть пустым.
     */
    @Column(name = "name_task", nullable = false)
    private String nameTask;

    /**
     * Статус задачи.
     * @Enumerated(EnumType.STRING) указывает, что в базе данных статус будет храниться как строковое значение из перечисления TaskStatus.
     * @Column(name = "status_task", nullable = false) столбец не может быть пустым.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status_task", nullable = false)
    private TaskStatus statusTask;

    // Геттеры и сеттеры (методы доступа к полям класса)

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameTask() {
        return nameTask;
    }

    public void setNameTask(String nameTask) {
        this.nameTask = nameTask;
    }

    public TaskStatus getStatusTask() {
        return statusTask;
    }

    public void setStatusTask(TaskStatus statusTask) {
        this.statusTask = statusTask;
    }
}
