# java-lab8

Данный проект связан с реализацией лабораторных №5,6,7. 
В данной лабораторной работе заменён консольный клиент на клиент с графическим интерфейсом пользователя(GUI). 
В функционал клиента входит:

- Окно с авторизацией/регистрацией.
- Отображение текущего пользователя.
- Таблица, отображающая все объекты из коллекции.
- Каждое поле объекта - отдельная колонка таблицы.
- Строки таблицы можно фильтровать/сортировать по значениям любой из колонок. Сортировка и фильтрация значений столбцов реализована с помощью Streams API.
- Поддержка всех команд из предыдущих лабораторных работ.
- Область, визуализирующую объекты коллекции
- Объекты должны быть нарисованы с помощью графических примитивов с использованием Graphics, Canvas или аналогичных средств графической библиотеки.
- При визуализации использовать данные о координатах и размерах объекта.
- Объекты от разных пользователей должны быть нарисованы разными цветами.
- При нажатии на объект должна выводиться информация об этом объекте.
- При добавлении/удалении/изменении объекта, он должен автоматически появиться/исчезнуть/измениться  на области как владельца, так и всех других клиентов. 
- При отрисовке объекта воспроизводится анимация.
- Возможность редактирования отдельных полей любого из объектов (принадлежащего пользователю). Переход к редактированию объекта возможен из таблицы с общим списком объектов и из области с визуализацией объекта.
- Возможность удаления выбранного объекта (даже если команды remove ранее не было).
