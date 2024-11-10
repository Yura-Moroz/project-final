## [REST API](http://localhost:8080/doc)

## Concept:

- Spring Modulith
    - [Spring Modulith: достигли ли мы зрелости модульности](https://habr.com/ru/post/701984/)
    - [Introducing Spring Modulith](https://spring.io/blog/2022/10/21/introducing-spring-modulith)
    - [Spring Modulith - Reference documentation](https://docs.spring.io/spring-modulith/docs/current-SNAPSHOT/reference/html/)

```
  url: jdbc:postgresql://localhost:5432/jira
  username: jira
  password: JiraRush
```


There are 2 common tables on which there is no fk
- _Reference_ - reference book. We make the connection using _code_ (you can’t use id, as id is tied to the environment, a specific database)
- _UserBelong_ - binding of users with a type (owner, lead, ...) to an object (task, project, sprint, ...). FK manually we will
check

## Analogs

- https://java-source.net/open-source/issue-trackers

## Testing

- https://habr.com/ru/articles/259055/

Список выполненных задач:
- 2 + removed all redundant code
- 3 + placed all sensitive information to the environment variables. 
- 5 + wrote some tests for the ProfileRestController class.
- 6 + made code refactoring in FileUtil class so that it uses modern way of file handling.
- 7 + implemented task_tag table. You can find addTags() methods in TaskController & TaskService
- 9 + Wrote Dockerfile but it refused to start without a database so I decided to write docker-compose file. 
- 10 +/- Made docker-compose. The nginx & db containers start, but when I want to start the main application, it produces a strange stacktrace and the container crashes after 4 seconds(
  Took 2 days trying to fix it. I still haven't found the error.