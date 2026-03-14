# Задание DI

***[Read in English](#assignment-di)***

Это задание основано на приложении из предыдущей работы. Мы будем развивать приложение [Cookbook](/cookbook/README.md), используя практики 
Чистой Архитектуры и DI. Модуль уже содержит все необходимые компоненты для прошлого задания и полностью работает.

Для лучшего понимания задачи, приложение уже разделено на отдельные модули:

- `model` - содержит общие модели данных приложения и сервера
- `core` - базовые структуры и функции
- `domain` - бизнес-правила
- `data` - реализация обмена данными с сервером и локального хранения
- `mockdata` - тестовая реализация доменных интерфейсов
- `appcore` - базовые компоненты всех UI-модулей
- `login` - пользовательский интерфейс авторизации
- `recipelist` - пользовательский интерфейс списка рецептов
- `addrecipe` - пользовательский интерфейс нового рецепта
- `recipe` - пользовательский интерфейс рецепта
- `cookbook` - модуль приложения, собирающий все вместе.

В этот раз, мы с вами сделаем следующие шаги:

- Воспользуемся библиотеками [Dagger](https://dagger.dev/dev-guide/) и [Hilt](https://dagger.dev/hilt/) для управления зависимостями.
- Напишем тестовое приложение для одного из UI-модулей приложения.
- Сделаем варианты конфигурации приложения для использования сервера и mock-реализации слоя данных.

## Готовое решение

Готовое решение для этого задания можно посмотреть в отдельной [ветке репозитория](https://github.com/motorro/Android/tree/practice/6.DI-solution/cookbook).

## Задание 1. Dagger/Hilt для управления зависимостями

- Подключите зависимости [Dagger](https://dagger.dev/dev-guide/) и [Hilt](https://dagger.dev/hilt/) в сборочный сценарий модуля приложения.
- Добавьте необходимые аннотации в классы приложения, Activity, фрагментов и ViewModel, чтобы Hilt мог внедрить зависимости.
- Удалите фабрики ViewModel и используйте Hilt для внедрения зависимостей в ViewModel.
- Используйте аннотацию `@Inject` для внедрения генератора deep-link [Links](/cookbook/appcore/src/main/kotlin/com/motorro/cookbook/appcore/navigation/links.kt)
  во фрагменты UI-модулей, вместо использования класса приложения.
- Напишите модули Hilt для сборки зависимостей. Используйте `@Provide`, `@Bind`, или `@Inject` аннотации по вашему выбору.

## Задание 2. Демо-приложение для модуля `recipe`

В этом задании вам нужно написать демонстрационное приложение для модуля `recipe`. В лекции по модульной архитектуре мы писали такое мини-приложение
для удобства разработки и тестирования feature-модуля. Вам нужно написать приложение для тестирования модуля `recipe`.

- Создайте новый application-модуль.
- Подключите основные модули: `domain`, `appcore`
- Подключите модуль рецепта `recipe`
- Подключите `Hilt`
- Создайте hilt-модуль необходимых зависимостей для запуска рецепта, используя модуль тестовых данных [mockdata](/cookbook/mockdata)
- Cоздайте навигационный граф, открывающий произвольный [рецепт](/cookbook/mockdata/src/main/kotlin/com/motorro/cookbook/data/recipes/recipes.kt)

## Задание 3. Product-flavors

В этом задании мы создадим `product-flavors` - варианты сборки приложения. Варианты сборки могут использовать разный состав модулей и исходных файлов.
Мы применяем варианты сборки для конфигурации особенностей приложения - платформ, используемых библиотек, и состава модулей.
В этому задании мы создадим варианты приложения для работы с сервером и с тестовыми данными:

- `server` - вариант использует реальные данные и взаимодействует с сервером. Используем модуль `data` для реализации слой данных.
- `mock` - вариант использует модуль `mockdata` для создания компонентов хранения сессии и рецептов.

- Добавьте новое измерение `flavorDimensions` в основном приложении.
- Опишите `productFlavors` для вариантов `server` и `mock`. Добавьте в один из них `applicationIdSuffix`, чтобы оба варианта можно было одновременно установить
  на устройство.
- Опишите зависимости каждого из вариантов сборки. Включите модуль `data` для варианта `server` и модуль `mockdata` для варианта `mock`.
- Добавьте отдельный source-set в модуль `cookbook` для варианта сбоки `mock` и соберите в нем граф зависимостей приложения, используя тестовые реализации
  компонентов из модуля `mockdata`.

---

#  Assignment: DI

This assignment is based on the application from the previous work. We will be developing the [Cookbook](/cookbook/README.md) application using Clean Architecture 
and DI practices. The module already contains all the necessary components from the previous assignment and is fully functional.

For a better understanding of the task, the application is already divided into separate modules:

- `model` - contains common data models for the application and the server
- `core` - basic structures and functions
- `domain` - business rules
- `data` - implementation of data exchange with the server and local storage
- `mockdata` - test implementation of domain interfaces
- `appcore` - basic components of all UI modules
- `login` - user authorization interface
- `recipelist` - user interface for the recipe list
- `addrecipe` - user interface for a new recipe
- `recipe` - user interface for a recipe
- `cookbook` - the application module that brings everything together.

This time, we will take the following steps:

- We will use the [Dagger](https://dagger.dev/dev-guide/) and [Hilt](https://dagger.dev/hilt/) libraries for dependency management.
- We will write a test application for one of the application's UI modules.
- We will create application configuration variants for using the server and a mock implementation of the data layer.

## Finished Solution

The finished solution for this assignment can be found in a separate [repository branch](https://github.com/motorro/Android/tree/practice/6.DI-solution/cookbook).

## Task 1. Dagger/Hilt for dependency management

- Add the [Dagger](https://dagger.dev/dev-guide/) and [Hilt](https://dagger.dev/hilt/) dependencies to the application module's build script.
- Add the necessary annotations to the application classes, Activities, Fragments, and ViewModels so that Hilt can inject dependencies.
- Remove the ViewModel factories and use Hilt to inject dependencies into the ViewModels.
- Use the `@Inject` annotation to inject the deep-link generator [Links](/cookbook/appcore/src/main/kotlin/com/motorro/cookbook/appcore/navigation/links.kt) into 
  the UI module fragments, instead of using the application class.
- Write Hilt modules to build dependencies. Use `@Provide`, `@Bind`, or `@Inject` annotations of your choice.

## Task 2. Demo application for the `recipe` module

In this task, you need to write a demo application for the `recipe` module. In the lecture on modular architecture, we wrote such a mini-application for 
the convenience of developing and testing a feature module. You need to write an application to test the `recipe` module.

- Create a new application module.
- Connect the main modules: `domain`, `appcore`
- Connect the `recipe` module.
- Connect `Hilt`.
- Create a Hilt module of the necessary dependencies to launch a recipe, using the test data module [mockdata](/cookbook/mockdata).
- Create a navigation graph that opens an arbitrary [recipe](/cookbook/mockdata/src/main/kotlin/com/motorro/cookbook/data/recipes/recipes.kt).

## Task 3. Product-flavors

In this task, we will create `product-flavors` - application build variants. Build variants can use a different composition of modules and source files. We use 
build variants to configure application features - platforms, used libraries, and the composition of modules.
In this task, we will create application variants for working with the server and with test data:

- `server` - this variant uses real data and interacts with the server. We use the `data` module to implement the data layer.
- `mock` - this variant uses the `mockdata` module to create session and recipe storage components.

- Add a new `flavorDimensions` dimension to the main application.
- Describe the `productFlavors` for the `server` and `mock` variants. Add an `applicationIdSuffix` to one of them so that both variants can be installed 
  on a device simultaneously.
- Describe the dependencies for each build variant. Include the `data` module for the `server` variant and the `mockdata` module for the `mock` variant.
- Add a separate source-set to the `cookbook` module for the `mock` build variant and build the application's dependency graph in it, using test implementations 
  of components from the `mockdata` module.
