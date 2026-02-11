# Вебинар 9. View - 1

***[Read in English](#webinar-9-view---1)***

1. Общая информация о компонентах View в Android
2. Создание View в коде и в XML, получение ссылок на View
3. Работа с View в коде, View Binding
4. Группы View: ViewGroup, LinearLayout
5. ConstraintLayout

## Вопросы для самопроверки

1. Что такое View в Android?
   [] View - это базовый класс для всех компонентов пользовательского интерфейса в Android, таких как кнопки, текстовые поля и изображения.
   [] View - это фоновый поток, выполняющий длительные операции.
   [] View - это компонент, отвечающий за хранение данных в приложении.
2. Какие два вида основных компонентов View существуют в Android?
   [] Activity и Fragment.
   [] Service и BroadcastReceiver.
   [] View (виджеты) и ViewGroup (контейнеры).
3. Как создать View в Activity из XML?
   [] View создается путем вызова конструктора View в методе `onCreate()` Activity.
   [] View создается путем объявления тега View в файле макета XML (например, `res/layout/activity_main.xml`) и затем его раздувания с помощью `setContentView()`.
   [] View создается путем загрузки его из базы данных.
4. Как создать View в Activity из кода?
   [] View создается путем объявления тега View в файле XML и его последующей загрузки.
   [] View создается путем вызова конструктора класса View (например, `val myButton = Button(this)`), а затем добавления его в ViewGroup.
   [] View создается автоматически при запуске Activity.
5. Как получить ссылку на View, созданное в XML?
   [] Ссылка на View получается путем создания нового экземпляра View в коде.
   [] Ссылка на View получается с помощью метода `findViewById(R.id.my_view_id)` после того, как макет был раздут (`setContentView()`).
   [] Ссылка на View получается путем обращения к ней по имени тега XML.
6. Что такое View Binding?
   [] View Binding - это библиотека для выполнения сетевых запросов.
   [] View Binding - это функция, которая позволяет напрямую обращаться к View в коде без необходимости использования `findViewById()`, генерируя класс
      связывания для каждого файла макета XML.
   [] View Binding - это способ анимации View.
7. Какая группа View используется для организации других View и располагает их друг за другом?
   [] ConstraintLayout.
   [] RelativeLayout.
   [] LinearLayout.
8. Какая группа View используется для организации других View и располагает при помощи привязок друг к другу?
   [] LinearLayout.
   [] FrameLayout.
   [] ConstraintLayout.
9. Какие основные атрибуты используются для настройки расположения View в LinearLayout?
   [] `layout_constraint*` атрибуты.
   [] `android:layout_weight`, `android:orientation`, `android:gravity`, `android:layout_width`, `android:layout_height`.
   [] `app:srcCompat` и `android:text`.
10. Какие основные атрибуты используются для настройки расположения View в ConstraintLayout?
    [] `android:orientation` и `android:gravity`.
    [] `android:layout_alignParent*` атрибуты.
    [] `app:layout_constraintTop_toTopOf`, `app:layout_constraintBottom_toBottomOf`, `app:layout_constraintStart_toStartOf`, `app:layout_constraintEnd_toEndOf`
       и другие `layout_constraint*` атрибуты, а также `android:layout_width`, `android:layout_height`.

---

# Webinar 9. View - 1

1. General information about View components in Android
2. Creating a View in code and in XML, getting references to a View
3. Working with a View in code, View Binding
4. View groups: ViewGroup, LinearLayout
5. ConstraintLayout

## Self-check questions

1. What is a View in Android?
   [] A View is the base class for all user interface components in Android, such as buttons, text fields, and images.
   [] A View is a background thread that performs long-running operations.
   [] A View is a component responsible for storing data in an application.
2. What are the two main types of View components in Android?
   [] Activity and Fragment.
   [] Service and BroadcastReceiver.
   [] View (widgets) and ViewGroup (containers).
3. How to create a View in an Activity from XML?
   [] A View is created by calling the View constructor in the Activity's `onCreate()` method.
   [] A View is created by declaring a View tag in an XML layout file (e.g., `res/layout/activity_main.xml`) and then inflating it with `setContentView()`.
   [] A View is created by loading it from a database.
4. How to create a View in an Activity from code?
   [] A View is created by declaring a View tag in an XML file and then loading it.
   [] A View is created by calling the constructor of the View class (e.g., `val myButton = Button(this)`) and then adding it to a ViewGroup.
   [] A View is created automatically when the Activity starts.
5. How to get a reference to a View created in XML?
   [] A reference to a View is obtained by creating a new instance of the View in code.
   [] A reference to a View is obtained using the `findViewById(R.id.my_view_id)` method after the layout has been inflated (`setContentView()`).
   [] A reference to a View is obtained by referring to it by its XML tag name.
6. What is View Binding?
   [] View Binding is a library for making network requests.
   [] View Binding is a feature that allows you to directly access Views in code without needing to use `findViewById()`, by generating a binding class 
      for each XML layout file.
   [] View Binding is a way to animate Views.
7. Which View group is used to organize other Views and arrange them one after another?
   [] ConstraintLayout.
   [] RelativeLayout.
   [] LinearLayout.
8. Which View group is used to organize other Views and arrange them using constraints to each other?
   [] LinearLayout.
   [] FrameLayout.
   [] ConstraintLayout.
9. What are the main attributes used to configure the layout of a View in LinearLayout?
   [] `layout_constraint*` attributes.
   [] `android:layout_weight`, `android:orientation`, `android:gravity`, `android:layout_width`, `android:layout_height`.
   [] `app:srcCompat` and `android:text`.
10. What are the main attributes used to configure the layout of a View in ConstraintLayout?
    [] `android:orientation` and `android:gravity`.
    [] `android:layout_alignParent*` attributes.
    [] `app:layout_constraintTop_toTopOf`, `app:layout_constraintBottom_toBottomOf`, `app:layout_constraintStart_toStartOf`, `app:layout_constraintEnd_toEndOf` 
       and other `layout_constraint*` attributes, as well as `android:layout_width`, `android:layout_height`.
