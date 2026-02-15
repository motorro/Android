# Вебинар 24. Локальный репозиторий

***[Read in English](#webinar-24-local-repository)***

1. Паттерн Repository
2. Путь данных в UDF приложении и Single Source of Truth
3. Преимущества disk-first подхода для мобильных приложений
4. Пример приложения "Список книг"

## Тестовый сервер:

Для запуска сервера воспользуйтесь задачей `run` соответствующего модуля из панели Gradle или введите команду:
```shell
./gradlew :repository:server:run
```
Приложение расчитано на запуск сервера на локальном компьютере и приложения на эмуляторе.
Если вы хотите установить приложение на телефон, измените адрес сервера в [этом файле](app/src/main/kotlin/com/motorro/network/net/Config.kt).

## Вопросы для самопроверки:

1. При использовании какого паттерна мы можем использовать разные источники данных?
   [] При использовании паттерна Observer.
   [] При использовании паттерна Repository. Он абстрагирует слой данных от остальной части приложения, позволяя переключаться между различными источниками 
      данных (сеть, локальная база данных, кэш) без изменения логики в слое UI или бизнес-логики.
   [] При использовании паттерна Factory.
2. Что такое Single Source of Truth?
   [] Single Source of Truth (SSOT) - это концепция, согласно которой все данные в приложении должны храниться в нескольких разных местах для надежности.
   [] Single Source of Truth (SSOT) - это принцип, согласно которому данные в приложении должны иметь одно, авторитетное место хранения (например, база данных 
      или кэш), которое является единственным источником истины для всех остальных частей приложения. Это помогает избежать несогласованности данных и упрощает 
      управление ими.
   [] Single Source of Truth (SSOT) - это база данных, которая используется только для чтения данных.
3. Почему важно использовать immutable данные при создании реактивных приложений?
   [] Immutable данные используются для экономии оперативной памяти, так как они занимают меньше места.
   [] Использование immutable (неизменяемых) данных в реактивных приложениях важно, потому что это предотвращает непреднамеренные побочные эффекты и гонки 
      данных при параллельном доступе или обновлении. Каждое изменение данных приводит к созданию новой версии, что упрощает отслеживание изменений, тестирование 
      и обеспечивает предсказуемость поведения приложения.
   [] Immutable данные обеспечивают автоматическое шифрование данных при их сохранении.
4. Что такое disk-first подход?
   [] Disk-first подход - это стратегия, при которой приложение сначала загружает все данные из сети, а затем сохраняет их на диск.
   [] Disk-first подход - это архитектурная стратегия, при которой основное внимание уделяется локальному хранилищу (диску) как первичному источнику данных 
      для приложения. Все данные сначала записываются на диск, а затем, при необходимости, синхронизируются с сетью. Это обеспечивает стабильную работу 
      приложения в офлайн-режиме и быстрый доступ к данным.
   [] Disk-first подход - это подход, при котором приложение хранит все данные только в оперативной памяти и не использует диск вовсе.

---

# Webinar 24. Local Repository

1. Repository Pattern
2. Data path in a UDF application and Single Source of Truth
3. Advantages of the disk-first approach for mobile applications
4. Example application "Book List"

## Test Server:

To start the server, use the `run` task of the corresponding module from the Gradle panel or enter the command:
```shell
./gradlew :repository:server:run
```
The application is designed to run the server on a local computer and the application on an emulator.
If you want to install the application on a phone, change the server address in [this file](app/src/main/kotlin/com/motorro/network/net/Config.kt).

## Self-check questions:

1. With which pattern can we use different data sources?
   [] With the Observer pattern.
   [] With the Repository pattern. It abstracts the data layer from the rest of the application, allowing switching between different data sources (network, 
      local database, cache) without changing the logic in the UI layer or business logic.
   [] With the Factory pattern.
2. What is a Single Source of Truth?
   [] Single Source of Truth (SSOT) is a concept that all data in an application should be stored in several different places for reliability.
   [] Single Source of Truth (SSOT) is a principle that data in an application should have one authoritative storage location (e.g., a database or cache) that 
      is the single source of truth for all other parts of the application. This helps to avoid data inconsistency and simplifies its management.
   [] Single Source of Truth (SSOT) is a database that is used only for reading data.
3. Why is it important to use immutable data when creating reactive applications?
   [] Immutable data is used to save RAM, as it takes up less space.
   [] The use of immutable data in reactive applications is important because it prevents unintentional side effects and data races with parallel access 
      or updates. Each data change creates a new version, which simplifies change tracking, testing, and ensures predictable application behavior.
   [] Immutable data provides automatic data encryption when it is saved.
4. What is the disk-first approach?
   [] The disk-first approach is a strategy where the application first downloads all data from the network and then saves it to the disk.
   [] The disk-first approach is an architectural strategy where the main focus is on local storage (disk) as the primary data source for the application. 
      All data is first written to the disk, and then, if necessary, synchronized with the network. This ensures stable application operation in offline mode 
      and fast data access.
   [] The disk-first approach is an approach where the application stores all data only in RAM and does not use the disk at all.
