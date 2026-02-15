# Вебинар 23. Хранение объектов

***[Read in English](#webinar-23-object-storage)***

1. Использование DataStore в приложении против собственных решений
2. Хранение данных ключ-значение: Preferences DataStore
3. Хранение данных в виде объектов: Proto DataStore и Protocol Buffers
4. DataStore и пользовательская сериализация - JSON DataStore
5. Шифрование данных в DataStore

## Вопросы для самопроверки:

1. Какие преимущества дает использование DataStore перед собственными решениями?
   [] DataStore предоставляет асинхронный API на основе Kotlin Coroutines и Flow, что предотвращает блокировку основного потока, а также обеспечивает 
      типобезопасность (с Proto DataStore) и обработку ошибок миграции данных.
   [] Собственные решения всегда быстрее и эффективнее, чем DataStore.
   [] DataStore не предоставляет никаких преимуществ перед SharedPreferences.
2. Какие типы DataStore существуют и для чего они предназначены?
   [] Существует только один тип DataStore для хранения любых данных.
   [] Preferences DataStore для хранения простых пар ключ-значение (аналогично SharedPreferences) и Proto DataStore для хранения типизированных объектов 
      с использованием Protocol Buffers.
   [] Proto DataStore используется только для хранения изображений.
3. В каких случаях удобнее применять Proto DataStore перед Preferences DataStore?
   [] Proto DataStore удобнее, когда нужно хранить сложные, типизированные объекты с заранее определенной схемой, что обеспечивает типобезопасность и лучшую 
      производительность по сравнению с сериализацией JSON. Preferences DataStore подходит для простых пар ключ-значение.
   [] Preferences DataStore всегда предпочтительнее, так как он проще в использовании.
   [] Proto DataStore следует использовать только в приложениях, которые работают с сетью.
4. Каким образом реализуется сериализация данных в JSON DataStore?
   [] JSON DataStore - это встроенная функция Android, которая не требует реализации сериализации.
   [] Сериализация данных в JSON DataStore реализуется с помощью создания кастомного класса `Serializer`, который определяет, как преобразовывать объект 
      в `InputStream` (для записи) и из `OutputStream` (для чтения) с использованием библиотеки для работы с JSON, например, kotlinx.serialization.
   [] JSON DataStore не поддерживает сериализацию, только хранение строк.
5. Как защитить данные в DataStore от несанкционированного доступа?
   [] Данные в DataStore можно защитить с помощью EncryptedSharedPreferences.
   [] Данные в DataStore защищены по умолчанию и не требуют дополнительных действий.
   [] Для шифрования данных в DataStore можно использовать библиотеку `androidx.security.crypto`, создав экземпляр `EncryptedFile` и передав его в `DataStoreFactory` 
      для создания зашифрованного хранилища.

---

# Webinar 23. Object Storage

1. Using DataStore in an application versus custom solutions
2. Key-value data storage: Preferences DataStore
3. Object data storage: Proto DataStore and Protocol Buffers
4. DataStore and custom serialization - JSON DataStore
5. Encrypting data in DataStore

## Self-check questions:

1. What are the advantages of using DataStore over custom solutions?
   [] DataStore provides an asynchronous API based on Kotlin Coroutines and Flow, which prevents blocking the main thread, and also ensures type safety (with Proto DataStore) and handles data migration errors.
   [] Custom solutions are always faster and more efficient than DataStore.
   [] DataStore offers no advantages over SharedPreferences.
2. What types of DataStore exist and what are they for?
   [] There is only one type of DataStore for storing any data.
   [] Preferences DataStore for storing simple key-value pairs (similar to SharedPreferences) and Proto DataStore for storing typed objects using Protocol Buffers.
   [] Proto DataStore is only used for storing images.
3. In which cases is it more convenient to use Proto DataStore over Preferences DataStore?
   [] Proto DataStore is more convenient when you need to store complex, typed objects with a predefined schema, which provides type safety and better performance compared to JSON serialization. Preferences DataStore is suitable for simple key-value pairs.
   [] Preferences DataStore is always preferable because it is easier to use.
   [] Proto DataStore should only be used in applications that work with the network.
4. How is data serialization implemented in JSON DataStore?
   [] JSON DataStore is a built-in Android feature that does not require serialization implementation.
   [] Data serialization in JSON DataStore is implemented by creating a custom `Serializer` class that defines how to convert an object to an `InputStream` (for writing) and from an `OutputStream` (for reading) using a JSON library, for example, kotlinx.serialization.
   [] JSON DataStore does not support serialization, only string storage.
5. How to protect data in DataStore from unauthorized access?
   [] Data in DataStore can be protected using EncryptedSharedPreferences.
   [] Data in DataStore is protected by default and requires no additional action.
   [] To encrypt data in DataStore, you can use the `androidx.security.crypto` library by creating an `EncryptedFile` instance and passing it to the `DataStoreFactory` to create an encrypted store.
