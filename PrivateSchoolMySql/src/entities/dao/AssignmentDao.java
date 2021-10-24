package entities.dao;

import entities.DBMS.DBMS;
import entities.Assignment;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class AssignmentDao implements ActionsInterface<Assignment> {

    @Override
    public ArrayList<Assignment> selectAll() {
        ArrayList<Assignment> assignments = new ArrayList<Assignment>();
        Connection conn = DBMS.getDBMS().getConnection();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("call allSelect('assignments');");
            while (rs.next()) {
                assignments.add(new Assignment(rs.getInt("id"), rs.getString("title"), rs.getString("adescription"), LocalDateTime.parse(rs.getString("subDateTime"), DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")), rs.getInt("oralMark"), rs.getInt("totalMark")));
            }
        } catch (SQLException ex) {
            assignments = null;
        } finally {
            DBMS.getDBMS().closeConnection(rs, stmt, conn);
        }
        return assignments;
    }

    @Override
    public Assignment findById(int id) {
        Connection conn = DBMS.getDBMS().getConnection();
        Statement stmt = null;
        ResultSet rs = null;
        Assignment asn;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("call findById('assignments'," + id + ");");
            rs.next();
            asn = new Assignment(rs.getInt("id"), rs.getString("title"), rs.getString("adescription"), LocalDateTime.parse(rs.getString("subDateTime"), DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")), rs.getInt("oralMark"), rs.getInt("totalMark"));
        } catch (SQLException ex) {
            asn = null;
        } finally {
            DBMS.getDBMS().closeConnection(rs, stmt, conn);
        }
        return asn;
    }

    @Override
    public boolean insert(Assignment t) {
        Connection conn = DBMS.getDBMS().getConnection();
        Statement stmt = null;
        boolean result;
        try {
            stmt = conn.createStatement();
            result = (stmt.executeUpdate("call insertToAssignments('" + t.getTitle() + "','" + t.getDescription() + "','" + t.getSubDateTime() + "','" + t.getOralMark() + "','" + t.getTotalMark() + "');") == 1);
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
            result = (stmt.executeUpdate("call deleteById('assignments'," + id + ");") == 1);
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
            result = (stmt.executeUpdate("call deleteAll('assignments');") >= 0);
            stmt.execute("alter table assignments auto_increment=1;");//gia na xekinaei to id panta apo 1 se periptwsh pou eixe hdh data o pinakas 
        } catch (SQLException ex) {
            result = false;
        } finally {
            DBMS.getDBMS().closeConnection(null, stmt, conn);
        }
        return result;
    }
}
