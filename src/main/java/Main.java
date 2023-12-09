import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static Table table = new Table();
    private static boolean isTableInitialized = false;

    public static void main(String[] args) throws ParseException, IOException {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            if (!isTableInitialized) {
                System.out.println("1. Создать пустую таблицу");
                System.out.println("2. Загрузить таблицу из файла Excel");
                System.out.println("3. Выход");
            } else {
                // Остальные пункты меню
                System.out.println("1. Добавить строку в таблицу");
                System.out.println("2. Добавить столбец в таблицу");
                System.out.println("3. Отобразить таблицу");
                System.out.println("4. Сортировать таблицу");
                System.out.println("5. Сохранить таблицу в файл Excel");
                System.out.println("6. Загрузить другую таблицу из файла Excel");
                System.out.println("7. Выход");
            }

            System.out.print("Введите номер действия: ");
            int action = scanner.nextInt();
            scanner.nextLine();
            if (!isTableInitialized) {
                switch (action) {
                    case 1:
                        createAndFillTable(scanner);
                        break;
                    case 2:
                        System.out.println("Введите полный путь к таблице:");
                        String filename = scanner.nextLine();
                        table.readFromExcel(filename);
                        isTableInitialized = true;
                        break;
                    case 3:
                        System.out.println("Выход из программы.");
                        scanner.close();
                        return;
                    default:
                        System.out.println("Неверный ввод, попробуйте еще раз.");
                        break;
                }
            } else {
                switch (action)
                {
                    case(1):
                        addRowToTable(scanner);
                        break;
                    case(2):
                        addColumnToTable(scanner);
                        break;
                    case(3):
                        table.printTable();
                        break;
                    case(4):
                        sortTable(scanner);
                        break;
                    case(5):
                        System.out.println("Введите полный путь куда сохранить таблицу:");
                        String saveFilename = scanner.nextLine();
                        table.saveToExcel(saveFilename);
                        System.out.println("Таблица успешно сохранена");
                        break;
                    case(6):
                        System.out.println("Введите полный путь к таблице:");
                        String readFilename = scanner.nextLine();
                        table.readFromExcel(readFilename);
                        break;
                    case(7):
                        System.out.println("Выход из программы.");
                        scanner.close();
                        return;
                    default:
                        System.out.println("Неверный ввод, попробуйте еще раз.");
                        break;
                }
            }
        }
    }

    private static void createAndFillTable(Scanner scanner) throws ParseException {
        System.out.print("Введите количество столбцов: ");
        int columnsCount = scanner.nextInt();
        scanner.nextLine(); // Очищаем буфер, считывая оставшийся символ новой строки после nextInt()

        List<String> columnNames = new ArrayList<>();
        TableRow prototypeRow = new TableRow();
        TableRow firstRow = new TableRow();

        for (int i = 0; i < columnsCount; i++) {
            System.out.print("Введите название для столбца " + (i + 1) + ": ");
            String columnName = scanner.nextLine(); // Теперь здесь не требуется второй вызов nextLine()
            columnNames.add(columnName);

            System.out.println("Выберите тип для столбца " + columnName + ":");
            System.out.println("1. Целое число (Integer)");
            System.out.println("2. Строка (String)");
            System.out.println("3. Дата (Date)");
            System.out.println("4. Время (Time)");
            System.out.println("5. GPS координаты (GPS)");
            System.out.println("6. Вещественное число (Double)");

            DataPrototype dataPrototype = null;
            System.out.print("Введите номер типа данных: ");
            int columnType = scanner.nextInt();
            switch (columnType) {
                case 1:
                    dataPrototype = new IntegerData(0);
                    break;
                case 2:
                    dataPrototype = new StringData("");
                    break;
                case 3:
                    dataPrototype = new DateData("01.01.1970");
                    break;
                case 4:
                    dataPrototype = new TimeData("00:00:00");
                    break;
                case 5:
                    dataPrototype = new GpsData(0.0, 0.0);
                    break;
                case 6:
                    dataPrototype = new DoubleData(0.0);
                    break;
                default:
                    System.out.println("Неверный тип данных, используется тип данных String");
                    dataPrototype = new StringData("");
                    break;
            }
            prototypeRow.addColumn(dataPrototype);

            System.out.print("Введите значение для " + columnName + ": ");
            scanner.nextLine(); // Очищаем буфер
            String value = scanner.nextLine();
            firstRow.addColumn(parseData(value, dataPrototype));
        }

        table = new Table(columnNames, prototypeRow);
        table.addRow(firstRow);
        isTableInitialized = true;
        System.out.println("Пустая таблица создана.");
    }

    private static DataPrototype parseData(String value, DataPrototype prototype) throws ParseException {
        if (prototype instanceof IntegerData) {
            try {
                int intValue = Integer.parseInt(value);
                return new IntegerData(intValue);
            } catch (NumberFormatException e) {
                System.out.println("Некорректное целое число. Используется значение по умолчанию 0.");
                return new IntegerData(0);
            }
        } else if (prototype instanceof StringData) {
            return new StringData(value);
        } else if (prototype instanceof DateData) {
            // Здесь могут быть добавлены дополнительные проверки и форматирование даты
            return new DateData(value);
        } else if (prototype instanceof TimeData) {
            // Аналогично, могут потребоваться проверки и форматирование времени
            return new TimeData(value);
        } else if (prototype instanceof GpsData) {
            try {
                String[] parts = value.split(",");
                double latitude = Double.parseDouble(parts[0].trim());
                double longitude = Double.parseDouble(parts[1].trim());
                return new GpsData(latitude, longitude);
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                System.out.println("Некорректные GPS координаты. Используются значения по умолчанию 0.0, 0.0.");
                return new GpsData(0.0, 0.0);
            }
        }
        return new StringData(value); // Возвращаем StringData по умолчанию, если тип не определен
    }

    private static void addRowToTable(Scanner scanner) throws ParseException {
        if (table == null || !isTableInitialized) {
            System.out.println("Таблица не инициализирована. Пожалуйста, создайте или загрузите таблицу.");
            return;
        }

        TableRow newRow = new TableRow();
        for (String columnName : table.getColumnNames()) {
            System.out.print("Введите данные для столбца '" + columnName + "': ");
            String input = scanner.next();

            // Здесь используем фактический тип данных столбца для преобразования ввода пользователя
            DataPrototype prototype = table.getColumnPrototype(columnName);
            DataPrototype data = parseData(input, prototype);
            newRow.addColumn(data);
        }

        table.addRow(newRow);
        System.out.println("Строка успешно добавлена в таблицу.");
    }

    private static void sortTable(Scanner scanner){
        if (table == null || !isTableInitialized) {
            System.out.println("Таблица не инициализирована. Пожалуйста, создайте или загрузите таблицу.");
            return;
        }

        System.out.print("Введите имя столбца, по которому хотите отсортировать: ");
        String inputName = scanner.nextLine();
        //scanner.nextLine();

        System.out.println("В каком направлении вы хотите отсортировать:");
        System.out.println("1. По возрастанию");
        System.out.println("2. По убыванию");

        SortDirection direction = null;
        try {
            int inputDirection = Integer.parseInt(scanner.nextLine());
            if (inputDirection == 1) {
                direction = SortDirection.ASCENDING;
            } else if (inputDirection == 2) {
                direction = SortDirection.DESCENDING;
            } else {
                System.out.println("Вы ввели неправильное число");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Неправильный ввод. Пожалуйста, введите число.");
            return;
        }

        try {
            table.sortTableByColumn(inputName, direction);
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private static void addColumnToTable(Scanner scanner) throws ParseException {
        if (table == null || !isTableInitialized) {
            System.out.println("Таблица не инициализирована. Пожалуйста, создайте или загрузите таблицу.");
            return;
        }

        System.out.print("Введите название нового столбца: ");
        String columnName = scanner.nextLine();

        System.out.println("Выберите тип данных для нового столбца:");
        System.out.println("1. Целое число (Integer)");
        System.out.println("2. Строка (String)");
        System.out.println("3. Дата (Date)");
        System.out.println("4. Время (Time)");
        System.out.println("5. GPS координаты (GPS)");
        System.out.println("6. Вещественное число (Double)");

        int choice = scanner.nextInt();
        scanner.nextLine(); // Очищаем буфер

        DataPrototype columnPrototype = null;
        switch (choice) {
            case 1:
                columnPrototype = new IntegerData(0);
                break;
            case 2:
                columnPrototype = new StringData("");
                break;
            case 3:
                columnPrototype = new DateData("1970-01-01");
                break;
            case 4:
                columnPrototype = new TimeData("00:00:00");
                break;
            case 5:
                columnPrototype = new GpsData(0.0, 0.0);
                break;
            case 6:
                columnPrototype = new DoubleData(0.0);
                break;
            default:
                System.out.println("Некорректный выбор. Используется тип данных String.");
                columnPrototype = new StringData("");
                break;
        }

        table.addColumn(columnName, columnPrototype);
        System.out.println("Столбец '" + columnName + "' успешно добавлен.");
    }
}
