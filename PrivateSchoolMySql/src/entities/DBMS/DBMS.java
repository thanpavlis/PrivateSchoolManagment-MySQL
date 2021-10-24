package entities.DBMS;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/*Singleton*/
 /*o constructor einai private wste na mhn mporei kapoios apexw na ftiaxei antikeimno thn klashs auths*/
public class DBMS {

    private static DBMS dbms = null;//mporei na yparxei mono ena antikeimeno typou DBMS, alliws epistrefetai panta to reference se auto
    private final String urlDB = "jdbc:mysql://localhost:3306/school";//stoixeia gia th syndesh sth bash
    private final String usernameDB = "root";
    private final String passwordDB = "V@182119931993";

    private DBMS() {

    }

    /*thn prwth fora dhmiourgei antikeimeno, tis ipoloipes epistrefei to reference se auto*/
    public static DBMS getDBMS() {
        if (dbms == null) {
            dbms = new DBMS();
        }
        return dbms;
    }

    /*epistrefei ena Connection pros th bash*/
    public Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(urlDB, usernameDB, passwordDB);
        } catch (SQLException ex) {
            Logger.getLogger(DBMS.class.getName()).log(Level.SEVERE, null, ex);
        }
        return connection;
    }

    /*kleinei to Connection pros th bash meta apo kathe query*/
    public void closeConnection(ResultSet rs, Statement stmt, Connection conn) {
        try {//h seira pou kanw close() einai anapodh apo thn dhmiourgia
            if (rs != null) {//rs close()
                rs.close();
            }
            if (stmt != null) {//stmt close()
                stmt.close();
            }
            if (conn != null) {// conn close()
                conn.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBMS.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
