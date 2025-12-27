# 🚀 Репозиторий курса "Программирование под ОС Андроид"

***[Read in English](#-android-programming-course-repository)***

![Merion Academy](readme/logo_merion.svg)

Этот репозиторий - часть **полного курса "Android-разработчик с нуля"**, размещенного на платформе Merion Academy.

Курс содержит **29 лекций** и **22 часа видео** с детальным разбором заданий и примеров, и представляет собой полное руководство для старта в Android-разработке.

[**Перейти на страницу курса**](https://wiki.merionet.ru/merion-academy/course/android-razrabotchik-s-nulya)

---

Репозиторий содержит полный набор практических примеров и решений к каждому вебинару и каждой практической работе курса.

## ✨ Ключевые особенности проекта

* **Проект к каждой лекции:** Для каждой темы курса подготовлен отдельный, функциональный пример проекта, который демонстрирует изучаемую концепцию.
* **Полное покрытие тем:** Код покрывает все разделы курса, от основ Android до современных архитектурных паттернов, работы с I/O, многопоточности и Jetpack
  Compose.
* **Пошаговая разработка:** Код для каждой лекции содержится в отдельной ветке. Коммиты в этой ветке отражают последовательные этапы прохождения вебинара,
  позволяя вам следить за процессом разработки шаг за шагом.

## 📖 Оглавление курса и навигация по репозиторию

Для удобства каждый вебинар и практическая работа связаны с соответствующей веткой в этом репозитории. Вы можете перейти к коду, кликнув по ссылке `[Код]` в
таблице ниже.

| №                                             | Тип материала   | Тема                                                                   | Ветка (Ссылка на код)                                                                                                |
|:----------------------------------------------|:----------------|:-----------------------------------------------------------------------|:---------------------------------------------------------------------------------------------------------------------|
| **Вводные уроки**                             |                 |                                                                        |                                                                                                                      |
| 1                                             | Вводный урок    | 0.1 Инструменты Android разработчика                                   | [`webinars/1.HelloWorld`](https://github.com/motorro/Android/tree/webinars/1.HelloWorld)                             |
| 2                                             | Вводный урок    | 0.2 ОС Android и приложение. Основные компоненты                       | [`webinars/2.Android`](https://github.com/motorro/Android/tree/webinars/2.Android)                                   |
| 3                                             | Вводный урок    | 0.3 Пример архитектуры Android-приложения, поток данных                | [`webinars/3.Android_app_architecture`](https://github.com/motorro/Android/tree/webinars/3.Android-App-Architecture) |
| **1. Классический Android**                   |                 |                                                                        |                                                                                                                      |
| 4                                             | Основные лекции | 1.1 Activity: Intent, Result contracts, запрос разрешений              | [`webinars/4.Activity`](https://github.com/motorro/Android/tree/webinars/4.Activity)                                 |
| 5                                             | Основные лекции | 1.2 Activity: Жизненный цикл - Lifecycle                               | [`webinars/5.Lifecycle`](https://github.com/motorro/Android/tree/webinars/5.Lifecycle)                               |
| 6                                             | Основные лекции | 1.3 Activity: Навигация в Activity, Task, Backstack                    | [`webinars/6.Activity-Navigation`](https://github.com/motorro/Android/tree/webinars/6.Activity-Navigation)           |
| 7                                             | Практика        | Практическая работа: Activity                                          | [`practice/1.Activity`](https://github.com/motorro/Android/tree/practice/1.Activity)                                 |
| 8                                             | Основные лекции | 1.4. Ресурсы: обзор, квалификаторы, строки, картинки                   | [`webinars/7.Resources-1`](https://github.com/motorro/Android/tree/webinars/7.Resources-1)                           |
| 9                                             | Основные лекции | 1.5. View: основные компоненты и группы View                           | [`webinars/9.View-1`](https://github.com/motorro/Android/tree/webinars/9.View-1)                                     |
| 10                                            | Основные лекции | 1.6. Ресурсы: стили, темы                                              | [`webinars/10.Resources-2`](https://github.com/motorro/Android/tree/webinars/10.Resources-2)                         |
| 11                                            | Основные лекции | 1.7. View: составные и собственные компоненты                          | [`webinars/11.View-2`](https://github.com/motorro/Android/tree/webinars/11.View-2)                                   |
| 12                                            | Основные лекции | 1.8. RecyclerView - списки и архитектура приложения с виджетами        | [`webinars/12.RecyclerView`](https://github.com/motorro/Android/tree/webinars/12.RecyclerView)                       |
| 13                                            | Практика        | Практическая работа: Верстка интерфейса                                | [`practice/2.Layout`](https://github.com/motorro/Android/tree/practice/2.Layout)                                     |
| 14                                            | Основные лекции | 1.9. State & logic в классическом Андроид приложении, ViewModel...     | [`webinars/14.State-classic`](https://github.com/motorro/Android/tree/webinars/14.State-classic)                     |
| 15                                            | Основные лекции | 1.10. Fragment: Lifecycle, Fragment Manager                            | [`webinars/15.Fragments`](https://github.com/motorro/Android/tree/webinars/15.Fragments)                             |
| 16                                            | Основные лекции | 1.11. Навигация в Андроид-приложении и экранный подход                 | [`webinars/16.Navigation`](https://github.com/motorro/Android/tree/webinars/16.Navigation)                           |
| 17                                            | Практика        | Практическая работа: Навигация и архитектура MVVM                      | [`practice/3.Navigation`](https://github.com/motorro/Android/tree/practice/3.Navigation)                             |
| **2. Асинхронные операции и многопоточность** |                 |                                                                        |                                                                                                                      |
| 18                                            | Основные лекции | 2.1. Асинхорнное программирование и многопоточность                    | [`webinars/18.Multithreading`](https://github.com/motorro/Android/tree/webinars/18.Multithreading)                   |
| 19                                            | Основные лекции | 2.2. Kotlin Coroutines                                                 | [`webinars/19.Coroutines`](https://github.com/motorro/Android/tree/webinars/19.Coroutines)                           |
| 20                                            | Основные лекции | 2.3. Kotlin Flow                                                       | [`webinars/20.Flow`](https://github.com/motorro/Android/tree/webinars/20.Flow)                                       |
| 21                                            | Практика        | Практическая работа: Coroutines and Flow                               | [`practice/4.Coroutines`](https://github.com/motorro/Android/tree/practice/4.Coroutines)                             |
| **3. IO**                                     |                 |                                                                        |                                                                                                                      |
| 22                                            | Основные лекции | 3.1. Файловое хранение и обмен файлами: Диск, MediaStore, FileProvider | [`webinars/22.File-Storage`](https://github.com/motorro/Android/tree/webinars/22.File-Storage)                       |
| 23                                            | Основные лекции | 3.2. Объектное хранение: DataStore, SQLite, Room                       | [`webinars/23.Object-Storage`](https://github.com/motorro/Android/tree/webinars/23.Object-Storage)                   |
| 24                                            | Основные лекции | 3.3. HTTP запросы в сеть                                               | [`webinars/24.Network`](https://github.com/motorro/Android/tree/webinars/24.Network)                                 |
| 25                                            | Основные лекции | 3.4. Repository                                                        | [`webinars/25.Repository`](https://github.com/motorro/Android/tree/webinars/25.Repository)                           |
| 26                                            | Практика        | Проектная работа. Часть 1: IO                                          | [`practice/5.IO`](https://github.com/motorro/Android/tree/practice/5.IO)                                             |
| **4. Архитектурная секция**                   |                 |                                                                        |                                                                                                                      |
| 27                                            | Основные лекции | 4.1. Составные части приложения - разделение ответственности, модули   | [`webinars/27.Architecture`](https://github.com/motorro/Android/tree/webinars/27.Architecture)                       |
| 28                                            | Основные лекции | 4.2. Управление зависимостями, DI                                      | [`webinars/28.DI`](https://github.com/motorro/Android/tree/webinars/28.DI)                                           |
| 29                                            | Практика        | Проектная работа. Часть 2: архитектура и разделение ответственности    | [`practice/6.DI`](https://github.com/motorro/Android/tree/practice/6.DI)                                             |
| **5. Jetpack Compose**                        |                 |                                                                        |                                                                                                                      |
| 30                                            | Основные лекции | 5.1. Jetpack Compose. Смена парадигмы View                             | [`webinars/30.Compose-view`](https://github.com/motorro/Android/tree/webinars/30.Compose-view)                       |
| 31                                            | Основные лекции | 5.2. Jetpack Compose. Состояние данных и side-effects                  | [`webinars/31.Compose-state`](https://github.com/motorro/Android/tree/webinars/31.Compose-state)                     |
| 32                                            | Основные лекции | 5.3. UDF архитектура и стейт-машина. Полный контроль состояния         | [`webinars/32.StateMachine`](https://github.com/motorro/Android/tree/webinars/32.StateMachine)                       |
| 33                                            | Практика        | Проектная работа. Часть 3: UDF приложение                              | [`practice/7.Compose`](https://github.com/motorro/Android/tree/practice/7.Compose)                                   |
| **6. Дополнительные компоненты приложения**   |                 |                                                                        |                                                                                                                      |
| 34                                            | Основные лекции | 6.1. Уведомления и Push                                                | [`webinars/34.Notifications`](https://github.com/motorro/Android/tree/webinars/34.Notifications)                     |
| 35                                            | Основные лекции | 6.3. Фоновые задачи                                                    | [`webinars/35.Background`](https://github.com/motorro/Android/tree/webinars/35.Background)                           |
| 36                                            | Практика        | Проектная работа. Часть 4: Фоновые задачи                              | [`practice/8.Background`](https://github.com/motorro/Android/tree/practice/8.Background)                             |
| 37                                            | Основные лекции | 6.4. Подготовка к выпуску                                              | [`webinars/37.Release`](https://github.com/motorro/Android/tree/webinars/37.Release)                                 |

## ❗ Важное замечание по Git

Для того чтобы сохранить пошаговую историю разработки (коммиты, отражающие этапы вебинара), библиотеки в этом репозитории обновляются следующим образом:

1. Библиотеки обновляются в ветке `master`.
2. Выполняется `rebase` всех веток на этот коммит.

**Чтобы сохранить правильный порядок коммитов и избежать конфликтов при обновлении вашей локальной копии, настоятельно рекомендуется не использовать `merge`, а
применять команду `git pull --rebase` при получении обновлений.**

---

# 🚀 Android Programming Course Repository

![Merion Academy](readme/logo_merion.svg)

This repository is part of the **full "Android Developer from Scratch" course**, hosted on the Merion Academy platform.

The course includes **29 lectures** and **22 hours of video** with detailed reviews of assignments and examples, and serves as a comprehensive guide for starting Android development.

[**Go to the Course Page**](https://wiki.merionet.ru/merion-academy/course/android-razrabotchik-s-nulya)

---

Thу repository contains the complete set of practical examples and solutions for every webinar and practical assignment in the course.

## ✨ Key Project Features

* **Project per Lecture:** A separate, functional example project is provided for each course topic, demonstrating the concept being studied.
* **Full Topic Coverage:** The code covers all sections of the course, from Android fundamentals to modern architectural patterns, I/O handling, multithreading,
  and Jetpack Compose.
* **Step-by-Step Development:** The code for each lecture resides in a separate branch. Commits within each branch reflect the sequential stages of the webinar,
  allowing you to follow the development process step-by-step.

## 📖 Course Contents and Repository Navigation

| №                                        | Material Type       | Topic                                                              | Branch (Code Link)                                                                                                   |
|:-----------------------------------------|:--------------------|:-------------------------------------------------------------------|:---------------------------------------------------------------------------------------------------------------------|
| **Introductory Lessons**                 |                     |                                                                    |                                                                                                                      |
| 1                                        | Introductory Lesson | 0.1 Android Developer Tools                                        | [`webinars/1.HelloWorld`](https://github.com/motorro/Android/tree/webinars/1.HelloWorld)                             |
| 2                                        | Introductory Lesson | 0.2 An Application in Android OS. Main Components                  | [`webinars/2.Android`](https://github.com/motorro/Android/tree/webinars/2.Android)                                   |
| 3                                        | Introductory Lesson | 0.3 Example of Android App Architecture, Data Flow                 | [`webinars/3.Android_app_architecture`](https://github.com/motorro/Android/tree/webinars/3.Android-App-Architecture) |
| **1. Classic Android**                   |                     |                                                                    |                                                                                                                      |
| 4                                        | Core Lectures       | 1.1 Activity: Intent, Result Contracts, Permission Request         | [`webinars/4.Activity`](https://github.com/motorro/Android/tree/webinars/4.Activity)                                 |
| 5                                        | Core Lectures       | 1.2 Activity: Lifecycle                                            | [`webinars/5.Lifecycle`](https://github.com/motorro/Android/tree/webinars/5.Lifecycle)                               |
| 6                                        | Core Lectures       | 1.3 Activity: Activity Navigation, Task, Backstack                 | [`webinars/6.Activity-Navigation`](https://github.com/motorro/Android/tree/webinars/6.Activity-Navigation)           |
| 7                                        | Practice            | Practice: Activity                                                 | [`practice/1.Activity`](https://github.com/motorro/Android/tree/practice/1.Activity)                                 |
| 8                                        | Core Lectures       | 1.4. Resources: Overview, Qualifiers, Strings, Images              | [`webinars/7.Resources-1`](https://github.com/motorro/Android/tree/webinars/7.Resources-1)                           |
| 9                                        | Core Lectures       | 1.5. View: Main Components and View Groups                         | [`webinars/9.View-1`](https://github.com/motorro/Android/tree/webinars/9.View-1)                                     |
| 10                                       | Core Lectures       | 1.6. Resources: Styles, Themes                                     | [`webinars/10.Resources-2`](https://github.com/motorro/Android/tree/webinars/10.Resources-2)                         |
| 11                                       | Core Lectures       | 1.7. View: Composite and Custom Components                         | [`webinars/11.View-2`](https://github.com/motorro/Android/tree/webinars/11.View-2)                                   |
| 12                                       | Core Lectures       | 1.8. RecyclerView - Lists and App Architecture with Widgets        | [`webinars/12.RecyclerView`](https://github.com/motorro/Android/tree/webinars/12.RecyclerView)                       |
| 13                                       | Practice            | Practice: Interface Layout                                         | [`practice/2.Layout`](https://github.com/motorro/Android/tree/practice/2.Layout)                                     |
| 14                                       | Core Lectures       | 1.9. State & logic in Classic Android App, ViewModel...            | [`webinars/14.State-classic`](https://github.com/motorro/Android/tree/webinars/14.State-classic)                     |
| 15                                       | Core Lectures       | 1.10. Fragment: Lifecycle, Fragment Manager                        | [`webinars/15.Fragments`](https://github.com/motorro/Android/tree/webinars/15.Fragments)                             |
| 16                                       | Core Lectures       | 1.11. Classic screen-by-screen Navigation in Android App           | [`webinars/16.Navigation`](https://github.com/motorro/Android/tree/webinars/16.Navigation)                           |
| 17                                       | Practice            | Practice: Navigation and MVVM Architecture                         | [`practice/3.Navigation`](https://github.com/motorro/Android/tree/practice/3.Navigation)                             |
| **2. Async Operations & Multithreading** |                     |                                                                    |                                                                                                                      |
| 18                                       | Core Lectures       | 2.1. Async Programming and Multithreading                          | [`webinars/18.Multithreading`](https://github.com/motorro/Android/tree/webinars/18.Multithreading)                   |
| 19                                       | Core Lectures       | 2.2. Kotlin Coroutines                                             | [`webinars/19.Coroutines`](https://github.com/motorro/Android/tree/webinars/19.Coroutines)                           |
| 20                                       | Core Lectures       | 2.3. Kotlin Flow                                                   | [`webinars/20.Flow`](https://github.com/motorro/Android/tree/webinars/20.Flow)                                       |
| 21                                       | Practice            | Practice: Coroutines and Flow                                      | [`practice/4.Coroutines`](https://github.com/motorro/Android/tree/practice/4.Coroutines)                             |
| **3. IO**                                |                     |                                                                    |                                                                                                                      |
| 22                                       | Core Lectures       | 3.1. File Storage and File Sharing: Disk, MediaStore, FileProvider | [`webinars/22.File-Storage`](https://github.com/motorro/Android/tree/webinars/22.File-Storage)                       |
| 23                                       | Core Lectures       | 3.2. Object Storage: DataStore, SQLite, Room                       | [`webinars/23.Object-Storage`](https://github.com/motorro/Android/tree/webinars/23.Object-Storage)                   |
| 24                                       | Core Lectures       | 3.3. HTTP Network Requests                                         | [`webinars/24.Network`](https://github.com/motorro/Android/tree/webinars/24.Network)                                 |
| 25                                       | Core Lectures       | 3.4. Repository                                                    | [`webinars/25.Repository`](https://github.com/motorro/Android/tree/webinars/25.Repository)                           |
| 26                                       | Practice            | Project work. Part 1: IO                                           | [`practice/5.IO`](https://github.com/motorro/Android/tree/practice/5.IO)                                             |
| **4. Architecture**                      |                     |                                                                    |                                                                                                                      |
| 27                                       | Core Lectures       | 4.1. App Components - Separation of Concerns, Modules              | [`webinars/27.Architecture`](https://github.com/motorro/Android/tree/webinars/27.Architecture)                       |
| 28                                       | Core Lectures       | 4.2. Dependency Management, DI                                     | [`webinars/28.DI`](https://github.com/motorro/Android/tree/webinars/28.DI)                                           |
| 29                                       | Practice            | Project work. Part 2: Architecture and Separation of Concerns      | [`practice/6.DI`](https://github.com/motorro/Android/tree/practice/6.DI)                                             |
| **5. Jetpack Compose**                   |                     |                                                                    |                                                                                                                      |
| 30                                       | Core Lectures       | 5.1. Jetpack Compose. View Paradigm Shift                          | [`webinars/30.Compose-view`](https://github.com/motorro/Android/tree/webinars/30.Compose-view)                       |
| 31                                       | Core Lectures       | 5.2. Jetpack Compose. Data State and Side-effects                  | [`webinars/31.Compose-state`](https://github.com/motorro/Android/tree/webinars/31.Compose-state)                     |
| 32                                       | Core Lectures       | 5.3. UDF Architecture and State Machine. Full State Control        | [`webinars/32.StateMachine`](https://github.com/motorro/Android/tree/webinars/32.StateMachine)                       |
| 33                                       | Practice            | Project work. Part 3: UDF Application                              | [`practice/7.Compose`](https://github.com/motorro/Android/tree/practice/7.Compose)                                   |
| **6. Additional Components**             |                     |                                                                    |                                                                                                                      |
| 34                                       | Core Lectures       | 6.1. Notifications and Push                                        | [`webinars/34.Notifications`](https://github.com/motorro/Android/tree/webinars/34.Notifications)                     |
| 35                                       | Core Lectures       | 6.3. Background Tasks                                              | [`webinars/35.Background`](https://github.com/motorro/Android/tree/webinars/35.Background)                           |
| 36                                       | Practice            | Project work. Part 4: Background Tasks                             | [`practice/8.Background`](https://github.com/motorro/Android/tree/practice/8.Background)                             |
| 37                                       | Core Lectures       | 6.4. Preparing to Release                                          | [`webinars/37.Release`](https://github.com/motorro/Android/tree/webinars/37.Release)                                 |

## ❗ Important Git Note

To preserve the step-by-step history (commits reflecting webinar stages), libraries are updated as follows:

1. Libraries are updated on the `master` branch.
2. All feature branches are `rebase`d onto this commit.

**To maintain the correct commit order and avoid conflicts when updating your local copy, it is strongly recommended not to use `merge`, but rather to use the
command `git pull --rebase` when pulling updates.**
