# Задание Background

***[Read in English](#assignment-background)***

Это задание основано на приложении из предыдущей работы. Мы будем развивать приложение [Cookbook](/cookbook/README.md),
и заменим операции синхронизации списка, создания и удаления рецептов на использование [WorkManager](https://developer.android.com/develop/background-work/background-tasks/persistent/getting-started). 
Модуль основан на полностью реализованом приложении из наших прошлых заданий.

В этот раз, мы с вами сделаем следующие шаги:

- Добавим зависимости на `WorkManager` и инициализируем библиотеку для совместного использования с [Hilt](https://developer.android.com/training/dependency-injection/hilt-jetpack#workmanager).
- Напишем задачу для периодического обновления списка рецептов.
- Напишем задачу для удаления рецепта с сервера.
- Напишем задачу для добавления рецепта на сервер.

## Готовое решение

Готовое решение для этого задания можно посмотреть в отдельной [ветке репозитория](tree/practice/8.Background-solution/cookbook).

## 1. Инициализация WorkManager

Для инициализации `WorkManager` и его совместной работы с `Hilt` воспользуйтесь документацией к библиотекам:

- [Совместная работа Hilt и WorkManager](https://developer.android.com/training/dependency-injection/hilt-jetpack#workmanager)]
- [Специальная конфигурация WorkManager](https://developer.android.com/develop/background-work/background-tasks/persistent/configuration/custom-configuration)
- [App Startup](https://developer.android.com/topic/libraries/app-startup)

Пример такой инициализации можно найти в [материалах к нашей прошлой лекции](/tree/webinars/35.Background/background/src/main)

Последовательность шагов:

1. Добавляем зависимости на библиотеки `WorkManager` и плагин `dagger.hilt.android` в модуль `data`.
2. Добавляем зависимости на библиотеки `WorkManager` и плагин `dagger.hilt.android` в модуль `cookbook`. Дополнительно добавляем зависимость на библиотеку 
   `androidx.startup`.
3. Создаем класс инициализации, реализующий `Initializer<WorkManager>` в модуле `cookbook`. Для примера смотрите документацию выше и готовый пример в нашей 
   [лекции по фоновой](/blob/webinars/35.Background/background/src/main/kotlin/com/motorro/background/init/WorkManagerInitializer.kt) работе.
4. Регистрируем инициализатор в манифесте по инструкции или по [примеру](/blob/webinars/35.Background/background/src/main/AndroidManifest.xml).

### 2. Переделываем загрузку списка рецептов с использованием однократной задачи WorkManager

На сегодня, use-case загрузки списка рецептов явно использует основной `CoroutineScope` нашего приложения для синхронизации списка рецептов.
В этом подходе есть недостатки:

- Синхронизация может неожиданно завершиться, если процесс приложения перешел в фон и система освобождает ресурсы.
- Не обрабатываются ошибки синхронизации.

Изменим наш модуль данных таким образом, чтобы операция синхронизации списка делалась при помощи `WorkManager`.

Последовательность шагов:

1. Создаем новый `Worker`, который будет вызывать `cookbookApi.getRecipeList()` и записывать данные в базу данных с помощью `cookbookDao.insertList(...)`.
2. Модифицируем функцию `synchronize()` в [RecipeListUsecase](/cookbook/data/src/main/kotlin/com/motorro/cookbook/data/recipes/usecase/RecipeListUsecase.kt). 
   Удаляем код, непосредственно вызывающий методы получения рецептов и записи их в базу данных. Вместо этого, отправляем в `WorkManager` запрос на одиночный 
   запуск нашего `Worker`.
3. Для свойства `recipes` нам необходимо реализовать поток `LCE` состояний. Для этого получим поток данных о состоянии заданий `WorkManager` и нашего `Worker`.
   Воспользуйтесь одним из методов получения состояния задач. Например `getWorkInfosByTagFlow`. Преобразуйте поток данных о состоянии таким образом, чтобы:
   
   - `WorkInfo.State.RUNNING` приводил к состоянию `LceState.Loading` на выходе
   - `WorkInfo.State.FAILED` приводил к состоянию `LceState.Error` на выходе
   - все остальные входные состояния приводили к состоянию `LceState.Content` на выходе

### 3. Подключаем задачу периодического фонового обновления списка

В прошлом задании мы переделали загрузку списка рецептов с сервера с использованием одиночной задачи `WorkManager`. В этом задании усовершенствуем наше
приложение и будем автоматически обновлять список в фоновой задаче. Для этого, предлагаю следующий сценарий:

- ставить периодическую задачу в `WorkManager`, когда пользователь авторизовался.
- снимать периодическую задачу, когда пользователь вышел из системы.

Последовательность шагов:

1. Выбираем способ отслеживать факты входа и выхода пользователя из системы. Это может быть какой-то монитор сессии или плагин к нашему 
   [SessionManager](/cookbook/domain/src/main/kotlin/com/motorro/cookbook/domain/session/SessionManagerImpl.kt).
2. Когда пользователь вошел в систему, ставим уникальную периодическую задачу, используя `Worker` синхронизации из предыдущего задания.
3. Когда пользователь выходит из системы, снимаем задачу из планировщика при помощи одного из методов `WorkManager`, например `cancelAllWorkByTag()`.

### 4. Фоновое удаление рецепта с сервера

В этом задании мы переделаем наш [use-case удаления рецепта](/cookbook/data/src/main/kotlin/com/motorro/cookbook/data/recipes/usecase/DeleteRecipeUsecase.kt).
Воспользуемся `WorkManager`, чтобы сделать фоновую работу более надежной.

Последовательность шагов:

1. Создаем новый `Worker` и переносим в него код из use-case для обращения к серверу - вызов метода `cookbookApi.deleteRecipe(recipeId)`.
2. Для выполнения задания нам необходимо передать в `Worker` идентификатор рецепта. Воспользуйтесь методом `setInputData` билдера задания `WorkManager` для 
   передачи идентификатора из use-case.
3. Читайте это значение из входных данных в методе `doWork()`.
4. Замените явный вызов API в use-case удаления рецепта на постановку уникальной задачи в `WorkManager`.

### 5. Создание нового рецепта с помощью составной задачи

Создание нового рецепта на сервере состоит из одного или двух шагов:

1. Создание основы рецепта при помощи вызова метода: `addRecipe(recipe: Recipe): Result<Recipe>`
2. Если рецепт содержит картинку, дополнительного шага по загрузке картинки к рецепту: `uploadRecipeImage(recipeId: Uuid, imageUri: Uri): Result<ImageUpload>`

Сейчас оба этих метода явно вызываются в [AddRecipeUsecase](/cookbook/data/src/main/kotlin/com/motorro/cookbook/data/recipes/usecase/AddRecipeUsecase.kt).
Давайте переделаем этот use-case на использование составной задачи `WorkManager`.

Последовательность шагов:

1. Создаем `Worker` для загрузки основной части рецепта.
2. Создаем `Worker` для загрузки картинки.
3. Изменяем use-case создания рецепта таким образом, чтобы вместо явного вызова API в `WorkManager` создавалась составная задача для обоих пунктов выше.
4. Дополнительно, запускаем синхронизацию списка при помощи `Worker` из второго задания при успешном завершении основной операции.

### Вывод

Эта практическая работа посвящена усовершенствованию приложения "Кулинарная книга" путем переноса фоновых операций (синхронизация списка, создания и удаления 
рецептов) с использованием `WorkManager`.

Наши основные достижения:

*   **Инициализация WorkManager:** Интеграция `WorkManager` с Hilt для внедрения зависимостей и его настройка с использованием `App Startup`.
*   **Синхронизация списка рецептов:** Рефакторинг загрузки списка рецептов для использования одноразовой задачи `WorkManager`, что повышает надежность и 
*   улучшает обработку ошибок. В этом задании мы также научились получать состояние выполнения задач `WorkManager`.
*   **Периодические обновления списка:** Реализация периодических фоновых обновлений списка рецептов с помощью `WorkManager`, запускаемых при входе пользователя 
*   в систему и отменяемых при выходе.
*   **Фоновое удаление рецептов:** Перенос удаления рецептов на надежную задачу `WorkManager`, передача идентификаторов рецептов в качестве входных данных.
*   **Составная задача для создания рецептов:** Реализация составной задачи `WorkManager` для создания новых рецептов, которая может включать два шага: 
    создание базового рецепта и, при необходимости, загрузку изображения. Успешное создание рецепта также запускает задачу синхронизации списка.

В целом, практическая работа значительно улучшает фоновую обработку приложения за счет использования `WorkManager` для более надежных, стабильных 
и отказоустойчивых операций. Теперь наше приложение закончено и готово к выпуску.

---

# Assignment: Background

This assignment is based on the application from the previous work. We will be developing the [Cookbook](/cookbook/README.md) application and replacing the list
synchronization, recipe creation, and deletion operations with [WorkManager](https://developer.android.com/develop/background-work/background-tasks/persistent/getting-started). 
The module is based on the fully implemented application from our previous assignments.

This time, we will take the following steps:

- Add dependencies for `WorkManager` and initialize the library for use with [Hilt](https://developer.android.com/training/dependency-injection/hilt-jetpack#workmanager).
- Write a task for periodically updating the recipe list.
- Write a task for deleting a recipe from the server.
- Write a task for adding a recipe to the server.

## Finished Solution

The finished solution for this assignment can be found in a separate [repository branch](tree/practice/8.Background-solution/cookbook).

## 1. Initializing WorkManager

To initialize `WorkManager` and make it work with `Hilt`, use the documentation for the libraries:

- [Using Hilt with WorkManager](https://developer.android.com/training/dependency-injection/hilt-jetpack#workmanager)
- [Custom WorkManager Configuration](https://developer.android.com/develop/background-work/background-tasks/persistent/configuration/custom-configuration)
- [App Startup](https://developer.android.com/topic/libraries/app-startup)

An example of such initialization can be found in the [materials from our last lecture](/tree/webinars/35.Background/background/src/main).

Sequence of steps:

1. Add dependencies for the `WorkManager` library and the `dagger.hilt.android` plugin to the `data` module.
2. Add dependencies for the `WorkManager` library and the `dagger.hilt.android` plugin to the `cookbook` module. Additionally, add a dependency for 
   the `androidx.startup` library.
3. Create an initialization class that implements `Initializer<WorkManager>` in the `cookbook` module. For an example, see the documentation above and the 
   ready-made example in our [lecture on background work](/blob/webinars/35.Background/background/src/main/kotlin/com/motorro/background/init/WorkManagerInitializer.kt).
4. Register the initializer in the manifest according to the instructions or the [example](/blob/webinars/35.Background/background/src/main/AndroidManifest.xml).

### 2. Refactoring Recipe List Loading Using a One-Time WorkManager Task

Currently, the recipe list loading use case explicitly uses our application's main `CoroutineScope` to synchronize the recipe list. This approach has drawbacks:

- Synchronization can be unexpectedly terminated if the application process goes into the background and the system reclaims resources.
- Synchronization errors are not handled.

Let's change our data module so that the list synchronization operation is done using `WorkManager`.

Sequence of steps:

1. Create a new `Worker` that will call `cookbookApi.getRecipeList()` and write the data to the database using `cookbookDao.insertList(...)`.
2. Modify the `synchronize()` function in [RecipeListUsecase](/cookbook/data/src/main/kotlin/com/motorro/cookbook/data/recipes/usecase/RecipeListUsecase.kt). 
   Remove the code that directly calls the methods for getting recipes and writing them to the database. Instead, send a request to `WorkManager` for a one-time 
   execution of our `Worker`.
3. For the `recipes` property, we need to implement a flow of `LCE` states. To do this, get a flow of data about the state of `WorkManager` tasks and our `Worker`. 
   Use one of the methods for getting task status, for example, `getWorkInfosByTagFlow`. Transform the state data flow so that:
   - `WorkInfo.State.RUNNING` results in an `LceState.Loading` state on output.
   - `WorkInfo.State.FAILED` results in an `LceState.Error` state on output.
   - All other input states result in an `LceState.Content` state on output.

### 3. Connecting a Periodic Background List Update Task

In the previous task, we refactored the loading of the recipe list from the server using a one-time `WorkManager` task. In this task, we will improve our 
application and automatically update the list in a background task. For this, I propose the following scenario:

- Schedule a periodic task in `WorkManager` when the user logs in.
- Cancel the periodic task when the user logs out.

Sequence of steps:

1. Choose a way to track user login and logout events. This could be some kind of session monitor or a plugin for our [SessionManager](/cookbook/domain/src/main/kotlin/com/motorro/cookbook/domain/session/SessionManagerImpl.kt).
2. When the user logs in, schedule a unique periodic task using the synchronization `Worker` from the previous task.
3. When the user logs out, cancel the task from the scheduler using one of the `WorkManager` methods, for example, `cancelAllWorkByTag()`.

### 4. Background Deletion of a Recipe from the Server

In this task, we will refactor our [recipe deletion use case](/cookbook/data/src/main/kotlin/com/motorro/cookbook/data/recipes/usecase/DeleteRecipeUsecase.kt).
We will use `WorkManager` to make the background work more reliable.

Sequence of steps:

1. Create a new `Worker` and move the code from the use case for accessing the server into it - the call to the `cookbookApi.deleteRecipe(recipeId)` method.
2. To complete the task, we need to pass the recipe identifier to the `Worker`. Use the `setInputData` method of the `WorkManager` task builder to pass 
   the identifier from the use case.
3. Read this value from the input data in the `doWork()` method.
4. Replace the explicit API call in the recipe deletion use case with scheduling a unique task in `WorkManager`.

### 5. Creating a New Recipe Using a Composite Task

Creating a new recipe on the server consists of one or two steps:

1. Creating the basic recipe by calling the method: `addRecipe(recipe: Recipe): Result<Recipe>`
2. If the recipe contains an image, an additional step to upload the image for the recipe: `uploadRecipeImage(recipeId: Uuid, imageUri: Uri): Result<ImageUpload>`

Currently, both of these methods are explicitly called in [AddRecipeUsecase](/cookbook/data/src/main/kotlin/com/motorro/cookbook/data/recipes/usecase/AddRecipeUsecase.kt).
Let's refactor this use case to use a composite `WorkManager` task.

Sequence of steps:

1. Create a `Worker` to upload the main part of the recipe.
2. Create a `Worker` to upload the image.
3. Modify the recipe creation use case so that instead of an explicit API call, a composite task is created in `WorkManager` for both of the above points.
4. Additionally, start list synchronization using the `Worker` from the second task upon successful completion of the main operation.

### Conclusion

This practical work is dedicated to improving the "Cookbook" application by transferring background operations (list synchronization, recipe creation, 
and deletion) to use `WorkManager`.

Our main achievements:

*   **WorkManager Initialization:** Integration of `WorkManager` with Hilt for dependency injection and its configuration using `App Startup`.
*   **Recipe List Synchronization:** Refactoring the recipe list loading to use a one-time `WorkManager` task, which increases reliability and improves error 
    handling. In this task, we also learned how to get the execution status of `WorkManager` tasks.
*   **Periodic List Updates:** Implementation of periodic background updates of the recipe list using `WorkManager`, triggered on user login and canceled 
    on logout.
*   **Background Recipe Deletion:** Transferring recipe deletion to a reliable `WorkManager` task, passing recipe identifiers as input data.
*   **Composite Task for Recipe Creation:** Implementation of a composite `WorkManager` task for creating new recipes, which can include two steps: creating 
    the basic recipe and, if necessary, uploading an image. Successful recipe creation also triggers a list synchronization task.

Overall, the practical work significantly improves the application's background processing by using `WorkManager` for more reliable, stable, and fault-tolerant 
operations. Now our application is finished and ready for release.
