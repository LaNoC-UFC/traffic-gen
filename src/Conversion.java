public class Conversion {
    public static String zeroLeftPad(String str, int length){
        String paddedString = str;
        while(paddedString.length() < length) {
            paddedString = "0" + paddedString;
        }
        paddedString = paddedString.substring(paddedString.length() - length, paddedString.length());
        return paddedString;
    }

    public static String formatAddress(int addX, int addY, int flitWidth) {
        String targetXBin = zeroLeftPad(Integer.toBinaryString(addX), (flitWidth / 2));
        String targetYBin = zeroLeftPad(Integer.toBinaryString(addY), (flitWidth / 2));
        int target = Integer.parseInt((targetXBin + targetYBin),2);
        return zeroLeftPad(Integer.toHexString(target), (flitWidth / 4)).toUpperCase();
    }
}