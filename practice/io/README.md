# Задание IO

***[Read in English](#io-assignment)***

В этом задании мы будем:

- Отправлять запросы на сервер при помощи Retrofit и Ktor.
- Использовать Proto-Datastore для хранения сессии пользователя.
- Использовать Room для локального хранения данных.

Начиная с этого задания, мы будем шаг за шагом развивать приложение [Cookbook](/cookbook/README.md), модификацию приложения будем делать в исходном модуле.
Описание приложения смотрите в основном файле модуля. К началу работы над заданием, приложение работает на фальшивых данных. Все его экраны можно пройти и
посмотреть, как работает приложение в динамике.

## Готовое решение

Готовое решение для этого задания можно посмотреть в отдельной [ветке репозитория](https://github.com/motorro/Android/tree/practice/5.IO-solution/cookbook).

## Фоновые задачи

По заданию, часть операций с данными нам потребуется делать в фоновых задачах, когда UI не ждет завершения операции. Например, мы хотим, чтобы добавление и 
удаление рецептов из книги выглядело "мгновенным". К этому моменту, мы еще не знакомы с сервисами и планировщиком WorkManager, поэтому везде, где вы 
видите теребование "запустить фоновую задачу" - просто запускайте корутину в `GlobalScope`. 

## Задание 1. Авторизация на сервере (login)

Для авторизации в приложении мы будем использовать [Basic HTTP authentication](https://en.wikipedia.org/wiki/Basic_access_authentication). Для простоты, мы не 
будем хранить никаких сессионных токенов, а будем использовать открытые имя пользователя и пароль, сохраняя их в 
[активной сессии](/cookbook/cookbook/src/main/kotlin/com/motorro/cookbook/app/session/data/Session.kt).
Чтобы проверить правильность имени пользователя и пароля, запросим на сервере и сделаем частью сессии сделаем также и 
[профиль](/cookbook/data/src/main/kotlin/com/motorro/cookbook/data/Profile.kt) пользователя.
Для получить данные профиля, нужно обратиться с верным заголовком авторизации по ссылке:
[http://0.0.0.0/profile](/cookbook/server/src/main/resources/openapi/documentation.yaml#L15). Менеджер сессии все сделает за вас, в этом задании вам нужно 
только правильно реализовать работу сетевого интерфейса [UserApi](/cookbook/cookbook/src/main/kotlin/com/motorro/cookbook/app/session/UserApi.kt).

Задание:

- Добавьте нужные разрешения для использования сети в манифест.
- Вы можете использовать любую реализацию работы с HTTP, но для практики, в этом задании, я предлагаю использовать связку библиотек 
  [OkHttp](/gradle/libs.versions.toml#L138) и [Retrofit](/gradle/libs.versions.toml#141).
- Создайте экземпляр `OkHttp`
- Создайте ретрофит. Добавьте к нему ваш экземпляр `OkHttp` и добавьте конвертер JSON [kotlin-serialization](/gradle/libs.versions.toml#142)
- Создайте интерфейс службы для получения профиля, используя аннотации `Retrofit`.
- Создайте экземпляр службы при помощи экземпляра `Retrofit` и реализуйте `UserApi` с его помощью.
- Подмените текущую mock-реализацию в приложении на вашу.

## Задание 2. Хранение сессии на диске

Для того, чтобы пользователю не нужно было вводить имя пользователя и пароль каждый раз при входе в приложение, будем хранить сессию на диске. За хранение 
сессии для `SessionManager` отвечает отдельный интерфейс [SessionStorage](/cookbook/cookbook/src/main/kotlin/com/motorro/cookbook/app/session/SessionStorage.kt), 
содержащий поток текущего состояния сессии и метод для ее изменения. Мы реализуем этот интерфейс при помощи [Proto Datastore](https://developer.android.com/codelabs/android-proto-datastore#6) 
и JSON - сериализации.

- Добавьте [зависимость на DataStore](/gradle/libs.versions.toml#L97) в сборочный сценарий модуля приложения.
- Напишите `Serializer<Session>`, используя сериализацию в JSON при помощи Kotlin Serializable.
- Создайте singleton-свойство для хранилища сессии.
- Реализуйте `SessionStorage` с использованием этого свойства.

## Задание 3. Реализация обмена рецептами с сервером

На этом этапе займемся реализацей сетевого взаимодействия приложения и нашего сервера.
Нам нужно реализовать все методы, касающиеся обмена рецептами:

- получение списка
- получение рецепта по идентификатору
- добавление рецепта
- загрузка картинки к рецепту
- удаление рецепта

Полное описание API работы с сервером смотрите в файле документации OpenAPI или, при запущенном сервере, 
по [локальной ссылке](http://0.0.0.0/openapi).

Вы можете воспользоваться любым набором библиотек для реализации API. Подробности реализации с помощью библиотеки
[Ktor Client](https://ktor.io/docs/client-create-new-application.html) смотрите в нашем видеоуроке.

- Добавьте необходимые библиотеки (по выбору)
- Все методы сетевого API требуют авторизации. Добавляейте заголовок [Basic Authentication](https://en.wikipedia.org/wiki/Basic_access_authentication), используя
  имя пользователя и пароль из текущей активной сессии.
- Создайте реализацию [RecipeRepository](/cookbook/cookbook/src/main/kotlin/com/motorro/cookbook/app/repository/RecipeRepository.kt) с помощью вашего сетевого
  интерфейса.

Реактивным обновлением списков и рецептов в этом задании можно пренебречь. Основная задача - полная реализация сетевого интерфейса: запрос списков и рецептов, 
создание нового рецепта, загрузка картинки на сервер. Дополнительную помощь по составлению запросов к серверу можно посмотреть в 
[файле тестов](/cookbook/server/src/test/kotlin/com/motorro/cookbook/server/ApplicationKtTest.kt) сервера.

## Задание 4. Репозиторий рецептов

В этом задании мы создадим полноценный репозиторий рецептов с двумя источниками: сервером и локальной базой данных Room. База данных будет выступать 
"источником правды" и синхронизировать рецепты с сервером. Для упрощения синхронизации, мы сделали следующие шаги:

- Идентификатор рецепта - это [UUID](https://en.wikipedia.org/wiki/Universally_unique_identifier) - уникальный идентификатор. Когда мы создаем рецепт 
  в приложении и посылаем его на сервер, мы считаем, что рецепт всегда будет уникальным.
- Когда мы стираем рецепт, он не удаляется из базы данных, а помечается флагом `deleted`. Таким образом, у нас нет нужды в программировании синхронизации 
  удалений элементов списка.

Требования:

- Если у нас есть активная сессия, и мы уже один раз загрузили список рецептов, пользователь может смотреть рецепты даже при неработающем сервере
- Добавление и удаление рецептов в локальную базу рецептов, с точки зрения пользователя, происходит "мгновенно". Поэтому методы репозитория `addRecipe` и 
  `deleteRecipe` - синхронные. Операции по записи базы данных и сетевой запрос будем выполнять в фоновой задаче.
- Когда пользователь открывает приложение, список рецептов или рецепт синхронизируется с сервером.
- У пользователя есть возможность вручную обновить список рецептов из основного экрана.
- Синхронизацию рецептов нужно проводить в фоновой задаче (методы `synchronizeList` и `synchronizeRecipe` - синхронные).
- Нужно отображать состояние синхронизации и ошибки в интерфейсе. Поэтому мы используем потоки [LCEState](/core/src/main/kotlin/com/motorro/core/lce/LceState.kt)
  в качестве элемента данных при загрузке списка и рецепта в сыойстве `recipes` и методе `getRecipe` нашего репозиторя.

Подробности реализации смотрите в нашем видеоуроке, а вот примерные шаги для выполнения задания:

- Добавьте в проект зависимости Room: плагин обработки аннотаций [KSP](/gradle/libs.versions.toml#L158), процессор и runtime-библиотеку [Room](/gradle/libs.versions.toml#L101).
- Создайте [конфигурацию](https://developer.android.com/training/data-storage/room#setup) KSP и Room в build-script приложения.
- Создайте entity для элемента списка и полных данных рецепта.
- Реализуйте методы репозитория для сочетания загрузки данных из локальной базы и сервера таким образом, чтобы изменение в базе данных вызывало реактивное
  обновление списка рецептов и данных рецепта через методы `recipes` и `getRecipe` репозитория.
- Выполните добавление и удаление рецептов рецептов в базу данных с последующим вызовом синхронизации в фоновой задаче.

---

# IO Assignment

In this assignment, we will:

- Send requests to the server using Retrofit and Ktor.
- Use Proto-Datastore to store the user session.
- Use Room for local data storage.

Starting from this assignment, we will gradually develop the [Cookbook](/cookbook/README.md) application, modifying the original module.
See the main module file for a description of the application. At the beginning of this assignment, the application works with fake data. All its screens can 
be navigated to see how the application works in dynamics.

## Finished Solution

The finished solution for this assignment can be found in a separate [repository branch](https://github.com/motorro/Android/tree/practice/5.IO-solution/cookbook).

## Background Tasks

According to the assignment, some data operations will need to be done in background tasks, when the UI is not waiting for the operation to complete. For example, 
we want the addition and removal of recipes from the book to look "instantaneous". At this point, we are not yet familiar with services and the WorkManager 
scheduler, so wherever you see the requirement to "run a background task" - just launch a coroutine in `GlobalScope`.

## Task 1. Server Authorization (login)

We will use [Basic HTTP authentication](https://en.wikipedia.org/wiki/Basic_access_authentication) for authorization in the application. For simplicity, we 
will not store any session tokens, but will use the public username and password, saving them in the
[active session](/cookbook/cookbook/src/main/kotlin/com/motorro/cookbook/app/session/data/Session.kt).
To verify the username and password, we will also request the user's [profile](/cookbook/data/src/main/kotlin/com/motorro/cookbook/data/Profile.kt) from 
the server and make it part of the session.
To get the profile data, you need to make a request with the correct authorization header to the link:
[http://0.0.0.0/profile](/cookbook/server/src/main/resources/openapi/documentation.yaml#L15). The session manager will do everything for you; in this assignment, 
you only need to correctly implement the network interface [UserApi](/cookbook/cookbook/src/main/kotlin/com/motorro/cookbook/app/session/UserApi.kt).

Task:

- Add the necessary permissions for using the network in the manifest.
- You can use any HTTP implementation, but for practice in this assignment, I suggest using the combination of libraries
  [OkHttp](/gradle/libs.versions.toml#L138) and [Retrofit](/gradle/libs.versions.toml#141).
- Create an `OkHttp` instance.
- Create a Retrofit instance. Add your `OkHttp` instance to it and add the JSON converter [kotlin-serialization](/gradle/libs.versions.toml#142).
- Create a service interface to get the profile using `Retrofit` annotations.
- Create a service instance using the `Retrofit` instance and implement `UserApi` with it.
- Replace the current mock implementation in the application with yours.

## Task 2. Storing the session on disk

To prevent the user from having to enter their username and password every time they open the application, we will store the session on disk. A separate 
interface, [SessionStorage](/cookbook/cookbook/src/main/kotlin/com/motorro/cookbook/app/session/SessionStorage.kt), is responsible for storing the
session for the `SessionManager`. It contains a flow of the current session state and a method to change it. We will implement this interface using 
[Proto Datastore](https://developer.android.com/codelabs/android-proto-datastore#6) and JSON serialization.

- Add the [DataStore dependency](/gradle/libs.versions.toml#L97) to the application module's build script.
- Write a `Serializer<Session>` using JSON serialization with Kotlin Serializable.
- Create a singleton property for the session storage.
- Implement `SessionStorage` using this property.

## Task 3. Implementing recipe exchange with the server

At this stage, we will implement the network interaction between the application and our server.
We need to implement all methods related to recipe exchange:

- getting a list
- getting a recipe by ID
- adding a recipe
- uploading an image for a recipe
- deleting a recipe

For a full description of the server API, see the OpenAPI documentation file or, when the server is running,
the [local link](http://0.0.0.0/openapi).

You can use any set of libraries to implement the API. For details on implementation with the
[Ktor Client](https://ktor.io/docs/client-create-new-application.html) library, see our video tutorial.

- Add the necessary libraries (your choice).
- All network API methods require authorization. Add the [Basic Authentication](https://en.wikipedia.org/wiki/Basic_access_authentication) header, using
  the username and password from the current active session.
- Create an implementation of [RecipeRepository](/cookbook/cookbook/src/main/kotlin/com/motorro/cookbook/app/repository/RecipeRepository.kt) using your network
  interface.

Reactive updates of lists and recipes can be ignored in this assignment. The main task is the full implementation of the network interface: requesting lists 
and recipes, creating a new recipe, and uploading an image to the server. For additional help in composing requests to the server, see the server's
[test file](/cookbook/server/src/test/kotlin/com/motorro/cookbook/server/ApplicationKtTest.kt).

## Task 4. Recipe Repository

In this assignment, we will create a full-fledged recipe repository with two sources: a server and a local Room database. The database will act as the
"source of truth" and synchronize recipes with the server. To simplify synchronization, we have taken the following steps:

- The recipe identifier is a [UUID](https://en.wikipedia.org/wiki/Universally_unique_identifier) - a universally unique identifier. When we create a recipe
  in the application and send it to the server, we assume that the recipe will always be unique.
- When we delete a recipe, it is not removed from the database, but is marked with a `deleted` flag. Thus, we do not need to program the synchronization of
  list item deletions.

Requirements:

- If we have an active session and have already downloaded the recipe list once, the user can view recipes even if the server is not working.
- From the user's point of view, adding and deleting recipes in the local recipe database is "instantaneous". Therefore, the repository methods `addRecipe` and
  `deleteRecipe` are synchronous. We will perform database write operations and the network request in a background task.
- When the user opens the application, the recipe list or a recipe is synchronized with the server.
- The user has the ability to manually refresh the recipe list from the main screen.
- Recipe synchronization should be done in a background task (the `synchronizeList` and `synchronizeRecipe` methods are synchronous).
- The synchronization status and errors should be displayed in the interface. Therefore, we use [LCEState](/core/src/main/kotlin/com/motorro/core/lce/LceState.kt) 
  flows as a data element when loading the list and recipe in the `recipes` property and the `getRecipe` method of our repository.

For implementation details, see our video tutorial, and here are the approximate steps to complete the task:

- Add Room dependencies to the project: the [KSP](/gradle/libs.versions.toml#L158) annotation processing plugin, the processor, and the [Room](/gradle/libs.versions.toml#L101) 
  runtime library.
- Create a KSP and Room [configuration](https://developer.android.com/training/data-storage/room#setup) in the application's build script.
- Create entities for the list item and the full recipe data.
- Implement the repository methods to combine loading data from the local database and the server in such a way that a change in the database causes a reactive
  update of the recipe list and recipe data through the `recipes` and `getRecipe` methods of the repository.
- Implement adding and deleting recipes in the database, followed by a synchronization call in a background task.
