# Вебинар 16. Навигация с помощью Android Jetpack Navigation

***[Read in English](#webinar-16-navigation-with-android-jetpack-navigation)***

1. Что такое навигационный граф?
2. Как создать навигационный граф?
3. Интеграция компонентов Android View с навигацией.
4. Безопасная передача аргументов между фрагментами.
5. Дочерние навигационные графы.
6. Deep-links

## Вопросы для самопроверки:

1. Что такое навигационный граф?
   [] Навигационный граф - это XML-файл, который описывает все возможные пути навигации в приложении, включая пункты назначения (destinations) 
      и действия (actions) между ними.
   [] Навигационный граф - это база данных, которая хранит все данные о пользователях приложения.
   [] Навигационный граф - это набор классов Java/Kotlin, которые управляют жизненным циклом Activity.
2. Как создать навигационный граф?
   [] Навигационный граф создается автоматически при создании нового Android проекта.
   [] Навигационный граф создается в папке `res/navigation` как новый ресурсный файл (`.xml`).
   [] Навигационный граф создается путем добавления зависимостей в файл `build.gradle`.
3. Как связать навигационный граф с Activity?
   [] Навигационный граф связывается с Activity через файл `AndroidManifest.xml`.
   [] Навигационный граф связывается с Activity путем добавления `NavHostFragment` в макет Activity. `NavHostFragment` является контейнером для навигационного 
      графа и его пунктов назначения.
   [] Навигационный граф связывается с Activity с помощью вызова метода `bindNavigationGraph()` в `onCreate()` Activity.
4. Как связать навигационный граф с BottomNavigationView?
   [] Навигационный граф связывается с `BottomNavigationView` путем добавления слушателя нажатий на каждый элемент меню.
   [] Навигационный граф связывается с `BottomNavigationView` с помощью метода расширения `setupWithNavController()` из библиотеки `NavigationUI`, который 
      автоматически обрабатывает выбор элементов меню и соответствующую навигацию.
   [] Навигационный граф связывается с `BottomNavigationView` путем переопределения метода `onNavigationItemSelected()`.
5. Как связать навигационный граф с Toolbar?
   [] Навигационный граф связывается с `Toolbar` путем установки слушателя кликов на кнопку "назад".
   [] Навигационный граф связывается с `Toolbar` с помощью метода расшижения `setupWithNavController()` из библиотеки `NavigationUI`. Это обеспечивает 
      автоматическую настройку кнопки "назад" и отображение заголовка текущего пункта назначения.
   [] Навигационный граф связывается с `Toolbar` через глобальные переменные приложения.
6. Каким образом можно передавать аргументы между фрагментами?
   [] Аргументы передаются между фрагментами с использованием глобальных статических полей.
   [] Аргументы передаются между фрагментами с помощью `Bundle` и `setArguments()`, либо безопаснее и предпочтительнее с помощью Safe Args плагина, который 
      генерирует классы для безопасной передачи типов.
   [] Аргументы передаются между фрагментами через базу данных.

---

# Webinar 16. Navigation with Android Jetpack Navigation

1. What is a navigation graph?
2. How to create a navigation graph?
3. Integrating Android View components with navigation.
4. Safe argument passing between fragments.
5. Nested navigation graphs.
6. Deep-links

## Self-check questions:

1. What is a navigation graph?
   [] A navigation graph is an XML file that describes all possible navigation paths in an application, including destinations and actions between them.
   [] A navigation graph is a database that stores all application user data.
   [] A navigation graph is a set of Java/Kotlin classes that manage the Activity lifecycle.
2. How to create a navigation graph?
   [] A navigation graph is created automatically when a new Android project is created.
   [] A navigation graph is created in the `res/navigation` folder as a new resource file (`.xml`).
   [] A navigation graph is created by adding dependencies to the `build.gradle` file.
3. How to link a navigation graph with an Activity?
   [] A navigation graph is linked with an Activity through the `AndroidManifest.xml` file.
   [] A navigation graph is linked with an Activity by adding a `NavHostFragment` to the Activity's layout. The `NavHostFragment` is a container for 
      the navigation graph and its destinations.
   [] A navigation graph is linked with an Activity by calling the `bindNavigationGraph()` method in the Activity's `onCreate()`.
4. How to link a navigation graph with a BottomNavigationView?
   [] A navigation graph is linked with a `BottomNavigationView` by adding a click listener to each menu item.
   [] A navigation graph is linked with a `BottomNavigationView` using the `setupWithNavController()` extension method from the `NavigationUI` library, which 
      automatically handles menu item selection and corresponding navigation.
   [] A navigation graph is linked with a `BottomNavigationView` by overriding the `onNavigationItemSelected()` method.
5. How to link a navigation graph with a Toolbar?
   [] A navigation graph is linked with a `Toolbar` by setting a click listener on the back button.
   [] A navigation graph is linked with a `Toolbar` using the `setupWithNavController()` extension method from the `NavigationUI` library. This provides 
      automatic back button setup and displays the title of the current destination.
   [] A navigation graph is linked with a `Toolbar` through global application variables.
6. How can arguments be passed between fragments?
   [] Arguments are passed between fragments using global static fields.
   [] Arguments are passed between fragments using a `Bundle` and `setArguments()`, or more safely and preferably using the Safe Args plugin, which generates 
      classes for type-safe passing.
   [] Arguments are passed between fragments through a database.
