# bsc_bosov

# Запуск с помощью docker-compose

`docker-compose up`

# Запуск локально

Необходимо поставить пакеты `gradle>=7.4.2`, `python>=3.10`,
`python3-pygments`, `python3-pillow`, `python3-autopep8`, установить
и запустить MongoDB. Также необходимо иметь
в системе jdk поддерживающий Java 11.

Для запуска необходимо прописать параметры соединения с базой данных в
`config.properties`, находящийся в `server/src/resources`. Также можно
установить значение переменной среды `GENERATION_DIR` обозначающей путь к
директории, в которой будет происходить обработка файлов с исходным кодом
(по умолчанию все действия происходят в папке `server`)

Чтобы запустить сервер, необходимо выполнить `./gradlew server:run` в корневой
директории проекта.

# Управление задачами

С помощью модуля `task-manager` осуществляется добавление и удаление шаблонов
задач. В файле `Main.kt` надо в переменную `tasks` записать список задач
(название, тэг и шаблон) которые вы хотите добавить или записать в `deletedTasks`
имена задач, которые вы хотите удалить. Шаблоны можно писать с помощью
DSL (см модуль `dsl`) в отдельных файлах (см пример в файле `task.kt`).
Затем запустить `./gradlew task-manager:installDist`, далее запустить
`task-manager/task-manager`. Возможные параметры:

```shell
Usage: task-manager options_list
Arguments: 
    Command { Value should be one of [add, delete] }
    Task name -> Name of created task { String }
Options: 
    --host [0.0.0.0] -> Host of code generator { String }
    --port [8080] -> Port of code generator { Int }
    --help, -h -> Usage info 
```
