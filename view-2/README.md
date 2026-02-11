# Вебинар 11. View-2

***[Read in English](#webinar-11-view-2)***

1. Подходы к созданию собственных компонентов View
2. Составной компонент на основе группы элементов
3. Атрибуты и стили компонентов
4. Собственный компонент View

## Вопросы для самопроверки:

1. Какие подходы к созданию собственных компонентов View вы знаете?
   [] Использование только стандартных View из Android SDK.
   [] Композиция (создание составного компонента из существующих View) и кастомное рисование (наследование от View и переопределение `onDraw()`).
   [] Только создание View через XML-файлы.
2. Как создать составной компонент на основе группы элементов?
   [] Создать новый класс, наследующий от `View`, и переопределить все методы рисования.
   [] Создать макет XML, который содержит несколько View, и затем раздуть этот макет в кастомном классе, наследующем от `ViewGroup` (например, `LinearLayout` 
      или `ConstraintLayout`).
   [] Создать отдельный класс для каждого элемента и объединить их в коде.
3. Как мы получаем значения атрибутов и стилей компонентов?
   [] Извлекаем их напрямую из XML-файла с помощью парсера.
   [] Используем методы `Context.obtainStyledAttributes()` с набором атрибутов, определенных в `declare-styleable` в `attrs.xml`, чтобы получить `TypedArray`, 
      из которого можно извлечь значения.
   [] Они передаются в конструктор View автоматически.
4. Как создать собственный компонент View?
   [] Наследовать от класса `Activity` и переопределить метод `onCreate()`.
   [] Создать новый класс, который наследует от `View` (для кастомного рисования) или от `ViewGroup` (для составного компонента), и реализовать необходимые 
      конструкторы и методы (например, `onMeasure()`, `onDraw()`).
   [] Создать Java-класс и добавить его в `AndroidManifest.xml`.
5. Какой метод отвечает за измерение размеров компонента?
   [] `onLayout()`
   [] `onDraw()`
   [] `onMeasure()`
6. Какой метод отвечает за отрисовку компонента?
   [] `onMeasure()`
   [] `onDraw()`
   [] `onLayout()`

---

# Webinar 11. View-2

1. Approaches to creating custom View components
2. Composite component based on a group of elements
3. Component attributes and styles
4. Custom View component

## Self-check questions:

1. What approaches to creating custom View components do you know?
   [] Using only standard Views from the Android SDK.
   [] Composition (creating a composite component from existing Views) and custom drawing (inheriting from View and overriding `onDraw()`).
   [] Only creating Views through XML files.
2. How to create a composite component based on a group of elements?
   [] Create a new class that inherits from `View` and override all drawing methods.
   [] Create an XML layout that contains several Views, and then inflate this layout in a custom class that inherits from `ViewGroup` (e.g., `LinearLayout` 
      or `ConstraintLayout`).
   [] Create a separate class for each element and combine them in code.
3. How do we get the values of component attributes and styles?
   [] Extract them directly from the XML file using a parser.
   [] Use the `Context.obtainStyledAttributes()` method with a set of attributes defined in `declare-styleable` in `attrs.xml` to get a `TypedArray` from which 
      values can be extracted.
   [] They are passed to the View's constructor automatically.
4. How to create a custom View component?
   [] Inherit from the `Activity` class and override the `onCreate()` method.
   [] Create a new class that inherits from `View` (for custom drawing) or from `ViewGroup` (for a composite component), and implement the necessary 
      constructors and methods (e.g., `onMeasure()`, `onDraw()`).
   [] Create a Java class and add it to `AndroidManifest.xml`.
5. Which method is responsible for measuring the component's dimensions?
   [] `onLayout()`
   [] `onDraw()`
   [] `onMeasure()`
6. Which method is responsible for drawing the component?
   [] `onMeasure()`
   [] `onDraw()`
   [] `onLayout()`
