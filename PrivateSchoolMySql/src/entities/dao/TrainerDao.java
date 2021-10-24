package entities.dao;

import entities.DBMS.DBMS;
import entities.Trainer;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class TrainerDao implements ActionsInterface<Trainer> {

    @Override
    public ArrayList<Trainer> selectAll() {
        ArrayList<Trainer> trainers = new ArrayList<Trainer>();
        Connection conn = DBMS.getDBMS().getConnection();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("call allSelect('trainers');");
            while (rs.next()) {
                trainers.add(new Trainer(rs.getInt("id"), rs.getString("firstName"), rs.getString("lastName"), rs.getString("tsubject")));
            }
        } catch (SQLException ex) {
            trainers = null;
        } finally {
            DBMS.getDBMS().closeConnection(rs, stmt, conn);
        }
        return trainers;
    }

    @Override
    public Trainer findById(int id) {
        Connection conn = DBMS.getDBMS().getConnection();
        Statement stmt = null;
        ResultSet rs = null;
        Trainer trn;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("call findById('trainers'," + id + ");");
            rs.next();
            trn = new Trainer(rs.getInt("id"), rs.getString("firstName"), rs.getString("lastName"), rs.getString("tsubject"));
        } catch (SQLException ex) {
            trn = null;
        } finally {
            DBMS.getDBMS().closeConnection(rs, stmt, conn);
        }
        return trn;
    }

    @Override
    public boolean insert(Trainer t) {
        Connection conn = DBMS.getDBMS().getConnection();
        Statement stmt = null;
        boolean result;
        try {
            stmt = conn.createStatement();
            result = (stmt.executeUpdate("call insertToTrainers('" + t.getFirstName() + "','" + t.getLastName() + "','" + t.getSubject() + "');") == 1);
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
            result = (stmt.executeUpdate("call deleteById('trainers'," + id + ");") == 1);
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
            result = (stmt.executeUpdate("call deleteAll('trainers');") >= 0);
            stmt.execute("alter table trainers auto_increment=1;");//gia na xekinaei to id panta apo 1 se periptwsh pou eixe hdh data o pinakas 
        } catch (SQLException ex) {
            result = false;
        } finally {
            DBMS.getDBMS().closeConnection(null, stmt, conn);
        }
        return result;
    }
}
