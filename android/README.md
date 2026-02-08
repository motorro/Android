# Вебиinar 2. ОС Android

***[Read in English](#webinar-2-android-os)***

1. Из чего состоит приложение
2. Доступ к данным приложения и Application Sandbox
3. Основы передачи данных между компонентами (IPC)
4. ОС и приложение - взаимодействие: Intent, Lifecycle
5. Основные компоненты: Activity, Content Provider, Service, Broadcast Receiver
6. Контекст приложения

## Вопросы для самопроверки:

1. Чем обеспечивается безопасность данных приложения Android?
   [] Безопасность данных приложения Android обеспечивается Application Sandbox, который изолирует каждое приложение в отдельном процессе, а также системой 
      разрешений, которая контролирует доступ к системным ресурсам и данным других приложений.
   [] Безопасность данных обеспечивается исключительно путем шифрования всех данных, хранящихся на устройстве.
   [] За безопасность данных отвечает только пользователь, который должен быть осторожен при установке приложений из неизвестных источников.
2. В чем разница между процессом и потоком?
   [] Процесс - это независимый исполняемый экземпляр программы с собственным адресным пространством, а поток - это легковесная единица выполнения внутри 
      процесса, которая делит его адресное пространство и ресурсы.
   [] Процесс и поток - это одно и то же, просто используются разные термины.
   [] Процесс используется для фоновых задач, а поток - для операций пользовательского интерфейса.
3. Из каких структурных компонент состоит приложение Android?
   [] Приложение Android состоит из основных компонентов: 
      - Activity (для пользовательского интерфейса)
      - Service (для фоновых операций)
      - Broadcast Receiver (для обработки системных сообщений) 
      - Content Provider (для управления общими данными).
   [] Приложение Android состоит только из одного основного файла с кодом и набора изображений.
   [] Приложение Android состоит из веб-страниц, отображаемых в браузере.
4. Как происходит обмен данными между компонентами?
   [] Обмен данными между компонентами Android-приложения происходит с использованием технологии Binder (IPC) 
      и механизмов на его основе: Intent, Content Provider и т.д.
   [] Обмен данными между компонентами происходит только через локальные файлы на устройстве.
   [] Компоненты приложения не могут обмениваться данными друг с другом напрямую.

---

# Webinar 2. Android OS

1. What an application consists of
2. Access to application data and the Application Sandbox
3. Basics of data transfer between components (IPC)
4. OS and application interaction: Intent, Lifecycle
5. Main components: Activity, Content Provider, Service, Broadcast Receiver
6. Application context

## Self-check questions:

1. How is the security of Android application data ensured?
   [] The security of Android application data is ensured by the Application Sandbox, which isolates each application in a separate process, as well as by 
      the permission system, which controls access to system resources and other applications' data.
   [] Data security is ensured solely by encrypting all data stored on the device.
   [] The user is solely responsible for data security and must be careful when installing applications from unknown sources.
2. What is the difference between a process and a thread?
   [] A process is an independent executable instance of a program with its own address space, while a thread is a lightweight unit of execution within 
      a process that shares its address space and resources.
   [] A process and a thread are the same thing, just different terms are used.
   [] A process is used for background tasks, while a thread is used for user interface operations.
3. What are the structural components of an Android application?
   [] An Android application consists of the main components:
      - Activity (for the user interface)
      - Service (for background operations)
      - Broadcast Receiver (for handling system messages)
      - Content Provider (for managing shared data).
   [] An Android application consists of only one main code file and a set of images.
   [] An Android application consists of web pages displayed in a browser.
4. How is data exchanged between components?
   [] Data is exchanged between components of an Android application using Binder (IPC) technology and mechanisms based on it: Intent, Content Provider, etc.
   [] Data is exchanged between components only through local files on the device.
   [] Application components cannot exchange data with each other directly.
