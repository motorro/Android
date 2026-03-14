# Вебинар 1. Уведомления

***[Read in English](#webinar-1-notifications)***

1. Основы работы с уведомлениями: разрешения и каналы.
2. Создание и управление локальными уведомлениями.
3. Интерактивные уведомления: действия, кнопки и ответы.
4. Введение в Push-сообщения: теория и архитектура FCM.
5. Практическая реализация Push-уведомлений с Firebase Cloud Messaging.

### Вопросы для самопроверки

1.  Какие два обязательных шага необходимо предпринять в коде, чтобы уведомление успешно отобразилось на современном устройстве Android (API 33+)? Почему каждый
    из этих шагов является критически важным?
    [] Достаточно просто вызвать `NotificationManager.notify()` с любым уведомлением; система сама определит, как его показать.
    [] Получение разрешения `POST_NOTIFICATIONS` от пользователя и создание канала уведомлений (`NotificationChannel`). Разрешение критически важно, так как 
       без него система не покажет уведомление. Канал критически важен, так как он определяет поведение и важность уведомления, а уведомления без канала 
       при версии Android 26+ не отображаются.
    [] Необходимо только добавить уведомление в манифест приложения и отправить его с помощью `Intent`.
2.  В чем заключается основная роль `PendingIntent` при создании интерактивных уведомлений? Почему для добавления действия (например, открытия экрана) нельзя 
    передать системе обычный `Intent` напрямую?
    [] `PendingIntent` используется для шифрования данных, передаваемых с уведомлением.
    [] `PendingIntent` является оберткой для `Intent`, которая предоставляет системе Android право выполнить `Intent` от имени вашего приложения позже, 
       даже если ваше приложение не запущено. Нельзя передать обычный `Intent` напрямую, потому что система должна иметь возможность выполнить его *после того*,
       как ваше приложение, возможно, будет уничтожено или находится в неактивном состоянии. `PendingIntent` действует как "токен", дающий системе это разрешение.
    [] `PendingIntent` нужен для того, чтобы уведомление всегда открывало главный экран приложения.
3.  Опишите сценарий, в котором ваше приложение получит push-сообщение, но **не** покажет видимое для пользователя уведомление. Как называется тип полезной 
    нагрузки (payload) в FCM, который позволяет это реализовать, и какова его основная цель?
    [] Это невозможно, любое push-сообщение FCM всегда приводит к видимому уведомлению.
    [] Сценарий: приложение получает push-сообщение, которое содержит только `data` полезную нагрузку (data-message), но не `notification` полезную нагрузку. 
       В этом случае FCM доставляет сообщение напрямую в метод `onMessageReceived()` вашего `FirebaseMessagingService`, и приложение может обработать данные без 
       показа видимого уведомления. Основная цель `data` полезной нагрузки - отправка данных приложению для фоновой обработки или внутренней логики.
    [] Приложение получит push-сообщение с `notification` полезной нагрузкой, но пользователь просто проигнорирует его.
4.  Ваше приложение находится в фоновом режиме (background). С сервера отправляется push-сообщение, содержащее **одновременно** и `notification`, и `data` 
    полезные нагрузки. Будет ли в этом случае вызван метод `onMessageReceived()` в вашем `FirebaseMessagingService` и какой компонент в первую очередь 
    обработает это сообщение?
    [] Да, `onMessageReceived()` будет вызван, и он обработает обе части сообщения.
    [] Нет, сообщение будет полностью проигнорировано, так как оно содержит обе полезные нагрузки.
    [] Если приложение находится в фоновом режиме, и push-сообщение содержит **обе** полезные нагрузки (`notification` и `data`), то `onMessageReceived()` в 
       `FirebaseMessagingService` **не будет вызван** для обработки `notification` части. Вместо этого `notification` часть будет обработана самой системой 
       Android, которая отобразит уведомление в системном трее. `data` полезная нагрузка будет доступна через `Intent`, который запускается при нажатии 
       пользователем на уведомление.
5.  Каково назначение регистрационного токена FCM и почему критически важно реализовывать метод `onNewToken()`? Что со временем произойдет, если вы получите 
    и отправите токен на сервер только один раз при первой установке приложения?
    [] Регистрационный токен FCM используется для аутентификации пользователя в приложении. `onNewToken()` нужен для обновления пароля пользователя.
    [] Регистрационный токен FCM - это уникальный идентификатор для конкретного экземпляра приложения на конкретном устройстве, используемый для отправки 
       push-сообщений именно этому экземпляру. Критически важно реализовывать `onNewToken()`, потому что токен может меняться (например, при переустановке 
       приложения, сбросе данных). Если отправлять токен на сервер только один раз, со временем сервер будет пытаться отправлять сообщения на устаревший токен, 
       и уведомления не будут доходить до пользователя, так как устройство больше не будет ассоциироваться с этим токеном.
    [] Регистрационный токен FCM нужен только для аналитики. Если его не обновлять, это не повлияет на доставку уведомлений.

---

# Webinar 1. Notifications

1.  Basics of working with notifications: permissions and channels.
2.  Creating and managing local notifications.
3.  Interactive notifications: actions, buttons, and replies.
4.  Introduction to Push messages: theory and architecture of FCM.
5.  Practical implementation of Push notifications with Firebase Cloud Messaging.

### Self-check questions

1.  What two mandatory steps must be taken in the code for a notification to be successfully displayed on a modern Android device (API 33+)? Why is each 
    of these steps critical?
    [] It is enough to simply call `NotificationManager.notify()` with any notification; the system will figure out how to display it.
    [] Obtaining the `POST_NOTIFICATIONS` permission from the user and creating a notification channel (`NotificationChannel`). The permission is critical 
    because the system will not show the notification without it. The channel is critical because it defines the behavior and importance of the notification, 
    and notifications without a channel are not displayed on Android 26+.
    [] It is only necessary to add the notification to the application manifest and send it using an `Intent`.
2.  What is the main role of `PendingIntent` when creating interactive notifications? Why can't a regular `Intent` be passed directly to the system to add 
    an action (e.g., opening a screen)?
    [] `PendingIntent` is used to encrypt the data transmitted with the notification.
    [] `PendingIntent` is a wrapper for an `Intent` that grants the Android system the right to execute the `Intent` on behalf of your application later, even 
    if your application is not running. A regular `Intent` cannot be passed directly because the system must be able to execute it *after* your application may 
    have been destroyed or is in an inactive state. `PendingIntent` acts as a "token" that gives the system this permission.
    [] `PendingIntent` is needed so that the notification always opens the main screen of the application.
3.  Describe a scenario where your application would receive a push message but **not** show a visible notification to the user. What is the name of the payload
    type in FCM that allows this, and what is its main purpose?
    [] This is impossible; any FCM push message always results in a visible notification.
    [] Scenario: the application receives a push message that contains only a `data` payload (data-message), but not a `notification` payload. In this case, 
    FCM delivers the message directly to the `onMessageReceived()` method of your `FirebaseMessagingService`, and the application can process the data without 
    showing a visible notification. The main purpose of the `data` payload is to send data to the application for background processing or internal logic.
    [] The application will receive a push message with a `notification` payload, but the user will simply ignore it.
4.  Your application is in the background. A push message is sent from the server containing **both** `notification` and `data` payloads. 
    Will the `onMessageReceived()` method in your `FirebaseMessagingService` be called in this case, and which component will handle this message first?
    [] Yes, `onMessageReceived()` will be called, and it will process both parts of the message.
    [] No, the message will be completely ignored because it contains both payloads.
    [] If the application is in the background and the push message contains **both** payloads (`notification` and `data`), then `onMessageReceived()` 
    in the `FirebaseMessagingService` **will not be called** to process the `notification` part. Instead, the `notification` part will be handled by the Android
    system itself, which will display the notification in the system tray. The `data` payload will be available through the `Intent` that is launched when 
    the user taps on the notification.
5.  What is the purpose of the FCM registration token, and why is it critically important to implement the `onNewToken()` method? What will happen over time 
    if you get and send the token to the server only once when the application is first installed?
    [] The FCM registration token is used to authenticate the user in the application. `onNewToken()` is needed to update the user's password.
    [] The FCM registration token is a unique identifier for a specific instance of the application on a specific device, used to send push messages to 
    that specific instance. It is critically important to implement `onNewToken()` because the token can change (e.g., when the application is reinstalled, or data is cleared). If you send the token to the server only once, over time the server will try to send messages to an outdated token, and the notifications will not reach the user because the device will no longer be associated with that token.
    [] The FCM registration token is only needed for analytics. If it is not updated, it will not affect the delivery of notifications.
