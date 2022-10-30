# GraduateWork  
Итоговая квалификационная работа по курсу FQA-37 профессии Тестировщик. 
Дипломный проект — автоматизация тестирования комплексного сервиса, взаимодействующего с СУБД и API Банка.
## Начало работы
### Прграммное обеспечение  
Ниже перечисленны программы, которые необходимы для запуска проекта на локальном ПК:

  - **Git Bash** - для инициализации и клонирования проекта
  - **IntelliJ IDEA** - наиболее подходящая среда для работы с проектом
  - **Docker** - для работы с контейнеризацией
  - **JUnit 5** - тестовый фраймворк
  - **Selenide** - фрэймворк для автоматизированных UI тестов
  - **Lombok** - библиотека Java
  - **Google Chrome** - браузер для работы UI тестов
  
С более подробным обоснованием выбранного программного обеспечение можно ознакомится в плане автоматизации [**Plane.md**](https://github.com/IliaMaksimenko/GraduateWork/blob/master/documentations/Plane.md).
  
### Установка и запуск проекта
Ниже перечислены шаги, которые позволят получить проект, установить и корректно запустить его.  
#### Клонирование проекта на локальный ПК:
  - Скопировать ссылку на данный репозиторий
  - На локальном ПК создать пустую папку
  - В пустой папке выполнить клик правой кнопкой мыши и в меню выбрать пункт **Git Bash Here**
  - В открывшемся командном окне выполнить команду ```git clone <ссылка на данный репозиторий>```  
  
#### Запуск проекта:
  - Запустить склонированный проект в IntelliJ IDEA 
  - Запустить docker-machine start default 
  - Для создания образа баз данных MySQL и PostgreSQL в терминале IntelliJ IDEA выполнить команду:   
  ```docker-compose up -d```  
    **MySQL**:  
  - Для запуска SUT с базой данных MySQL в терминале IntelliJ IDEA выполнить команду:   
  ```java "-Dspring.datasource.url=jdbc:mysql://localhost:3306/app" -jar artifacts/aqa-shop.jar```
  - Для запуска тестов по ранее написанному сценарию [**TestCase.md**](https://github.com/IliaMaksimenko/GraduateWork/blob/master/documentations/TestCase.md) в новой вкладке терминала IntelliJ IDEA выполнить команду:   
  ```gradlew clean test```  
    **PostgreSQL**:
  - Для запуска SUT с базой данных PostgreSQL в терминале IntelliJ IDEA выполнить команду:   
  ```java "-Dspring.datasource.url=jdbc:postgresql://localhost:5432/app" -jar artifacts/aqa-shop.jar```
  - Для запуска тестов по ранее написанному сценарию [**TestCase.md**](https://github.com/IliaMaksimenko/GraduateWork/blob/master/documentations/TestCase.md) в новой вкладке терминала IntelliJ IDEA выполнить команду:  
  ```gradlew clean test "-Ddb.url=jdbc:postgresql://localhost:5432/app"```
