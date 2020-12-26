package nl.starlightwebsites.planclock.helpers;

import org.tinylog.Logger;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class StringHelpers {

    public static String toHex(long hex) {
        return String.format("%#04X",hex);
    }

    public static String toHex(long hex, int hexLength) {
        hexLength+=2; // 0X is also included    (WHY)
        return String.format("%#0"+hexLength+"X",hex);
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder strBuilder = new StringBuilder();
        for(byte b : bytes){
            strBuilder.append(toHex(b & 0xFF));
            strBuilder.append(" ");
        }
        return strBuilder.toString();
    }

    public static String numberToBits(long number){
        StringBuilder sb = new StringBuilder();
        for(int i = Long.SIZE; i > 0; i--){
            sb.append(number >> i & 1);
        }
        return sb.toString();
    }

    // Method to encode a string value using `UTF-8` encoding scheme
    public static String urlEncodeString(String str) {
        try {
            return URLEncoder.encode(str, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException ex) {
            return str;
        }
    }
}
