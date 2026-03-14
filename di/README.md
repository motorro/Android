# Вебинар 28. DI

***[Read in English](#webinar-28-di)***

1. Основные задачи Dependency Injection
2. Dagger: компоненты, модули, inject
3. Dagger Scopes: управление временем жизни зависимостей
4. Модульный DI: Subcomponents
5. Hilt: используем Dagger в Android

## Вопросы для самопроверки:

1. Что такое Dependency Injection и зачем он нужен?
   [] DI - это способ шифрования данных для безопасного хранения.
   [] DI - это шаблон проектирования, при котором зависимости объекта предоставляются извне, а не создаются самим объектом. DI полезен для уменьшения связанности,
      повышения тестируемости и гибкости кода.
   [] DI - это механизм для обработки сетевых запросов в фоновом потоке.
2. Каковы основные составные части Dagger?
   [] Основные части Dagger - это ViewModel, LiveData и Coroutines.
   [] Основные части Dagger включают компоненты (Components), предоставляющие зависимости и модули (Modules) - фабрики зависимостей.
   [] Основные части Dagger - это Activity, Fragment и Service.
3. Что такое Dagger Scopes и как они помогают управлять временем жизни зависимостей?
   [] Dagger Scopes - это механизм для обработки ошибок в коде.
   [] Dagger Scopes используются для определения доступа к сетевым ресурсам.
   [] Dagger Scopes (области видимости) - это аннотации, которые определяют время жизни экземпляров зависимостей. Они гарантируют, что для определенного
      компонента будет создан только один экземпляр зависимости в течение его жизненного цикла, что позволяет переиспользовать объекты и управлять
      их временем жизни.
4. Что такое Subcomponents в Dagger и как они используются?
   [] Subcomponents - это способ создания асинхронных задач в Dagger.
   [] Subcomponents (подкомпоненты) в Dagger позволяют расширять или наследовать граф зависимостей родительского компонента. Они используются для создания
      иерархии компонентов, что помогает управлять зависимостями в модульных приложениях, имеющих свой собственный жизненный цикл и набор зависимостей.
   [] Subcomponents используются для автоматической генерации UI-кода.
5. Как Hilt упрощает использование Dagger в Android?
   [] Hilt - это библиотека, построенная на Dagger, которая упрощает внедрение зависимостей в Android, автоматически генерируя и управляя Dagger компонентами
      для стандартных компонентов Android (Activity, Fragment, Service и т.д.), а также предоставляя предопределенные Scope'ы. Это значительно сокращает
      количество шаблонного кода.
   [] Hilt - это полная замена Dagger, которая не использует Dagger вовсе.
   [] Hilt - это инструмент для отладки Dagger-приложений.

---

# Webinar 28. DI

1.  Main tasks of Dependency Injection
2.  Dagger: components, modules, inject
3.  Dagger Scopes: managing the lifetime of dependencies
4.  Modular DI: Subcomponents
5.  Hilt: using Dagger in Android

## Self-check questions:

1.  What is Dependency Injection and why is it needed?
    [] DI is a method of encrypting data for secure storage.
    [] DI is a design pattern in which an object's dependencies are provided from the outside, rather than being created by the object itself. DI is useful for 
       reducing coupling, increasing testability, and code flexibility.
    [] DI is a mechanism for handling network requests in a background thread.
2.  What are the main components of Dagger?
    [] The main parts of Dagger are ViewModel, LiveData, and Coroutines.
    [] The main parts of Dagger include components, which provide dependencies, and modules, which are dependency factories.
    [] The main parts of Dagger are Activity, Fragment, and Service.
3.  What are Dagger Scopes and how do they help manage the lifetime of dependencies?
    [] Dagger Scopes are a mechanism for handling errors in code.
    [] Dagger Scopes are used to define access to network resources.
    [] Dagger Scopes are annotations that define the lifetime of dependency instances. They ensure that only one instance of a dependency is created for 
       a specific component during its lifecycle, which allows for object reuse and managing their lifetime.
4.  What are Subcomponents in Dagger and how are they used?
    [] Subcomponents are a way to create asynchronous tasks in Dagger.
    [] Subcomponents in Dagger allow extending or inheriting the dependency graph of a parent component. They are used to create a hierarchy of components, 
       which helps manage dependencies in modular applications that have their own lifecycle and set of dependencies.
    [] Subcomponents are used for automatic UI code generation.
5.  How does Hilt simplify the use of Dagger in Android?
    [] Hilt is a library built on top of Dagger that simplifies dependency injection in Android by automatically generating and managing Dagger components for 
       standard Android components (Activity, Fragment, Service, etc.), as well as providing predefined Scopes. This significantly reduces the amount 
       of boilerplate code.
    [] Hilt is a complete replacement for Dagger that does not use Dagger at all.
    [] Hilt is a tool for debugging Dagger applications.
