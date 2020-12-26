package nl.starlightwebsites.planclock.database;

import org.tinylog.Logger;

import java.sql.*;

public class DBConnector {

    static Connection connection;
    public static void connectToDatabase() throws SQLException {
        connection = DriverManager.getConnection("jdbc:mariadb://192.168.7.2:3306/planclock?user=admin&password=password");
    }

    public static void dropDataBase(){
        Logger.error("DATABASE FOR PLANCLOCK IS BEING CLEARED");
        try {
            connection.createStatement().execute("DELETE FROM reminder_time_planning");
            connection.createStatement().execute("DELETE FROM time_planning");
            connection.createStatement().execute("DELETE FROM motivations");
            connection.createStatement().execute("DELETE FROM reminders");
        }catch(SQLException ex){
            Logger.error(ex);
        }
    }

    public static PreparedStatement prepareStatement(String sql) throws SQLException {
        return connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
    }

    public static int getInsertedKey(PreparedStatement pst) throws SQLException {
        ResultSet generatedKeys = pst.getGeneratedKeys();
        if(generatedKeys.next()){
            return generatedKeys.getInt(1);
        } else{
            Logger.error(new Throwable(), "The insert did not go as planned");
        }
        return -1;
    }
}
