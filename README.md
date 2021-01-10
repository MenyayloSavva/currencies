### Currencies
Spring Boot 2 application. Checks if today's currency rate has changed negatively or positively, comparing with the previous day. Returns a corresponding GIF as a response.

### Используемые версии:

Java 1.8\
Docker 20.10

### Инструкция по запуску:

Откройте командную строку, перейдите в рабочую директорию проекта.
Далее запустите следующие команды:

Linux:\
_./gradlew build ( ".\gradlew build" for Windows OS, single backslash)_\
_docker build --build-arg JAR_FILE=build/libs/*.jar -t springio/gs-spring-boot-docker_ .\
_docker run -p 8080:8080 springio/gs-spring-boot-docker_

Должно запуститься Spring Boot приложение в docker-контейнере, к которому можно отправлять post-запросы.
Для отправки запросов удобнее всего использовать Postman.

Отправляем Post - запрос к http://localhost:8080/evaluate/currency \
В теле которого должен быть JSON с обязательным полем "code":\
_{_\
_"code": "UAH"_\
_}_

Где 3-х буквенный код может быть любым из списка доступных валют: https://openexchangerates.org/api/currencies.json
Кроме USD (Т. к. в бесплатной версии аккаунта https://openexchangerates.org/ доступна только USD в качестве базовой валюты, по отношению к которой производится сравнение,
поэтому с самой собой сравнивать валюту бессмысленно)

В ответ получаем JSON типа:\
_{_\
_"rate_today": 28.331616,_\
_"rate_yesterday": 28.430255,_\
_"is_rich": false,_\
_"gif_url": "https://giphy.com/gifs/cartoonhangover-cartoons-bravestwarriors-WgSIUlqU9NH3ulLnZf"_ \
_}_
