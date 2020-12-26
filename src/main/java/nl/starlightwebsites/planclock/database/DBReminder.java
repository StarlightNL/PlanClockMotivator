package nl.starlightwebsites.planclock.database;

import nl.starlightwebsites.planclock.models.Reminder;
import org.tinylog.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DBReminder {
    public static List<Reminder> getAllReminders(){

        List<Reminder> reminders = new ArrayList<>();
        String getSql = "SELECT *\n" +
                "FROM reminders\n";
        try {
            ResultSet result =DBConnector.connection.createStatement().executeQuery(getSql);
            while(result.next()){
                reminders.add(createReminderFromResult(result));
            }
        } catch (SQLException e) {
            Logger.error(e,"SQL Failure");
        }
        return reminders;
    }

    public static Reminder getReminderById(int remindersId){
        String getSql = "SELECT *\n" +
                "FROM reminders\n" +
                "WHERE id = ?";
        try {
            PreparedStatement pst = DBConnector.connection.prepareStatement(getSql);
            pst.setInt(1, remindersId);
            ResultSet result = pst.executeQuery();
            if(result.next()) {
                return createReminderFromResult(result);
            }
        } catch (SQLException e) {
            Logger.error(e,"SQL Failure");
        }
        return null;
    }

    public static Reminder getReminderByTitle(String remindersTitle){
        String getSql = "SELECT *\n" +
                "FROM reminders\n" +
                "WHERE title = ?";
        try {
            PreparedStatement pst = DBConnector.connection.prepareStatement(getSql);
            pst.setString(1, remindersTitle);
            ResultSet result = pst.executeQuery();
            if(result.next()) {
                return createReminderFromResult(result);
            } else {
                return null; // Reminder does not exist yet
            }
        } catch (SQLException e) {
            Logger.error(e,"SQL Failure");
            return null; // Uhm...
        }
    }

    public static Reminder addReminder(String title){
        String insertSQL = "insert into reminders (title) VALUES (?)";
        try {
            PreparedStatement ipst = DBConnector.prepareStatement(insertSQL);
            ipst.setString(1, title);
            ResultSet result = ipst.executeQuery();
            return getReminderById(DBConnector.getInsertedKey(ipst));
        } catch (SQLException e) {
            Logger.error(e,"SQL Failure");
        }
        return null;
    }


    public static void deleteReminderById(int id) {
        String insertSQL = "DELETE FROM reminders WHERE id = ?";
        try {
            PreparedStatement ipst = DBConnector.prepareStatement(insertSQL);
            ipst.setInt(1, id);
            ResultSet result = ipst.executeQuery();
        } catch (SQLException e) {
            Logger.error(e,"SQL Failure");
        }
    }

    public static Reminder updateReminder(int id, Reminder reminder) {
//        String  updateSQL = "UPDATE reminders\n" +
//                "SET text = ?,\n" +
//                "reminder = ?\n" +
//                "WHERE id = ?";
//        try {
//            PreparedStatement pst = DBConnector.prepareStatement(updateSQL);
//            pst.setString(1, motivation.getReminderText());
//            pst.setInt(2, motivation.getReminderId());
//            pst.setInt(3, id);
//            pst.executeQuery();
//        } catch (SQLException e) {
//            Logger.error(e,"SQL Failure");
//        }
        return getReminderById(id);
    }

    private static Reminder createReminderFromResult(ResultSet result) throws SQLException {
        Reminder reminder = new Reminder();
        reminder.setDbid(result.getInt("reminders.id"));
        reminder.setTitle(result.getString("reminders.title"));
        reminder.addTimePlannings(DBTimePlanning.getAllTimePlanningsByReminderId(reminder.getDbid()));
        reminder.addMotivations(DBMotivation.getMotivationsByReminderId(reminder.getDbid()));
        reminder.setGoogleReminder(result.getBoolean("reminders.google_reminder"));
        return reminder;
    }
}
