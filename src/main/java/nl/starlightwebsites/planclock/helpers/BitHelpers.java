package nl.starlightwebsites.planclock.helpers;
/*
#define bitRead(value, bit) (((value) >> (bit)) & 0x01)
#define bitSet(value, bit) ((value) |= (1UL << (bit)))
#define bitClear(value, bit) ((value) &= ~(1UL << (bit)))
 */
public class BitHelpers {
    public static boolean bitRead(int value, int bit){
        return (((value) >> (bit)) & 0x01) == 1;
    }
    public static int bitSet(int value, int bit){
        return ((value) |= (1 << (bit)));
    }
    public static int bitClear(int value, int bit){
        return ((value) &= ~(1 << (bit)));
    }

    /**
     * Sets a bit in a value to bitvalue
     * @param value value in where the bit needs to be set
     * @param bit bit that needs to be set. IT IS SHIFTED TO THE LEFT
     * @param bitvalue value that it needs to be.
     * @return the param value value
     */
    public static int setBit(int value, int bit, boolean bitvalue){
        if(bitvalue){
            return bitSet(value, bit);
        } else{
            return bitClear(value,bit);
        }
    }
}
