import java.util.ArrayList;
import java.util.List;


public class Main {
    public static void main(String[] args) {
        // Создание пустого множества
        CustomSet nullSet = new CustomSet();
        System.out.print("Создание пустого множества: ");
        System.out.println(nullSet);

        // Создание множества на основе готового списка
        List<Integer> readyList= new ArrayList<>();
        readyList.add(1);
        readyList.add(5);
        readyList.add(7);
        CustomSet readySet = new CustomSet(readyList);
        System.out.print("Создание множества на основе готового списка: ");
        System.out.println(readySet);

        // Создание множества на основе параметров конструктора - целых чисел
        CustomSet paramSet = new CustomSet(5, 6, 7, 7, 9);
        System.out.print("Создание множества на основе параметров конструктора - целых чисел: ");
        System.out.println(paramSet);

        // Добавление элемента 20 в множество paramSet
        System.out.print("Добавление элемента 20 в множество paramSet: ");
        paramSet.add(20);
        System.out.println(paramSet);

        // Удаление элемента 6 из множества paramSet
        System.out.print("Удаление элемента 6 из множества paramSet: ");
        paramSet.del(6);
        System.out.println(paramSet);

        // Ввод элементов множества с клавиатуры
        System.out.println("Ввод элементов множества с клавиатуры");
        CustomSet inputSet = new CustomSet();
        inputSet.addFromInput();
        System.out.print("Множество из введенных данных: ");
        System.out.println(inputSet);

        // Операция объединения между readySet и paramSet
        System.out.print("Операция объединения между readySet и paramSet: ");
        CustomSet unionSet = readySet.union(paramSet);
        System.out.println(unionSet);

        // Операция пересечения между readySet и paramSet
        System.out.print("Операция пересечения между readySet и paramSet: ");
        CustomSet intersectionSet = readySet.intersection(paramSet);
        System.out.println(intersectionSet);

        // Операция разницы между readySet и paramSet
        System.out.print("Операция разницы между readySet и paramSet: ");
        CustomSet diffSet = readySet.diff(paramSet);
        System.out.println(diffSet);

        // Сохранение объекта в тектовый и бинарный файл
        inputSet.saveToTextFile("inputSet.txt");
        System.out.println("Данные множества inputSet сохранены в текстовый файл inputSet.txt");
        inputSet.saveToBinaryFile("inputSet.bin");
        System.out.println("Данные множества inputSet сохранены в бинарный файл inputSet.bin");


        // Загрузка нового множества из текстового и бинарного файлов
        CustomSet readTextSet = new CustomSet();
        readTextSet.readFromTextFile("inputSet.txt");
        System.out.print("Множество readTextSet, загруженное из текстового файла inputSet.txt");
        System.out.println(readTextSet);

        CustomSet readBinarySet = new CustomSet();
        readBinarySet.readFromBinaryFile("inputSet.bin");
        System.out.print("Множество readBinarySet, загруженное из текстового файла inputSet.bin");
        System.out.println(readBinarySet);

    }
}