public class IntegerData extends DataPrototype {
    private int value;

    public IntegerData() {
        this.value = 0;
    }

    public IntegerData(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public DataPrototype clone() {
        return new IntegerData(this.value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public void parseFromString(String data) {
        try {
            this.value = Integer.parseInt(data);
        } catch (NumberFormatException e) {
            this.value = 0;
        }
    }

    @Override
    public int compareTo(DataPrototype other) {
        if (!(other instanceof IntegerData otherInteger)) {
            throw new IllegalArgumentException("Can only compare with same type");
        }
        return Integer.compare(this.value, otherInteger.value);
    }

    public IntegerData add(IntegerData other) {
        return new IntegerData(this.value + other.value);
    }
}