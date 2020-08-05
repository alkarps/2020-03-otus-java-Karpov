###Домашняя работа №4. GC

<p>Домашняя работа для изучения работы GC, осваивании инструментов мониторинга работы JVM.</p>

В качестве GC использовались:
+ G1
+ Serial Collector
+ Parallel Collector

<h3>Однопоточный режим</h3>

В качестве подопытной программы использовался класс `ListOOM`, основанный на постоянно увеличивающимся списке.
Используемые параметры:
```shell script
java -Xms200m -Xmx200m -verbose:gc -Xlog:gc*:file=./logs/gc_pid_%p.log -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=./logs/dump
```
<p>Результаты:</p>
Первый запуск:

|Тип замера|G1 (Java heap space)|Serial (Java heap space)|Parallel (GC overhead limit exceeded)|
|:---------|:------------------:|:----------------------:|:-----------------------------------:|
|Количество элементов|2442848|2375576|2181832|
|Время работы|320.232s|265.138s|202.027s|
|Время первого срабатывания полной сборки|253.134s|60.427s|54.623s|
|Длительность первого срабатывания полной сборки|112.480ms|163.568ms|236.042ms|
|Порог срабатывания|200M->176M(200M)|190M->111M(193M)|183M->105M(186M)|
|Количество сборок|300|310|143|
|Количество полных сборок|78|282|104|

Второй запуск:

|Тип замера|G1 (Java heap space)|Serial (Java heap space)|Parallel (GC overhead limit exceeded)|
|:---------|:------------------:|:----------------------:|:-----------------------------------:|
|Количество элементов|2442184|2375504|2182424|
|Время работы|248.746s|267.403s|200.250s|
|Время первого срабатывания полной сборки|182.099s|54.207s|54.860s|
|Длительность первого срабатывания полной сборки|112.390ms|136.616ms|230.333ms|
|Порог срабатывания|199M->176M(200M)|191M->108M(193M)|183M->105M(186M)|
|Количество сборок|294|325|140|
|Количество полных сборок|76|296|101|

Третий запуск:

|Тип замера|G1 (Java heap space)|Serial (Java heap space)|Parallel (GC overhead limit exceeded)|
|:---------|:------------------:|:----------------------:|:-----------------------------------:|
|Количество элементов|2442464|2375552|2196831|
|Время работы|252.578s|268.447s|213.778s|
|Время первого срабатывания полной сборки|172.057s|56.324s|57.158s|
|Длительность первого срабатывания полной сборки|131.520ms|147.477ms|248.065ms|
|Порог срабатывания|199M->170M(200M)|190M->111M(193M)|184M->107M(183M)|
|Количество сборок|287|342|146|
|Количество полных сборок|70|314|106|

Вывод:
+ Полная сборка у G1 происходит на более поздних этапах, когда старое поколение заполняется. Время сборки меньше, чем у остальных двух. При этом использование хипа программой больше. Так же стоит отметить, что количество полных сборок мусора меньше, чем у остальных.
+ При использовании Parallel необходимо учитывать выделение памяти для самого сборщика мусора. Время отрабатывания больше, чем у всех остальных, но общее количество сборок - меньше всего. Первое срабатывание полной сборки мусора происходит немного позже, чем у Serial Collector. Программе выделено меньше всего памяти в хипе.
+ Первоначально Serial оказался полезнее, чем Parallel Collector, но хуже, чем G1. Но под конец он начал сильно влиять на работу программы. Так же он первый произвел полную сборку. В остальном он на втором месте, кроме количества сборок мусора (и общих и только полных) - их больше всего.

В проведенном анализе G1 оказался лучшим, по этому, при возможности, лучше всего использовать именно его.
Так же при выборе Serial Collector нужно помнить, что при ООМ работа программы практически парализована.
При выборе Parallel Collector нужно так же выделять определенное количество памяти из кучи для GC для корректной работы.  

<h3>Многопоточный режим</h3>

В качестве подопытной программы использовался класс `ThreadsOOM`. Основан он на параллельном запуске класса `ListOOM` из однопоточного режима.
Используемые параметры:
```shell script
java -Xms200m -Xmx200m -verbose:gc -Xlog:gc*:file=./logs/gc_pid_%p.log -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=./logs/dump
```

Parallel Collector:
При использовании данного GC первая полная сборка происходит примерно на 13.493 секунде на уровне 126M->122M(178M) и длилась 211.144 мс:
```log
[13.493s][info][gc,start     ] GC(16) Pause Full (Ergonomics)
[13.704s][info][gc             ] GC(16) Pause Full (Ergonomics) 126M->122M(178M) 211.144ms
```

После этого идут частые полные сборки, но в основном уровень заполненности хипа держится на 155M->155M(178M):
```log
[113.802s][info][gc             ] GC(760) Pause Full (Ergonomics) 155M->155M(178M) 81.809ms
```

Но иногда удается очистить память из-за завершения порожденного потока с ООМ:
```log
[114.155s][info][gc             ] GC(763) Pause Full (Ergonomics) 155M->148M(178M) 103.148ms
``` 

Чем ближе поток к ООМ, тем чаще идут сборки:
```log
[327.839s][info][gc] GC(2336) Pause Full (Ergonomics) 155M->155M(178M) 96.280ms
[327.936s][info][gc] GC(2337) Pause Full (Ergonomics) 155M->155M(178M) 96.094ms
[328.052s][info][gc] GC(2338) Pause Full (Ergonomics) 155M->155M(178M) 115.052ms
[328.153s][info][gc] GC(2339) Pause Full (Ergonomics) 155M->155M(178M) 100.897ms
[328.247s][info][gc] GC(2340) Pause Full (Ergonomics) 155M->155M(178M) 93.040ms
Exception in thread "Thread-70" java.lang.OutOfMemoryError: GC overhead limit exceeded
```

Процесс завершается с падением потока `main` с ООМ на 340.082 секунде после 2364 сборок, из которых 2272 были полными.

Serial Collector:

Первая полная сборка мусора запустилась на 14.952 секунде на уровне 181M->141M(193M) и продлилась 163.979 мс:
```log
[14.952s][info][gc,start     ] GC(11) Pause Full (Allocation Failure)
[15.116s][info][gc             ] GC(11) Pause Full (Allocation Failure) 181M->141M(193M) 163.979ms
```

Довольно скоро GC стал работать активно из-за ООМ в порожденных потоках:
```log
[28.074s][info][gc] GC(66) Pause Full (Allocation Failure) 193M->193M(193M) 168.560ms
[28.244s][info][gc] GC(67) Pause Full (Allocation Failure) 193M->193M(193M) 169.243ms
[28.419s][info][gc] GC(68) Pause Full (Allocation Failure) 193M->193M(193M) 174.941ms
[28.595s][info][gc] GC(69) Pause Full (Allocation Failure) 193M->193M(193M) 174.661ms
[28.767s][info][gc] GC(70) Pause Full (Allocation Failure) 193M->193M(193M) 171.560ms
[28.937s][info][gc] GC(71) Pause Full (Allocation Failure) 193M->193M(193M) 169.894ms
java.lang.OutOfMemoryError: Java heap space
...
[36.956s][info][gc] GC(115) Pause Full (Allocation Failure) 193M->193M(193M) 170.469ms
[37.126s][info][gc] GC(116) Pause Full (Allocation Failure) 193M->193M(193M) 168.748ms
[37.297s][info][gc] GC(117) Pause Full (Allocation Failure) 193M->193M(193M) 171.602ms
[37.466s][info][gc] GC(118) Pause Full (Allocation Failure) 193M->193M(193M) 168.128ms
[37.635s][info][gc] GC(119) Pause Full (Allocation Failure) 193M->193M(193M) 168.923ms
[37.814s][info][gc] GC(120) Pause Full (Allocation Failure) 193M->190M(193M) 178.275ms
...
Exception in thread "Thread-3" java.lang.OutOfMemoryError: Java heap space
Exception in thread "Thread-6" java.lang.OutOfMemoryError: Java heap space
Exception in thread "Thread-0" java.lang.OutOfMemoryError: Java heap space
```

После нескольких ООМ процесс завершился на 127.720 секунде после 464 сборок мусора, из которых полных было 448.

G1 Collector:
Полная сборка мусора запустилась на 13.280 секунде на пороге 200M->179M(200M) и продлилась 110.348 мс:
```log
[13.280s][info][gc,start     ] GC(45) Pause Full (G1 Evacuation Pause)
[13.390s][info][gc             ] GC(45) Pause Full (G1 Evacuation Pause) 200M->179M(200M) 110.348ms
```
Не смотря на отличные результаты при однопоточном режиме, G1 довольно скоро начал убивать потоки с ООМ:
```log
[16.325s][info][gc] GC(99) To-space exhausted
[16.325s][info][gc] GC(99) Pause Young (Normal) (G1 Evacuation Pause) 199M->199M(200M) 6.472ms
[16.420s][info][gc] GC(100) Pause Full (G1 Evacuation Pause) 199M->199M(200M) 94.899ms
[16.524s][info][gc] GC(101) Pause Full (G1 Evacuation Pause) 199M->199M(200M) 103.950ms
[16.526s][info][gc] GC(102) Pause Young (Concurrent Start) (G1 Evacuation Pause) 199M->199M(200M) 0.769ms
[16.526s][info][gc] GC(104) Concurrent Cycle
[16.613s][info][gc] GC(103) Pause Full (G1 Evacuation Pause) 199M->199M(200M) 87.280ms
[16.717s][info][gc] GC(105) Pause Full (G1 Evacuation Pause) 199M->199M(200M) 103.382ms
java.lang.OutOfMemoryError: Java heap space
``` 

И в скором времени завершил выполнение программы:
```log
[67.540s][info][gc] GC(553) Pause Full (G1 Evacuation Pause) 199M->199M(200M) 104.208ms
[67.646s][info][gc] GC(554) Pause Full (G1 Evacuation Pause) 199M->199M(200M) 105.714ms
[67.648s][info][gc] GC(555) Pause Young (Concurrent Start) (G1 Evacuation Pause) 199M->199M(200M) 0.717ms
[67.648s][info][gc] GC(557) Concurrent Cycle
[67.652s][info][gc] GC(556) Pause Full (G1 Evacuation Pause) 199M->1M(200M) 4.581ms
[67.653s][info][gc] GC(557) Concurrent Cycle 4.977ms
Exception in thread "Thread-6" java.lang.OutOfMemoryError: Java heap space

Process finished with exit code 1
```

В итоге время работы программы составило 67.654 секунду с 557 сборками мусора, из которых 177 оказались полными.

Вывод: тк оценить качество работы по сохраненным объектом в памяти сложно, то оценка дана с точки зрения времени работы программы.
В многопоточном режиме лучше всего показал себя Parallel Collector, в отличие от однопоточного режима.
G1 Collector оказался хуже всего по времени работы, но в соотношении количества полных сборок к общему количеству сборок оказался лидером. 

Общий вывод:
Таким образом выбор GC - один из важных шагов к тюнингу производительности, выбор к которому нужно подходить исходя из потребностей и произведенных исследований.  

В данном сравнении не анализировалось потребление CPU и других важный параметров работы системы.