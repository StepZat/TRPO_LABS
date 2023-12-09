import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class TimeData extends DataPrototype {
    private LocalTime value;

    private DateTimeFormatter formatter;

    public TimeData() {
        this.value = LocalTime.now();
        this.formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    }

    public TimeData(String timeStr) {
        this.formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        this.value = LocalTime.parse(timeStr, formatter);
    }

    public LocalTime getValue() {
        return value;
    }

    public void setValue(LocalTime value) {
        this.value = value;
    }

    @Override
    public TimeData clone() {
        TimeData clone = new TimeData();
        clone.setValue(this.value);
        return clone;
    }

    @Override
    public String toString() {
        return value.format(formatter);
    }

    @Override
    public void parseFromString(String data) {
        try {
            this.value = LocalTime.parse(data, formatter);
        } catch (DateTimeParseException e) {
            return;
        }
    }

    public TimeData add(TimeData other) {
        long totalSeconds = this.toSeconds() + other.toSeconds();
        long secondsInDay = 24 * 60 * 60;
        long resultSeconds = totalSeconds % secondsInDay;

        LocalTime resultTime = LocalTime.ofSecondOfDay(resultSeconds);
        return new TimeData(resultTime.format(formatter));
    }

    private long toSeconds() {
        return this.value.toSecondOfDay();
    }

    @Override
    public int compareTo(DataPrototype other) {
        if (!(other instanceof TimeData otherTime)) {
            throw new IllegalArgumentException("Can only compare with TimeData");
        }
        return this.value.compareTo(otherTime.value);
    }
}