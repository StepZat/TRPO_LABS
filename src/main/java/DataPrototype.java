public abstract class DataPrototype implements Cloneable, Comparable<DataPrototype> {
    public abstract DataPrototype clone();
    public abstract String toString();
    public abstract void parseFromString(String data);

    public abstract int compareTo(DataPrototype data2);
}


