## [REST API](http://localhost:8080/doc)

## Концепция:

- Spring Modulith
    - [Spring Modulith: достигли ли мы зрелости модульности](https://habr.com/ru/post/701984/)
    - [Introducing Spring Modulith](https://spring.io/blog/2022/10/21/introducing-spring-modulith)
    - [Spring Modulith - Reference documentation](https://docs.spring.io/spring-modulith/docs/current-SNAPSHOT/reference/html/)

```
  url: jdbc:postgresql://localhost:5432/jira
  username: jira
  password: JiraRush
```

- Есть 2 общие таблицы, на которых не fk
    - _Reference_ - справочник. Связь делаем по _code_ (по id нельзя, тк id привязано к окружению-конкретной базе)
    - _UserBelong_ - привязка юзеров с типом (owner, lead, ...) к объекту (таска, проект, спринт, ...). FK вручную будем
      проверять

## Аналоги

- https://java-source.net/open-source/issue-trackers

## Тестирование

- https://habr.com/ru/articles/259055/

Список выполненных задач:
- 2 +
- 3 +
- 5 +
- 6 +
- 7 + 
методы addTags можете найти в TaskController & TaskService
- 9 + написал Dockerfile, вроде все правильно, но запуститься без БД контейнер не захотел. Начал писать docker-compose 
- 10 +/- сделал docker-compose. Контейнеры nginx & db стартуют, но когда хочу запустить основное приложение, выдает странный стэктрейс и контейнер падает через 4 секунды(
 Ушло 2 дня на попытку это исправить. Ошибку так и не нашел.