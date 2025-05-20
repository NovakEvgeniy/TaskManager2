package com.taskmanager2.novak.service2.mapper;

/*Роль TaskMapper:
Этот класс является слоем преобразования данных, который используется для конвертации данных между объектами сущностей
 (которые часто используются в базе данных) и DTO (которые передаются через слои приложения, такие как веб-слой).
 * */
import com.taskmanager2.novak.common2.dto.TaskDto;
import com.taskmanager2.novak.persistence2.entity.TaskEntity;
import com.taskmanager2.novak.common2.enums.TaskStatus;
import org.springframework.stereotype.Component;

@Component // Указывает, что класс является компонентом Spring, то есть он будет автоматически зарегистрирован в контейнере Spring.
public class TaskMapper {

    // Метод для преобразования TaskEntity в TaskDto
    public TaskDto toDto(TaskEntity entity) {
        // Если переданный объект entity равен null, возвращаем null
        if (entity == null) {
            return null;
        }
        
        // Создаем новый объект DTO
        TaskDto dto = new TaskDto();
        
        // Устанавливаем значения из сущности в DTO
        dto.setNameTask(entity.getNameTask());
        dto.setStatusTask(entity.getStatusTask().name()); // Преобразуем статус сущности в строку (name()) и устанавливаем в DTO
        
        return dto; // Возвращаем DTO
    }

    // Метод для преобразования TaskDto в TaskEntity
    public TaskEntity toEntity(TaskDto dto) {
        // Если переданный объект dto равен null, возвращаем null
        if (dto == null) {
            return null;
        }
        
        // Создаем новый объект сущности
        TaskEntity entity = new TaskEntity();
        
        // Устанавливаем значения из DTO в сущность
        entity.setNameTask(dto.getNameTask());
        
        // Если статус задачи в DTO не равен null, преобразуем его в TaskStatus и устанавливаем в сущность
        if (dto.getStatusTask() != null) {
            entity.setStatusTask(TaskStatus.valueOf(dto.getStatusTask())); // Преобразуем строку из DTO в перечисление TaskStatus
        }
        
        return entity; // Возвращаем сущность
    }
}
