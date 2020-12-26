package nl.starlightwebsites.planclock.database;

import org.apache.commons.codec.digest.DigestUtils;
import org.conscrypt.OpenSSLMessageDigestJDK;
import org.tinylog.Logger;

import java.nio.file.Path;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBAudio {
    public static void addAudioFile(byte[] audio, String text){
        String insertSQL = "insert into audio_cache (hash, audioText, audio) VALUES (?,?,?)";
        try{
            if(!audioFileExists(text))
            {
                String md5 = DigestUtils.md5Hex(text).toUpperCase();
                PreparedStatement ipst = DBConnector.connection.prepareStatement(insertSQL);
                ipst.setString(1, md5);
                ipst.setString(2, text);
                ipst.setBytes(3, audio);
                var iresult = ipst.executeUpdate();
                if(iresult != 1) {
                    Logger.error("Result from insert query is not what we expected");
                }
            }
        } catch (SQLException e) {
            Logger.error(e,"SQL Failure");
        }
    }

    public static boolean audioFileExists(String text){
        try{
            String existsSQL = "select * from audio_cache WHERE hash=?;";
            String md5 = DigestUtils.md5Hex(text).toUpperCase();
            PreparedStatement pst = DBConnector.connection.prepareStatement(existsSQL);
            pst.setString(1, md5);
            var result = pst.executeQuery();
            int rowCount = result.last() ? result.getRow() : 0;
            return rowCount != 0;
        } catch (SQLException e) {
            Logger.error(e,"SQL Failure");
            return false;
        }
    }

    public static byte[] getAudioSample(String text){
        if(!audioFileExists(text)) return null;
        try{
            String existsSQL = "SELECT * FROM audio_cache WHERE hash=?;";
            String md5 = DigestUtils.md5Hex(text).toUpperCase();
            PreparedStatement pst = DBConnector.connection.prepareStatement(existsSQL);
            pst.setString(1, md5);
            var result = pst.executeQuery();
            if(result.next()) {
                return result.getBytes("audio");
            } else{
                return null;
            }
        } catch (SQLException e) {
            Logger.error(e,"SQL Failure");
            return null;
        }
    }
}
