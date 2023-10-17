import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CustomSet {
    private List<Integer> elements;

    public CustomSet() {                                    // Непараметрический конструктор
        this.elements = new ArrayList<>();
    }

    public CustomSet(List<Integer> BaseElements) {          // Параметрический конструктор
        this.elements = new ArrayList<>(BaseElements);      // Создание на основе готового списка
    }

    public CustomSet(Integer... BaseElements) {             // Параметрический конструктор
        this.elements = new ArrayList<>();                  // Создание на основе параметров - целых чисел
        for (Integer element : BaseElements) {
            if (!this.elements.contains(element)) {
                this.elements.add(element);
            }
        }
    }


    public String toString() {                          // Метод для вывода множества в stdout
        StringBuilder array = new StringBuilder("{ ");
        for (Integer elem : this.elements) {
            array.append(elem.toString()).append(" ");
        }
        array.append("}");
        return array.toString();
    }

    public void addFromInput() {
        this.elements = new ArrayList<>();
        System.out.println("Введите элементы множества через пробел. При окончании нажмите Enter");
        Scanner sc = new Scanner(System.in);
        if (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] inputArray = line.split(" ");
            for (String s : inputArray) {
                try {
                    int num = Integer.parseInt(s);
                    this.elements.add(num);
                } catch (NumberFormatException e) {
                    System.out.println("Ошибка при чтении числа " + s);
                }
            }
        }
    }

    public void add(Integer element) {
        if (!containsElement(element)) {
            this.elements.add(element);
        }
    }

    public void del(Integer element) {
        if (containsElement(element)) {
            this.elements.remove(element);
        }
    }

    public boolean containsElement(Integer element) {
        return this.elements.contains(element);
    }

    public CustomSet intersection(CustomSet other) {
        List<Integer> result = new ArrayList<>();
        for (Integer element : this.elements) {
            if (other.containsElement(element)) {
                result.add(element);
            }
        }
        return new CustomSet(result);
    }

    public CustomSet union(CustomSet other) {
        List<Integer> result = new ArrayList<>(this.elements);
        for (Integer element : other.elements) {
            if (!containsElement(element)) {
                result.add(element);
            }
        }
        return new CustomSet(result);
    }

    public CustomSet diff(CustomSet other) {
        List<Integer> result = new ArrayList<>();
        for (Integer element : this.elements) {
            if (!other.containsElement(element)) {
                result.add(element);
            }
        }
        return new CustomSet(result);
    }

    public void saveToTextFile(String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (Integer element : this.elements) {
                writer.print(element + " ");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveToBinaryFile(String filename) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(filename))) {
            for (Integer element : this.elements) {
                outputStream.writeObject(element);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readFromTextFile(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line = reader.readLine();
            if (line != null) {
                String[] values = line.split(" ");
                for (String value : values) {
                    this.elements.add(Integer.parseInt(value));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readFromBinaryFile(String filename) {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(filename))) {
            while (true) {
                try {
                    this.elements.add((Integer) inputStream.readObject());
                } catch (EOFException e) {
                    break;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
