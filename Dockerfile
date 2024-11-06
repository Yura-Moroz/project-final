FROM openjdk:17

COPY resources ./resources

COPY target/jira-1.0.jar /jira.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "/jira.jar"]