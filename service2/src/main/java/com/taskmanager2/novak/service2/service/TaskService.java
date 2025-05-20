package com.taskmanager2.novak.service2.service;
/*Логирование:
В каждом методе используется логирование через Logger, чтобы отслеживать действия, такие как создание, обновление, удаление
 и поиск задач. Это важно для отладки и мониторинга работы приложения.
Преобразование данных:
Используется TaskMapper для преобразования между объектами DTO и сущностями базы данных, что позволяет отделить логику
 бизнес-слоя от слоев представления и хранения данных.
Это сервисный слой, который координирует работу с репозиториями и выполняет бизнес-логику приложения.
 * */
import com.taskmanager2.novak.common2.dto.TaskDto;
import com.taskmanager2.novak.common2.enums.TaskStatus;
import com.taskmanager2.novak.persistence2.entity.TaskEntity;
import com.taskmanager2.novak.persistence2.repository.TaskRepository;
import com.taskmanager2.novak.service2.mapper.TaskMapper;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service // Аннотация указывает, что это сервисный компонент, который может быть инъецирован в другие классы
@Validated // Обеспечивает валидацию входных данных в методах, которые используют аннотации для валидации (например, @Valid)
public class TaskService {
    
    // Логирование для отслеживания действий в сервисе
    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);
    
    // Репозиторий для работы с сущностями Task
    private final TaskRepository taskRepository;
    
    // Маппер для преобразования между TaskDto и TaskEntity
    private final TaskMapper taskMapper;

    // Конструктор с внедрением зависимостей
    public TaskService(TaskRepository taskRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

    // Создание новой задачи с валидацией DTO
    @Transactional // Гарантирует, что все операции с БД будут выполнены в рамках одной транзакции
    public TaskEntity createTask(@Valid TaskDto taskDto) {
        logger.info("Creating new task: {}", taskDto.getNameTask());
        
        // Преобразуем DTO в сущность
        TaskEntity task = taskMapper.toEntity(taskDto);
        
        // Сохраняем сущность в базе данных и возвращаем сохраненную задачу
        return taskRepository.save(task);
    }

    // Обновление существующей задачи по ID
    @Transactional
    public TaskEntity updateTask(Long id, @Valid TaskDto taskDto) {
        logger.info("Updating task with id: {}", id);
        
        // Ищем задачу в базе данных по ID, если не находим, выбрасываем исключение
        TaskEntity task = taskRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Task not found with id: {}", id);
                    return new RuntimeException("Task not found with id: " + id);
                });
        
        // Обновляем поля задачи из DTO
        task.setNameTask(taskDto.getNameTask());
        task.setStatusTask(TaskStatus.valueOf(taskDto.getStatusTask())); // Статус задачи преобразуется из строки в перечисление
        
        // Сохраняем обновленную задачу в базе данных
        return taskRepository.save(task);
    }

    // Удаление задачи по ID
    @Transactional
    public void deleteTask(Long id) {
        logger.info("Deleting task with id: {}", id);
        
        // Проверяем наличие задачи в базе данных
        if (!taskRepository.existsById(id)) {
            logger.error("Task not found for deletion with id: {}", id);
            throw new RuntimeException("Task not found with id: " + id);
        }
        
        // Удаляем задачу из базы данных по ID
        taskRepository.deleteById(id);
    }

    // Получение всех задач из базы данных
    public List<TaskEntity> getAllTasks() {
        logger.info("Fetching all tasks");
        return taskRepository.findAll(); // Возвращаем список всех задач
    }

    // Получение задач по статусу
    public List<TaskEntity> getTasksByStatus(String status) {
        logger.info("Fetching tasks by status: {}", status);
        
        // Ищем задачи в базе данных с определенным статусом
        return taskRepository.findByStatusTask(TaskStatus.valueOf(status)); // Преобразуем строку в перечисление
    }

    // Получение задачи по ID
    public TaskEntity getTaskById(Long id) {
        logger.info("Fetching task by id: {}", id);
        
        // Ищем задачу в базе данных по ID, если не находим, выбрасываем исключение
        return taskRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Task not found with id: {}", id);
                    return new RuntimeException("Task not found with id: " + id);
                });
    }
}
