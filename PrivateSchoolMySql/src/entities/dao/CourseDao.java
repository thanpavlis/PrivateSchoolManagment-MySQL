package entities.dao;

import entities.DBMS.DBMS;
import entities.Course;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class CourseDao implements ActionsInterface<Course> {

    @Override
    public ArrayList<Course> selectAll() {
        ArrayList<Course> courses = new ArrayList<Course>();
        Connection conn = DBMS.getDBMS().getConnection();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("call allSelect('courses');");
            while (rs.next()) {
                courses.add(new Course(rs.getInt("id"), rs.getString("title"), rs.getString("cstream"), rs.getString("ctype"), LocalDate.parse(rs.getString("startDate"), DateTimeFormatter.ofPattern("dd/MM/yyyy")), LocalDate.parse(rs.getString("endDate"), DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
            }
        } catch (SQLException ex) {
            courses = null;
        } finally {
            DBMS.getDBMS().closeConnection(rs, stmt, conn);
        }
        return courses;
    }

    @Override
    public Course findById(int id) {
        Connection conn = DBMS.getDBMS().getConnection();
        Statement stmt = null;
        ResultSet rs = null;
        Course crs;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("call findById('courses'," + id + ");");
            rs.next();
            crs = new Course(rs.getInt("id"), rs.getString("title"), rs.getString("cstream"), rs.getString("ctype"), LocalDate.parse(rs.getString("startDate"), DateTimeFormatter.ofPattern("dd/MM/yyyy")), LocalDate.parse(rs.getString("endDate"), DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        } catch (SQLException ex) {
            crs = null;
        } finally {
            DBMS.getDBMS().closeConnection(rs, stmt, conn);
        }
        return crs;
    }

    @Override
    public boolean insert(Course t) {
        Connection conn = DBMS.getDBMS().getConnection();
        Statement stmt = null;
        boolean result;
        try {
            stmt = conn.createStatement();
            result = (stmt.executeUpdate("call insertToCourses('" + t.getTitle() + "','" + t.getStream() + "','" + t.getType() + "','" + t.getStartDate() + "','" + t.getEndDate() + "');") == 1);
        } catch (SQLException ex) {
            result = false;
        } finally {
            DBMS.getDBMS().closeConnection(null, stmt, conn);
        }
        return result;
    }

    @Override
    public boolean deleteById(int id) {
        Connection conn = DBMS.getDBMS().getConnection();
        Statement stmt = null;
        boolean result;
        try {
            stmt = conn.createStatement();
            result = (stmt.executeUpdate("call deleteById('courses'," + id + ");") == 1);
        } catch (SQLException ex) {
            result = false;
        } finally {
            DBMS.getDBMS().closeConnection(null, stmt, conn);
        }
        return result;
    }

    @Override
    public boolean deleteAllData() {
        Connection conn = DBMS.getDBMS().getConnection();
        Statement stmt = null;
        boolean result;
        try {
            stmt = conn.createStatement();
            result = (stmt.executeUpdate("call deleteAll('courses');") >= 0);
            stmt.execute("alter table courses auto_increment=1;");//gia na xekinaei to id panta apo 1 se periptwsh pou eixe hdh data o pinakas 
        } catch (SQLException ex) {
            result = false;
        } finally {
            DBMS.getDBMS().closeConnection(null, stmt, conn);
        }
        return result;
    }
}
