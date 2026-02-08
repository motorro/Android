# Вебинар 4. Activity

***[Read in English](#webinar-4-activity)***

1. Компонента Activity
2. Запуск Activity, Intent, передача данных
3. Получение результата от Activity и Result Contracts
4. Запрос Runtime Permissions

## Вопросы для самопроверки:

1. Какие задачи решает компонент Activity?
   [] Activity предоставляет экран для взаимодействия пользователя с приложением, управляет жизненным циклом этого экрана, обрабатывает пользовательский 
      ввод и отображает информацию.
   [] Activity отвечает за выполнение фоновых сетевых запросов без участия пользователя.
   [] Activity используется исключительно для хранения глобальных настроек приложения.

2. Каковы два основных типа Intent?
   [] Явные (Explicit) Intent, которые указывают конкретный компонент для запуска (по имени класса), и неявные (Implicit) Intent, которые объявляют действие, 
      позволяя системе выбрать подходящий компонент.
   [] Intent для отправки данных и Intent для получения данных.
   [] Intent для запуска Activity и Intent для остановки Activity.

3. Как и какие данные мы можем передавать в Intent?
   [] Данные в Intent можно передавать с помощью `putExtra()` для примитивных типов и объектов, реализующих `Parcelable` или `Serializable`. Передавать можно 
      строки, числа, булевы значения, массивы, а также сложные объекты.
   [] В Intent можно передавать только строковые данные.
   [] В Intent можно передавать только данные, которые помещаются в URL-адрес.

4. Что такое Parcelable?
   [] `Parcelable` - это интерфейс Android для эффективной сериализации объектов, позволяющий передавать их между компонентами приложения (например, через 
      Intent) с высокой производительностью, так как он оптимизирован для Android IPC.
   [] `Parcelable` - это формат файла для хранения изображений в Android.
   [] `Parcelable` - это тип данных, используемый для работы с базами данных SQLite.

5. При помощи какого механизма мы можем получить результат от запуска Activity?
   [] Результат от запуска Activity можно получить с помощью `registerForActivityResult()` и `ActivityResultContracts`, которые предоставляют типобезопасный 
      способ взаимодействия и обработки результатов.
   [] Результат от Activity можно получить, только если Activity возвращает данные через глобальную статическую переменную.
   [] Результат от Activity невозможно получить, так как компоненты работают полностью изолированно.

6. Что такое Runtime Permissions и как их запрашивать?
   [] Runtime Permissions - это разрешения, которые приложение запрашивает у пользователя во время выполнения, а не при установке. Их запрашивают с помощью 
      `requestPermissions()` или `ActivityResultContracts.RequestPermission()`/`RequestMultiplePermissions()`, когда приложению требуется доступ к 
      чувствительным данным или ресурсам.
   [] Runtime Permissions - это разрешения, которые выдаются автоматически при установке приложения.
   [] Runtime Permissions - это разрешения, которые запрашиваются только один раз при первом запуске приложения и не могут быть изменены.

7. Как часто и когда следует запрашивать Runtime Permissions?
   [] Runtime Permissions следует запрашивать только тогда, когда они действительно необходимы для выполнения конкретной функции, и непосредственно перед тем, 
      как эта функция будет использоваться. Запрашивать их следует нечасто, чтобы не раздражать пользователя.
   [] Runtime Permissions следует запрашивать при каждом запуске приложения, независимо от того, нужны ли они.
   [] Runtime Permissions следует запрашивать только один раз, при установке приложения.

---

# Webinar 4. Activity

1. The Activity Component
2. Starting an Activity, Intent, passing data
3. Getting a result from an Activity and Result Contracts
4. Requesting Runtime Permissions

## Self-check questions:

1. What tasks does the Activity component solve?
   [] An Activity provides a screen for user interaction with the application, manages the lifecycle of that screen, handles user input, and displays information.
   [] An Activity is responsible for performing background network requests without user intervention.
   [] An Activity is used exclusively for storing global application settings.

2. What are the two main types of Intent?
   [] Explicit Intents, which specify a specific component to start (by class name), and Implicit Intents, which declare an action, allowing the system 
      to choose a suitable component.
   [] Intents for sending data and Intents for receiving data.
   [] Intents for starting an Activity and Intents for stopping an Activity.

3. How and what data can we pass in an Intent?
   [] Data can be passed in an Intent using `putExtra()` for primitive types and objects that implement `Parcelable` or `Serializable`. You can pass strings, 
      numbers, boolean values, arrays, as well as complex objects.
   [] Only string data can be passed in an Intent.
   [] Only data that can fit in a URL can be passed in an Intent.

4. What is Parcelable?
   [] `Parcelable` is an Android interface for efficient object serialization, allowing them to be passed between application components (e.g., via Intent) with 
      high performance, as it is optimized for Android IPC.
   [] `Parcelable` is a file format for storing images in Android.
   [] `Parcelable` is a data type used for working with SQLite databases.

5. Using what mechanism can we get a result from starting an Activity?
   [] A result from starting an Activity can be obtained using `registerForActivityResult()` and `ActivityResultContracts`, which provide a type-safe way 
      to interact and handle results.
   [] A result from an Activity can only be obtained if the Activity returns data through a global static variable.
   [] It is impossible to get a result from an Activity because the components work completely in isolation.

6. What are Runtime Permissions and how do you request them?
   [] Runtime Permissions are permissions that the application requests from the user at runtime, rather than at installation. They are requested using 
      `requestPermissions()` or `ActivityResultContracts.RequestPermission()`/`RequestMultiplePermissions()` when the application needs access to sensitive 
      data or resources.
   [] Runtime Permissions are permissions that are granted automatically when the application is installed.
   [] Runtime Permissions are permissions that are requested only once when the application is first launched and cannot be changed.

7. How often and when should Runtime Permissions be requested?
   [] Runtime Permissions should be requested only when they are really needed to perform a specific function, and immediately before that function is to be 
      used. They should be requested infrequently so as not to annoy the user.
   [] Runtime Permissions should be requested every time the application is launched, regardless of whether they are needed.
   [] Runtime Permissions should be requested only once, when the application is installed.
