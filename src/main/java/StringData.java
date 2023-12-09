public class StringData extends DataPrototype {
    private String value;

    public StringData() {
        this.value = "";
    }

    public StringData(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public DataPrototype clone() {
        return new StringData(this.value);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public void parseFromString(String data) {
        this.value = data;
    }

    @Override
    public int compareTo(DataPrototype other) {
        if (!(other instanceof StringData otherString)) {
            throw new IllegalArgumentException("Can only compare with StringData");
        }
        return this.value.compareTo(otherString.value);
    }


    public StringData add(StringData other) {
        return new StringData(this.value + other.value);
    }


}
