package nl.starlightwebsites.planclock.database;


import nl.starlightwebsites.planclock.models.Reminder;
import nl.starlightwebsites.planclock.models.database.Motivation;
import org.tinylog.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class DBMotivation {
    public static List<Motivation> getAllMotivations(){

        List<Motivation> motivations = new ArrayList<>();
        String getSql = "SELECT *\n" +
                "FROM motivations\n" +
                "INNER JOIN reminders\n" +
                "ON motivations.reminder = reminders.id";
        try {
            ResultSet result =DBConnector.connection.createStatement().executeQuery(getSql);
            while(result.next()){
                motivations.add(createMotivationFromResult(result));
            }
        } catch (SQLException e) {
            Logger.error(e,"SQL Failure");
        }
        return motivations;
    }

    public static List<Motivation> getMotivationsByReminderId(int reminderDBId){
        List<Motivation> motivations = new ArrayList<>();
        String getSql = "SELECT *\n" +
                "FROM motivations\n" +
                "INNER JOIN reminders\n" +
                "ON motivations.reminder = reminders.id\n" +
                "WHERE reminders.id = ?";
        try {
            PreparedStatement pst = DBConnector.connection.prepareStatement(getSql);
            pst.setInt(1, reminderDBId);
            ResultSet result = pst.executeQuery();
            while(result.next()){
                motivations.add(createMotivationFromResult(result));
            }
        } catch (SQLException e) {
            Logger.error(e,"SQL Failure");
        }
        return motivations;
    }

    public static Motivation getMotivationById(int motivationId){
        String getSql = "SELECT *\n" +
                "FROM motivations\n" +
                "INNER JOIN reminders\n" +
                "ON motivations.reminder = reminders.id\n" +
                "WHERE motivations.id = ?";
        try {
            PreparedStatement pst = DBConnector.connection.prepareStatement(getSql);
            pst.setInt(1, motivationId);
            ResultSet result = pst.executeQuery();
            result.next();
            return createMotivationFromResult(result);
        } catch (SQLException e) {
            Logger.error(e,"SQL Failure");
        }
        return null;
    }

    public static Motivation addMotivation(int reminderId, String motivationText){
        String insertSQL = "insert into motivations (reminder, text) VALUES (?,?)";
        try {
            PreparedStatement ipst = DBConnector.prepareStatement(insertSQL);
            ipst.setInt(1, reminderId);
            ipst.setString(2, motivationText);
            ResultSet result = ipst.executeQuery();
            return getMotivationById(DBConnector.getInsertedKey(ipst));
        } catch (SQLException e) {
            Logger.error(e,"SQL Failure");
        }
        return null;
    }


    public static void deleteMotivationById(int id) {
        String insertSQL = "DELETE FROM motivations WHERE id = ?";
        try {
            PreparedStatement ipst = DBConnector.prepareStatement(insertSQL);
            ipst.setInt(1, id);
            ResultSet result = ipst.executeQuery();
        } catch (SQLException e) {
            Logger.error(e,"SQL Failure");
        }
    }

    public static Motivation updateMotivation(int id, Motivation motivation) {
        String  updateSQL = "UPDATE motivations\n" +
                "SET text = ?,\n" +
                "reminder = ?\n" +
                "WHERE id = ?";
        try {
            PreparedStatement pst = DBConnector.prepareStatement(updateSQL);
            pst.setString(1, motivation.getMotivationText());
            pst.setInt(2, motivation.getReminderId());
            pst.setInt(3, id);
            pst.executeQuery();
        } catch (SQLException e) {
            Logger.error(e,"SQL Failure");
        }
        return getMotivationById(id);
    }

    private static Motivation createMotivationFromResult(ResultSet result) throws SQLException {
        return new Motivation(result.getInt("motivations.id"),result.getInt("motivations.reminder"), result.getString("motivations.text"), result.getTimestamp("motivations.createdate").toInstant(),result.getTimestamp("motivations.updatedate").toInstant(), result.getString("reminders.title"));
    }
}
