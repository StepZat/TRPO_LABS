public class DoubleData extends DataPrototype {
    private double value;

    public DoubleData() {
        this.value = 0.0;
    }

    public DoubleData(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public DataPrototype clone() {
        return new DoubleData(this.value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public void parseFromString(String data) {
        try {
            this.value = Double.parseDouble(data);
        } catch (NumberFormatException e) {
            this.value = 0.0;
        }
    }

    @Override
    public int compareTo(DataPrototype other) {
        if (!(other instanceof DoubleData)) {
            throw new IllegalArgumentException("Can only compare with DoubleData");
        }
        DoubleData otherDouble = (DoubleData) other;
        return Double.compare(this.value, otherDouble.value);
    }

    public DoubleData add(DoubleData other) {
        return new DoubleData(this.value + other.value);
    }

}