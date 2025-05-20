// Пакет, содержащий репозитории для работы с базой данных
package com.taskmanager2.novak.persistence2.repository;

// Импорт сущности TaskEntity, которая будет использоваться для работы с данными
import com.taskmanager2.novak.persistence2.entity.TaskEntity;

// Импорт перечисления TaskStatus для фильтрации по статусу задачи
import com.taskmanager2.novak.common2.enums.TaskStatus;

// Импорт JpaRepository для работы с базой данных через Spring Data JPA
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Репозиторий для работы с сущностью TaskEntity.
 * Расширяет JpaRepository, что позволяет использовать стандартные методы CRUD для работы с задачами.
 * Также содержит пользовательский метод для поиска задач по статусу.
 * @Repository (не требуется явной аннотации, так как Spring Data JPA автоматически обрабатывает интерфейсы репозиториев):
 *  Это интерфейс для работы с сущностью TaskEntity. Он расширяет интерфейс JpaRepository, который предоставляет стандартные
 *   методы для работы с базой данных  (например, сохранение, удаление, поиск и обновление).
JpaRepository<TaskEntity, Long>: Это интерфейс репозитория, параметризированный сущностью TaskEntity (тип данных для работы
 с сущностью) и типом идентификатора (в данном случае Long).
Метод findByStatusTask: Этот метод используется для получения списка всех задач с определенным статусом. Spring Data JPA
 автоматически генерирует SQL-запрос на основе имени метода (так называемая "Query Derivation" — вывод запроса из имени метода).
  Метод ищет задачи, у которых статус соответствует значению, переданному в параметре statusTask.

Таким образом, этот репозиторий предоставляет простое и эффективное взаимодействие с базой данных для работы с задачами
 в приложении.
 */
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

    /**
     * Метод для поиска задач по их статусу.
     * Использует встроенную возможность Spring Data JPA для создания запроса на основе имени метода.
     * 
     * @param statusTask статус задач, по которому будет выполнен поиск.
     * @return Список задач, соответствующих переданному статусу.
     */
    List<TaskEntity> findByStatusTask(TaskStatus statusTask);
}
