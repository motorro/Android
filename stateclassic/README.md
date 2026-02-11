# Вебинар 14. Архитекрурные компоненты Android (классический подход)

***[Read in English](#webinar-14-android-architectural-components-classic-approach)***

1. Проблема повторного использования кода в Activity и решение - Livecycle Observer
2. Тестируемые компоненты Livecycle Observer
3. Проблема концентрации кода в Activity и решение - ViewModel, LiveData, MVVM
4. Тестируемые компоненты ViewModel, LiveData
5. Сохранение состояния Activity и решение - ViewModel, SavedStateHandle

## Вопросы для самопроверки:

1. Что такое Livecycle Observer?
   [] Livecycle Observer - это интерфейс, который позволяет делегировать часть обработки событий жизненного цикла компонентов с Lifecycle (например, Activity)
      выделенным дополнительным компонентам.
   [] Livecycle Observer - это компонент, который позволяет отслеживать изменения данных в базе данных.
   [] Livecycle Observer - это класс, который управляет сетевыми запросами.
2. В чем преимущество использования Lifecycle Observer?
   [] Lifecycle Observer позволяет выполнить код в Activity после завершения всех фоновых задач.
   [] Lifecycle Observer помогает отделить логику, зависящую от жизненного цикла, от Activity/Fragment, что делает код более чистым, модульным и легким 
      для тестирования. Он позволяет безопасно выполнять действия в ответ на изменения состояния жизненного цикла.
   [] Lifecycle Observer обеспечивает автоматическую синхронизацию данных между Activity и ViewModel.
3. Какие проблемы возникают при концентрации кода в Activity?
   [] Проблемы с производительностью приложения из-за избыточного количества Activity.
   [] Увеличение размера APK файла и сложности развертывания приложения.
   [] `God Object` (раздутая Activity), сложности с тестированием (поскольку логика тесно связана с Android SDK), проблемы с сохранением состояния при изменении 
      конфигурации (например, поворот экрана), и трудности с повторным использованием кода.
4. Что такое ViewModel и какие проблемы она решает?
   [] ViewModel - это компонент UI, отвечающий за отображение данных пользователю.
   [] ViewModel - это класс, предназначенный для хранения и управления данными пользовательского интерфейса таким образом, чтобы данные переживали изменения 
      конфигурации (например, поворот экрана). Она решает проблему потери данных при пересоздании Activity/Fragment и упрощает тестирование логики UI, 
      отделяя ее от View.
   [] ViewModel - это база данных для хранения локальных данных приложения.
5. Что такое LiveData и каким образом View получает данные из ViewModel?
   [] LiveData - это механизм для выполнения фоновых операций в Android.
   [] LiveData - это наблюдаемый класс-хранитель данных, который учитывает жизненный цикл. View (Activity/Fragment) подписывается на LiveData в ViewModel 
      и получает обновления данных только тогда, когда компонент находится в активном состоянии жизненного цикла, что предотвращает утечки памяти.
   [] LiveData - это способ взаимодействия между Activity и Service.
6. Как написать тестируемую логику в ViewModel?
   [] Для тестирования ViewModel необходимо запускать приложение на эмуляторе и вручную проверять все сценарии.
   [] Тестируемая логика в ViewModel пишется с использованием передаваемых извне зависимостей, которые можно имитировать во время модульного тестирования. 
      ViewModel не должна иметь прямых зависимостей от Android SDK View или Context, что позволяет тестировать ее как обычный Java/Kotlin класс.
   [] Тестируемая логика в ViewModel пишется с использованием специальных аннотаций, которые автоматически генерируют тестовые кейсы.
7. Что такое SavedStateHandle и как она используется в ViewModel?
   [] SavedStateHandle - это компонент для работы с сетевыми запросами.
   [] SavedStateHandle - это карта ключ-значение, которая позволяет ViewModel сохранять и восстанавливать данные, переживая процесс уничтожения процесса Activity 
      системой (например, при нехватке памяти). Она используется в конструкторе ViewModel для получения доступа к сохраненному состоянию, похожему на 
      `onSaveInstanceState()` Activity, но привязанному к ViewModel.
   [] SavedStateHandle - это механизм для работы с общими настройками приложения.

---

# Webinar 14. Android Architectural Components (Classic Approach)

1. The problem of code reuse in Activity and the solution - Lifecycle Observer
2. Testable Lifecycle Observer components
3. The problem of code concentration in Activity and the solution - ViewModel, LiveData, MVVM
4. Testable ViewModel and LiveData components
5. Saving Activity state and the solution - ViewModel, SavedStateHandle

## Self-check questions:

1. What is a Lifecycle Observer?
   [] A Lifecycle Observer is an interface that allows delegating part of the lifecycle event handling of components with a Lifecycle (e.g., Activity) 
      to dedicated additional components.
   [] A Lifecycle Observer is a component that allows tracking data changes in a database.
   [] A Lifecycle Observer is a class that manages network requests.
2. What is the advantage of using a Lifecycle Observer?
   [] A Lifecycle Observer allows executing code in an Activity after all background tasks are completed.
   [] A Lifecycle Observer helps to separate lifecycle-dependent logic from the Activity/Fragment, which makes the code cleaner, more modular, and easier 
      to test. It allows for safely performing actions in response to lifecycle state changes.
   [] A Lifecycle Observer provides automatic data synchronization between an Activity and a ViewModel.
3. What problems arise from concentrating code in an Activity?
   [] Application performance problems due to an excessive number of Activities.
   [] Increased APK file size and application deployment complexity.
   [] `God Object` (bloated Activity), difficulties with testing (as the logic is tightly coupled with the Android SDK), problems with saving state 
      on configuration changes (e.g., screen rotation), and difficulties with code reuse.
4. What is a ViewModel and what problems does it solve?
   [] A ViewModel is a UI component responsible for displaying data to the user.
   [] A ViewModel is a class designed to store and manage UI-related data in a way that the data survives configuration changes (e.g., screen rotation). 
      It solves the problem of data loss when an Activity/Fragment is recreated and simplifies testing of UI logic by separating it from the View.
   [] A ViewModel is a database for storing local application data.
5. What is LiveData and how does the View get data from the ViewModel?
   [] LiveData is a mechanism for performing background operations in Android.
   [] LiveData is a lifecycle-aware observable data holder class. The View (Activity/Fragment) subscribes to the LiveData in the ViewModel and receives data 
      updates only when the component is in an active lifecycle state, which prevents memory leaks.
   [] LiveData is a way of interaction between an Activity and a Service.
6. How to write testable logic in a ViewModel?
   [] To test a ViewModel, you need to run the application on an emulator and manually check all scenarios.
   [] Testable logic in a ViewModel is written using externally provided dependencies that can be mocked during unit testing. The ViewModel should not have 
      direct dependencies on the Android SDK View or Context, which allows it to be tested as a regular Java/Kotlin class.
   [] Testable logic in a ViewModel is written using special annotations that automatically generate test cases.
7. What is SavedStateHandle and how is it used in a ViewModel?
   [] SavedStateHandle is a component for handling network requests.
   [] SavedStateHandle is a key-value map that allows a ViewModel to save and restore data, surviving the process destruction of an Activity by the system 
      (e.g., due to low memory). It is used in the ViewModel's constructor to access the saved state, similar to an Activity's `onSaveInstanceState()`, but tied 
      to the ViewModel.
   [] SavedStateHandle is a mechanism for working with the application's shared settings.
