import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DatabaseUtils {
    public static String DB_URL = "jdbc:mysql://localhost:3306/game";
    public static String USER_NAME = "root";
    public static String PASSWORD = "";

    public static Connection getConnection(String dbURL, String userName, String password) {
        Connection con = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(dbURL, userName, password);
            System.out.println("connect successfully!");
        } catch (Exception e) {
            System.out.println("connect failure!");
            e.printStackTrace();
        }
        return con;
    }

    public boolean authenticate(String username, String password){
        int countRows = 0;
        boolean isLoggedIn = false;
        try {
            // connect to database 'game'
            Connection con = DatabaseUtils.getConnection(DatabaseUtils.DB_URL, DatabaseUtils.USER_NAME, DatabaseUtils.PASSWORD);
            // crate statement
            Statement st = con.createStatement();
            // get data from table 'scoreboard'
            ResultSet rs = st.executeQuery("SELECT * FROM scoreboard WHERE username ='" + username + "' AND password ='" + password + "'");
            // search for account
            while (rs.next()) {
                countRows++;
            }
            if(countRows > 0) isLoggedIn = true;
            // close connection
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isLoggedIn;
    }

    public int getUserHighScore(String username){
        int highScore = 0;
        try {
            // connect to database 'game'
            Connection con = DatabaseUtils.getConnection(DatabaseUtils.DB_URL, DatabaseUtils.USER_NAME, DatabaseUtils.PASSWORD);
            // crate statement
            Statement st = con.createStatement();
            // get data from table 'scoreboard'
            ResultSet rs = st.executeQuery("SELECT highscore FROM scoreboard WHERE username ='" + username + "'");

            rs.next();
            highScore = rs.getInt("highscore");
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return highScore;
    }

    public String[][] getUserHistory(String username){
        String[][] data = null;
        try {
            // connect to database 'game'
            Connection con = DatabaseUtils.getConnection(DatabaseUtils.DB_URL, DatabaseUtils.USER_NAME, DatabaseUtils.PASSWORD);
            // crate statement
            Statement st = con.createStatement();
            // get data from table 'scoreboard'
            ResultSet rs = st.executeQuery("SELECT * FROM history WHERE username ='" + username + "'");

            int countRows = 0;
            while (rs.next()){
                countRows++;
            }

            rs = st.executeQuery("SELECT * FROM history WHERE username ='" + username + "'");

            data = new String[countRows][2];
            int index = 0;
            while (rs.next()) {
                data[index][0] = rs.getString(3);
                data[index][1] = String.valueOf(rs.getInt(2));
                index++;
            }
            // close connection
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public void insertScore(String username, int score){
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String time = dateFormat.format(date);

        try{
            Connection con = DatabaseUtils.getConnection(DatabaseUtils.DB_URL, DatabaseUtils.USER_NAME, DatabaseUtils.PASSWORD);
            Statement statement = con.createStatement();
            String sql = "INSERT INTO history(username, score, date) VALUES('" +username+"','"+score+"','" +time+"')";
            statement.executeUpdate(sql);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void updateHighScore(String username, int highScore) {
        try{
            Connection con = DatabaseUtils.getConnection(DatabaseUtils.DB_URL, DatabaseUtils.USER_NAME, DatabaseUtils.PASSWORD);
            Statement statement = con.createStatement();
            String sql = "UPDATE scoreboard SET highscore = '" + highScore + "'WHERE username = '" + username + "'";
            statement.executeUpdate(sql);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
