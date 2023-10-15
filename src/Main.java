import java.util.List;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        CustomSet cset = new CustomSet(5, 6, 7, 7, 9);
        System.out.println(cset);
        cset.add(20);
        System.out.println(cset);
        cset.del(7);
        System.out.println(cset);

        CustomSet other = new CustomSet(7, 9, 10, 20);
        CustomSet intersectionSet = cset.intersection(other);
        System.out.println(intersectionSet);
        CustomSet unionSet = cset.union(other);
        System.out.println(unionSet);
        CustomSet diffSet = cset.diff(other);
        System.out.println(diffSet);
        CustomSet cset2 = new CustomSet();
        cset2.readFromTextFile("file.txt");
        cset2.add(123);
        cset2.saveToBinaryFile("file2.bin");
        CustomSet cset3 = new CustomSet();
        cset3.readFromBinaryFile("file2.bin");
        System.out.println(cset3);

    }
}