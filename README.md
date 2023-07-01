# Индивидуальный проект IT Школы Samsung: Планер для путешествий - SmartTravel
Защита: 30 мая 2020г.
## Идеи приложения 
При планировании поездок нужно учитывать множество факторов, таких как распределение бюджета, поиск красивых мест для остановки и тд. Для этой цели часто используют заметки или блокноты, что не очень удобно. 
Для этой цели я создала приложение, которое позволит планировать поездку: путем создания записей об остановках, с возможностью указывать тип и стоимость(если нужно), в тоже время позволит просматривать маршрут на карте.
## Цели и задачи проекта
Цель проекта: создать приложение которое позволит планировать поездки.
Задачи: 
* Создать удобный пользовательский интерфейс  
* Реализовать sqlite базу данных внутри устройства  
* Предусмотреть возможность добавления, удаления, редактирования записей о пунктах маршрута  
* Реализовать ввод начального и конечного адресов маршрута, получение координат этих мест, просмотр маршрута на карте  
## Реализация 
Приложение реализовано на языке java, на базе Android; построение маршрута — API Яндекса -  MapKit, данные о поездке хранятся в локальной SQLite базе данных.
## Работа приложения
![alt-текст](https://github.com/NicoWithARedCarnation/SmartTravel/blob/master/pic1.png)
![alt-текст](https://github.com/NicoWithARedCarnation/SmartTravel/blob/master/pic2.png)
![alt-текст](https://github.com/NicoWithARedCarnation/SmartTravel/blob/master/pic3.png)
![alt-текст](https://github.com/NicoWithARedCarnation/SmartTravel/blob/master/pic4.png)
## Структура приложения
![alt-текст](https://github.com/NicoWithARedCarnation/SmartTravel/blob/master/pic5.png)
## Идеи для дальнейшей разработки
* Создание серверной части приложения  
* Улучшение дизайна приложения  
* Возможность создания меток на карте  
* Добавление "Дневника путешественника" для возможности заметок  
* Возможность создания нескольких поездок, и свободного переключения между ними  