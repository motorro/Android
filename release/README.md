# Вебинар 37. Подготовка к выпуску (Release)

***[Read in English](#webinar-37-preparing-to-release)***

1. Стратегии версионирования (Semantic Versioning, Timestamp).
2. Версия приложения Android: `versionCode` и `versionName`.
3. Уровни Android SDK (`compileSdk`, `targetSdk`, `minSdk`).
4. Подпись приложения и безопасность ключей.
5. Оптимизация и минификация кода с помощью R8.

## Вопросы для самопроверки:

1. В чем заключается основное техническое требование Google Play к `versionCode` при обновлении приложения, и чем он отличается от `versionName`?
   [] `versionCode` — это целое положительное число, которое должно строго увеличиваться с каждым новым обновлением для управления версиями в магазине. 
      `versionName` — это строка для отображения пользователям, формат которой определяет разработчик (например, 1.0.0).
   [] `versionCode` и `versionName` — это идентичные строковые параметры. Оба должны меняться при каждом релизе, чтобы пользователь видел изменения.
   [] `versionCode` — это дата сборки, которая генерируется автоматически. `versionName` — это внутренний идентификатор для Google Play, который никогда 
      не должен меняться.

2. За что отвечает параметр `targetSdkVersion` в конфигурации сборки?
   [] Это версия Android SDK, которая используется компилятором для сборки проекта. Она определяет, какие классы и методы доступны разработчику при написании кода.
   [] Это декларация того, что приложение протестировано на указанной версии Android. Система использует этот параметр для включения или отключения режимов 
      обратной совместимости (игра по правилам конкретной версии ОС).
   [] Это минимальная версия Android, на которую может быть установлено приложение. Если версия ОС на устройстве ниже этого значения, установка будет невозможна.

3. Какие основные задачи выполняет инструмент R8 при сборке Release-версии приложения?
   [] R8 занимается только подписью приложения криптографическим ключом и генерацией `versionCode` на основе истории Git.
   [] R8 используется для компиляции ресурсов (картинок и layout-файлов) в бинарный формат для ускорения загрузки, но не трогает исходный код классов.
   [] R8 выполняет минимизацию (удаление неиспользуемого кода и ресурсов), обфускацию (переименование классов и методов для защиты и уменьшения размера) 
      и оптимизацию (улучшение производительности кода).

4. Почему код, использующий Reflection (динамический вызов классов/методов по имени), может падать с ошибкой в Release-сборке после обработки R8, 
   и как это исправить?
   [] Reflection запрещен в Android на уровне системы безопасности в релизных сборках. Исправить это нельзя, нужно переписывать код без Reflection.
   [] R8 не видит прямых ссылок на классы, вызываемые через Reflection, и может удалить их как неиспользуемые или переименовать (обфусцировать). Исправить это 
      можно, добавив правило `-keep` в файл `proguard-rules.pro`.
   [] R8 удаляет все строковые константы из кода для экономии места, поэтому имя класса просто исчезает. Нужно включить флаг `isShrinkStrings = false`.

5. Какая практика хранения ключей подписи (Keystore) для релизных сборок является правильной с точки зрения безопасности?
   [] Ключи нужно хранить в корне проекта и коммитить в Git, чтобы любой член команды мог собрать релизную версию на своем компьютере.
   [] Ключи и пароли нельзя хранить в системе контроля версий (Git). Их следует выносить в отдельные файлы (например, local.properties) или переменные окружения, 
      добавленные в `.gitignore`.
   [] Ключи нужно вшивать прямо в код `build.gradle.kts` в открытом виде, так как Gradle автоматически шифрует их при сборке.

---

# Webinar 37. Preparing to Release

1.  Versioning strategies (Semantic Versioning, Timestamp).
2.  Android application version: `versionCode` and `versionName`.
3.  Android SDK levels (`compileSdk`, `targetSdk`, `minSdk`).
4.  Application signing and key security.
5.  Code optimization and minification with R8.

## Self-check questions:

1.  What is the main technical requirement of Google Play for `versionCode` when updating an application, and how does it differ from `versionName`?
    [] `versionCode` is a positive integer that must strictly increase with each new update to manage versions in the store. `versionName` is a string 
    for display to users, the format of which is determined by the developer (e.g., 1.0.0).
    [] `versionCode` and `versionName` are identical string parameters. Both must change with each release so that the user sees the changes.
    [] `versionCode` is the build date, which is generated automatically. `versionName` is an internal identifier for Google Play that should never change.

2.  What is the `targetSdkVersion` parameter in the build configuration responsible for?
    [] This is the version of the Android SDK that the compiler uses to build the project. It determines which classes and methods are available to the developer 
    when writing code.
    [] It is a declaration that the application has been tested on the specified version of Android. The system uses this parameter to enable or disable backward 
    compatibility modes (playing by the rules of a specific OS version).
    [] This is the minimum version of Android on which the application can be installed. If the OS version on the device is lower than this value, installation 
    will be impossible.

3.  What are the main tasks performed by the R8 tool when building a Release version of an application?
    [] R8 is only responsible for signing the application with a cryptographic key and generating the `versionCode` based on the Git history.
    [] R8 is used to compile resources (images and layout files) into a binary format to speed up loading, but it does not touch the source code of the classes.
    [] R8 performs minimization (removing unused code and resources), obfuscation (renaming classes and methods for protection and size reduction), 
    and optimization (improving code performance).

4.  Why can code using Reflection (dynamic invocation of classes/methods by name) crash with an error in a Release build after being processed by R8, and how 
    can this be fixed?
    [] Reflection is prohibited in Android at the security level in release builds. This cannot be fixed; the code must be rewritten without Reflection.
    [] R8 does not see direct references to classes called via Reflection and may remove them as unused or rename (obfuscate) them. This can be fixed by adding 
    a `-keep` rule to the `proguard-rules.pro` file.
    [] R8 removes all string constants from the code to save space, so the class name simply disappears. You need to enable the `isShrinkStrings = false` flag.

5.  What is the correct practice for storing signing keys (Keystore) for release builds from a security point of view?
    [] Keys should be stored in the project root and committed to Git so that any team member can build a release version on their computer.
    [] Keys and passwords should not be stored in a version control system (Git). They should be moved to separate files (e.g., `local.properties`) or environment 
    variables added to `.gitignore`.
    [] Keys should be embedded directly into the `build.gradle.kts` code in plain text, as Gradle automatically encrypts them during the build.
