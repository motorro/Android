# Вебинар 6. Навигация в Activity

***[Read in English](#webinar-6-navigation-in-activity)***

1. Навигация в классическом Android-приложении
2. Режимы запуска Activity
3. Флаги запуска Activity

## Вопросы для самопроверки:

1. Что такое Task?
   [] Task - это фоновый процесс, который управляет жизненным циклом компонентов приложения.
   [] Task - это коллекция Activity, организованных в стек, которые пользователь видит как единое целое.
   [] Task - это модуль в приложении, отвечающий за определенную функциональность.
2. Что такое Back Stack, и как он связан с Task?
   [] Back Stack - это список всех запущенных сервисов в приложении.
   [] Back Stack - это история последних посещенных пользователем приложений.
   [] Back Stack - это стек Activity внутри Task, в котором Activity располагаются в порядке их открытия. Когда пользователь нажимает кнопку "Назад", Activity 
      извлекаются из стека в обратном порядке.
3. Сколько Task может содержать приложение?
   [] Приложение может содержать только один Task.
   [] Количество Task, которое может содержать приложение, ограничено только объемом оперативной памяти устройства.
   [] Приложение может содержать несколько Task. Каждая Activity по умолчанию запускается в Task своего приложения, но может быть настроена для запуска в новом Task.
4. Чем отличаются режимы запуска Activity `singleTop` и `singleTask`?
   [] `singleTop`: Activity всегда создается как новый экземпляр. `singleTask`: Activity всегда перезапускается с нуля.
   [] `singleTop`: Activity может быть только одной в приложении. `singleTask`: Activity может быть несколько, но только в одном Task.
   [] `singleTop`: если экземпляр Activity уже находится на вершине стека Task, то вместо создания нового экземпляра вызывается метод `onNewIntent()` существующего. 
      `singleTask`: Activity всегда запускается в новом Task (или перемещается в него, если он уже существует) и становится корневой Activity этого Task. Если  
      Task уже существует, все Activity над ней будут удалены.
5. Как запустить новый экземпляр Activity в новом Task?
   [] Для запуска нового экземпляра Activity в новом Task достаточно просто вызвать `startActivity()` с нужным Intent.
   [] Для запуска нового экземпляра Activity в новом Task необходимо использовать флаг `Intent.FLAG_ACTIVITY_NEW_TASK` в Intent, а также можно указать 
      `android:launchMode="singleTask"` в манифесте Activity.
   [] Для запуска нового экземпляра Activity в новом Task нужно вызвать метод `finish()` для текущей Activity перед запуском новой.

---

# Webinar 6. Navigation in Activity

1. Navigation in a classic Android application
2. Activity launch modes
3. Activity launch flags

## Self-check questions:

1. What is a Task?
   [] A Task is a background process that manages the lifecycle of application components.
   [] A Task is a collection of Activities, organized in a stack, that the user sees as a single whole.
   [] A Task is a module in an application that is responsible for a specific functionality.
2. What is the Back Stack, and how is it related to a Task?
   [] The Back Stack is a list of all running services in the application.
   [] The Back Stack is a history of the last applications visited by the user.
   [] The Back Stack is a stack of Activities within a Task, where Activities are arranged in the order they were opened. When the user presses the "Back" 
      button, Activities are popped from the stack in reverse order.
3. How many Tasks can an application contain?
   [] An application can contain only one Task.
   [] The number of Tasks an application can contain is limited only by the amount of RAM on the device.
   [] An application can contain multiple Tasks. By default, each Activity is launched in its application's Task, but it can be configured to launch in a new Task.
4. What is the difference between the `singleTop` and `singleTask` Activity launch modes?
   [] `singleTop`: A new instance of the Activity is always created. `singleTask`: The Activity is always restarted from scratch.
   [] `singleTop`: There can be only one instance of the Activity in the application. `singleTask`: There can be multiple instances, but only in one Task.
   [] `singleTop`: if an instance of the Activity is already at the top of the Task stack, the `onNewIntent()` method of the existing instance is called instead
      of creating a new one. `singleTask`: The Activity is always launched in a new Task (or moved to it if it already exists) and becomes the root Activity 
      of that Task. If the Task already exists, all Activities above it will be destroyed.
5. How to launch a new instance of an Activity in a new Task?
   [] To launch a new instance of an Activity in a new Task, simply call `startActivity()` with the desired Intent.
   [] To launch a new instance of an Activity in a new Task, you need to use the `Intent.FLAG_ACTIVITY_NEW_TASK` flag in the Intent, and you can also specify 
      `android:launchMode="singleTask"` in the Activity's manifest.
   [] To launch a new instance of an Activity in a new Task, you need to call the `finish()` method for the current Activity before launching the new one.
