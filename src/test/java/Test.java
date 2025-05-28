import software.leonov.common.quantities.BinaryByteUnit;

public class Test {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
System.out.println(BinaryByteUnit.format(1024, BinaryByteUnit.YOTTABYTES));
System.out.println(BinaryByteUnit.BYTES.convert(1.3, BinaryByteUnit.BITS));
System.out.println(BinaryByteUnit.format(BinaryByteUnit.BYTES.convert(1.3, BinaryByteUnit.BITS), BinaryByteUnit.BITS));
    }

}
