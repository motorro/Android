# Задание Compose

***[Read in English](#assignment-compose)***

Это задание основано на приложении из предыдущей работы. Мы будем развивать приложение [Cookbook](/cookbook/README.md),
постепенно мигрируя проект на новый фреймворк. Модуль уже содержит все необходимые компоненты для прошлого задания и полностью работает.
В рамках этой практики мы не только познакомимся с созданием Compose-view, но и займемся миграцией существующего приложения с классических
View и навигации на современный стек.

Приложение уже разделено на отдельные модули:

- `model` - содержит общие модели данных приложения и сервера
- `core` - базовые структуры и функции
- `domain` - бизнес-правила
- `data` - реализация обмена данными с сервером и локального хранения
- `mockdata` - тестовая реализация доменных интерфейсов
- `appcore` - базовые компоненты всех UI-модулей
- `login` - пользовательский интерфейс авторизации
- `recipelist` - пользовательский интерфейс списка рецептов
- `addrecipe` - пользовательский интерфейс нового рецепта
- `recipe` - пользовательский интерфейс рецепта
- `cookbook` - модуль приложения, собирающий все вместе.

В этот раз, мы с вами сделаем следующие шаги:

- Модуль за модулем переведем приложение на использование Compose. Это позволит нам попрактиковаться в Compose View и в миграции существующий проектов.
- Переведем приложение на обновленную библиотеку навигации, используя [Navigation Compose](https://developer.android.com/jetpack/compose/navigation).
- Попробуем традиционную архитектуру и архитектуру [StateMachine](https://github.com/motorro/CommonStateMachine)

## Готовое решение

Готовое решение для этого задания можно посмотреть в отдельной [ветке репозитория](https://github.com/motorro/Android/tree/practice/7.Compose-solution/cookbook).

## 1. Основная часть задания: переход на Compose

В этой части мы мигрируем интерфейс приложения на использование Compose. Также мы займемся рефакторингом моделей на использование
State Machine. Это упростит логику и поможет покрыть ее Unit-тестами.

### 1.1. Подготовка модулей и тема приложения

- Добавьте зависимости на плагин компоненты Compose к модулю [appcore](/cookbook/appcore/build.gradle.kts)
- Создайте тему приложения по вашему вкусу при помощи инструмента [Material Theme Builder](https://material-foundation.github.io/material-theme-builder/)
- Сохраните тему в модуль `appcore` и исправьте шаблонный код для использования в вашем проекте.

### 1.2. Переход на Compose внутри модуля **login**

Будем переходить на `Compose` модуль за модулем и будем сохранять общую структуру проекта и навигации на первом этапе.
Поэтому, в этом шаге, для примера, переведем на `Compose` внутреннюю структуру модуля `login`. Для этого, воспользуемся документацией
с официального сайта: [ComposeView in Fragments](https://developer.android.com/develop/ui/compose/migrate/interoperability-apis/compose-in-views#compose-in-fragments).

План работ:

1. Напишите Composable функцию экрана авторизации. Используйте существующую модель данных. У вас должна получиться функция примерно следующего вида:
    ```kotlin
    @Composable
    fun LoginScreen(
        state: LoginViewState,
        onLoginChanged: (String) -> Unit,
        onPasswordChanged: (String) -> Unit,
        onLoginPressed: () -> Unit,
        onComplete: () -> Unit, // Используйте для завершения навигации во фрагменте
        modifier: Modifier = Modifier
    ) { }
    ```
2. Перенесите в `Compose` все элементы из [файла верстки фрагмента](/cookbook/login/src/main/res/layout/fragment_login.xml).
3. Удалите классические виджеты из фрагмента и добавьте `ComposeView`, занимающий все пространство фрагмента.
4. Перепишите код фрагмента, чтобы он использовал `ComposeView`, как указано по ссылке в описании раздела.
5. Подключите существующую модель к новому отображению.
6. Проверьте работу приложения.

### 1.3. Рефакторинг Composable экрана и модели с использованием и жестов.

Состояние нашего экрана уже однозначно описывается структурой [LoginViewState](cookbook/login/src/main/kotlin/com/motorro/cookbook/login/data/LoginViewState.kt), 
поэтому сигнатура функции экрана получилась относительно простой. Давайте полностью перейдем на рекомендуемую MVI архитектуру и создадим единую точку входа для 
всех действий пользователя. 

1. Опишите все возможные действия с моделью при помощи закрытой структуры `LoginGesture`
2. В модели сделайте единую точку входа - функцию `process`, которая принимает `LoginGesture` и производит соответствующие действия.

В результате, у вас должна получиться функция с примерно следующего вида:

```kotlin
@Composable
fun LoginScreen(
   state: LoginViewState,
   onGesture: (LoginGesture) -> Unit,
   onComplete: () -> Unit, // Используйте для завершения навигации во фрагменте
   modifier: Modifier = Modifier
) {}
```

### 1.4. Рефакторинг модели с использованием StateMachine

К этому моменту, вся логика нашего экрана авторизации определяется внутри [LoginViewModel](cookbook/login/src/main/kotlin/com/motorro/cookbook/login/LoginViewModel.kt)
и, в целом, нам удалось построить полную UDF/MVI архитектуру в рамках модуля `login`. Однако, наша модель, на мой взгляд, содержит следующие недостатки:

1. Одна модель обслуживает три экрана, что нарушает принцип Single Responsibility и усложняет логику.
2. Модель никак не разделяет допустимые действия для разных состояний экрана. Например, изменение имени пользователя и пароля может происходить и в состоянии 
   аутентификации. В нашем случае, это не страшно, однако при усложнении логики может привести к проблемам.
3. Тестирование модели возможно только интегрально. Нам, относительно трудно протестировать отдельно состояние формы ввода данных и обращение на сервер. Опять 
   же, это может принести сложности и проблемы в поддержке модели при усложнении логики.

Я предлагаю попробовать перевести модель авторизации на более поддерживаемое и понятное решение при помощи паттерна `State Machine`.

Сценарий:

1. Подготовьте модуль `login` для использования State Machine: добавьте зависимости, создайте интерфейс фабрики и базовый класс State Machine.
2. Напишите класс `LoginFormState`, который будет отвечать за обслуживание формы. Создайте тест для него.
3. Напишите класс `TerminatedState`, который будет отвечать за завершенное состояние. Установите `LoginViewState.LoggedIn` в функции `doStart`.
4. Напишите класс `LoggingInState`, который будет запускать процесс аутентификации и переходить к состоянию `TerminatedState` в случае успешной авторизации и 
   обратно к состоянию формы в случае ошибки. Создайте тест для него.
5. Напишите реализацию фабрики состояний и соберите новую модель с использованием State Machine.

### 1.5. Общие Composable функции для экрана авторизации и ошибки.

Для перевода остальных модулей на использование Compose, нам нужно подготовить общие экраны. 

1. Создайте Composable функцию `AuthPromptView` на основе [layout_auth.xml](/cookbook/appcore/src/main/res/layout/layout_auth.xml) и поместите ее 
   в модуль `appcore`.
2. Создайте Composable функцию `ErrorView` на основе [layout_error.xml](/cookbook/appcore/src/main/res/layout/layout_error.xml) и поместите ее 
   в модуль `appcore`.
3. Опционально, создайте Composable функцию `LceView`, которая будет разбирать состояние загрузки данных. Наша задача в этом пункте - облегчить разбор состояния
   экрана, как мы делали в [RecipeFragment](/cookbook/recipe/src/main/kotlin/com/motorro/cookbook/recipe/RecipeFragment.kt)

### 1.6. Переделываем экран рецепта на Compose и StateMachine

Для практики, переделаем экран рецепта на использование Compose и State Machine. Мы можем оставить и нашу текущую модель, так как она простая, но сделаем это, 
чтобы "набить руку".
Представим логику экрана тремя основными состояниями и одним служебным - для перехода в "мир навигации":

1. Состояние `ContentState` - в этом состоянии мы подписываемся на репозиторий и просто транслируем LCE-поток в наш ui-state.
2. Состояние `DeleteConfirmationState` - при переходе в это состояние мы фиксируем внутренние данные и показваем диалог подтверждения. 
3. Состояние `DeletingState` - в этом состоянии мы вызваем репозиторий и удаляем рецепт. Мы также можем использовать данные, полученные в `ContentState`, 
   для отображения экрана.
4. Состояние `Terminated` - так же, как и в случае аутентификации, будем использовать это состояние для завершения работы фрагмента. В это состояние мы будем 
   переходить по нажатию `Back` и после удаления рецепта.

План работ:

1. Создайте набор жестов `RecipeGesture`, содержащий все необходимые жесты экрана.
2. Создайте набор состояний `RecipeViewState`, содержащий все возможные состояния экрана: `Content`, `DeleteConfirmation`, `Deleting`, `Terminated`. 
3. Создайте экран рецепта `RecipeScreen` для работы с этими состояниями. Предусмотрите обратный вызов для перехода к авторизации.
4. Пользуйтесь приложением `recipedemo` для отладки экрана.

### 1.7. Переход на Compose внутри модуля добавления рецепта

Переведем на Compose модуль добавления рецепта. Для практики также воспользуемся подходом MVI: жестами и закрытой структурой состояния экрана.
Также, для практики, переведем модель на использование State Machine. У нас три состояния логики:

1. Состояние `FormState` - в этом состоянии мы заполняем и валидируем форму.
2. Состояние `AddingRecipeState` - при переходе в это состояние мы фиксируем внутренние данные и сохраняем рецепт в репозиторий.
3. Состояние `Terminated` - так же, как и в предыдущем задании, будем использовать это состояние для завершения работы фрагмента. 

План работ:

1. Напишите структуры `AddRecipeGesture` и `AddRecipeViewState`, которые опишут состояние вашего экрана.
2. Перенесите в `Compose` все элементы из [файла верстки фрагмента](/cookbook/addrecipe/src/main/res/layout/fragment_add_recipe.xml).
3. Напишите состояния модели State Machine аналогично предыдущим заданиям.

### 1.8. Переход на Compose внутри модуля списка рецептов

Переведем на Compose и State Machine модуль списка рецепта. У нас три состояния логики:

1. Состояние `ContentState` - показываем список элементов.
2. Состояние `LoggingOutState` - выполняем процедуру выхода.
3. Состояние `Terminated` - так же, как и в предыдущем задании, будем использовать это состояние для завершения работы фрагмента. 

План работ:

1. Напишите структуры `RecipeListGesture` и `RecipeListViewState`, которые опишут состояние вашего экрана.
2. Перенесите в `Compose` все элементы из [файла верстки фрагмента](/cookbook/recipelist/src/main/res/layout/fragment_recipe_list.xml).
3. Переделайте `RecyclerView` и его элементы на [LazyColumn](https://developer.android.com/develop/ui/compose/lists)
4. Напишите состояния модели State Machine аналогично предыдущим заданиям.

### 1.9. Избавляемся от фрагментов и переходим на Navigation Compose

Теперь, все наши экраны основаны на Compose и мы можем избавиться от фрагментов и заменить навигацию на [Compose Navigation](https://developer.android.com/develop/ui/compose/navigation).

План работ:

1. Удалите зависимости на библиотеки Fragment Navigation и добавьте библиотеки [Compose Navigation](https://developer.android.com/develop/ui/compose/navigation#setup) 
   и [Hilt Navigation](https://developer.android.com/training/dependency-injection/hilt-jetpack#viewmodel-navigation).
2. Удалите фрагменты модулей
3. Заведите навигационные точки в общем модуле приложения `appcore` по аналогии с [дип-линками](/cookbook/appcore/src/main/kotlin/com/motorro/cookbook/appcore/navigation/Links.kt) 
   классической навигации. Например, маршрут навигации для списка рецептов может выглядеть так:
   ```kotlin
    /**
     * Recipe list destination
     */
    @Serializable
    data object RecipeListDestination : Destination
   ```
4. Создайте билдеры навигационных точек в модулях. Например, билдер для экрана списка рецептов может выглядеть так:
   ```kotlin
   fun NavGraphBuilder.recipeListGraph(navController: NavController) {
      composable<Destination.RecipeListDestination> {
           val model: RecipeListViewModel = hiltViewModel()

           RecipeListScreen(
               viewState = model.viewState.collectAsStateWithLifecycle().value,
               onGesture = model::process,
               onRecipe = {
                   navController.navigate(Destination.RecipeDestination(it.toString()))
               },
               onAddRecipe = {
                   navController.navigate(Destination.AddRecipeDestination)
               },
               onLogin = {
                   navController.navigate(Destination.LoginDestination)
               },
               onTerminated = { navController.popBackStack() }
           )
       }
   }
   ```
5. Переделайте код основной Activity на использование Compose-содержимого.
6. Удалите XML-граф из основного модуля и инициализируйте `NavHost`, включив дочерние модули.

## 2. Дополнительное задание: полная миграция архитектуры на использование State Machine.

Эта часть задания - не обязательная. Я сам покажу вам, как мы можем полностью перевести приложение с классической поэкранной навигацией 
на глобальное использование паттерна State Machine. Вы можете следить за шагами задания по видео-презентации, а также смотреть историю
изменения кода по коммитам в [ветке с решением](https://github.com/motorro/Android/tree/practice/7.Compose-solution/cookbook).
В конце занятия вы сможете сравнить оба подхода к созданию сложного многомодульного приложения и выбрать тот, который покажется вам более
простым и удовлетворяющим вашим требованиям и подходам.

Общий план задания:

1. Сделаем сценарий создания нового рецепта дочерним к основному при помощи [ProxyMachineState](https://github.com/motorro/CommonStateMachine?tab=readme-ov-file#adopting-feature-flows).
2. Сделаем сценарий просмотра и удаления рецепта дочерним к основному при помощи [ProxyMachineState](https://github.com/motorro/CommonStateMachine?tab=readme-ov-file#adopting-feature-flows).
3. Выделим модуль логина в proxy-state и подкулючим его динамически при помощи DI
4. Сделаем полную navigation inversion и избавимся от Jetpack Navigation component.

### 2.1. Мигрируем модуль добавления рецепта в дочерний state-flow

К этому моменту, модуль добавления рецептов уже переведен на использование паттерна State Machine, однако внешнее управление 
состоянием модуля до сих пор делается средствами Android Framework.

1. У модуля выделенная ViewModel
2. Жизненный цикл модели привязан к точке навигации
3. Для завершения работы модуля, мы используем особое состояние `Terminated`, по которому запускается побочный эффект, а фактический выход из 
   модуля делает navigation controller.
 
В этом задании, для простоты, мы сделаем модуль `addrecipe` прямой зависимостью модуля списка рецептов и будем запускать его логику
в `proxy-state` основного потока состояний.

План работ:

1. Переносим зависимость модуля `addrecipe` в модуль `recipelist` из основного приложения.
2. Удаляем модель и навигационный граф модуля `addrecipe`.
3. Создаем родительский прокси-модуль в модуле `recipelist`.
4. Запускаем дочерний модуль в рамках основного flow.

### 2.2. Мигрируем модуль просмотра и удаления рецепта в дочерний state-flow

С модулем просмотра рецепта поступаем аналогично. Удаляем модель и навигационный граф модуля `recipe` и готовим его для работы в виде 
дочернего модуля в модуле `recipelist`.
 
В этом задании, так же, мы сделаем модуль `addrecipe` прямой зависимостью модуля списка рецептов и будем запускать его логику
в `proxy-state` основного потока состояний. Вы можете использовать тот же подход, что и в предыдущем задании или любой понятный вам путь подключения
дочернего состояния при помощи `ProxyMachineState`

План работ:

1. Переносим зависимость модуля `recipe` в модуль `recipelist` из основного приложения.
2. Удаляем модель и навигационный граф модуля `addrecipe`.
3. Создаем родительский прокси-модуль в модуле `recipelist`.
4. Запускаем дочерний модуль в рамках основного flow.

### 2.3. Мигрируем модуль авторизации в дочерний state-flow

В предыдущих двух заданиях мы статически подключили дочерние модули в родительский модуль `recipelist`. Модуль `login` мы используем
в двух сценариях: при просмотре списка и при просмотре рецепта. Поэтому, для демонстрации, попробуем написать общее решение для обоих модулей
и подключить его динамически при помощи DI.

План работ:

1. Проектируем API модуля авторизации и создаем его в модуле `appcore`.
2. Реализуем универсальный proxy в модуле `login`
3. Подключиваем дочерний модуль в родительские модули `recipelist` и `recipe`.
4. Подключаем логику при помощи DI.
5. Подключаем compose-view при помощи [composition local](https://developer.android.com/develop/ui/compose/compositionlocal).
6. Запускаем дочерний модуль в рамках основного flow.

### 2.4. Отключаем Jetpack Navigation Component и переходим на State Machine в основном приложении.

К этому моменту, у нас остается только одна навигационная точка - это экран списка рецептов. Закончим миграцию на модульную State Machine,
заменив модуль навигации в основном приложении на proxy-машину.

План работ:

1. Отключаем модуль навигации в основном приложении.
2. Мигрируем модуль списка рецептов в дочерний модуль State Machine
3. Организуем корневую State Machine и основную View-Model родительского приложения.

## Заключение

В данном документе мы рассмотрели процесс миграции Android-приложения на Jetpack Compose и Navigation Compose, 
а также исследовали альтернативный подход к управлению состоянием и навигацией с использованием модульной State Machine.

В основной части задания мы:

- Постепенно перевели все экраны приложения с традиционных View на Jetpack Compose.
- Реализовали навигацию между экранами с помощью Navigation Compose.
- Рефакторили ViewModel, используя паттерн MVI (Model-View-Intent) и State Machine для более четкого разделения логики и состояний.

В дополнительной части мы углубились в архитектуру State Machine и:

- Преобразовали отдельные экраны в дочерние State Machine, управляемые из родительских модулей.
- Показали, как можно организовать динамическое подключение таких модулей с помощью [ProxyMachineState](https://github.com/motorro/CommonStateMachine?tab=readme-ov-file#adopting-feature-flows) 
  и Dependency Injection.
- Полностью отказались от Jetpack Navigation Component, реализовав навигацию исключительно средствами State Machine.

В результате мы получили два варианта архитектуры приложения: один, использующий стандартные компоненты Jetpack (Compose и Navigation Compose), и второй, 
основанный на более гибкой и кастомизируемой модульной State Machine.

Предлагаю вам внимательно изучить оба подхода, сравнить их сильные и слабые стороны в контексте данного проекта. Обратите внимание на организацию кода, 
сложность управления состоянием и навигацией, а также на возможности тестирования в каждом из вариантов. Это поможет вам сделать осознанный выбор в пользу той 
или иной архитектуры для ваших будущих проектов.

---

# Assignment: Compose

This assignment is based on the application from the previous work. We will be developing the [Cookbook](/cookbook/README.md) application,
gradually migrating the project to a new framework. The module already contains all the necessary components from the previous assignment and is fully functional.
In this practice, we will not only get acquainted with creating Compose views but also migrate an existing application from classic
Views and navigation to a modern stack.

The application is already divided into separate modules:

- `model` - contains common data models for the application and the server
- `core` - basic structures and functions
- `domain` - business rules
- `data` - implementation of data exchange with the server and local storage
- `mockdata` - test implementation of domain interfaces
- `appcore` - basic components of all UI modules
- `login` - user authorization interface
- `recipelist` - user interface for the recipe list
- `addrecipe` - user interface for a new recipe
- `recipe` - user interface for a recipe
- `cookbook` - the application module that brings everything together.

This time, we will take the following steps:

- We will migrate the application to use Compose, module by module. This will allow us to practice with Compose View and migrating existing projects.
- We will migrate the application to the updated navigation library using [Navigation Compose](https://developer.android.com/jetpack/compose/navigation).
- We will try a traditional architecture and a [StateMachine](https://github.com/motorro/CommonStateMachine) architecture.

## Finished Solution

The finished solution for this assignment can be found in a separate [repository branch](https://github.com/motorro/Android/tree/practice/7.Compose-solution/cookbook).

## 1. Main Part of the Assignment: Migrating to Compose

In this part, we will migrate the application's interface to use Compose. We will also refactor the models to use
a State Machine. This will simplify the logic and help cover it with Unit tests.

### 1.1. Preparing Modules and Application Theme

- Add dependencies for the Compose components plugin to the [appcore](/cookbook/appcore/build.gradle.kts) module.
- Create an application theme to your taste using the [Material Theme Builder](https://material-foundation.github.io/material-theme-builder/) tool.
- Save the theme to the `appcore` module and correct the template code for use in your project.

### 1.2. Migrating to Compose within the **login** module

We will migrate to `Compose` module by module and will keep the overall project structure and navigation in the first stage.
Therefore, in this step, as an example, we will migrate the internal structure of the `login` module to `Compose`. For this, we will use the documentation
from the official site: [ComposeView in Fragments](https://developer.android.com/develop/ui/compose/migrate/interoperability-apis/compose-in-views#compose-in-fragments).

Work plan:

1. Write a Composable function for the authorization screen. Use the existing data model. You should end up with a function something like this:
    ```kotlin
    @Composable
    fun LoginScreen(
        state: LoginViewState,
        onLoginChanged: (String) -> Unit,
        onPasswordChanged: (String) -> Unit,
        onLoginPressed: () -> Unit,
        onComplete: () -> Unit, // Use to complete navigation in the fragment
        modifier: Modifier = Modifier
    ) { }
    ```
2. Migrate all elements from the [fragment layout file](/cookbook/login/src/main/res/layout/fragment_login.xml) to `Compose`.
3. Remove the classic widgets from the fragment and add a `ComposeView` that occupies the entire fragment space.
4. Rewrite the fragment code to use `ComposeView`, as indicated in the link in the section description.
5. Connect the existing model to the new display.
6. Check that the application works.

### 1.3. Refactoring the Composable Screen and Model Using Gestures

The state of our screen is already unambiguously described by the [LoginViewState](cookbook/login/src/main/kotlin/com/motorro/cookbook/login/data/LoginViewState.kt) structure,
so the screen function signature turned out to be relatively simple. Let's fully switch to the recommended MVI architecture and create a single entry point for
all user actions.

1. Describe all possible actions with the model using a sealed `LoginGesture` structure.
2. In the model, create a single entry point - a `process` function that takes a `LoginGesture` and performs the corresponding actions.

As a result, you should have a function something like this:

```kotlin
@Composable
fun LoginScreen(
   state: LoginViewState,
   onGesture: (LoginGesture) -> Unit,
   onComplete: () -> Unit, // Use to complete navigation in the fragment
   modifier: Modifier = Modifier
) {}
```

### 1.4. Refactoring the Model Using a StateMachine

By this point, all the logic of our authorization screen is defined within the [LoginViewModel](cookbook/login/src/main/kotlin/com/motorro/cookbook/login/LoginViewModel.kt)
and, in general, we have managed to build a complete UDF/MVI architecture within the `login` module. However, in my opinion, our model has the following drawbacks:

1. One model serves three screens, which violates the Single Responsibility Principle and complicates the logic.
2. The model does not separate the permissible actions for different screen states. For example, changing the username and password can also occur in the
   authentication state. In our case, this is not critical, but as the logic becomes more complex, it can lead to problems.
3. The model can only be tested integrally. It is relatively difficult for us to test the state of the data entry form and the server request separately. Again,
   this can create difficulties and problems in maintaining the model as the logic becomes more complex.

I suggest trying to migrate the authorization model to a more maintainable and understandable solution using the `State Machine` pattern.

Scenario:

1. Prepare the `login` module to use a State Machine: add dependencies, create a factory interface, and a base State Machine class.
2. Write a `LoginFormState` class that will be responsible for servicing the form. Create a test for it.
3. Write a `TerminatedState` class that will be responsible for the completed state. Set `LoginViewState.LoggedIn` in the `doStart` function.
4. Write a `LoggingInState` class that will start the authentication process and transition to the `TerminatedState` on successful authorization and
   back to the form state on error. Create a test for it.
5. Write an implementation of the state factory and assemble a new model using the State Machine.

### 1.5. Common Composable Functions for Authorization and Error Screens

To migrate the remaining modules to Compose, we need to prepare common screens.

1. Create a Composable function `AuthPromptView` based on [layout_auth.xml](/cookbook/appcore/src/main/res/layout/layout_auth.xml) and place it
   in the `appcore` module.
2. Create a Composable function `ErrorView` based on [layout_error.xml](/cookbook/appcore/src/main/res/layout/layout_error.xml) and place it
   in the `appcore` module.
3. Optionally, create a Composable function `LceView` that will handle the data loading state. Our task in this point is to facilitate the parsing of the screen
   state, as we did in [RecipeFragment](/cookbook/recipe/src/main/kotlin/com/motorro/cookbook/recipe/RecipeFragment.kt).

### 1.6. Remaking the Recipe Screen with Compose and StateMachine

For practice, let's remake the recipe screen using Compose and a State Machine. We can keep our current model, as it is simple, but we will do this
to "get the hang of it".
Let's represent the screen logic with three main states and one service state - for transitioning to the "navigation world":

1. `ContentState` - in this state, we subscribe to the repository and simply broadcast the LCE flow to our UI state.
2. `DeleteConfirmationState` - when transitioning to this state, we fix the internal data and show a confirmation dialog.
3. `DeletingState` - in this state, we call the repository and delete the recipe. We can also use the data obtained in `ContentState`
   to display the screen.
4. `Terminated` state - just as in the case of authentication, we will use this state to finish the work of the fragment. We will transition to this state 
   by pressing `Back` and after deleting the recipe.

Work plan:

1. Create a set of `RecipeGesture` gestures containing all the necessary screen gestures.
2. Create a set of `RecipeViewState` states containing all possible screen states: `Content`, `DeleteConfirmation`, `Deleting`, `Terminated`.
3. Create a `RecipeScreen` recipe screen to work with these states. Provide a callback for transitioning to authorization.
4. Use the `recipedemo` application to debug the screen.

### 1.7. Migrating to Compose within the Add Recipe module

Let's migrate the add recipe module to Compose. For practice, we will also use the MVI approach: gestures and a sealed screen state structure.
Also, for practice, we will migrate the model to use a State Machine. We have three logic states:

1. `FormState` - in this state, we fill out and validate the form.
2. `AddingRecipeState` - when transitioning to this state, we fix the internal data and save the recipe to the repository.
3. `Terminated` state - just as in the previous task, we will use this state to finish the work of the fragment.

Work plan:

1. Write `AddRecipeGesture` and `AddRecipeViewState` structures that will describe the state of your screen.
2. Migrate all elements from the [fragment layout file](/cookbook/addrecipe/src/main/res/layout/fragment_add_recipe.xml) to `Compose`.
3. Write the state machine model states similarly to the previous tasks.

### 1.8. Migrating to Compose within the Recipe List module

Let's migrate the recipe list module to Compose and a State Machine. We have three logic states:

1. `ContentState` - show a list of items.
2. `LoggingOutState` - perform the logout procedure.
3. `Terminated` state - just as in the previous task, we will use this state to finish the work of the fragment.

Work plan:

1. Write `RecipeListGesture` and `RecipeListViewState` structures that will describe the state of your screen.
2. Migrate all elements from the [fragment layout file](/cookbook/recipelist/src/main/res/layout/fragment_recipe_list.xml) to `Compose`.
3. Convert the `RecyclerView` and its elements to a [LazyColumn](https://developer.android.com/develop/ui/compose/lists).
4. Write the state machine model states similarly to the previous tasks.

### 1.9. Getting Rid of Fragments and Switching to Navigation Compose

Now, all our screens are based on Compose, and we can get rid of fragments and replace the navigation with [Compose Navigation](https://developer.android.com/develop/ui/compose/navigation).

Work plan:

1. Remove dependencies on the Fragment Navigation libraries and add the [Compose Navigation](https://developer.android.com/develop/ui/compose/navigation#setup)
   and [Hilt Navigation](https://developer.android.com/training/dependency-injection/hilt-jetpack#viewmodel-navigation) libraries.
2. Remove the module fragments.
3. Create navigation points in the common `appcore` application module by analogy with the [deep-links](/cookbook/appcore/src/main/kotlin/com/motorro/cookbook/appcore/navigation/Links.kt)
   of classic navigation. For example, the navigation route for the recipe list might look like this:
   ```kotlin
    /**
     * Recipe list destination
     */
    @Serializable
    data object RecipeListDestination : Destination
   ```
4. Create navigation point builders in the modules. For example, the builder for the recipe list screen might look like this:
   ```kotlin
   fun NavGraphBuilder.recipeListGraph(navController: NavController) {
      composable<Destination.RecipeListDestination> {
           val model: RecipeListViewModel = hiltViewModel()

           RecipeListScreen(
               viewState = model.viewState.collectAsStateWithLifecycle().value,
               onGesture = model::process,
               onRecipe = {
                   navController.navigate(Destination.RecipeDestination(it.toString()))
               },
               onAddRecipe = {
                   navController.navigate(Destination.AddRecipeDestination)
               },
               onLogin = {
                   navController.navigate(Destination.LoginDestination)
               },
               onTerminated = { navController.popBackStack() }
           )
       }
   }
   ```
5. Rework the main Activity's code to use Compose content.
6. Remove the XML graph from the main module and initialize `NavHost`, including the child modules.

## 2. Optional Task: Complete Architecture Migration to Use a State Machine

This part of the task is not mandatory. I will show you myself how we can completely migrate an application from classic screen-by-screen navigation
to the global use of the State Machine pattern. You can follow the steps of the task in the video presentation, as well as see the history of
code changes through commits in the [solution branch](https://github.com/motorro/Android/tree/practice/7.Compose-solution/cookbook).
At the end of the lesson, you will be able to compare both approaches to creating a complex multi-module application and choose the one that seems simpler and
more satisfying to your requirements and approaches.

Overall task plan:

1. We will make the scenario of creating a new recipe a child to the main one using [ProxyMachineState](https://github.com/motorro/CommonStateMachine?tab=readme-ov-file#adopting-feature-flows).
2. We will make the scenario of viewing and deleting a recipe a child to the main one using [ProxyMachineState](https://github.com/motorro/CommonStateMachine?tab=readme-ov-file#adopting-feature-flows).
3. We will extract the login module into a proxy-state and connect it dynamically using DI.
4. We will do a full navigation inversion and get rid of the Jetpack Navigation component.

### 2.1. Migrating the Add Recipe module to a child state-flow

By this point, the add recipe module has already been migrated to use the State Machine pattern, but the external management of the
module's state is still done by the Android Framework.

1. The module has a dedicated ViewModel.
2. The model's lifecycle is tied to the navigation point.
3. To finish the module's work, we use a special `Terminated` state, which triggers a side effect, and the actual exit from the
   module is done by the navigation controller.

In this task, for simplicity, we will make the `addrecipe` module a direct dependency of the recipe list module and will run its logic
in the `proxy-state` of the main state flow.

Work plan:

1. Move the dependency of the `addrecipe` module to the `recipelist` module from the main application.
2. Remove the model and navigation graph of the `addrecipe` module.
3. Create a parent proxy module in the `recipelist` module.
4. Launch the child module within the main flow.

### 2.2. Migrating the View and Delete Recipe module to a child state-flow

We will do the same with the view recipe module. Remove the model and navigation graph of the `recipe` module and prepare it to work as a
child module in the `recipelist` module.

In this task, we will also make the `addrecipe` module a direct dependency of the recipe list module and will run its logic
in the `proxy-state` of the main state flow. You can use the same approach as in the previous task or any way you understand to connect a
child state using `ProxyMachineState`.

Work plan:

1. Move the dependency of the `recipe` module to the `recipelist` module from the main application.
2. Remove the model and navigation graph of the `addrecipe` module.
3. Create a parent proxy module in the `recipelist` module.
4. Launch the child module within the main flow.

### 2.3. Migrating the Authorization module to a child state-flow

In the previous two tasks, we statically connected the child modules to the parent `recipelist` module. We use the `login` module
in two scenarios: when viewing the list and when viewing a recipe. Therefore, for demonstration, let's try to write a common solution for both modules
and connect it dynamically using DI.

Work plan:

1. Design the API of the authorization module and create it in the `appcore` module.
2. Implement a universal proxy in the `login` module.
3. Connect the child module to the parent `recipelist` and `recipe` modules.
4. Connect the logic using DI.
5. Connect the compose-view using [composition local](https://developer.android.com/develop/ui/compose/compositionlocal).
6. Launch the child module within the main flow.

### 2.4. Disabling Jetpack Navigation Component and Switching to a State Machine in the Main Application

By this point, we have only one navigation point left - the recipe list screen. Let's finish the migration to a modular State Machine
by replacing the navigation module in the main application with a proxy-machine.

Work plan:

1. Disable the navigation module in the main application.
2. Migrate the recipe list module to a child State Machine module.
3. Organize the root State Machine and the main View-Model of the parent application.

## Conclusion

In this document, we have reviewed the process of migrating an Android application to Jetpack Compose and Navigation Compose,
and also explored an alternative approach to state management and navigation using a modular State Machine.

In the main part of the assignment, we:

- Gradually migrated all application screens from traditional Views to Jetpack Compose.
- Implemented navigation between screens using Navigation Compose.
- Refactored the ViewModel using the MVI (Model-View-Intent) pattern and a State Machine for a clearer separation of logic and states.

In the additional part, we delved into the State Machine architecture and:

- Converted individual screens into child State Machines, managed from parent modules.
- Showed how to organize the dynamic connection of such modules using [ProxyMachineState](https://github.com/motorro/CommonStateMachine?tab=readme-ov-file#adopting-feature-flows)
  and Dependency Injection.
- Completely abandoned the Jetpack Navigation Component, implementing navigation exclusively with a State Machine.

As a result, we have two variants of the application's architecture: one using standard Jetpack components (Compose and Navigation Compose), and the second,
based on a more flexible and customizable modular State Machine.

I suggest you carefully study both approaches, compare their strengths and weaknesses in the context of this project. Pay attention to the organization of the code,
the complexity of state and navigation management, as well as the testing capabilities in each of the variants. This will help you make an informed choice 
in favor of one or another architecture for your future projects.
