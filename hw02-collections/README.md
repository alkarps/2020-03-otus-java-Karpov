###Домашняя работа №2: DIY ArrayList

<p>Домашняя работа содержит в себе класс реализации самодельного списка с реализацией итератора. Данная реализаци не потокобезопасна.</p>

Класс [DiyArrayList](src/main/java/my/alkarps/DiyArrayList.java).
Способы создания списка:
- Создать пустой список. Создает пустой список с массивом размером = 0.
- Создать список с указанным размером массива = указанному значению.
- Создает список на основе указанной коллекции.

Реализованные методы:

- `size()`
- `isEmpty()`
- `iterator()`
- `toArray()`
- `toArray(T1[] a)`
- `add(T t)`
- `addAll(Collection<? extends T> c)`
- `clear()`
- `get(int index)`
- `add(int index, T element)`
- `remove(int index)`
- `listIterator()`
- `listIterator(int index)`

Особенности:
- Массив расширяется на 5 элементов при добавлении нового элемента, если новый размер списка = размеру массиву.
- Добавлена проверка на `null` входных коллекций и массивов.
- Потоконебезопасна.

В качестве реализации интерфейсов `Iterator` и `ListIterator` использовались внутренние классы `DiyIterator` и `DiyListIterator`.  


В качестве проверки работы самодельного листа реализован тест [CollectionsTest](src/test/java/my/alkarps/CollectionsTest.java).

Для проверки использовались следующие методы:

- `Collections.addAll`
- `Collections.copy`
- `Collections.reverse`
- `Collections.sort`