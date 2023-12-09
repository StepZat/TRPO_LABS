import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateData extends DataPrototype {
    private Date value;
    private SimpleDateFormat formatter;

    public DateData() {
        this.value = new Date();
        this.formatter = new SimpleDateFormat("dd.MM.yyyy");
    }

    public DateData(Date value) {
        this.value = value;
    }

    public DateData(String dateStr) throws ParseException {
        this.formatter = new SimpleDateFormat("dd.MM.yyyy");
        this.value = formatter.parse(dateStr);
    }

    public Date getValue() {
        return value;
    }

    public void setValue(Date value) {
        this.value = value;
    }

    public void setValue(String dateStr) throws ParseException {
        this.value = formatter.parse(dateStr);
    }


    @Override
    public DataPrototype clone() {
        try {
            DateData clone = new DateData();
            clone.setValue((Date) this.value.clone());
            return clone;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String toString() {
        return formatter.format(value);
    }

    @Override
    public void parseFromString(String data) {
        try {
            this.value = formatter.parse(data);
        } catch (ParseException e) {
            return;
        }
    }

    @Override
    public int compareTo(DataPrototype other) {
        if (!(other instanceof DateData otherDate)) {
            throw new IllegalArgumentException("Can only compare with DateData");
        }
        return this.value.compareTo(otherDate.value);
    }

    public DateData add(DateData other) {
        return (DateData) (this.value.compareTo(other.value) >= 0 ? this.clone() : other.clone());
    }

}