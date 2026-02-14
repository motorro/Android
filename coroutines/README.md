# Вебинар 18. Kotlin Coroutines

***[Read in English](#webinar-18-kotlin-coroutines)***

1. Coroutines - решение для асинхронных задач
2. Базовые понятия и синтаксис
3. Конструкторы корутин и задачи
4. CoroutineContext и CoroutineScope
5. Обработка ошибок

## Вопросы для самопроверки:

1. Что такое корутины и для чего они используются?
   [] Корутины - это легковесные потоки, используемые для параллельного выполнения кода.
   [] Корутины - это способ написания асинхронного, неблокирующего кода в последовательном стиле, что упрощает работу с асинхронными операциями, такими как 
      сетевые запросы или операции с базой данных, без блокировки основного потока.
   [] Корутины - это вид Android-сервисов, работающих в фоновом режиме.
2. Что такое suspend-функции и как они работают?
   [] Suspend-функции - это обычные функции, которые могут быть вызваны только из Java-кода.
   [] Suspend-функции - это функции, которые могут приостанавливать свое выполнение без блокировки потока, в котором они запущены, а затем возобновляться позже. 
      Они могут быть вызваны только из других suspend-функций или из корутин.
   [] Suspend-функции - это функции, которые всегда выполняются в отдельном фоновом потоке.
3. Как создать корутину и запустить ее выполнение?
   [] Корутина создается путем объявления класса, наследующего от `Coroutine` и переопределения метода `run()`.
   [] Корутина создается и запускается с использованием билдеров корутин, таких как `launch` (для fire-and-forget задач) или `async` (для задач, возвращающих 
      результат), в рамках `CoroutineScope`.
   [] Корутина создается автоматически при запуске приложения Android.
4. Что такое CoroutineContext и CoroutineScope?
   [] CoroutineContext - это менеджер жизненного цикла Activity, а CoroutineScope - это класс для создания View.
   [] CoroutineContext - это набор элементов (например, Dispatcher, Job, CoroutineName), которые определяют поведение корутины. CoroutineScope определяет 
      жизненный цикл корутин, обеспечивая их структурированное выполнение и отмену.
   [] CoroutineContext - это база данных, а CoroutineScope - это способ доступа к ней.
5. Как обрабатываются ошибки в корутинах?
   [] Ошибки в корутинах всегда игнорируются, чтобы избежать крашей приложения.
   [] Ошибки в корутинах обрабатываются с помощью стандартных блоков `try-catch`, `CoroutineExceptionHandler` (для корневых корутин) или `SupervisorJob` 
      (для изоляции ошибок среди дочерних корутин).
   [] Ошибки в корутинах перехватываются только системой Android и не доступны для обработки разработчику.

---

# Webinar 18. Kotlin Coroutines

1. Coroutines - a solution for asynchronous tasks
2. Basic concepts and syntax
3. Coroutine builders and jobs
4. CoroutineContext and CoroutineScope
5. Error handling

## Self-check questions:

1. What are coroutines and what are they used for?
   [] Coroutines are lightweight threads used for parallel code execution.
   [] Coroutines are a way of writing asynchronous, non-blocking code in a sequential style, which simplifies working with asynchronous operations such 
      as network requests or database operations without blocking the main thread.
   [] Coroutines are a type of Android service that runs in the background.
2. What are suspend functions and how do they work?
   [] Suspend functions are regular functions that can only be called from Java code.
   [] Suspend functions are functions that can suspend their execution without blocking the thread they are running on, and then resume later. They can only 
      be called from other suspend functions or from coroutines.
   [] Suspend functions are functions that always execute on a separate background thread.
3. How to create a coroutine and run it?
   [] A coroutine is created by declaring a class that inherits from `Coroutine` and overriding the `run()` method.
   [] A coroutine is created and launched using coroutine builders such as `launch` (for fire-and-forget tasks) or `async` (for tasks that return a result), 
      within a `CoroutineScope`.
   [] A coroutine is created automatically when an Android application starts.
4. What are CoroutineContext and CoroutineScope?
   [] CoroutineContext is an Activity lifecycle manager, and CoroutineScope is a class for creating Views.
   [] CoroutineContext is a set of elements (e.g., Dispatcher, Job, CoroutineName) that define the behavior of a coroutine. CoroutineScope defines the lifecycle
      of coroutines, ensuring their structured execution and cancellation.
   [] CoroutineContext is a database, and CoroutineScope is a way to access it.
5. How are errors handled in coroutines?
   [] Errors in coroutines are always ignored to avoid application crashes.
   [] Errors in coroutines are handled using standard `try-catch` blocks, `CoroutineExceptionHandler` (for root coroutines), or `SupervisorJob` (to isolate 
      errors among child coroutines).
   [] Errors in coroutines are only caught by the Android system and are not available for the developer to handle.
