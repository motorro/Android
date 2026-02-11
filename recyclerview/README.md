# Вебинар 12. RecyclerView

***[Read in English](#webinar-12-recyclerview)***

1. Механика RecyclerView
2. Layout Manager
3. Утилиты RecyclerView
4. Разнородные элементы списка

## Вопросы для самопроверки:

1. Как работает RecyclerView?
   [] RecyclerView создает и переиспользует View (ViewHolder) для элементов списка, которые видны на экране, вместо того чтобы создавать View для каждого 
      элемента списка. Это значительно снижает потребление памяти и повышает производительность.
   [] RecyclerView создает новый View для каждого элемента данных в списке.
   [] RecyclerView хранит все View в памяти одновременно, независимо от того, видны они на экране или нет.
2. Какие есть Layout Manager'ы?
   [] Только LinearLayoutManager для вертикального и горизонтального списка.
   [] GridLayoutManager, StaggeredGridLayoutManager, и LinearLayoutManager. Также можно создать свой собственный кастомный LayoutManager.
   [] LayoutManager'ы не используются в RecyclerView, вместо них используются ViewPager.
3. Как используется ItemDecoration?
   [] ItemDecoration используется для добавления функциональности Drag&Drop.
   [] ItemDecoration используется для создания разделителей между элементами списка, добавления отступов, или для рисования произвольных элементов поверх или 
      под элементами списка (например, заголовков секций).
   [] ItemDecoration используется для определения анимации появления и исчезновения элементов.
4. При помощи какой утилиты делается поддержка Drag&Drop?
   [] При помощи `RecyclerView.Adapter`.
   [] При помощи `ItemTouchHelper`.
   [] При помощи `GestureDetector`.
5. Можно ли использовать разные типы элементов в одном списке?
   [] Нет, RecyclerView поддерживает только один тип элементов в списке.
   [] Да, можно. Для этого необходимо переопределить метод `getItemViewType()` в адаптере и возвращать различные типы в зависимости от позиции или типа данных. 
      Затем в `onCreateViewHolder()` и `onBindViewHolder()` обрабатывать эти типы соответствующим образом.
   [] Да, но только если все элементы имеют одинаковый макет.
6. Каким образом можно автоматически обработать разницу между двумя списками?
   [] Только вручную, обновляя весь адаптер методом `notifyDataSetChanged()`.
   [] С помощью `DiffUtil`. Он вычисляет минимальный набор изменений между двумя списками и отправляет эти изменения в адаптер, что приводит к более плавной 
      анимации и эффективному обновлению UI.
   [] С помощью AsyncTask для сравнения списков в фоновом режиме.

---

# Webinar 12. RecyclerView

1. RecyclerView Mechanics
2. Layout Manager
3. RecyclerView Utilities
4. Heterogeneous List Items

## Self-check questions:

1. How does RecyclerView work?
   [] RecyclerView creates and reuses Views (ViewHolders) for list items that are visible on the screen, instead of creating a View for each list item. 
      This significantly reduces memory consumption and improves performance.
   [] RecyclerView creates a new View for each data item in the list.
   [] RecyclerView keeps all Views in memory at the same time, regardless of whether they are visible on the screen or not.
2. What Layout Managers are there?
   [] Only LinearLayoutManager for vertical and horizontal lists.
   [] GridLayoutManager, StaggeredGridLayoutManager, and LinearLayoutManager. You can also create your own custom LayoutManager.
   [] LayoutManagers are not used in RecyclerView; ViewPagers are used instead.
3. How is ItemDecoration used?
   [] ItemDecoration is used to add Drag & Drop functionality.
   [] ItemDecoration is used to create dividers between list items, add offsets, or draw custom elements over or under list items (e.g., section headers).
   [] ItemDecoration is used to define the appearance and disappearance animations of items.
4. Which utility is used to support Drag & Drop?
   [] Using `RecyclerView.Adapter`.
   [] Using `ItemTouchHelper`.
   [] Using `GestureDetector`.
5. Can different types of items be used in the same list?
   [] No, RecyclerView only supports one type of item in a list.
   [] Yes, it's possible. To do this, you need to override the `getItemViewType()` method in the adapter and return different types depending on the position 
      or data type. Then, in `onCreateViewHolder()` and `onBindViewHolder()`, handle these types accordingly.
   [] Yes, but only if all items have the same layout.
6. How can the difference between two lists be automatically processed?
   [] Only manually, by updating the entire adapter with the `notifyDataSetChanged()` method.
   [] Using `DiffUtil`. It calculates the minimum set of changes between two lists and sends these changes to the adapter, resulting in smoother animations 
      and more efficient UI updates.
   [] Using AsyncTask to compare lists in the background.
