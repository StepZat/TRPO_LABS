import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Table {
    private List<String> columnNames;
    private TableRow prototypeRow;
    private List<TableRow> rows;

    public Table(List<String> columnNames, TableRow prototypeRow) {
        this.columnNames = new ArrayList<>(columnNames);
        this.prototypeRow = prototypeRow;
        this.rows = new ArrayList<>();
    }

    public Table() {
        this.columnNames = new ArrayList<>();
        this.rows = new ArrayList<>();
    }

    public void addRow(TableRow row) {
        rows.add(row);
    }

    public void removeRow(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < rows.size()) {
            rows.remove(rowIndex);
        }
    }

    public TableRow getRow(int index) {
        return rows.get(index);
    }

    public void sortTableByColumn(String columnName, SortDirection direction) {
        int columnIndex = columnNames.indexOf(columnName);
        if (columnIndex < 0 || columnIndex >= columnNames.size()) {
            throw new IllegalArgumentException("Invalid column index");
        }

        Comparator<TableRow> comparator = new Comparator<TableRow>() {
            @Override
            public int compare(TableRow row1, TableRow row2) {
                DataPrototype data1 = row1.getColumn(columnIndex);
                DataPrototype data2 = row2.getColumn(columnIndex);
                return data1.compareTo(data2);
            }
        };

        if (direction == SortDirection.DESCENDING) {
            comparator = Collections.reverseOrder(comparator);
        }

        Collections.sort(rows, comparator);
    }

    public void printTable() {
        int[] columnWidths = new int[columnNames.size()];
        for (int i = 0; i < columnNames.size(); i++) {
            columnWidths[i] = columnNames.get(i).length();
            for (TableRow row : rows) {
                int cellWidth = row.getColumn(i).toString().length();
                if (cellWidth > columnWidths[i]) {
                    columnWidths[i] = cellWidth;
                }
            }
        }

        printTableBorder(columnWidths);

        for (int i = 0; i < columnNames.size(); i++) {
            System.out.print("| " + padRight(columnNames.get(i), columnWidths[i]) + " ");
        }
        System.out.println("|");
        printTableBorder(columnWidths);

        for (TableRow row : rows) {
            for (int i = 0; i < row.getColumnCount(); i++) {
                System.out.print("| " + padRight(row.getColumn(i).toString(), columnWidths[i]) + " ");
            }
            System.out.println("|");
            printTableBorder(columnWidths);
        }
    }

    private void printTableBorder(int[] columnWidths) {
        for (int width : columnWidths) {
            System.out.print("+");
            for (int i = 0; i < width + 2; i++) {
                System.out.print("-");
            }
        }
        System.out.println("+");
    }

    private String padRight(String s, int n) {
        return String.format("%-" + n + "s", s);
    }

    public void saveToExcel(String filePath) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Table Data");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < columnNames.size(); i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columnNames.get(i));
        }

        for (int rowIndex = 0; rowIndex < rows.size(); rowIndex++) {
            Row row = sheet.createRow(rowIndex + 1);
            TableRow tableRow = rows.get(rowIndex);

            for (int colIndex = 0; colIndex < tableRow.getColumnCount(); colIndex++) {
                Cell cell = row.createCell(colIndex);
                DataPrototype data = tableRow.getColumn(colIndex);

                if (data instanceof IntegerData) {
                    cell.setCellValue(((IntegerData) data).getValue());
                } else if (data instanceof StringData) {
                    cell.setCellValue(((StringData) data).getValue());
                } else if (data instanceof DoubleData) {
                    cell.setCellValue(((DoubleData) data).getValue());
                } else if (data instanceof DateData) {
                    cell.setCellValue(dateFormat.format(((DateData) data).getValue()));
                } else if (data instanceof TimeData) {
                    cell.setCellValue(timeFormat.format(((TimeData) data).getValue()));
                } else if (data instanceof GpsData gpsData) {
                    cell.setCellValue(gpsData.getLatitude() + ", " + gpsData.getLongitude());
                }
            }
        }

        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);
        }

        workbook.close();
    }
    public void readFromExcel(String filePath) throws IOException, ParseException {
        Scanner scanner = new Scanner(System.in);
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

        try (FileInputStream fileIn = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fileIn)) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            if (rowIterator.hasNext()) {
                Row headerRow = rowIterator.next();
                columnNames = new ArrayList<>();
                prototypeRow = new TableRow();
                for (Cell cell : headerRow) {
                    String columnName = cell.getStringCellValue();
                    columnNames.add(columnName);

                    DataPrototype columnPrototype = determineColumnType(scanner, columnName);
                    prototypeRow.addColumn(columnPrototype);
                }
            }
            rows = new ArrayList<>();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                TableRow tableRow = new TableRow();

                for (int i = 0; i < row.getLastCellNum(); i++) {
                    Cell cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    switch (cell.getCellType()) {
                        case STRING:
                            String stringValue = cell.getStringCellValue();
                            if (stringValue.contains(",")) {
                                String[] parts = stringValue.split(",");
                                double latitude = Double.parseDouble(parts[0]);
                                double longitude = Double.parseDouble(parts[1]);
                                tableRow.addColumn(new GpsData(latitude, longitude));
                            } else {
                                tableRow.addColumn(new StringData(stringValue));
                            }
                            break;
                        case NUMERIC:
                            if (DateUtil.isCellDateFormatted(cell)) {
                                tableRow.addColumn(new DateData(cell.getDateCellValue()));
                            } else if (isTimeFormat(cell, timeFormat)) {
                                tableRow.addColumn(new TimeData(timeFormat.format(cell.getDateCellValue())));
                            } else {
                                double numericValue = cell.getNumericCellValue();
                                if (numericValue == (int) numericValue) {
                                    tableRow.addColumn(new IntegerData((int) numericValue));
                                } else {
                                    tableRow.addColumn(new DoubleData(numericValue));
                                }
                            }
                            break;
                    }
                }
                rows.add(tableRow);
            }
        }

    }

    private static DataPrototype determineColumnType(Scanner scanner, String columnName) throws ParseException {
        System.out.println("Выберите тип данных для столбца '" + columnName + "':");
        System.out.println("1. Целое число (Integer)");
        System.out.println("2. Строка (String)");
        System.out.println("3. Дата (Date)");
        System.out.println("4. Время (Time)");
        System.out.println("5. GPS координаты (GPS)");
        System.out.println("6. Вещественное число (Double)");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                return new IntegerData(0);
            case 2:
                return new StringData("");
            case 3:
                return new DateData("01.01.1970");
            case 4:
                return new TimeData("00:00:00");
            case 5:
                return new GpsData(0.0, 0.0);
            case 6:
                return new DoubleData(0.0);
            default:
                System.out.println("Некорректный ввод, используется тип данных String");
                return new StringData("");
        }
    }

    private boolean isTimeFormat(Cell cell, SimpleDateFormat timeFormat) {
        try {
            timeFormat.parse(cell.getStringCellValue());
            return true;
        } catch (ParseException | IllegalStateException e) {
            return false;
        }
    }

    public void addColumn(String columnName, DataPrototype defaultValue) {
        // Добавить новое имя столбца
        columnNames.add(columnName);

        // Добавить значение по умолчанию в каждую существующую строку
        for (TableRow row : rows) {
            row.addColumn(defaultValue.clone()); // Клонируем defaultValue для каждой строки
        }
    }

    public List<String> getColumnNames() {
        return columnNames;
    }

    public Object getPrototypeRow() {
        return prototypeRow;
    }

    public DataPrototype getColumnPrototype(String columnName) {
        int columnIndex = columnNames.indexOf(columnName);
        if (columnIndex == -1) {
            throw new IllegalArgumentException("Столбец с именем " + columnName + " не найден.");
        }
        return prototypeRow.getColumnPrototype(columnIndex);
    }
}