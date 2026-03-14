# Вебинар 31. Compose state

***[Read in English](#webinar-31-compose-state)***

1. Состояние в Compose
2. Управление состоянием
3. Эффективная работа с данными и рекомпозиция
4. Сайд-эффекты в Jetpack Compose

## Вопросы для самопроверки:

1. Что такое MutableState и как оно используется в Jetpack Compose?
   [] MutableState - это класс, который позволяет хранить неизменяемые данные в Compose.
   [] MutableState - это обертка для значения, которое может изменяться, и при этом Compose автоматически отслеживает эти изменения для запуска рекомпозиции UI.
   [] MutableState - это функция для выполнения асинхронных операций в Compose.
2. В чём разница между `remember` и `rememberSaveable`? Когда следует использовать каждый из них?
   [] `remember` сохраняет состояние только до тех пор, пока Composable находится в дереве композиции, а `rememberSaveable` сохраняет состояние при изменениях конфигурации (например, поворот экрана) и уничтожении процесса, используя `SavedStateHandle`. `remember` используется для временного состояния, `rememberSaveable` - для состояния, которое должно переживать изменения конфигурации и процессы.
   [] `remember` сохраняет состояние только до тех пор, пока Composable находится в дереве композиции (во время рекомпозиции), а `rememberSaveable` сохраняет 
      состояние при изменениях конфигурации (например, поворот экрана) и уничтожении процесса, используя `SavedStateHandle`. `remember` следует использовать 
      для временного состояния UI, которое не критично при потере (например, состояние анимации), а `rememberSaveable` - для состояния, которое должно 
      сохраняться (например, введенный текст в поле ввода).
   [] `remember` используется для сохранения больших объемов данных, а `rememberSaveable` - для небольших.
3. Как `collectAsStateWithLifecycle` помогает безопасно работать с потоками (Flow) в Compose?
   [] `collectAsStateWithLifecycle` собирает значения из Flow только тогда, когда Composable находится в состоянии `STARTED` или выше, и прекращает сбор, 
       когда Composable переходит в состояние `STOPPED`. Это предотвращает утечки памяти и ненужную работу, так как подписка активна только тогда, 
       когда UI виден.
   [] `collectAsStateWithLifecycle` полностью блокирует основной поток, пока не получит все данные из Flow.
   [] `collectAsStateWithLifecycle` используется только для работы с базами данных, а не с потоками.
4. Для чего используется `derivedStateOf` и как он помогает оптимизировать рекомпозицию?
   [] `derivedStateOf` используется для создания новых изменяемых состояний.
   [] `derivedStateOf` используется для создания состояния, которое является производным от других состояний. Оно помогает оптимизировать рекомпозицию, так как 
      рекомпозиция Composable, использующего `derivedStateOf`, будет происходить только тогда, когда изменится *результат* вычисления, а не при каждом изменении 
      одного из исходных состояний. Это позволяет избежать избыточных рекомпозиций.
   [] `derivedStateOf` автоматически сохраняет состояние приложения при повороте экрана.
5. Какие сайд-эффекты существуют в Jetpack Compose и в каких случаях следует использовать `LaunchedEffect` или `DisposableEffect`?
   [] Сайд-эффекты в Jetpack Compose - это любые операции, которые не приводят к изменению UI.
   [] Сайд-эффекты - это изменения состояния приложения, которые происходят за пределами области Composable функций (например, сетевые запросы, работа с базой 
      данных, подписки на внешние источники). `LaunchedEffect` используется для запуска корутин, которые должны выполняться, пока Composable находится 
      в композиции (например, запрос данных, запуск анимации). `DisposableEffect` используется для сайд-эффектов, которые требуют очистки при выходе из 
      композиции (например, подписка на слушателя, открытие/закрытие ресурсов).
   [] Сайд-эффекты в Jetpack Compose используются для создания кастомных анимаций.

---

# Webinar 31. Compose state

1. State in Compose
2. State management
3. Efficient data handling and recomposition
4. Side-effects in Jetpack Compose

## Self-check questions:

1. What is MutableState and how is it used in Jetpack Compose?
   [] MutableState is a class that allows storing immutable data in Compose.
   [] MutableState is a wrapper for a value that can change, and Compose automatically tracks these changes to trigger UI recomposition.
   [] MutableState is a function for performing asynchronous operations in Compose.
2. What is the difference between `remember` and `rememberSaveable`? When should each be used?
   [] `remember` saves the state only as long as the Composable is in the composition tree, while `rememberSaveable` saves the state across configuration 
   changes (e.g., screen rotation) and process death, using `SavedStateHandle`. `remember` is used for temporary state, `rememberSaveable` - for state that must
   survive configuration changes and processes.
   [] `remember` saves state only as long as the Composable is in the composition tree (during recomposition), while `rememberSaveable` saves state across 
   configuration changes (e.g., screen rotation) and process death, using `SavedStateHandle`. `remember` should be used for temporary UI state 
   that is not critical to lose (e.g., animation state), while `rememberSaveable` is for state that must be preserved (e.g., text entered in an input field).
   [] `remember` is used for saving large amounts of data, and `rememberSaveable` for small amounts.
3. How does `collectAsStateWithLifecycle` help to work safely with Flows in Compose?
   [] `collectAsStateWithLifecycle` collects values from a Flow only when the Composable is in the `STARTED` state or higher, and stops collecting when 
   the Composable enters the `STOPPED` state. This prevents memory leaks and unnecessary work, as the subscription is active only when the UI is visible.
   [] `collectAsStateWithLifecycle` completely blocks the main thread until it receives all data from the Flow.
   [] `collectAsStateWithLifecycle` is used only for working with databases, not with flows.
4. What is `derivedStateOf` used for and how does it help optimize recomposition?
   [] `derivedStateOf` is used to create new mutable states.
   [] `derivedStateOf` is used to create a state that is derived from other states. It helps optimize recomposition because a Composable using `derivedStateOf` 
   will only recompose when the *result* of the calculation changes, not on every change of one of the source states. This helps avoid unnecessary recompositions.
   [] `derivedStateOf` automatically saves the application state on screen rotation.
5. What side-effects exist in Jetpack Compose, and in which cases should `LaunchedEffect` or `DisposableEffect` be used?
   [] Side-effects in Jetpack Compose are any operations that do not lead to a UI change.
   [] Side-effects are changes to the application's state that occur outside the scope of Composable functions (e.g., network requests, database operations, 
   subscriptions to external sources). `LaunchedEffect` is used to launch coroutines that should run as long as the Composable is in the composition 
   (e.g., fetching data, starting an animation). `DisposableEffect` is used for side-effects that require cleanup upon leaving the composition (e.g., subscribing 
   to a listener, opening/closing resources).
   [] Side-effects in Jetpack Compose are used to create custom animations.
