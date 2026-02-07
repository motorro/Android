# Вебинар 32. StateMachine

***[Read in English](#webinar-32-statemachine)***

1. Узкие места официально рекомендуемой архитектуры и [Jetpack Navigation](https://developer.android.com/guide/navigation/use-graph/conditional)
2. Navigation Inversion - смена традиционного подхода к навигации и архитектуре в приложении
3. StateMachine - вариант простой и доступной UDF-архитектуры

## Вопросы для самопроверки:

1. В чем трудности навигации Jetpack Compose изнутри `ViewModel`?
   [] ViewModel не может взаимодействовать с Composable функциями.
   [] ViewModel не имеет прямого доступа к `NavController` или `Context`, что затрудняет прямую навигацию. Она должна делегировать события навигации 
      слою UI (Composable) через состояния или одноразовые события.
   [] Jetpack Compose не поддерживает навигацию из ViewModel.
2. В чем проблема поддерживаемости монолитных ViewModel, обслуживающих несколько экранов?
   [] Монолитные ViewModel потребляют слишком много оперативной памяти.
   [] Монолитные ViewModel становятся слишком большими, сложными для понимания, тестирования и модификации, так как они содержат логику для множества экранов, 
      что приводит к сильной связанности и затрудняет повторное использование кода.
   [] Монолитные ViewModel автоматически блокируют основной поток UI.
3. В чем основные черты архитектуры MVI для внешнего клиента `ViewModel`? Как мы передаем действия пользователей и получаем обновления экрана?
   [] В MVI ViewModel напрямую манипулирует UI-элементами.
   [] MVI (Model-View-Intent) характеризуется однонаправленным потоком данных: пользователи отправляют действия (Intent) во ViewModel, ViewModel обрабатывает 
      их и генерирует новое состояние (State), которое затем отображается во View. View только отображает состояние и отправляет Intent.
   [] В MVI View напрямую изменяет модель данных.
4. В чем заключается принцип паттерна StateMachine, и как он относится к архитектуре MVI?
   [] Паттерн StateMachine используется для автоматической генерации UI-кода.
   [] Паттерн StateMachine описывает систему, которая может находиться в одном из конечного числа состояний и переходить между ними в ответ на события, 
      при этом каждый переход определен. В MVI StateMachine используется внутри ViewModel для управления сложной бизнес-логикой и генерации нового состояния 
      на основе входящих Intent и текущего состояния, обеспечивая предсказуемость.
   [] Паттерн StateMachine предназначен для сохранения состояния приложения в базе данных.

---
   
# Webinar 32. StateMachine

1.  Bottlenecks of the officially recommended architecture and [Jetpack Navigation](https://developer.android.com/guide/navigation/use-graph/conditional)
2.  Navigation Inversion - changing the traditional approach to navigation and architecture in the application
3.  StateMachine - a simple and accessible UDF architecture option

## Self-check questions:

1.  What are the difficulties of navigating with Jetpack Compose from within a `ViewModel`?
    [] A ViewModel cannot interact with Composable functions.
    [] A ViewModel does not have direct access to `NavController` or `Context`, which complicates direct navigation. It must delegate navigation events to the 
       UI layer (Composable) through states or one-time events.
    [] Jetpack Compose does not support navigation from a ViewModel.
2.  What is the problem with the maintainability of monolithic ViewModels that serve multiple screens?
    [] Monolithic ViewModels consume too much RAM.
    [] Monolithic ViewModels become too large, difficult to understand, test, and modify, as they contain logic for multiple screens, which leads to tight 
       coupling and hinders code reuse.
    [] Monolithic ViewModels automatically block the main UI thread.
3.  What are the main features of the MVI architecture for an external `ViewModel` client? How do we pass user actions and receive screen updates?
    [] In MVI, the ViewModel directly manipulates UI elements.
    [] MVI (Model-View-Intent) is characterized by a unidirectional data flow: users send actions (Intent) to the ViewModel, the ViewModel processes them and
       generates a new state (State), which is then displayed in the View. The View only displays the state and sends Intents.
    [] In MVI, the View directly modifies the data model.
4.  What is the principle of the StateMachine pattern, and how does it relate to the MVI architecture?
    [] The StateMachine pattern is used for automatic UI code generation.
    [] The StateMachine pattern describes a system that can be in one of a finite number of states and transition between them in response to events, with each 
       transition being defined. In MVI, a StateMachine is used inside the ViewModel to manage complex business logic and generate a new state based on incoming 
       Intents and the current state, ensuring predictability.
    [] The StateMachine pattern is intended for saving the application state in a database.
