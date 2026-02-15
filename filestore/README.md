# Вебинар 22. Файловое хранилище.

***[Read in English](#webinar-22-file-storage)***

1. Права доступа к файлам в Android.
2. Запись файлов во внутреннее хранилище приложения.
3. Обмен файлами между приложениями с помощью FileProvider.
4. Общие папки для хранения медиа-файлов - MediaStore.
5. Доступ к файлу в произвольной папке - Storage Access Framework.

## Вопросы для самопроверки:

1. Как организован доступ к файлам в Android?
   [] Доступ к файлам в Android организован через общую файловую систему, где любое приложение может читать и записывать файлы в любые папки без ограничений.
   [] Доступ к файлам в Android организован таким образом, что каждое приложение по умолчанию имеет свой собственный изолированный "песочницу" для хранения 
      данных, и доступ к файлам других приложений строго ограничен. Для обмена файлами используются специальные механизмы, такие как FileProvider или 
      Storage Access Framework.
   [] Доступ к файлам в Android контролируется только разрешениями, которые пользователь предоставляет при установке приложения, и после этого приложение может делать с файлами все, что угодно.
2. Почему внутреннее хранилище приложения называется приватным?
   [] Внутреннее хранилище приложения называется приватным, потому что его содержимое зашифровано и недоступно для чтения даже самому приложению.
   [] Внутреннее хранилище приложения называется приватным, потому что оно предназначено только для хранения персональных данных пользователя, и к нему не имеют
      доступа системные службы.
   [] Внутреннее хранилище приложения называется приватным, потому что оно доступно только приложению, которое его создало. Данные, хранящиеся там, не доступны 
      другим приложениям или пользователю напрямую.
3. Что такое FileProvider и как он используется?
   [] FileProvider - это инструмент для прямого доступа к файлам любого приложения на устройстве.
   [] FileProvider - это служба для синхронизации файлов между несколькими устройствами.
   [] FileProvider - это класс, который позволяет приложению предоставлять доступ к приватным файлам другим приложениям безопасным способом, без раскрытия 
      прямых путей к файлам. Он используется, например, для отправки файлов в другие приложения или для предоставления доступа камере для сохранения снимков.
4. Возможен ли произвольный доступ к файлам других приложений в Android?
   [] Да, произвольный доступ к файлам других приложений возможен, если приложение запрашивает и получает соответствующие разрешения.
   [] Нет, произвольный доступ к файлам других приложений в Android по умолчанию невозможен из соображений безопасности. Приложения работают в изолированных 
      "песочницах", и доступ к чужим файлам осуществляется только через специально предоставленные контент-провайдеры.
   [] Произвольный доступ возможен только для системных приложений, но не для сторонних.
5. Какие виды файлов можно использовать в MediaStore?
   [] В MediaStore можно хранить только текстовые файлы и документы.
   [] MediaStore используется для хранения любых типов файлов, но только если они находятся во внутреннем хранилище приложения.
   [] MediaStore предназначен для хранения медиа-файлов, таких как изображения (JPG, PNG), видео (MP4, MOV), аудио (MP3, WAV), а также метаданных о них.
6. Какие преимущества дает использование Storage Access Framework?
   [] Storage Access Framework (SAF) позволяет приложению напрямую обращаться к приватным файлам других приложений без согласия пользователя.
   [] SAF обеспечивает автоматическое шифрование всех файлов, которые хранятся на устройстве.
   [] SAF предоставляет унифицированный и безопасный способ для пользователей и приложений взаимодействовать с файлами и документами, хранящимися в различных 
      источниках (локальное хранилище, облачные сервисы), без необходимости запрашивать широкие разрешения на хранение. Пользователь сам выбирает, какие файлы 
      и откуда предоставить приложению.

---

# Webinar 22. File Storage

1. File access permissions in Android.
2. Writing files to the application's internal storage.
3. Sharing files between applications using FileProvider.
4. Shared folders for storing media files - MediaStore.
5. Accessing files in an arbitrary folder - Storage Access Framework.

## Self-check questions:

1. How is file access organized in Android?
   [] File access in Android is organized through a shared file system where any application can read and write files in any folder without restrictions.
   [] File access in Android is organized in such a way that each application by default has its own isolated "sandbox" for storing data, and access to 
      the files of other applications is strictly limited. Special mechanisms such as FileProvider or the Storage Access Framework are used for file sharing.
   [] File access in Android is controlled only by the permissions that the user grants when installing the application, and after that the application can 
      do whatever it wants with the files.
2. Why is the application's internal storage called private?
   [] The application's internal storage is called private because its contents are encrypted and cannot be read even by the application itself.
   [] The application's internal storage is called private because it is intended only for storing the user's personal data, and system services do not have 
      access to it.
   [] The application's internal storage is called private because it is only accessible to the application that created it. The data stored there is 
      not accessible to other applications or the user directly.
3. What is FileProvider and how is it used?
   [] FileProvider is a tool for direct access to the files of any application on the device.
   [] FileProvider is a service for synchronizing files between multiple devices.
   [] FileProvider is a class that allows an application to grant access to its private files to other applications in a secure way, without disclosing direct 
      file paths. It is used, for example, to send files to other applications or to grant the camera access to save pictures.
4. Is arbitrary access to the files of other applications possible in Android?
   [] Yes, arbitrary access to the files of other applications is possible if the application requests and receives the appropriate permissions.
   [] No, arbitrary access to the files of other applications in Android is not possible by default for security reasons. Applications run in isolated 
      "sandboxes", and access to foreign files is carried out only through specially provided content providers.
   [] Arbitrary access is possible only for system applications, but not for third-party ones.
5. What types of files can be used in MediaStore?
   [] Only text files and documents can be stored in MediaStore.
   [] MediaStore is used to store any type of file, but only if they are in the application's internal storage.
   [] MediaStore is designed to store media files, such as images (JPG, PNG), videos (MP4, MOV), audio (MP3, WAV), as well as metadata about them.
6. What are the advantages of using the Storage Access Framework?
   [] The Storage Access Framework (SAF) allows an application to directly access the private files of other applications without the user's consent.
   [] SAF provides automatic encryption of all files stored on the device.
   [] SAF provides a unified and secure way for users and applications to interact with files and documents stored in various sources (local storage, cloud 
      services), without the need to request broad storage permissions. The user chooses which files and from where to provide to the application.
