package nl.starlightwebsites.planclock.database;

import nl.starlightwebsites.planclock.models.Reminder;
import nl.starlightwebsites.planclock.models.TimePlanning;
import org.tinylog.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DBTimePlanning {
    public static List getAllTimePlannings(){
        List<TimePlanning> timePlannings = new ArrayList<>();
        String getSql = "SELECT *\n" +
                "FROM time_planning\n" +
                "LEFT JOIN reminder_time_planning\n" +
                "ON reminder_time_planning.time_planning_id = time_planning.id";
        try {
            ResultSet result =DBConnector.connection.createStatement().executeQuery(getSql);
            while(result.next()){
                timePlannings.add(createTimePlanningFromResult(result));
            }
        } catch (SQLException e) {
            Logger.error(e,"SQL Failure");
        }
        return timePlannings;
    }

    public static List<TimePlanning> getAllTimePlanningsByReminderId(int reminderId){
        List<TimePlanning> timePlannings = new ArrayList<>();
        String getSql = "SELECT *\n" +
                "FROM reminder_time_planning\n" +
                "RIGHT JOIN reminders \n" +
                "ON reminders.id = reminder_time_planning.reminder_id\n" +
                "RIGHT JOIN time_planning\n" +
                "ON time_planning.id = reminder_time_planning.time_planning_id\n" +
                "WHERE reminder_time_planning.reminder_id = ?";
        try {
            PreparedStatement pst = DBConnector.prepareStatement(getSql);
            pst.setInt(1, reminderId);
            var result = pst.executeQuery();
            while(result.next()){
                timePlannings.add(createTimePlanningFromResult(result));
            }
        } catch (SQLException e) {
            Logger.error(e,"SQL Failure");
        }
        return timePlannings;
    }

    public static TimePlanning getTimePlanningById(int planningId){
        String getSql = "SELECT *\n" +
                "FROM time_planning\n" +
                "WHERE id = ?";
        try {
            PreparedStatement pst = DBConnector.connection.prepareStatement(getSql);
            pst.setInt(1, planningId);
            ResultSet result = pst.executeQuery();
            result.next();
            return createTimePlanningFromResult(result);
        } catch (SQLException e) {
            Logger.error(e,"SQL Failure");
        }
        return null;
    }

    public static TimePlanning addTimePlanning(TimePlanning tp){
        String insertSQL = "insert into time_planning (days, time, weeksbetween) VALUES (?,?,?)";
        try {
            PreparedStatement ipst = DBConnector.prepareStatement(insertSQL);
            ipst.setInt(1, tp.getDayBitmask());
            ipst.setTime(2, tp.getPlannedTime());
            ipst.setInt(3,tp.getWeeksBetween());
            ResultSet result = ipst.executeQuery();
            return getTimePlanningById(DBConnector.getInsertedKey(ipst));
        } catch (SQLException e) {
            Logger.error(e,"SQL Failure");
        }
        return null;
    }

    public static void bindPlanningToReminder(TimePlanning tp, Reminder reminder) {
        String insertSQL = "insert into reminder_time_planning (reminder_id, time_planning_id) VALUES (?,?)";
        try {
            PreparedStatement ipst = DBConnector.prepareStatement(insertSQL);
            ipst.setInt(1, reminder.getDbid());
            ipst.setInt(2, tp.getId());
            ResultSet result = ipst.executeQuery();
        } catch (SQLException e) {
            Logger.error(e,"SQL Failure");
        }
    }

    private static TimePlanning createTimePlanningFromResult(ResultSet result) throws SQLException {
        TimePlanning timePlanning = new TimePlanning();
        timePlanning.setDayBitmask(result.getInt("days"));
        timePlanning.setId(result.getInt("id"));
        timePlanning.setPlannedTime(result.getTime("time"));
        timePlanning.setWeeksBetween(result.getInt("weeksbetween"));
        return timePlanning;
    }
}
