# Вебинар 27. Архитектура приложения и многомодульное приложение

***[Read in English](#webinar-27-application-architecture-and-multi-module-application)***

Приложение - пример расширенной архитектуры приложения, использования модулей и отношений.
Цель занятия - показать цели создания понятной архитектуры проекта для эффективной и удобной командной работы.
Приложение состоит из трех бизнес-задач (usecase):

- Создание аккаунта пользователя
- Регистрация профиля
- Показ профиля

Проектная документация находится в каталоге [doc](doc). Для просмотра диаграм в Android Studio удобно воспользоваться 
плагином [PlantUML](https://plugins.jetbrains.com/plugin/7017-plantuml-integration).

Документация:

- [RegisterUsecase](doc/RegisterUsecase.puml) - диаграмма юзкейсов, показывающая действия пользователя в системе.
- [RegisterObject](doc/RegisterObject.puml) - диаграмма объектов приложения и их связей
- [Modules](doc/Modules.puml) - диаграмма связи модулей

Этот проект не содержит сервер - обмен с сервером эмулируется. Однако, в папке с документацией лежат:

- [api.yaml] - описание API сервера в формате [OpenAPI](https://www.openapis.org/)
- [openapitools.json] - конфигурация кодогенератора для создания серверного и клиентского модулей.

Подробнее о кодо-генерации читайте на сайте [openapi-generator-cli](https://github.com/OpenAPITools/openapi-generator-cli)

## Модули приложения

Приложение состоит из нескольких модулей, которые разделены по функциональности и ответственности.

- `core` - содержит общие для приложения классы, интерфейсы и абстракции, которые могут использоваться другими модулями.
- `domain` - содержит бизнес-логику приложения, интерфейсы и абстракции, которые могут использоваться другими модулями.
- `domaintest` - содержит тестовые данные для бизнес-логики, которые могут использоваться другими модулями.
- `domainmockdata` - содержит мок-реализацию хранения доменных данных. Используется вместо сетевого и дискового хранения.
- `appcore` - содержит общие компоненты для всех Android-модулей.
- `account` - модули, отвечающие за создание аккаунта пользователя при помощи разных "социальных сетей".
- `registration` - модуль, отвечающий за регистрацию профиля пользователя.

## Вопросы для самопроверки:

1. Почему удобная и понятная архитектура проекта важна?
   [] Основная цель - максимально уменьшить количество кода в проекте.
   [] Основная цель - понятная архитектура проекта важна для эффективной и удобной командной работы.
   [] Основная цель - использовать как можно больше сторонних библиотек.
2. Как тестовое покрытие помогает в поддержке больших проектов?
   [] Тестовое покрытие помогает сократить время разработки, автоматически исправляя ошибки в коде.
   [] Тестовое покрытие обеспечивает надежность кода, обнаруживает ошибки на ранних стадиях разработки, упрощает рефакторинг и добавление новых функций, а также 
      служит формой документации для поведения системы.
   [] Тестовое покрытие используется для шифрования данных и защиты их от несанкционированного доступа.
3. Что такое "зависимость" в отношении между объектами? Как мы можем эффективно связь между зависимостью и зависящим объектами?
   [] Зависимость - это когда два объекта всегда выполняются в одном потоке. Эффективно связь между ними устанавливается через глобальные переменные.
   [] Зависимость - это наследование одного класса от другого. Эффективно связь устанавливается через прямые ссылки.
   [] Зависимость - это когда один объект (зависящий) нуждается в другом объекте (зависимости) для выполнения своих функций. Эффективно управлять связями можно 
      с помощью внедрения зависимостей извне, что снижает связанность и повышает гибкость.
4. Что такое Dependency Inversion Principle?
   [] Dependency Inversion Principle - это принцип, который утверждает, что все зависимости должны быть жестко закодированы для лучшей производительности.
   [] Dependency Inversion Principle (Принцип инверсии зависимостей) - это принцип объектно-ориентированного проектирования, который гласит, что высокоуровневые 
      модули не должны зависеть от низкоуровневых модулей. Оба должны зависеть от абстракций. 
   [] Dependency Inversion Principle - это принцип, который позволяет создавать циклические зависимости между модулями.
5. Как разделение приложения на модули упрощает выпуск версий приложения для разных регионов и платформ?
   [] Разделение на модули никак не влияет на выпуск версий для разных регионов и платформ, так как все изменения должны быть внесены в основной модуль.
   [] Разделение на модули усложняет выпуск версий, так как требует компиляции каждого модуля отдельно для каждой платформы.
   [] Разделение на модули упрощает выпуск версий для разных регионов и платформ, так как позволяет создавать специфичные для региона/платформы конфигурации, 
      содержащие уникальные ресурсы, бизнес-логику или UI, не затрагивая общую кодовую базу. Это способствует переиспользованию кода и более легкому управлению 
      версиями.

---

# Webinar 27. Application Architecture and Multi-module Application

The application is an example of an extended application architecture, the use of modules, and relationships.
The goal of the lesson is to show the objectives of creating a clear project architecture for effective and convenient teamwork.
The application consists of three business tasks (use cases):

- Creating a user account
- Registering a profile
- Displaying a profile

The project documentation is in the [doc](doc) directory. To view diagrams in Android Studio, it is convenient to use the
[PlantUML](https://plugins.jetbrains.com/plugin/7017-plantuml-integration) plugin.

Documentation:

- [RegisterUsecase](doc/RegisterUsecase.puml) - a use case diagram showing user actions in the system.
- [RegisterObject](doc/RegisterObject.puml) - a diagram of application objects and their relationships.
- [Modules](doc/Modules.puml) - a module relationship diagram.

This project does not contain a server - communication with the server is emulated. However, the documentation folder contains:

- [api.yaml] - a description of the server API in the [OpenAPI](https://www.openapis.org/) format.
- [openapitools.json] - a code generator configuration for creating server and client modules.

Read more about code generation on the [openapi-generator-cli](https://github.com/OpenAPITools/openapi-generator-cli) website.

## Application Modules

The application consists of several modules that are divided by functionality and responsibility.

- `core` - contains classes, interfaces, and abstractions common to the application that can be used by other modules.
- `domain` - contains the business logic of the application, interfaces, and abstractions that can be used by other modules.
- `domaintest` - contains test data for the business logic that can be used by other modules.
- `domainmockdata` - contains a mock implementation of domain data storage. Used instead of network and disk storage.
- `appcore` - contains common components for all Android modules.
- `account` - modules responsible for creating a user account using different "social networks".
- `registration` - a module responsible for registering a user profile.

## Self-check questions:

1. Why is a convenient and understandable project architecture important?
   [] The main goal is to minimize the amount of code in the project.
   [] The main goal is that a clear project architecture is important for effective and convenient teamwork.
   [] The main goal is to use as many third-party libraries as possible.
2. How does test coverage help in maintaining large projects?
   [] Test coverage helps to reduce development time by automatically fixing errors in the code.
   [] Test coverage ensures code reliability, detects errors in the early stages of development, simplifies refactoring and adding new features, and also
      serves as a form of documentation for the system's behavior.
   [] Test coverage is used to encrypt data and protect it from unauthorized access.
3. What is a "dependency" in the relationship between objects? How can we effectively link a dependency and a dependent object?
   [] A dependency is when two objects always execute in the same thread. The link between them is effectively established through global variables.
   [] A dependency is the inheritance of one class from another. The link is effectively established through direct references.
   [] A dependency is when one object (the dependent) needs another object (the dependency) to perform its functions. Links can be effectively managed
      by injecting dependencies from the outside, which reduces coupling and increases flexibility.
4. What is the Dependency Inversion Principle?
   [] The Dependency Inversion Principle is a principle that states that all dependencies should be hard-coded for better performance.
   [] The Dependency Inversion Principle is an object-oriented design principle that states that high-level
      modules should not depend on low-level modules. Both should depend on abstractions.
   [] The Dependency Inversion Principle is a principle that allows creating circular dependencies between modules.
5. How does dividing an application into modules simplify the release of application versions for different regions and platforms?
   [] Dividing into modules does not affect the release of versions for different regions and platforms, as all changes must be made in the main module.
   [] Dividing into modules complicates the release of versions, as it requires compiling each module separately for each platform.
   [] Dividing an application into modules simplifies the release of versions for different regions and platforms, as it allows creating region/platform-specific 
      configurations containing unique resources, business logic, or UI, without affecting the common codebase. This promotes code reuse and easier 
      version management.
