# Вебинар 24. Работа с сетью

***[Read in English](#webinar-24-network-operations)***

1. Архитектура клиент-сервер и протокол HTTP
2. Сериализация данных в JSON
3. Загрузка данных из сети при помощи OkHttp
4. Retrofit - клиент для работы с HTTP
5. Интерцепторы в OkHttp
6. Ktor - современный кроссплатформенный HTTP-клиент
7. OpenAPI - спецификация для REST API

## Тестовый сервер:

Для запуска сервера воспользуйтесь задачей `run` соответствующего модуля из панели Gradle или введите команду:
```shell
./gradlew :network:server:run
```
Используйте токет `token123` для доступа к API.

Приложение расчитано на запуск сервера на локальном компьютере и приложения на эмуляторе.
Если вы хотите установить приложение на телефон, измените адрес сервера в [этом файле](app/src/main/kotlin/com/motorro/network/net/Config.kt).

## Вопросы для самопроверки:

1. Что представляет собой архитектура клиент-сервер?
   [] Архитектура клиент-сервер - это способ хранения всех данных локально на устройстве пользователя.
   [] Архитектура клиент-сервер - это тип базы данных, который используется для высокопроизводительных вычислений.
   [] Архитектура клиент-сервер - это сетевая архитектура, где клиент (приложение или браузер) запрашивает ресурсы или службы у сервера, а сервер 
      их предоставляет.
2. Опишите протокол HTTP.
   [] HTTP - это язык программирования для создания веб-сайтов.
   [] HTTP - это протокол прикладного уровня для передачи гипертекста, изображений и других ресурсов по сети, основанный на модели "запрос-ответ" между 
      клиентом и сервером.
   [] HTTP - это протокол для обмена файлами между устройствами без использования интернета.
3. Для чего нам сериализация данных?
   [] Сериализация данных необходима для шифрования информации, чтобы ее нельзя было прочитать.
   [] Сериализация данных используется для создания резервных копий всех файлов на устройстве.
   [] Сериализация данных нужна для того, чтобы преобразовать сложные структуры данных (объекты) в формат, который можно легко передавать по сети 
   (например, JSON, XML) или сохранять в файл, а затем восстанавливать обратно.
4. Для чего используется библиотека OkHttp?
   [] OkHttp - это библиотека для работы с графическими изображениями в Android.
   [] OkHttp - это эффективный HTTP-клиент для Android и Java, используемый для выполнения сетевых запросов с такими функциями, как кэширование, перехват 
      запросов и повторные попытки при ошибках.
   [] OkHttp - это библиотека для управления жизненным циклом Activity.
5. Как работает библиотека Retrofit?
   [] Retrofit - это библиотека для работы с локальными базами данных на устройстве.
   [] Retrofit - это типобезопасный HTTP-клиент для Android и Java/Kotlin, который упрощает взаимодействие с REST API. Он использует аннотации для описания 
      HTTP-запросов (GET, POST и т.д.) и автоматически преобразует POJO-объекты в JSON/XML и обратно с помощью конвертеров (например, Gson, Moshi).
   [] Retrofit работает, автоматически генерируя весь UI для отображения сетевых данных.
6. Что такое интерцепторы в OkHttp?
   [] Интерцепторы в OkHttp - это механизмы для отмены выполнения сетевых запросов.
   [] Интерцепторы в OkHttp - это мощные механизмы, которые позволяют перехватывать и изменять HTTP-запросы и ответы между клиентом и сервером. Они используются 
      для добавления заголовков, логирования запросов/ответов, аутентификации, обработки ошибок и кэширования.
   [] Интерцепторы в OkHttp - это специальные потоки, которые выполняют сетевые запросы в фоновом режиме.
7. Что такое Ktor и чем его использование отличается от Retrofit?
   [] Ktor - это библиотека для работы с изображениями, а Retrofit - для видео.
   [] Ktor и Retrofit - это два названия для одной и той же библиотеки.
   [] Ktor - это фреймворк для создания асинхронных серверов и клиентов на Kotlin. В отличие от Retrofit, который использует аннотации и кодогенерацию для 
      реализации интерфейса, Ktor предлагает удобный DSL для конфигурации запросов на сервер.
9. Что такое OpenAPI и как его использовать?
   [] OpenAPI - это протокол для безопасного обмена файлами в локальной сети.
   [] OpenAPI - это язык программирования для создания мобильных приложений.
   [] OpenAPI - это спецификация для описания RESTful API в машинно-читаемом формате (JSON или YAML). Она используется для документирования API, генерации 
      клиентского кода (SDK), серверных заглушек, тестовых кейсов и упрощает взаимодействие между разработчиками фронтенда и бэкенда.

---

# Webinar 24. Network Operations

1. Client-server architecture and the HTTP protocol
2. Data serialization to JSON
3. Loading data from the network using OkHttp
4. Retrofit - a client for working with HTTP
5. Interceptors in OkHttp
6. Ktor - a modern cross-platform HTTP client
7. OpenAPI - a specification for REST APIs

## Test Server:

To start the server, use the `run` task of the corresponding module from the Gradle panel or enter the command:
```shell
./gradlew :network:server:run
```
Use the token `token123` for API access.

The application is designed to run the server on a local computer and the application on an emulator.
If you want to install the application on a phone, change the server address in [this file](app/src/main/kotlin/com/motorro/network/net/Config.kt).

## Self-check questions:

1. What is the client-server architecture?
   [] Client-server architecture is a way of storing all data locally on the user's device.
   [] Client-server architecture is a type of database used for high-performance computing.
   [] Client-server architecture is a network architecture where a client (application or browser) requests resources or services from a server, and the server 
      provides them.
2. Describe the HTTP protocol.
   [] HTTP is a programming language for creating websites.
   [] HTTP is an application-level protocol for transmitting hypertext, images, and other resources over a network, based on a "request-response" model between 
      a client and a server.
   [] HTTP is a protocol for exchanging files between devices without using the internet.
3. Why do we need data serialization?
   [] Data serialization is necessary for encrypting information so that it cannot be read.
   [] Data serialization is used to create backups of all files on the device.
   [] Data serialization is needed to convert complex data structures (objects) into a format that can be easily transmitted over a network (e.g., JSON, XML) 
      or saved to a file, and then restored.
4. What is the OkHttp library used for?
   [] OkHttp is a library for working with graphic images in Android.
   [] OkHttp is an efficient HTTP client for Android and Java, used for making network requests with features like caching, intercepting requests, and retrying 
      on errors.
   [] OkHttp is a library for managing the Activity lifecycle.
5. How does the Retrofit library work?
   [] Retrofit is a library for working with local databases on the device.
   [] Retrofit is a type-safe HTTP client for Android and Java/Kotlin that simplifies interaction with REST APIs. It uses annotations to describe HTTP requests 
      (GET, POST, etc.) and automatically converts POJO objects to and from JSON/XML using converters (e.g., Gson, Moshi).
   [] Retrofit works by automatically generating the entire UI for displaying network data.
6. What are interceptors in OkHttp?
   [] Interceptors in OkHttp are mechanisms for canceling the execution of network requests.
   [] Interceptors in OkHttp are powerful mechanisms that allow intercepting and modifying HTTP requests and responses between the client and the server. They 
      are used for adding headers, logging requests/responses, authentication, error handling, and caching.
   [] Interceptors in OkHttp are special threads that execute network requests in the background.
7. What is Ktor and how does its use differ from Retrofit?
   [] Ktor is a library for working with images, and Retrofit is for video.
   [] Ktor and Retrofit are two names for the same library.
   [] Ktor is a framework for creating asynchronous servers and clients in Kotlin. Unlike Retrofit, which uses annotations and code generation for interface 
      implementation, Ktor offers a convenient DSL for configuring server requests.
8. What is OpenAPI and how is it used?
   [] OpenAPI is a protocol for securely exchanging files on a local network.
   [] OpenAPI is a programming language for creating mobile applications.
   [] OpenAPI is a specification for describing RESTful APIs in a machine-readable format (JSON or YAML). It is used for documenting APIs, generating client 
      code (SDKs), server stubs, test cases, and simplifies interaction between frontend and backend developers.
