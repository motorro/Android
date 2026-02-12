# Вебинар 17. Многопоточное программирование

***[Read in English](#webinar-17-multithreaded-programming)***

1. Процессы и потоки
2. Необходимость многопоточности в Android
3. Диспетчеризация потоков и задач
4. Диспетчеризация задач в Android

## Вопросы для самопроверки

1. Что такое процесс?
   [] Процесс - это экземпляр запущенной программы, который имеет свое собственное адресное пространство, ресурсы и по крайней мере один поток выполнения.
   [] Процесс - это легкий поток выполнения внутри программы.
   [] Процесс - это часть операционной системы, отвечающая за управление памятью.
2. Что такое поток?
   [] Поток - это независимая программа, которая выполняется параллельно с другими программами.
   [] Поток - это наименьшая единица выполнения, которая может быть запланирована операционной системой. Несколько потоков могут существовать в одном процессе 
      и делить его ресурсы.
   [] Поток - это механизм для хранения временных данных.
3. Чем отличаются процессы и потоки?
   [] Процессы и потоки - это синонимы, обозначающие одно и то же.
   [] Процессы независимы друг от друга, имеют свое собственное адресное пространство и более тяжеловесны. Потоки существуют внутри процесса, делят его ресурсы 
      и являются более легковесными, чем процессы.
   [] Процессы используются для выполнения коротких задач, а потоки - для длительных.
4. Почему мы используем многопоточность в Android?
   [] Многопоточность в Android используется исключительно для сокращения размера APK-файла.
   [] Многопоточность используется для выполнения длительных операций (например, сетевые запросы, работа с базой данных, сложные вычисления) в фоновых потоках, 
      чтобы избежать блокировки основного (UI) потока, предотвращая зависание приложения (ANR) и обеспечивая отзывчивый пользовательский интерфейс.
   [] Многопоточность в Android нужна только для создания анимаций.
5. Какие существуют способы создания потоков в Kotlin?
   [] Только с использованием класса `Thread` и его метода `start()`.
   [] С использованием класса `Thread`, `Runnable` интерфейса, `HandlerThread`, `Executors` для пулов потоков, а также корутин Kotlin (с `launch` или `async`) 
      для асинхронного программирования.
   [] Только с помощью корутин Kotlin.
6. Как снизить накладные расходы на создание потоков?
   [] Снизить накладные расходы можно, создавая как можно больше потоков.
   [] Используя пулы потоков (`ExecutorService`) для переиспользования уже созданных потоков, вместо того чтобы создавать новые для каждой задачи. 
   [] Накладные расходы на создание потоков невозможно снизить.
7. Почему View должен обновляться только из главного потока?
   [] Обновление View из любого потока абсолютно безопасно и поддерживается Android.
   [] Обновление View должно происходить только из главного (UI) потока, потому что компоненты UI не являются потокобезопасными. Прямой доступ к UI-элементам 
      из фонового потока может привести к непредсказуемому поведению, гонкам данных и крашам приложения. Android UI-инструментарий не спроектирован 
      для многопоточного доступа.
   [] Обновление View из главного потока необходимо для экономии заряда батареи.
8. Что такое Looper и Handler?
   [] Looper и Handler - это компоненты для работы с сетью.
   [] Looper - это класс, который циклически извлекает задачи из очереди сообщений (MessageQueue) и отправляет их на обработку соответствующим Handler'ам. 
      Handler - это класс, который позволяет отправлять и обрабатывать объекты `Message` и `Runnable` с определенным `Looper`'ом, обычно связанным с конкретным 
      потоком (например, главным потоком UI).
   [] Looper и Handler - это инструменты для работы с базами данных.

---

# Webinar 17. Multithreaded Programming

1. Processes and threads
2. The need for multithreading in Android
3. Dispatching threads and tasks
4. Task dispatching in Android

## Self-check questions

1. What is a process?
   [] A process is an instance of a running program that has its own address space, resources, and at least one thread of execution.
   [] A process is a lightweight thread of execution within a program.
   [] A process is a part of the operating system responsible for memory management.
2. What is a thread?
   [] A thread is an independent program that runs in parallel with other programs.
   [] A thread is the smallest unit of execution that can be scheduled by the operating system. Multiple threads can exist within a single process and share 
      its resources.
   [] A thread is a mechanism for storing temporary data.
3. What is the difference between processes and threads?
   [] Processes and threads are synonyms for the same thing.
   [] Processes are independent of each other, have their own address space, and are more heavyweight. Threads exist within a process, share its resources, 
      and are more lightweight than processes.
   [] Processes are used for short tasks, while threads are used for long tasks.
4. Why do we use multithreading in Android?
   [] Multithreading in Android is used exclusively to reduce the size of the APK file.
   [] Multithreading is used to perform long-running operations (e.g., network requests, database operations, complex calculations) on background threads 
      to avoid blocking the main (UI) thread, preventing the application from freezing (ANR) and ensuring a responsive user interface.
   [] Multithreading in Android is only needed for creating animations.
5. What are the ways to create threads in Kotlin?
   [] Only using the `Thread` class and its `start()` method.
   [] Using the `Thread` class, the `Runnable` interface, `HandlerThread`, `Executors` for thread pools, as well as Kotlin coroutines (with `launch` or `async`) 
      for asynchronous programming.
   [] Only with Kotlin coroutines.
6. How to reduce the overhead of creating threads?
   [] You can reduce overhead by creating as many threads as possible.
   [] By using thread pools (`ExecutorService`) to reuse already created threads, instead of creating new ones for each task.
   [] It is impossible to reduce the overhead of creating threads.
7. Why should a View only be updated from the main thread?
   [] Updating a View from any thread is completely safe and supported by Android.
   [] A View should only be updated from the main (UI) thread because UI components are not thread-safe. Direct access to UI elements from a background thread 
      can lead to unpredictable behavior, data races, and application crashes. The Android UI toolkit is not designed for multithreaded access.
   [] Updating a View from the main thread is necessary to save battery power.
8. What are Looper and Handler?
   [] Looper and Handler are components for networking.
   [] Looper is a class that cyclically retrieves tasks from a message queue (MessageQueue) and sends them to the appropriate Handlers for processing. Handler 
      is a class that allows you to send and process `Message` and `Runnable` objects with a specific `Looper`, usually associated with a specific thread 
      (e.g., the main UI thread).
   [] Looper and Handler are tools for working with databases.
