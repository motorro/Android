# Вебинар 10. Стили и темы

***[Read in English](#webinar-10-styles-and-themes)***

1. Стили и темы. Что это такое, и как они работают.
2. Стили. Как их создавать и использовать.
3. Темы. Создание и использование.
4. Атрибуты компонентов и их приоритеты.

## Вопросы для самопроверки

1. Что такое стиль и для каких типов ресурсов он используется?
   [] Стиль - это класс Java, который управляет логикой приложения.
   [] Стиль - это набор атрибутов, который определяет внешний вид и форматирование одного View или ViewGroup. Он используется для таких типов ресурсов, 
      как текстовые поля, кнопки, макеты и другие элементы пользовательского интерфейса.
   [] Стиль - это способ организации файлов в проекте Android.
2. Как создать стиль?
   [] Стиль создается путем вызова метода `createStyle()` в коде Java.
   [] Стиль создается в XML-файле, обычно `res/values/styles.xml`, с использованием тега `<style>` и определением атрибутов внутри него.
   [] Стиль создается автоматически Android Studio при компиляции проекта.
3. Как применить стиль к компоненту?
   [] Стиль применяется к компоненту в XML-макете с помощью атрибута `style="@style/MyStyle"`.
   [] Стиль применяется к компоненту путем наследования от него в коде Java.
   [] Стиль применяется к компоненту через настройки проекта в Gradle.
4. Что такое тема и как она связана со стилями?
   [] Тема - это просто набор иконок для приложения.
   [] Тема - это коллекция стилей, которая применяется ко всему приложению или Activity, а не к отдельному View. Темы и стили связаны тем, что тема может 
      наследовать от других тем и переопределять или дополнять их стили. Тема также предоставляет значения для атрибутов, на которые ссылаются стили.
   [] Тема - это способ управления сетевыми запросами в приложении.
5. Каковы области действия стилей и тем?
   [] Стиль действует только на View, созданные в коде, а тема - только на View, созданные в XML.
   [] Стиль применяется к отдельному View или ViewGroup. Тема применяется ко всему приложению (в манифесте), к конкретной Activity (в манифесте) или к части 
      иерархии View (в коде с помощью `ContextThemeWrapper` или в XML с помощью `<androidx.appcompat.widget.ThemeUtils>` или подобного).
   [] Стиль и тема имеют глобальную область действия и применяются ко всем приложениям на устройстве.
6. Как создать тему?
   [] Тема создается с помощью специального инструмента в Android Studio, который генерирует код.
   [] Тема создается аналогично стилю в XML-файле (`res/values/themes.xml`) с использованием тега `<style>`, но она обычно наследуется от существующей темы 
      Android (например, `Theme.MaterialComponents.DayNight.NoActionBar`) и переопределяет или добавляет атрибуты темы.
   [] Тема создается путем изменения фонового цвета каждой Activity в приложении.
7. Как применить тему к приложению?
   [] Тема применяется ко всему приложению путем вызова метода `applyTheme()` в главном классе приложения.
   [] Тема применяется ко всему приложению путем добавления атрибута `android:theme="@style/MyApplicationTheme"` к тегу `<application>` 
      в файле `AndroidManifest.xml`.
   [] Тема применяется к приложению автоматически, основываясь на настройках устройства пользователя.
8. Как создать собственный атрибут темы?
   [] Собственный атрибут темы создается путем добавления новой переменной в класс `R`.
   [] Собственный атрибут темы создается в XML-файле (`res/values/attrs.xml`) с использованием тега `<attr>` внутри тега `<declare-styleable>`, а затем 
      используется в теме.
   [] Собственный атрибут темы создается путем модификации системных файлов Android.

---

# Webinar 10. Styles and Themes

1. Styles and themes. What they are and how they work.
2. Styles. How to create and use them.
3. Themes. Creating and using them.
4. Component attributes and their priorities.

## Self-check questions

1. What is a style and for what types of resources is it used?
   [] A style is a Java class that manages the application's logic.
   [] A style is a collection of attributes that defines the appearance and formatting of a single View or ViewGroup. It is used for resource types such as text
      fields, buttons, layouts, and other user interface elements.
   [] A style is a way of organizing files in an Android project.
2. How to create a style?
   [] A style is created by calling the `createStyle()` method in Java code.
   [] A style is created in an XML file, usually `res/values/styles.xml`, using the `<style>` tag and defining attributes within it.
   [] A style is created automatically by Android Studio when the project is compiled.
3. How to apply a style to a component?
   [] A style is applied to a component in the XML layout using the `style="@style/MyStyle"` attribute.
   [] A style is applied to a component by inheriting from it in Java code.
   [] A style is applied to a component through the project settings in Gradle.
4. What is a theme and how is it related to styles?
   [] A theme is just a set of icons for the application.
   [] A theme is a collection of styles that is applied to an entire application or Activity, rather than to an individual View. Themes and styles are related 
      in that a theme can inherit from other themes and override or supplement their styles. A theme also provides values for attributes that styles refer to.
   [] A theme is a way to manage network requests in an application.
5. What are the scopes of styles and themes?
   [] A style only affects Views created in code, and a theme only affects Views created in XML.
   [] A style is applied to an individual View or ViewGroup. A theme is applied to an entire application (in the manifest), to a specific Activity 
     (in the manifest), or to a part of the View hierarchy (in code using `ContextThemeWrapper` or in XML using `<androidx.appcompat.widget.ThemeUtils>` 
     or similar).
   [] A style and a theme have a global scope and are applied to all applications on the device.
6. How to create a theme?
   [] A theme is created using a special tool in Android Studio that generates code.
   [] A theme is created similarly to a style in an XML file (`res/values/themes.xml`) using the `<style>` tag, but it usually inherits from an existing Android 
      theme (e.g., `Theme.MaterialComponents.DayNight.NoActionBar`) and overrides or adds theme attributes.
   [] A theme is created by changing the background color of each Activity in the application.
7. How to apply a theme to an application?
   [] A theme is applied to the entire application by calling the `applyTheme()` method in the main application class.
   [] A theme is applied to the entire application by adding the `android:theme="@style/MyApplicationTheme"` attribute to the `<application>` tag 
      in the `AndroidManifest.xml` file.
   [] A theme is applied to the application automatically based on the user's device settings.
8. How to create a custom theme attribute?
   [] A custom theme attribute is created by adding a new variable to the `R` class.
   [] A custom theme attribute is created in an XML file (`res/values/attrs.xml`) using the `<attr>` tag inside a `<declare-styleable>` tag, and then used 
      in the theme.
   [] A custom theme attribute is created by modifying the Android system files.
