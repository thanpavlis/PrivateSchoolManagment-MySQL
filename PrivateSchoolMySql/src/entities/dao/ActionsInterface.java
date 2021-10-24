package entities.dao;

import entities.DBMS.DBMS;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public interface ActionsInterface<T> {

    public ArrayList<T> selectAll();//kanei select ola ta data

    public T findById(int id);//select gia sygkekrimeno id

    public boolean insert(T t);//kanei insert pernwntas ta atributes san antikeimeno

    public boolean deleteById(int id);//diagrafh sigkekrimenou id

    public boolean deleteAllData();//diagrafh olwn twn data se periptwsh pou o xrhsths thelei na eisagei dedomena apo to pliktrologio

    public static int sizeOf(String label) {//epistrefei to size, ilopoihsa to swma sto interface kathw o kwdikas einai koinos
        Connection conn = DBMS.getDBMS().getConnection();
        Statement stmt = null;
        ResultSet rs = null;
        int size;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("call arraySize('" + label + "');");
            rs.next();
            size = rs.getInt("size");
        } catch (SQLException ex) {
            size = 0;
        } finally {
            DBMS.getDBMS().closeConnection(rs, stmt, conn);
        }
        return size;
    }

    /*epistrefei enan pinaka me to max(length) gia kathe attribute ths kyrias ontothtas pou thelw*/
    public static int[] maxWidthsOf(String label) {
        Connection conn = DBMS.getDBMS().getConnection();
        Statement stmt = null;
        ResultSet rs = null;
        int widths[] = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("call maxWidthsOf('" + label + "');");
            rs.next();
            switch (label) {
                case "students":
                    widths = new int[]{(rs.getInt("id") < 2 ? 2 : rs.getInt("id")), (rs.getInt("firstName") < 9 ? 9 : rs.getInt("firstName")), (rs.getInt("lastName") < 8 ? 8 : rs.getInt("lastName")), 11, (rs.getInt("tuitionFees") < 11 ? 11 : rs.getInt("tuitionFees"))};
                    break;
                case "trainers":
                    widths = new int[]{(rs.getInt("id") < 2 ? 2 : rs.getInt("id")), (rs.getInt("firstName") < 9 ? 9 : rs.getInt("firstName")), (rs.getInt("lastName") < 8 ? 8 : rs.getInt("lastName")), (rs.getInt("tsubject") < 7 ? 7 : rs.getInt("tsubject"))};
                    break;
                case "assignments":
                    widths = new int[]{(rs.getInt("id") < 2 ? 2 : rs.getInt("id")), (rs.getInt("title") < 5 ? 5 : rs.getInt("title")), (rs.getInt("adescription") < 12 ? 12 : rs.getInt("adescription")), 19, (rs.getInt("oralMark") < 8 ? 8 : rs.getInt("oralMark")), (rs.getInt("totalMark") < 9 ? 9 : rs.getInt("totalMark"))};
                    break;
                default://courses
                    widths = new int[]{(rs.getInt("id") < 2 ? 2 : rs.getInt("id")), (rs.getInt("title") < 5 ? 5 : rs.getInt("title")), (rs.getInt("cstream") < 6 ? 6 : rs.getInt("cstream")), (rs.getInt("ctype") < 4 ? 4 : rs.getInt("ctype")), 10, 10};
            }
        } catch (SQLException ex) {
            Logger.getLogger(ActionsInterface.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DBMS.getDBMS().closeConnection(rs, stmt, conn);
        }
        return widths;
    }
}
