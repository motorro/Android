# Вебинар 35. Фоновая работа

***[Read in English](#webinar-35-background-work)***

1. Фоновая работа в Android. Общие понятия.
2. Started и Bound сервисы.
3. Периодические задачи в WorkManager
4. Составные задачи в WorkManager

## Вопросы для самопроверки:

1. В чем принципиальное различие между `Service` и `Thread` с точки зрения управления жизненным циклом и потоком выполнения? 
   [] `Service` является компонентом Android, работающим в основном потоке приложения по умолчанию и предназначенным для выполнения длительных 
   операций без взаимодействия с UI. Его жизненный цикл управляется системой Android. `Thread` — это низкоуровневая конструкция для 
   выполнения кода в отдельном потоке, не связанная напрямую с жизненным циклом компонентов Android и не имеющая собственного UI.
   [] `Service` всегда выполняется в отдельном фоновом потоке, созданном системой, что освобождает основной поток приложения. `Thread` же 
   всегда запускается в основном потоке и блокирует UI, пока не завершится.
   [] `Service` и `Thread` по своей сути одно и то же, просто `Service` имеет удобный API для управления фоновой работой, но оба они 
   управляются JVM, а не системой Android.
2. Для какой задачи вы выберете `Foreground Service`, и какое основное требование система Android предъявляет к его работе?
   [] `Foreground Service` используется для любых задач, требующих повышенного приоритета, даже если они кратковременны. Система требует,
   чтобы он показывал уведомление, которое пользователь может смахнуть в любой момент.
   [] `Foreground Service` выбирается для задач, которые должны быть заметны пользователю и продолжаться, даже если приложение не активно 
   (например, воспроизведение музыки, отслеживание местоположения). Основное требование — наличие постоянного уведомления, видимого 
   пользователю, сообщающего о работе сервиса.
   [] `Foreground Service` предназначен для выполнения длительных фоновых операций, которые не требуют прямого взаимодействия с пользователем 
   и не должны отображать уведомление, чтобы не отвлекать его.
3. Какую роль выполняет метод `onBind()` в компоненте `Service` и в каком случае он вызывается?
   [] `onBind()` вызывается сразу после `onCreate()` для всех типов сервисов, чтобы подготовить сервис к приему команд, даже если к нему
   никто не привязывается.
   [] Метод `onBind()` вызывается только в `Started Service` для инициализации фоновой работы, возвращая `null`, так как привязка в таком 
   сервисе невозможна.
   [] Метод `onBind()` вызывается, когда другой компонент приложения (например, `Activity`) пытается привязаться к `Service`, чтобы
   взаимодействовать с ним. Он возвращает `IBinder` объект, который является интерфейсом для взаимодействия клиента с сервисом.
4. В какой ситуации для выполнения фоновой задачи вы предпочтете `WorkManager`, а не `Foreground Service`? Приведите пример для каждого. 
   [] `WorkManager` следует использовать, когда требуется немедленное выполнение задачи и отображение прогресса в UI, например, для 
   загрузки большого файла с индикатором. `Foreground Service` подходит для очень коротких операций без взаимодействия с пользователем.
   [] `WorkManager` предпочтителен для отложенных, гарантированных и фоновых задач, которые не требуют немедленного выполнения или не
   должны быть постоянно видимы пользователю (например, синхронизация данных раз в день, загрузка изображений в фоновом режиме).
   `Foreground Service` — для задач, требующих немедленного и непрерывного выполнения, видимых пользователю
   (например, стриминг аудио).
   [] `WorkManager` идеален для любых фоновых задач, которые должны быть выполнены немедленно и с максимальным приоритетом. 
   `Foreground Service` применяется только для задач, которые могут быть отложены и не критичны для пользователя, но все же требуют 
   фоновой работы.
5. Как с помощью `WorkManager` организовать последовательное выполнение задач, где сначала загружаются несколько файлов, а затем отправляется 
   итоговый отчет с результатами их загрузки? 
   [] `WorkManager` не поддерживает прямое последовательное выполнение нескольких независимых задач. Вместо этого нужно запускать все 
   задачи по загрузке параллельно, а затем вручную инициировать отправку отчета после их завершения.
   [] Для последовательного выполнения задач в `WorkManager` можно использовать `WorkManager.beginWith()`, затем `then()` для добавления 
   последующих задач. Например: `WorkManager.getInstance(context).beginWith(uploadFileAWorkRequest).then(uploadFileBWorkRequest).
   then(sendReportWorkRequest).enqueue()`.
   [] Для такой задачи достаточно создать один `Worker`, который внутри себя будет по очереди вызывать методы для загрузки файлов, а затем 
   метод для отправки отчета, без использования цепочек `WorkManager`.

---

# Webinar 35. Background Work

1. Background work in Android. General concepts.
2. Started and Bound services.
3. Periodic tasks in WorkManager
4. Composite tasks in WorkManager

## Self-check questions:

1. What is the fundamental difference between a `Service` and a `Thread` in terms of lifecycle and execution flow management?
   [] A `Service` is an Android component that runs on the main thread of the application by default and is intended for performing long-running operations 
   without UI interaction. Its lifecycle is managed by the Android system. A `Thread` is a low-level construct for executing code in a separate thread, 
   not directly related to the lifecycle of Android components, and has no UI of its own.
   [] A `Service` always runs in a separate background thread created by the system, which frees up the application's main thread. A `Thread`, on the other hand, 
   always starts on the main thread and blocks the UI until it completes.
   [] A `Service` and a `Thread` are essentially the same thing, just that `Service` has a convenient API for managing background work, but both are managed by 
   the JVM, not the Android system.
2. For what task would you choose a `Foreground Service`, and what is the main requirement the Android system imposes on its operation?
   [] A `Foreground Service` is used for any task that requires a high priority, even if it is short-lived. The system requires it to show a notification that 
   the user can dismiss at any time.
   [] A `Foreground Service` is chosen for tasks that must be visible to the user and continue even if the application is not active (e.g., playing music, 
   tracking location). The main requirement is the presence of a persistent notification, visible to the user, informing about the service's operation.
   [] A `Foreground Service` is intended for performing long-running background operations that do not require direct user interaction and should not display 
   a notification so as not to distract the user.
3. What role does the `onBind()` method play in a `Service` component and in what case is it called?
   [] `onBind()` is called immediately after `onCreate()` for all types of services to prepare the service to receive commands, even if no one binds to it.
   [] The `onBind()` method is called only in a `Started Service` to initialize background work, returning `null` because binding is not possible 
   in such a service.
   [] The `onBind()` method is called when another application component (e.g., an `Activity`) tries to bind to the `Service` to interact with it. It returns 
   an `IBinder` object, which is the interface for the client's interaction with the service.
4. In what situation would you prefer `WorkManager` over a `Foreground Service` for performing a background task? Provide an example for each.
   [] `WorkManager` should be used when immediate task execution and UI progress display are required, for example, for downloading a large file with an indicator. 
   `Foreground Service` is suitable for very short operations without user interaction.
   [] `WorkManager` is preferable for deferred, guaranteed, and background tasks that do not require immediate execution or should not be constantly visible 
   to the user (e.g., syncing data once a day, downloading images in the background). `Foreground Service` is for tasks that require immediate and continuous 
   execution, visible to the user (e.g., audio streaming).
   [] `WorkManager` is ideal for any background task that must be executed immediately and with maximum priority. `Foreground Service` is used only for tasks 
   that can be deferred and are not critical to the user, but still require background work.
5. How to organize the sequential execution of tasks using `WorkManager`, where several files are first downloaded, and then a final report with the results 
   of their download is sent?
   [] `WorkManager` does not support the direct sequential execution of several independent tasks. Instead, you need to run all download tasks in parallel 
   and then manually initiate the report sending after they are completed.
   [] For sequential execution of tasks in `WorkManager`, you can use `WorkManager.beginWith()`, then `then()` to add subsequent tasks. For example: 
   `WorkManager.getInstance(context).beginWith(uploadFileAWorkRequest).then(uploadFileBWorkRequest).then(sendReportWorkRequest).enqueue()`.
   [] For such a task, it is enough to create a single `Worker` that will sequentially call methods for downloading files and then a method for sending a report, 
   without using `WorkManager` chains.
