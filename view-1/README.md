# Вебинар 9. View - 1

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