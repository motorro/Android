# Вебинар 3. Современный технологический стек

***[Read in English](#webinar-3-modern-technology-stack)***

1. Современный UI: Jetpack Compose
2. Состояние приложения и логика
3. Обмен данными по сети
4. Управление зависимостями - DI
5. Локальное хранение данных и offline-first
6. Фоновые задачи и планировщик

## Тестовый сервер:

Для запуска сервера воспользуйтесь задачей `run` соответствующего модуля из панели Gradle или введите команду:
```shell
./gradlew :tasks:server:run
```
Логин пользователя: `username`
Пароль пользователя: `password`

Приложение расчитано на запуск сервера на локальном компьютере и приложения на эмуляторе.
Если вы хотите установить приложение на телефон, измените адрес сервера в [этом файле](src/main/kotlin/ru/merionet/tasks/core/Config.kt).

## Вопросы для самопроверки:

1. Какой основной принцип лежит в основе Jetpack Compose?
   [] Декларативный подход к построению пользовательского интерфейса, где вы описываете, как UI должен выглядеть для данного состояния, а Compose сам управляет 
      изменениями.
   [] Императивный подход, требующий явного управления элементами UI и их атрибутами.
   [] XML-разметка для описания UI, а затем императивное изменение ее в коде.
2. Чего мы ждем от архитектуры приложения, и зачем она нам нужна?
   [] От архитектуры приложения мы ожидаем масштабируемости, тестируемости, поддерживаемости и разделения ответственности. Она нужна для организации кода таким 
      образом, чтобы он был понятным, легко изменяемым и устойчивым к ошибкам.
   [] Архитектура приложения нужна только для того, чтобы оно выглядело красиво.
   [] Архитектура приложения никак не влияет на его производительность.
3. Что облегчает обмен данными с сервером?
   [] Использование библиотек для работы с сетью (например, Ktor, Retrofit), которые абстрагируют низкоуровневые детали HTTP-запросов и ответов, предоставляют 
      удобные API для сериализации/десериализации данных и обработки ошибок.
   [] Ручная реализация всех сетевых запросов с использованием стандартных Java-классов.
   [] Отсутствие обмена данными с сервером вообще, так как это всегда сложно.
4. Какие виды локального хранения вы знаете?
   [] Локальное хранение данных может быть реализовано с помощью: Room Persistence Library (для структурированных данных в SQLite-базе), файлов (для больших 
      объемов неструктурированных данных) и DataStore (для асинхронного и типобезопасного хранения данных).
   [] Все данные должны храниться только на удаленном сервере.
   [] Локальное хранение данных не имеет никакого смысла в современных приложениях.
5. Какие преимущества дает использование планировщика?
   [] Использование планировщика (WorkManager) позволяет эффективно управлять фоновыми задачами, гарантируя их выполнение даже при закрытии приложения или 
      перезагрузке устройства, а также соблюдая системные ограничения и оптимизируя потребление ресурсов.
   [] Планировщик нужен только для выполнения задач, которые должны выполняться строго по расписанию, например, будильник.
   [] Планировщик значительно усложняет код и не дает никаких реальных преимуществ.

---
   
# Webinar 3. Modern Technology Stack

1. Modern UI: Jetpack Compose
2. Application state and logic
3. Data exchange over the network
4. Dependency management - DI
5. Local data storage and offline-first
6. Background tasks and scheduler

## Test server:

To start the server, use the `run` task of the corresponding module from the Gradle panel or enter the command:
```shell
./gradlew :tasks:server:run
```
Username: `username`
Password: `password`

The application is designed to run the server on a local computer and the application on an emulator.
If you want to install the application on a phone, change the server address in [this file](src/main/kotlin/ru/merionet/tasks/core/Config.kt).

## Self-check questions:

1. What is the main principle behind Jetpack Compose?
   [] A declarative approach to building a user interface, where you describe how the UI should look for a given state, and Compose manages the changes itself.
   [] An imperative approach that requires explicit management of UI elements and their attributes.
   [] XML markup to describe the UI, and then imperatively changing it in the code.
2. What do we expect from application architecture, and why do we need it?
   [] From the application architecture, we expect scalability, testability, maintainability, and separation of concerns. It is needed to organize the code 
      in such a way that it is understandable, easily modifiable, and resistant to errors.
   [] The application architecture is only needed to make it look beautiful.
   [] The application architecture does not affect its performance in any way.
3. What facilitates data exchange with the server?
   [] The use of libraries for working with the network (e.g., Ktor, Retrofit), which abstract the low-level details of HTTP requests and responses, provide 
      convenient APIs for data serialization/deserialization and error handling.
   [] Manual implementation of all network requests using standard Java classes.
   [] No data exchange with the server at all, as it is always difficult.
4. What types of local storage do you know?
   [] Local data storage can be implemented using: Room Persistence Library (for structured data in a SQLite database), files (for large amounts of unstructured
      data), and DataStore (for asynchronous and type-safe data storage).
   [] All data should only be stored on a remote server.
   [] Local data storage makes no sense in modern applications.
5. What are the advantages of using a scheduler?
   [] Using a scheduler (WorkManager) allows you to effectively manage background tasks, ensuring their execution even when the application is closed or 
      the device is rebooted, as well as complying with system restrictions and optimizing resource consumption.
   [] The scheduler is only needed for tasks that must be performed strictly on schedule, such as an alarm clock.
   [] The scheduler significantly complicates the code and provides no real advantages.
