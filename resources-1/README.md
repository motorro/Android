# Вебинар 8. Ресурсы. Часть 1

***[Read in English](#webinar-8-resources-part-1)***

Демо-приложение к вебинару Resources #1

## Содержание

1. Без ресурсов
2. Первый текстовый ресурс
3. Локализация и квалификаторы
4. Форматирование
5. Векторные картинки
6. Размеры

## Вопросы для самопроверки

1. Что такое ресурсы и в чем преимущество их использования?
   [] Ресурсы - это системные библиотеки Android, которые приложение использует для доступа к базовым функциям.
   [] Ресурсы - это фоновые процессы, которые управляют жизненным циклом компонентов приложения.
   [] Ресурсы - это внешние файлы (изображения, строки, макеты и т.д.), которые отделены от кода приложения. Преимущества включают легкую локализацию, поддержку 
      разных конфигураций устройств (экраны, ориентация), и упрощение поддержки и обновления приложения.
2. Какие типы ресурсов вы знаете?
   [] Функции, методы, интерфейсы.
   [] Базы данных, сетевые запросы, внутренние классы.
   [] Строки (strings), макеты (layouts), изображения (drawables), цвета (colors), стили и темы (styles and themes), размеры (dimensions), сырые ресурсы (raw), 
      анимации (animations), меню (menus).
3. Какие квалификаторы ресурсов вы знаете и для чего они применяются?
   [] Квалификаторы определяют порядок загрузки ресурсов при запуске приложения.
   [] Квалификаторы позволяют Android выбирать наиболее подходящие ресурсы для текущей конфигурации устройства. 
      Примеры: `ru` (русский язык), `en` (английский язык), `land` (ландшафтная ориентация), `port` (портретная ориентация), `hdpi` (плотность экрана), 
      `sw600dp` (ширина экрана 600dp и более).
   [] Квалификаторы используются для ограничения доступа к ресурсам из разных модулей приложения.
4. Какие способы форматирования текста вы знаете?
   [] Форматирование текста достигается путем создания отдельных строковых ресурсов для каждого варианта текста.
   [] Для форматирования текста в Android используются только внешние CSS-файлы.
   [] Форматирование строк с помощью плейсхолдеров (например, `%s`, `%d`), использование HTML-тегов внутри строковых ресурсов для базового форматирования 
      (например, `<b>`, `<i>`), и использование SpannableString для более сложного форматирования в коде.
5. Как получить доступ к ресурсам из кода?
   [] Доступ к ресурсам осуществляется путем прямого чтения файлов из файловой системы устройства.
   [] Для доступа к ресурсам необходимо создать экземпляр класса `ResourceLoader`.
   [] Доступ к ресурсам осуществляется через класс `R`. Например, `R.string.my_string`, `R.layout.my_layout`, `R.drawable.my_image`. Для получения значения 
      ресурса используются методы `Context` такие как `getString(R.string.my_string)`, `getDrawable(R.drawable.my_image)`.
6. Какие преимущества дает использование векторных изображений?
   [] Векторные изображения загружаются быстрее, чем растровые, независимо от их сложности.
   [] Векторные изображения масштабируются без потери качества на любых размерах экрана, занимают меньше места в APK по сравнению с растровыми изображениями для 
      разных плотностей, и их легко изменять (цвет, размер) без пересоздания файла.
   [] Векторные изображения поддерживают более широкий спектр цветовых пространств.
7. Какие единицы измерения используются в ресурсах?
   [] Только `px` (пиксели) для всех видов размеров.
   [] `pt` (points) и `pc` (picas) для всех видов размеров.
   [] `dp` (density-independent pixels) для размеров UI-элементов (масштабируются в зависимости от плотности экрана), `sp` (scale-independent pixels) 
      для размеров текста (масштабируются в зависимости от плотности экрана и настроек размера шрифта пользователя), `px` (pixels) для абсолютных размеров, 
      `in` (inches) и `mm` (millimeters) для физических размеров.

---

# Webinar 8. Resources. Part 1

Demo application for the Resources #1 webinar

## Contents

1. Without resources
2. First text resource
3. Localization and qualifiers
4. Formatting
5. Vector images
6. Dimensions

## Self-check questions

1. What are resources and what are the advantages of using them?
   [] Resources are system libraries of Android that the application uses to access basic functions.
   [] Resources are background processes that manage the lifecycle of application components.
   [] Resources are external files (images, strings, layouts, etc.) that are separated from the application code. Advantages include easy localization, support 
      for different device configurations (screens, orientation), and simplification of application maintenance and updates.
2. What types of resources do you know?
   [] Functions, methods, interfaces.
   [] Databases, network requests, inner classes.
   [] Strings, layouts, drawables, colors, styles and themes, dimensions, raw resources, animations, menus.
3. What resource qualifiers do you know and what are they used for?
   [] Qualifiers determine the order in which resources are loaded when the application starts.
   [] Qualifiers allow Android to select the most appropriate resources for the current device configuration. Examples: `ru` (Russian), `en` (English), `land` 
      (landscape orientation), `port` (portrait orientation), `hdpi` (screen density), `sw600dp` (screen width 600dp or more).
   [] Qualifiers are used to restrict access to resources from different application modules.
4. What text formatting methods do you know?
   [] Text formatting is achieved by creating separate string resources for each text variant.
   [] Only external CSS files are used for text formatting in Android.
   [] String formatting using placeholders (e.g., `%s`, `%d`), using HTML tags within string resources for basic formatting (e.g., `<b>`, `<i>`), and using 
      SpannableString for more complex formatting in code.
5. How to access resources from code?
   [] Resources are accessed by directly reading files from the device's file system.
   [] To access resources, you need to create an instance of the `ResourceLoader` class.
   [] Resources are accessed through the `R` class. For example, `R.string.my_string`, `R.layout.my_layout`, `R.drawable.my_image`. To get the value of 
      a resource, `Context` methods such as `getString(R.string.my_string)` and `getDrawable(R.drawable.my_image)` are used.
6. What are the advantages of using vector images?
   [] Vector images load faster than raster images, regardless of their complexity.
   [] Vector images scale without loss of quality on any screen size, take up less space in the APK compared to raster images for different densities, and are 
      easy to modify (color, size) without recreating the file.
   [] Vector images support a wider range of color spaces.
7. What units of measurement are used in resources?
   [] Only `px` (pixels) for all types of dimensions.
   [] `pt` (points) and `pc` (picas) for all types of dimensions.
   [] `dp` (density-independent pixels) for UI element dimensions (scale depending on screen density), `sp` (scale-independent pixels) for text sizes (scale 
      depending on screen density and user's font size settings), `px` (pixels) for absolute sizes, `in` (inches) and `mm` (millimeters) for physical sizes.
