# Вебинар 5. Жизненный цикл Activity

***[Read in English](#webinar-5-activity-lifecycle)***

1. Жизненный цикл - Activity Lifecycle
2. Смена конфигурации
3. О процессе приложения
4. Сохранение состояния (Instance State)

## Вопросы для самопроверки:

1. Для чего нужен жизненный цикл Activity?
   [] Жизненный цикл Activity нужен для того, чтобы приложение всегда оставалось активным в фоновом режиме.
   [] Жизненный цикл Activity используется для определения порядка запуска других компонентов приложения.
   [] Жизненный цикл Activity нужен для управления состоянием активности, эффективного использования ресурсов и корректного поведения приложения при различных 
      событиях (например, поворот экрана, входящий звонок, переключение между приложениями).

2. Какой метод вызывается при создании Activity?
   [] Метод `onCreate()` вызывается при создании Activity.
   [] Метод `onStop()` вызывается при создании Activity.
   [] Метод `onDestroy()` вызывается при создании Activity.

3. Какая пара методов вызывается при переходе Activity в фоновый режим и обратно?
   [] При переходе Activity в фоновый режим вызывается `onStart()`, а при возвращении - `onStop()`.
   [] При переходе Activity в фоновый режим вызывается `onRestart()`, а при возвращении - `onCreate()`.
   [] При переходе Activity в фоновый режим вызывается `onPause()`, а при возвращении из фонового режима - `onResume()`.

4. Что такое смена конфигурации и как она влияет на внутреннее состояние Activity?
   [] Смена конфигурации - это изменение темы оформления приложения, которое не влияет на состояние Activity.
   [] Смена конфигурации - это изменение характеристик устройства, таких как ориентация экрана, доступность клавиатуры или языка. Она приводит к уничтожению 
      и повторному созданию Activity по умолчанию, что может привести к потере временного состояния, если его не сохранить.
   [] Смена конфигурации - это обновление версии Android на устройстве, которое не влияет на работу приложения.

5. Какие процессы могут быть уничтожены системой и почему?
   [] Система уничтожает процессы только по запросу пользователя.
   [] Система уничтожает все процессы после 24 часов непрерывной работы.
   [] Система может уничтожить процессы, когда ей не хватает памяти, обычно начиная с процессов, которые находятся в кэше (Activity в состоянии `onStop()` или 
      `onDestroy()`), чтобы освободить ресурсы для более важных активных процессов.

6. Какие методы позволяют сохранить состояние Activity?
   [] Состояние Activity сохраняется автоматически системой без участия разработчика.
   [] Состояние Activity можно сохранить только путем записи всех данных в файл на внешнем хранилище.
   [] Сохранить состояние Activity позволяют методы `onSaveInstanceState()` (для небольшого объема данных, сохраняемых в `Bundle`) и 
      `onRetainNonConfigurationInstance()` (для более сложных объектов, которые не должны пересоздаваться при смене конфигурации).

---

# Webinar 5. Activity Lifecycle

1. Lifecycle - Activity Lifecycle
2. Configuration Change
3. About the Application Process
4. Saving State (Instance State)

## Self-check questions:

1. What is the purpose of the Activity lifecycle?
   [] The Activity lifecycle is needed to keep the application always active in the background.
   [] The Activity lifecycle is used to determine the launch order of other application components.
   [] The Activity lifecycle is needed to manage the state of an activity, use resources efficiently, and ensure the correct behavior of the application during 
      various events (e.g., screen rotation, incoming call, switching between applications).

2. Which method is called when an Activity is created?
   [] The `onCreate()` method is called when an Activity is created.
   [] The `onStop()` method is called when an Activity is created.
   [] The `onDestroy()` method is called when an Activity is created.

3. Which pair of methods is called when an Activity goes into the background and returns?
   [] When an Activity goes into the background, `onStart()` is called, and when it returns, `onStop()` is called.
   [] When an Activity goes into the background, `onRestart()` is called, and when it returns, `onCreate()` is called.
   [] When an Activity goes into the background, `onPause()` is called, and when it returns from the background, `onResume()` is called.

4. What is a configuration change and how does it affect the internal state of an Activity?
   [] A configuration change is a change in the application's theme, which does not affect the state of the Activity.
   [] A configuration change is a change in the device's characteristics, such as screen orientation, keyboard availability, or language. It leads to the 
      destruction and recreation of the Activity by default, which can lead to the loss of temporary state if it is not saved.
   [] A configuration change is an update to the Android version on the device, which does not affect the application's operation.

5. Which processes can be destroyed by the system and why?
   [] The system destroys processes only at the user's request.
   [] The system destroys all processes after 24 hours of continuous operation.
   [] The system can destroy processes when it is low on memory, usually starting with processes that are in the cache (Activity in the `onStop()` 
      or `onDestroy()` state), to free up resources for more important active processes.

6. Which methods allow you to save the state of an Activity?
   [] The state of an Activity is saved automatically by the system without developer intervention.
   [] The state of an Activity can only be saved by writing all data to a file on external storage.
   [] The state of an Activity can be saved using the `onSaveInstanceState()` method (for a small amount of data saved in a `Bundle`) 
      and `onRetainNonConfigurationInstance()` (for more complex objects that should not be recreated on configuration change).
