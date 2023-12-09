import java.util.ArrayList;

public class TableRow implements Cloneable {
    private ArrayList<DataPrototype> columns;

    public TableRow() {
        this.columns = new ArrayList<>();
    }

    public void addColumn(DataPrototype data) {
        this.columns.add(data.clone());
    }

    public DataPrototype getColumn(int index) {
        return this.columns.get(index);
    }

    @Override
    public TableRow clone() {
        TableRow clonedRow = new TableRow();
        for (DataPrototype data : this.columns) {
            clonedRow.addColumn(data.clone());
        }
        return clonedRow;
    }

    public int getColumnCount() {
        return this.columns.size();
    }

    public DataPrototype getColumnPrototype(int columnIndex) {
        if (columnIndex >= 0 && columnIndex < columns.size()) {
            return columns.get(columnIndex);
        } else {
            throw new IndexOutOfBoundsException("Столбец с индексом " + columnIndex + " не существует.");
        }
    }
}