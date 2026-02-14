# Вебинар 19. Kotlin Flow

***[Read in English](#webinar-19-kotlin-flow)***

Вебинар состоит из двух модулей:
1. `flow` - основной модуль приложения 
2. `experiments` - модуль для экспериментов с Flow

## Содержание

1. Реактивное программирование и Flow
2. Получение данных и контекст
3. Преобразования и объединения
4. SharedFlow и StateFlow
5. Обработка ошибок

## Вопросы для самопроверки:

1. Что такое реактивное программирование?
   [] Реактивное программирование - это парадигма программирования, ориентированная на потоки данных и распространение изменений. Оно позволяет работать 
      с асинхронными потоками данных, реагируя на изменения и события.
   [] Реактивное программирование - это способ написания синхронного кода для повышения производительности.
   [] Реактивное программирование - это только для работы с UI-элементами.
2. Как мы создаем реактивные потоки в Kotlin?
   [] Реактивные потоки в Kotlin создаются только с помощью `LiveData`.
   [] Реактивные потоки в Kotlin чаще всего создаются с помощью `Flow` из библиотеки Kotlin Coroutines. Можно использовать билдеры `flow`, `flowOf`, `asFlow` 
      для создания холодных потоков, а также `MutableSharedFlow` и `MutableStateFlow` для создания горячих потоков.
   [] Реактивные потоки в Kotlin создаются путем реализации интерфейса `Observable`.
3. Как подписаться на поток и получить данные?
   [] Для подписки на поток нужно вызвать метод `subscribe()` на Flow.
   [] Подписка на поток (коллекция) осуществляется с помощью терминальных операторов, таких как `collect()`. Этот оператор является suspend-функцией и должен 
      быть вызван в корутине.
   [] Подписка на поток происходит автоматически при объявлении Flow.
4. Когда поток начинает выполняться?
   [] Поток начинает выполняться сразу после его объявления.
   [] Холодные потоки (`Flow`) начинают выполняться только тогда, когда на них подписывается коллектор (`collect()`). Горячие потоки (`SharedFlow`, `StateFlow`) 
      начинают выполняться при первом подписчике или сразу, в зависимости от конфигурации.
   [] Поток начинает выполняться только после вызова метода `start()`.
5. Какие операторы преобразования потоков вы знаете?
   [] Только операторы для фильтрации данных, такие как `filter()`.
   [] Операторы преобразования включают `map()`, `filter()`, `onEach()`, `debounce()`, `sample()`, `flatMapConcat()`, `flatMapMerge()`, `zip()`, `combine()`.
   [] Операторы преобразования потоков используются для изменения типа данных потока на другой, не связанный с исходным.
6. Чем отличается SharedFlow от StateFlow?
   [] SharedFlow и StateFlow - это одно и то же, просто разные названия.
   [] SharedFlow - это горячий поток, который отправляет элементы всем своим подписчикам и не имеет начального значения. StateFlow - это горячий поток, который 
      всегда имеет начальное значение, отправляет только уникальные (distinct) обновления состояния своим подписчикам и всегда сохраняет последнее отправленное 
      значение.
   [] SharedFlow может быть только один на приложение, а StateFlow может быть несколько.
7. Как обрабатывать ошибки в потоках?
   [] Ошибки в потоках всегда приводят к краху приложения и не могут быть обработаны.
   [] Ошибки в потоках обрабатываются с помощью оператора `catch()` для перехвата исключений, а также с помощью `try-catch` блоков внутри корутин, подписанных 
      на Flow.
   [] Ошибки в потоках обрабатываются только в главном потоке UI.

---

# Webinar 19. Kotlin Flow

The webinar consists of two modules:
1. `flow` - the main application module
2. `experiments` - a module for experimenting with Flow

## Contents

1. Reactive programming and Flow
2. Data retrieval and context
3. Transformations and combinations
4. SharedFlow and StateFlow
5. Error handling

## Self-check questions:

1. What is reactive programming?
   [] Reactive programming is a programming paradigm focused on data streams and the propagation of change. It allows working with asynchronous data streams by 
      reacting to changes and events.
   [] Reactive programming is a way of writing synchronous code to improve performance.
   [] Reactive programming is only for working with UI elements.
2. How do we create reactive streams in Kotlin?
   [] Reactive streams in Kotlin are created only with `LiveData`.
   [] Reactive streams in Kotlin are most often created using `Flow` from the Kotlin Coroutines library. You can use builders like `flow`, `flowOf`, `asFlow` 
      to create cold streams, and `MutableSharedFlow` and `MutableStateFlow` to create hot streams.
   [] Reactive streams in Kotlin are created by implementing the `Observable` interface.
3. How to subscribe to a stream and receive data?
   [] To subscribe to a stream, you need to call the `subscribe()` method on the Flow.
   [] Subscribing to a stream (collection) is done using terminal operators such as `collect()`. This operator is a suspend function and must be called within 
      a coroutine.
   [] Subscribing to a stream happens automatically when the Flow is declared.
4. When does a stream start executing?
   [] A stream starts executing immediately after it is declared.
   [] Cold streams (`Flow`) start executing only when a collector (`collect()`) subscribes to them. Hot streams (`SharedFlow`, `StateFlow`) start executing with 
      the first subscriber or immediately, depending on the configuration.
   [] A stream starts executing only after calling the `start()` method.
5. What stream transformation operators do you know?
   [] Only operators for filtering data, such as `filter()`.
   [] Transformation operators include `map()`, `filter()`, `onEach()`, `debounce()`, `sample()`, `flatMapConcat()`, `flatMapMerge()`, `zip()`, `combine()`.
   [] Stream transformation operators are used to change the data type of a stream to another, unrelated to the original.
6. What is the difference between SharedFlow and StateFlow?
   [] SharedFlow and StateFlow are the same thing, just different names.
   [] SharedFlow is a hot stream that sends elements to all its subscribers and has no initial value. StateFlow is a hot stream that always has an initial value, 
      sends only unique (distinct) state updates to its subscribers, and always retains the last sent value.
   [] There can be only one SharedFlow per application, while there can be multiple StateFlows.
7. How to handle errors in streams?
   [] Errors in streams always cause the application to crash and cannot be handled.
   [] Errors in streams are handled using the `catch()` operator to intercept exceptions, as well as using `try-catch` blocks inside coroutines subscribed to 
      the Flow.
   [] Errors in streams are handled only on the main UI thread.
