package entities.dao;

import entities.DBMS.DBMS;
import entities.Student;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class StudentDao implements ActionsInterface<Student> {

    @Override
    public ArrayList<Student> selectAll() {
        ArrayList<Student> students = new ArrayList<Student>();
        Connection conn = DBMS.getDBMS().getConnection();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("call allSelect('students');");
            while (rs.next()) {
                students.add(new Student(rs.getInt("id"), rs.getString("firstName"), rs.getString("lastName"), LocalDate.parse(rs.getString("dateOfBirth"), DateTimeFormatter.ofPattern("dd/MM/yyyy")), rs.getInt("tuitionFees")));
            }
        } catch (SQLException ex) {
            students = null;
        } finally {
            DBMS.getDBMS().closeConnection(rs, stmt, conn);
        }
        return students;
    }

    @Override
    public Student findById(int id) {
        Connection conn = DBMS.getDBMS().getConnection();
        Statement stmt = null;
        ResultSet rs = null;
        Student std;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("call findById('students'," + id + ");");
            rs.next();
            std = new Student(rs.getInt("id"), rs.getString("firstName"), rs.getString("lastName"), LocalDate.parse(rs.getString("dateOfBirth"), DateTimeFormatter.ofPattern("dd/MM/yyyy")), rs.getInt("tuitionFees"));
        } catch (SQLException ex) {
            std = null;
        } finally {
            DBMS.getDBMS().closeConnection(rs, stmt, conn);
        }
        return std;
    }

    @Override
    public boolean insert(Student t) {
        Connection conn = DBMS.getDBMS().getConnection();
        Statement stmt = null;
        boolean result;
        try {
            stmt = conn.createStatement();
            result = (stmt.executeUpdate("call insertToStudents('" + t.getFirstName() + "','" + t.getLastName() + "','" + t.getDateOfBirth() + "','" + t.getTuitionFees() + "');") == 1);
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
            result = (stmt.executeUpdate("call deleteById('students'," + id + ");") == 1);
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
            result = (stmt.executeUpdate("call deleteAll('students');") >= 0);
            stmt.execute("alter table students auto_increment=1;");//gia na xekinaei to id panta apo 1 se periptwsh pou eixe hdh data o pinakas 
        } catch (SQLException ex) {
            result = false;
        } finally {
            DBMS.getDBMS().closeConnection(null, stmt, conn);
        }
        return result;
    }
}
