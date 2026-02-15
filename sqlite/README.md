# Вебинар 23.2. SQLite/Room

***[Read in English](#webinar-232-sqliteroom)***

1. SQLite - реляционная база данных
2. Room - абстракция над SQLite
3. Создание базы данных с помощью Room
4. Работа с данными в Room
5. Миграция базы данных в Room
6. Relationships в Room

## Вопросы для самопроверки:

1. Для хранения каких данных лучше использовать реляционную базу данных?
   [] Нереляционные данные, такие как JSON-документы или произвольные бинарные файлы.
   [] Структурированные данные, которые имеют четко определенные отношения между собой (например, пользователи, заказы, товары).
   [] Данные, которые изменяются очень редко и не требуют сложных запросов.
2. Чем Room помогает разработчику в работе с SQLite?
   [] Room полностью заменяет SQLite и не использует его вовсе.
   [] Room предоставляет абстракцию над SQLite, упрощая работу с базой данных за счет компиляционной проверки SQL-запросов, ORM-подобного подхода 
      (сущности, DAO), интеграции с LiveData/Flow и снижения количества шаблонного кода.
   [] Room автоматически генерирует весь код UI для отображения данных из базы.
3. Как создать таблицу в Room?
   [] Таблица в Room создается путем выполнения SQL-запроса `CREATE TABLE` в DAO-методе.
   [] Таблица создается путем добавления соответствующего тега в XML-файл.
   [] Таблица в Room создается путем определения класса-сущности с аннотацией `@Entity`, где поля класса представляют столбцы таблицы. 
      Room автоматически создает таблицу на основе этой сущности.
4. Как создать связь между таблицами в Room?
   [] Связи между таблицами (отношения) в Room создаются с использованием аннотаций, таких как `@Relation` (для отношений один-ко-многим, многие-ко-многим) 
      или `@ForeignKey` (для отношений один-ко-одному и ссылочной целостности), в сочетании с POJO-классами для представления объединенных данных.
   [] Связи создаются путем прямого написания сложных SQL `JOIN` запросов в каждом DAO-методе.
   [] Room не поддерживает отношения между таблицами, все данные должны храниться в одной таблице.
5. Каким образом реализуется миграция базы данных в Room?
   [] Room автоматически обновляет схему базы данных при изменении класса-сущности без необходимости явной миграции.
   [] Миграция в Room реализуется путем создания классов `Migration`, которые описывают изменения схемы базы данных между двумя версиями (например, добавление 
      столбца, изменение типа данных) с использованием SQL-запросов. Эти объекты `Migration` затем добавляются в `RoomDatabase.Builder`.
   [] Миграция базы данных в Room не поддерживается, при изменении схемы необходимо удалять и пересоздавать базу.

---

# Webinar 23.2. SQLite/Room

1. SQLite - relational database
2. Room - abstraction over SQLite
3. Creating a database with Room
4. Working with data in Room
5. Database migration in Room
6. Relationships in Room

## Self-check questions:

1. For what kind of data is it better to use a relational database?
   [] Non-relational data, such as JSON documents or arbitrary binary files.
   [] Structured data that has clearly defined relationships between its elements (e.g., users, orders, products).
   [] Data that changes very rarely and does not require complex queries.
2. How does Room help a developer work with SQLite?
   [] Room completely replaces SQLite and does not use it at all.
   [] Room provides an abstraction layer over SQLite, simplifying database operations through compile-time SQL query verification, an ORM-like approach 
      (entities, DAOs), integration with LiveData/Flow, and reduction of boilerplate code.
   [] Room automatically generates all the UI code to display data from the database.
3. How to create a table in Room?
   [] A table in Room is created by executing a `CREATE TABLE` SQL query in a DAO method.
   [] A table is created by adding a corresponding tag in an XML file.
   [] A table in Room is created by defining an entity class with the `@Entity` annotation, where the class fields represent the table columns. Room 
      automatically creates the table based on this entity.
4. How to create a relationship between tables in Room?
   [] Relationships between tables in Room are created using annotations such as `@Relation` (for one-to-many and many-to-many relationships) or `@ForeignKey` 
      (for one-to-one relationships and referential integrity), in combination with POJO classes to represent the joined data.
   [] Relationships are created by directly writing complex SQL `JOIN` queries in each DAO method.
   [] Room does not support relationships between tables; all data must be stored in a single table.
5. How is database migration implemented in Room?
   [] Room automatically updates the database schema when an entity class changes, without the need for explicit migration.
   [] Migration in Room is implemented by creating `Migration` classes that describe the database schema changes between two versions (e.g., adding a column, 
      changing a data type) using SQL queries. These `Migration` objects are then added to the `RoomDatabase.Builder`.
   [] Database migration is not supported in Room; when the schema changes, the database must be deleted and recreated.
