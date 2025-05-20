let currentUserRole = '';

document.addEventListener('DOMContentLoaded', async () => {
    await checkUserRole();
    setupEventHandlers();
	await loadTasks(); // Явно загружаем задачи при старте
    
    // Загружаем задачи или пользователей в зависимости от страницы
    if (window.location.pathname.includes('/admin/tasks')) {
        await loadTasks();
    } else if (window.location.pathname.includes('/admin/users')) {
        await loadUsers();
    }
});

async function checkUserRole() {
    try {
        const response = await fetch('/api/check-role', {
            credentials: 'include'
        });
        currentUserRole = await response.text();
    } catch (error) {
        console.error('Failed to check role:', error);
    }
}

function setupEventHandlers() {
    // Обработчик добавления задачи (только для страницы задач)
    if (document.querySelector('.task-form')) {
        document.querySelector('.task-form').addEventListener('submit', async (e) => {
            e.preventDefault();
            
            if (currentUserRole !== 'DIRECTOR' && currentUserRole !== 'ADMIN') {
                alert('Добавление задач запрещено для вашей роли');
                return;
            }

            const formData = new FormData(e.target);
            try {
                const response = await fetch('/api/tasks', {
                    method: 'POST',
                    body: formData,
                    credentials: 'include'
                });
                
                if (!response.ok) throw new Error(await response.text());
                await loadTasks();
                e.target.reset();
            } catch (error) {
                console.error('Ошибка:', error);
                alert('Ошибка при добавлении задачи: ' + error.message);
            }
        });
    }

    // Обработчики кликов по кнопкам (работают на всех страницах)
    document.addEventListener('click', async (e) => {
        // Редактирование задачи (только для страницы задач)
        if (e.target.classList.contains('edit-btn')) {
            if (currentUserRole !== 'DIRECTOR' && currentUserRole !== 'ADMIN') {
                alert('Редактирование запрещено для вашей роли');
                return;
            }
            
            const roleName = getRoleName(currentUserRole);
            if (!confirm(`${roleName}: подтвердите редактирование задачи`)) {
                return;
            }
            
            // Заполняем форму редактирования
            document.getElementById('edit-id').value = e.target.dataset.id;
            document.getElementById('edit-nameTask').value = e.target.dataset.name;
            document.getElementById('edit-statusTask').value = e.target.dataset.status;
            
            // Показываем модальное окно
            document.getElementById('editModal').style.display = 'block';
        }

        // Удаление задачи (только для страницы задач)
        if (e.target.classList.contains('delete-task-btn')) {
            if (currentUserRole !== 'DIRECTOR' && currentUserRole !== 'ECONOMIST' && currentUserRole !== 'ADMIN') {
                alert('Удаление запрещено для вашей роли');
                return;
            }
            
            const roleName = getRoleName(currentUserRole);
            if (!confirm(`${roleName}: подтвердите удаление задачи`)) {
                return;
            }
            
            try {
                const response = await fetch(`/api/tasks/${e.target.dataset.id}`, {
                    method: 'DELETE',
                    credentials: 'include'
                });
                
                if (!response.ok) throw new Error(await response.text());
                await loadTasks();
            } catch (error) {
                console.error('Ошибка:', error);
                alert('Ошибка при удалении задачи: ' + error.message);
            }
        }

        // Удаление пользователя (только для страницы пользователей)
        if (e.target.classList.contains('delete-user-btn')) {
            if (currentUserRole !== 'ADMIN') {
                alert('Удаление пользователей запрещено для вашей роли');
                return;
            }
            
            if (!confirm('Подтвердите удаление пользователя')) {
                return;
            }
            
            try {
                const response = await fetch(`/api/users/${e.target.dataset.id}`, {
                    method: 'DELETE',
                    credentials: 'include'
                });
                
                if (!response.ok) throw new Error(await response.text());
                await loadUsers();
            } catch (error) {
                console.error('Ошибка:', error);
                alert('Ошибка при удалении пользователя: ' + error.message);
            }
        }
    });

    // Обработчик формы редактирования (только для страницы задач)
    if (document.getElementById('edit-form')) {
        document.getElementById('edit-form').addEventListener('submit', async (e) => {
            e.preventDefault();
            const formData = new FormData(e.target);
            
            try {
                const response = await fetch(`/api/tasks/${formData.get('id')}`, {
                    method: 'PUT',
                    body: formData,
                    credentials: 'include'
                });
                
                if (!response.ok) throw new Error(await response.text());
                
                // Закрываем модальное окно и обновляем список
                document.getElementById('editModal').style.display = 'none';
                await loadTasks();
            } catch (error) {
                console.error('Ошибка:', error);
                alert('Ошибка при сохранении изменений: ' + error.message);
            }
        });
    }

    // Обработчик фильтрации (только для страницы задач)
    if (document.getElementById('filter-btn')) {
        document.getElementById('filter-btn').addEventListener('click', async () => {
            if (currentUserRole === 'VISITOR') {
                alert('Фильтрация запрещена для роли Посетитель');
                return;
            }
            
            const status = document.querySelector('.filter-form select').value;
            await loadTasks(status || null);
        });
    }

    // Закрытие модального окна (только для страницы задач)
    if (document.querySelector('.close')) {
        document.querySelector('.close').addEventListener('click', () => {
            document.getElementById('editModal').style.display = 'none';
        });
    }
}

async function loadTasks(filterStatus = null) {
    try {
        let url = '/api/tasks';
        if (filterStatus) {
            url = `/api/tasks/filter?status=${filterStatus}`;
        }
        
        const response = await fetch(url, {
            credentials: 'include'
        });
        
        if (!response.ok) throw new Error('Ошибка загрузки задач');
        
        const tasks = await response.json();
        renderTasks(tasks);
        updateTaskCount(tasks.length);
    } catch (error) {
        console.error('Error:', error);
        updateTaskCount(0);
    }
}

async function loadUsers() {
    try {
        const response = await fetch('/api/users', {
            credentials: 'include'
        });
        
        if (!response.ok) throw new Error('Ошибка загрузки пользователей');
        
        const users = await response.json();
        renderUsers(users);
        updateUserCount(users.length);
    } catch (error) {
        console.error('Error:', error);
        updateUserCount(0);
    }
}

function renderTasks(tasks) {
    const taskList = document.getElementById('task-list');
    if (taskList) {
        taskList.innerHTML = tasks.map(task => `
            <tr>
                <td>${task.nameTask}</td>
                <td>${task.statusTask}</td>
                <td>
                    <button class="edit-btn" 
                        data-id="${task.id}"
                        data-name="${task.nameTask}"
                        data-status="${task.statusTask}">
                        Редактировать
                    </button>
                </td>
                <td>
                    <button class="delete-task-btn" data-id="${task.id}">
                        Удалить
                    </button>
                </td>
            </tr>
        `).join('');
    }
}

function renderUsers(users) {
    const userList = document.getElementById('user-list');
    if (userList) {
        userList.innerHTML = users.map(user => `
            <tr>
                <td>${user.username}</td>
                <td>
                    <button class="delete-user-btn" data-id="${user.id}">
                        Удалить
                    </button>
                </td>
            </tr>
        `).join('');
    }
}

function updateTaskCount(count) {
    const counter = document.getElementById('task-count');
    if (counter) {
        counter.textContent = count;
    }
}

function updateUserCount(count) {
    const counter = document.getElementById('user-count');
    if (counter) {
        counter.textContent = count;
    }
}

function getRoleName(role) {
    switch(role) {
        case 'DIRECTOR': return 'Директор';
        case 'ECONOMIST': return 'Экономист';
        case 'ACCOUNTANT': return 'Бухгалтер';
        case 'VISITOR': return 'Посетитель';
        case 'ADMIN': return 'Администратор';
        default: return 'Пользователь';
    }
}