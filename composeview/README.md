# Вебинар 30. Compose View

***[Read in English](#webinar-30-compose-view)***

1. Концепция декларативного интерфейса
2. Механика работы Jetpack Compose
3. Композиция, рекомпозиция
4. Верстка Compose интерфейса
5. Архитектура Compose приложения

## Вопросы для самопроверки:

1. В чем основное отличие декларативного подхода к построению UI от императивного?
   [] В декларативном подходе UI описывается как текущее состояние данных, а в императивном UI изменяется путем прямых манипуляций с его элементами.
   [] Декларативный подход фокусируется на том, *как* изменить UI, а императивный - на том, *что* должно быть отображено.
   [] Декларативный подход использует только XML для создания UI, а императивный - только код.
2. Объясните своими словами, что такое композиция и рекомпозиция в Jetpack Compose?
   [] Композиция - это процесс создания нового UI каждый раз, когда данные меняются. Рекомпозиция - это оптимизация, которая предотвращает создание всего UI заново.
   [] Композиция - это процесс построения пользовательского интерфейса путем вызова Composable функций. Рекомпозиция - это процесс повторного выполнения 
      Composable функций, когда изменяются данные, от которых они зависят, чтобы обновить UI, при этом Compose старается обновить только те части, которые 
      действительно изменились.
   [] Композиция - это создание фоновых потоков для выполнения задач. Рекомпозиция - это остановка этих потоков.
3. Какие основные layout-компоненты предоставляет Jetpack Compose для верстки интерфейса? 
   [] LinearLayout, RelativeLayout, FrameLayout.
   [] Activity, Fragment, Service.
   [] `Column` (для вертикального расположения), `Row` (для горизонтального расположения), `Box` (для наложения элементов друг на друга), 
      `ConstraintLayout` (для гибкого позиционирования с ограничениями), `LazyColumn`, `LazyRow` (для эффективной работы со списками).
4. Что такое `Modifier` и для чего он служит?
   [] `Modifier` - это класс, который управляет жизненным циклом Composable функций.
   [] `Modifier` - это коллекция настроек, применяемых к Composable функции для изменения ее внешнего вида, поведения или макета. Он служит для добавления 
      отступов, размеров, фонов, обработки событий ввода, добавления кликов и многого другого.
   [] `Modifier` - это инструмент для отладки ошибок в Compose-приложениях.
5. В чем разница между элементами `Column` и `LazyColumn`
   [] `Column` и `LazyColumn` - это одно и то же, просто `LazyColumn` использует отложенную инициализацию.
   [] `Column` создает и размещает все свои дочерние элементы сразу, даже если они не видны на экране. `LazyColumn` (и `LazyRow`) оптимизированы для 
      отображения больших списков, так как они создают и размещают дочерние элементы только тогда, когда они становятся видимыми на экране, переиспользуя 
      Viewport для экономии ресурсов.
   [] `Column` используется для горизонтального расположения, а `LazyColumn` - для вертикального.

---

# Webinar 30. Compose View

1. The concept of a declarative interface
2. The mechanics of Jetpack Compose
3. Composition, recomposition
4. Composing an interface
5. Architecture of a Compose application

## Self-check questions:

1. What is the main difference between a declarative and an imperative approach to building a UI?
   [] In a declarative approach, the UI is described as the current state of the data, while in an imperative approach, the UI is changed by directly 
      manipulating its elements.
   [] A declarative approach focuses on *how* to change the UI, while an imperative approach focuses on *what* should be displayed.
   [] A declarative approach uses only XML to create a UI, while an imperative approach uses only code.
2. Explain in your own words what composition and recomposition are in Jetpack Compose.
   [] Composition is the process of creating a new UI every time the data changes. Recomposition is an optimization that prevents the entire UI 
      from being recreated.
   [] Composition is the process of building a user interface by calling Composable functions. Recomposition is the process of re-executing Composable functions
      when the data they depend on changes, to update the UI, while Compose tries to update only the parts that have actually changed.
   [] Composition is the creation of background threads to perform tasks. Recomposition is the stopping of these threads.
3. What are the main layout components provided by Jetpack Compose for interface layout?
   [] LinearLayout, RelativeLayout, FrameLayout.
   [] Activity, Fragment, Service.
   [] `Column` (for vertical arrangement), `Row` (for horizontal arrangement), `Box` (for overlapping elements), `ConstraintLayout` (for flexible positioning 
      with constraints), `LazyColumn`, `LazyRow` (for efficient work with lists).
4. What is a `Modifier` and what is it for?
   [] `Modifier` is a class that manages the lifecycle of Composable functions.
   [] `Modifier` is a collection of settings applied to a Composable function to change its appearance, behavior, or layout. It is used to add padding, sizes, 
      backgrounds, handle input events, add clicks, and much more.
   [] `Modifier` is a tool for debugging errors in Compose applications.
5. What is the difference between `Column` and `LazyColumn` elements?
   [] `Column` and `LazyColumn` are the same thing, just that `LazyColumn` uses lazy initialization.
   [] `Column` creates and places all its child elements at once, even if they are not visible on the screen. `LazyColumn` (and `LazyRow`) are optimized 
      for displaying large lists, as they create and place child elements only when they become visible on the screen, reusing the Viewport to save resources.
   [] `Column` is used for horizontal arrangement, and `LazyColumn` is for vertical.
