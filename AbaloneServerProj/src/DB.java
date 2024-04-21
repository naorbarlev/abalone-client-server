

import java.io.File;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * Document : DB - מחלקת שירות שקבלת חיבור למסד נתונים
 * Date     : 21/11/2018 
 * Author   : Naor Bar-Lev
 */
public class DB
{
    
    /**
     * הפעולה מחזירה חיבור למסד נתנונים רצוי
     * @param dbPath הנתיב המלא למסד הנתונים עבורו רוצים חיבור
     * @return nullמחזיר חיבור אם קיים מסד נתונים ואין בעיות אחרת מחזיר 
     */
    public static Connection getConnection(String dbPath) 
    {
        // for netbeans
       String dbFilePath = DB.class.getResource(dbPath).getPath().replaceAll("%20", " ").replace("build/classes","src");
       
        // for stand alone (jar)
        //String dbFilePath = new File("mydb.accdb").getPath();
        
        
        String dbURL = "jdbc:ucanaccess://" + dbFilePath;
        String dbUserName = "";
        String dbPassword = "";

        Connection con = null;
        try
        {
            con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
        } 
        catch(SQLException e) {Server.printStackTrace(e);}
        
        return con;
    }
    public static boolean isUserInDB(String un,String pw)
    {
        Connection con;
        Statement st;
        ResultSet rs;
        try
        {
            con = getConnection("/assets/mydb.accdb");
            st = con.createStatement();
            String sqlQuery = "select * from users where un='" + un + "' AND pw='" + pw + "'";
            rs = st.executeQuery(sqlQuery);
             if(rs.next())
                return true;
            return false;
        }
        catch(SQLException e) {Server.printStackTrace(e);}

        return false;
    }
    
    /**
     * הפונקציה מעדכנת לשחקן שניצח את מספר הנצחונות שלו
     * @param un 
     */
    public static void updateNumOfWins(String un) 
    {
        Statement stmt = null;
        Connection con;
        try
        {
            con = getConnection("/assets/mydb.accdb");
            stmt = con.createStatement();
            String sqlQuery = "UPDATE users SET wins= wins+1 WHERE un='" + un + "'";
            stmt.executeUpdate(sqlQuery);
        } 
        catch (SQLException ex)
        {
            Server.printStackTrace(ex);
        }
    }
}
        