###Домашняя работа №6. SOLID

<p>Домашняя работа по эмуляции работы АТМ.</p>
На данный момент реализовано 3 операции:

+ Получение денежных средств.
+ Выдача денежных средств.
+ Отображение текущей суммы в АТМ.

Основных классы:
+ `ATM` - в данный момент прокси-класс к классу кассы.
+ `CashBox` - реализация кассы с возможностью сохранения истории изменения кассы (не используется). В своей реализации основан на иммутабельном классе `Cassette`.
+ `Cassette` - реализация иммутабельной ячейки кассы.
