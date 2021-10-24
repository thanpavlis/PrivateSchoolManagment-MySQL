package entities.dao;

import entities.io.Input;
import entities.io.Printer;
import entities.DBMS.DBMS;
import entities.*;
import privateschoolmysql.*;
import static entities.io.Input.isNumberBiggerThanLimit;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EntitiesRelationshipDao extends Constants {

    /*elegxw ta ids pou thelei na diagrapsei o xrhsths apo thn bash, eite kyria ontothta, eite sysxetish*/
    public boolean checkRemoveIds(String label, String relationship) {
        String[] parts;
        String[] parts2;
        int[] refIds;
        int entitieId, i, j;
        ArrayList<EntitiesRelationship> eRArray = null;
        ArrayList<Integer> ids = null;
        boolean isValid = true, found = false;
        if (label.equals(stud) || label.equals(trai) || label.equals(cour) || label.equals(assig)) {//autes einai oi anexarthtes ontothtes
            ids = new ArrayList<>();
            parts = relationship.split("-");//p.x. 1-2-3
            refIds = new int[parts.length];
            i = 0;
            while ((i < parts.length) && isValid) {//gia kathe id tha psaxw na dw an einai arithmos kai megalyteros tou 0
                if (Input.isNumberBiggerThanLimit(parts[i], 0) != -1) {
                    refIds[i] = Input.isNumberBiggerThanLimit(parts[i], 0);
                    i++;
                } else {
                    isValid = false;
                }
            }
            if (isValid && (refIds.length >= 2)) {//tsekarw an o xrhsths edwse dipla ids
                i = 0;
                while ((i < refIds.length) && isValid) {
                    j = i + 1;
                    while ((j < refIds.length) && isValid) {
                        if (refIds[i] == refIds[j]) {//yparxei koino stoixei ston pinaka
                            isValid = false;
                        }
                        j++;
                    }
                    i++;
                }
            }
            if (isValid) {//exei perasei tous parapanw elegxous
                ids = selectAllMainEntitiesAsIds(label);//gia tis kyries ontothtes 
                i = 0;
                while ((i < refIds.length) && (isValid = true)) {
                    if (!ids.contains(refIds[i])) {
                        isValid = false;
                    }
                    i++;
                }
            }
        } else {//alliws prepei na afairesw kapoia apo tis sisxetiseis ths morfhs entitie_id - > refIds p.x. 10->1-2-3
            parts = relationship.split("->");
            if (parts.length == 2) {//an den spaei se dio parts analoga me thn morfh pou ypedeixa kanw return false
                eRArray = new ArrayList<>();
                entitieId = Input.isNumberBiggerThanLimit(parts[0], 0);//to parts[0] einai to entitieId analoga thn periptwsh, tsekarw an einai arithmos kai megalyteros tou 0
                if (entitieId == -1) {
                    isValid = false;
                } else {
                    parts2 = parts[1].split("-");//polla h ena id p.x. 1-2-3
                    refIds = new int[parts2.length];
                    i = 0;
                    while ((i < parts2.length) && isValid) {//gia kathe id tha psaxw na dw an einai arithmos kai megalyteros tou 0
                        if (Input.isNumberBiggerThanLimit(parts2[i], 0) != -1) {
                            refIds[i] = Input.isNumberBiggerThanLimit(parts2[i], 0);
                            i++;
                        } else {
                            isValid = false;
                        }
                    }
                    if (isValid && (refIds.length >= 2)) {//tsekarw an o xrhsths edwse dipla ids
                        i = 0;
                        while ((i < refIds.length) && isValid) {
                            j = i + 1;
                            while ((j < refIds.length) && isValid) {
                                if (refIds[i] == refIds[j]) {//yparxei koino stoixei ston pinaka
                                    isValid = false;
                                }
                                j++;
                            }
                            i++;
                        }
                    }
                    if (isValid) {//exei perasei tous parapanw elegxous
                        eRArray = selectAllRelationshipAsIds(label);
                        i = 0;
                        while ((i < eRArray.size()) && isValid) { //π.χ. 10->1-2-3-4 => entitieId -> refIds    
                            if (eRArray.get(i).getEntitieId() == entitieId) {//ean to entitieId -> id yparxei kataxwrhmeno sthn antistoixh sysxetish
                                found = true;//gia na xerw an exw vrei thn kiria ontothta
                                for (int refId : refIds) {//gia ola ta ids pou thelw na diagrapsw
                                    if (!eRArray.get(i).getIds().contains(refId)) {//ean den periexei estw kai ena apo ta id pou thelw na diagrapsw
                                        isValid = false;
                                    }
                                }
                            }
                            i++;
                        }
                    }
                    isValid = isValid && found;//to found to thelw gia na xerw an yparxei kataxwrhsh me to entitieId() pou zhthse o xrhsths
                }
            } else {
                isValid = false;
            }
        }
        return isValid;
    }

    /*diagrafei apo thn bash ta rows me ta sygkekrimena ids, eite apo kyria ontothta, eite apo sysxetiseis, afou exei oloklirwthei o elegxos*/
    public boolean deleteFromDB(String label, String ids) {
        String[] idS;
        String[] parts;
        String[] parts2;
        StringBuilder refIds = null;
        int entitieId = 0, i;
        int[] idRe = null;
        boolean result = false;
        Connection conn = DBMS.getDBMS().getConnection();
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            if (label.equals(stud) || label.equals(cour) || label.equals(trai) || label.equals(assig)) {//anexarthtes ontothtes ids ths morfhs p.x. 1-2-3
                idS = ids.split("-");
                for (String id : idS) {//gia kathe id kyrias ontothtas pou thelw na diagrapsw 
                    result = (stmt.executeUpdate("call deleteMainEntityById('" + label + "'," + Integer.parseInt(id) + ");") == 1);
                }
            } else {//einai ths morfhs entitieId -> refIds
                refIds = new StringBuilder();
                parts = ids.split("->");
                parts2 = parts[1].split("-");
                entitieId = Integer.parseInt(parts[0]);
                i = 0;//ftiaxnw thn morfh twn refIds pou tha steilw gia diagrafh
                while (i < parts2.length) {
                    refIds.append(parts2[i]);
                    refIds.append("-");
                    i++;
                }
                refIds.deleteCharAt(refIds.length() - 1);//vgazw to teleutaio -
                result = (stmt.executeUpdate("call deleteEntitiesRelationship(" + entitieId + ",'" + refIds.toString() + "','" + label + "');") == 1);
            }
        } catch (SQLException ex) {
            result = false;
        } finally {
            DBMS.getDBMS().closeConnection(null, stmt, conn);
        }
        return result;
    }

    /*epistrefei tis sisxetiseis se morfh ids mesw ArrayList ws enititeId -> ids*/
    public ArrayList<EntitiesRelationship> selectAllRelationshipAsIds(String label) {
        Connection conn = DBMS.getDBMS().getConnection();
        Statement stmt = null;
        ResultSet rs = null;
        ArrayList<EntitiesRelationship> eRArray = new ArrayList<>();
        EntitiesRelationship eRel = null;
        int tempId = 0, rowId = 1;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("call entitiesRelationship('" + label + "');");
            switch (label) {
                case studPerCo:
                    while (rs.next()) {
                        if (tempId != rs.getInt("courseID")) {//diaforetikh ontothta neo object sth lista
                            tempId = rs.getInt("courseID");
                            eRel = new EntitiesRelationship(rs.getInt("courseID"), rs.getInt("studentID"));
                            eRel.setRowId(rowId++);
                            eRArray.add(eRel);
                        } else {//idia ontothta idio object
                            eRArray.get(arrayListSearch(eRArray, rs.getInt("courseID"))).addListIds(rs.getInt("studentID"));
                        }
                    }
                    break;
                case traiPerCo:
                    while (rs.next()) {
                        if (tempId != rs.getInt("courseID")) {//diaforetikh ontothta neo object sth lista
                            tempId = rs.getInt("courseID");
                            eRel = new EntitiesRelationship(rs.getInt("courseID"), rs.getInt("trainerID"));
                            eRel.setRowId(rowId++);
                            eRArray.add(eRel);
                        } else {//idia ontothta idio object
                            eRArray.get(arrayListSearch(eRArray, rs.getInt("courseID"))).addListIds(rs.getInt("trainerID"));
                        }
                    }
                    break;
                case assiPerCo:
                    while (rs.next()) {
                        if (tempId != rs.getInt("courseID")) {//diaforetikh ontothta neo object sth lista
                            tempId = rs.getInt("courseID");
                            eRel = new EntitiesRelationship(rs.getInt("courseID"), rs.getInt("assignmentID"));
                            eRel.setRowId(rowId++);
                            eRArray.add(eRel);
                        } else {//idia ontothta idio object
                            eRArray.get(arrayListSearch(eRArray, rs.getInt("courseID"))).addListIds(rs.getInt("assignmentID"));
                        }
                    }
                    break;
                default://assignmentsPerStudent
                    while (rs.next()) {
                        if (tempId != rs.getInt("studentID")) {//diaforetikh ontothta neo object sth lista
                            tempId = rs.getInt("studentID");
                            eRel = new EntitiesRelationship(rs.getInt("studentID"), rs.getInt("assignmentID"));
                            eRel.setRowId(rowId++);
                            eRArray.add(eRel);
                        } else {//idia ontothta idio object
                            eRArray.get(arrayListSearch(eRArray, rs.getInt("studentID"))).addListIds(rs.getInt("assignmentID"));
                        }
                    }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBMS.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DBMS.getDBMS().closeConnection(rs, stmt, conn);
        }
        return eRArray;
    }

    /*epistrefei ta ids twn kyriwn ontothtwn mesw ArrayList*/
    public ArrayList<Integer> selectAllMainEntitiesAsIds(String label) {
        Connection conn = DBMS.getDBMS().getConnection();
        Statement stmt = null;
        ResultSet rs = null;
        ArrayList<Integer> ids = new ArrayList<>();
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("call mainEntitiesIds('" + label + "');");
            while (rs.next()) {
                ids.add(rs.getInt("id"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBMS.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DBMS.getDBMS().closeConnection(rs, stmt, conn);
        }
        return ids;
    }

    /*epistrefei ArrayList<EntitiesPairing<U, V>> opou ta U,V einai ta antikeimena ws apotelesma tou join pou kanw kathe fora*/
    public <U, V> ArrayList<EntitiesPairing<U, V>> selectAllAsJoin(String label) {
        Connection conn = DBMS.getDBMS().getConnection();
        Statement stmt = null;
        ResultSet rs = null;
        Course course;
        Student student;
        Trainer trainer;
        Assignment assignment;
        ArrayList<EntitiesPairing<U, V>> join = new ArrayList<>();
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("call allSelect('" + label + "');");
            switch (label) {
                case studPerCo:
                    while (rs.next()) {
                        course = new Course(rs.getInt("courseId"), rs.getString("title"), rs.getString("cstream"), rs.getString("ctype"), LocalDate.parse(rs.getString("startDate"), DateTimeFormatter.ofPattern("dd/MM/yyyy")), LocalDate.parse(rs.getString("endDate"), DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                        student = new Student(rs.getInt("studentId"), rs.getString("firstName"), rs.getString("lastName"), LocalDate.parse(rs.getString("dateOfBirth"), DateTimeFormatter.ofPattern("dd/MM/yyyy")), rs.getInt("tuitionFees"));
                        join.add(new EntitiesPairing(course, student));
                    }
                    break;
                case traiPerCo:
                    while (rs.next()) {
                        course = new Course(rs.getInt("courseId"), rs.getString("title"), rs.getString("cstream"), rs.getString("ctype"), LocalDate.parse(rs.getString("startDate"), DateTimeFormatter.ofPattern("dd/MM/yyyy")), LocalDate.parse(rs.getString("endDate"), DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                        trainer = new Trainer(rs.getInt("trainerId"), rs.getString("firstName"), rs.getString("lastName"), rs.getString("tsubject"));
                        join.add(new EntitiesPairing(course, trainer));
                    }
                    break;
                case assiPerCo:
                    while (rs.next()) {
                        course = new Course(rs.getInt("courseId"), rs.getString("title"), rs.getString("cstream"), rs.getString("ctype"), LocalDate.parse(rs.getString("startDate"), DateTimeFormatter.ofPattern("dd/MM/yyyy")), LocalDate.parse(rs.getString("endDate"), DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                        assignment = new Assignment(rs.getInt("AssignmentId"), rs.getString("title"), rs.getString("adescription"), LocalDateTime.parse(rs.getString("subDateTime"), DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")), rs.getInt("oralMark"), rs.getInt("totalMark"));
                        join.add(new EntitiesPairing(course, assignment));
                    }
                    break;
                default://assignmentsPerStudent      
                    while (rs.next()) {
                        student = new Student(rs.getInt("studentId"), rs.getString("firstName"), rs.getString("lastName"), LocalDate.parse(rs.getString("dateOfBirth"), DateTimeFormatter.ofPattern("dd/MM/yyyy")), rs.getInt("tuitionFees"));
                        assignment = new Assignment(rs.getInt("AssignmentId"), rs.getString("title"), rs.getString("adescription"), LocalDateTime.parse(rs.getString("subDateTime"), DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")), rs.getInt("oralMark"), rs.getInt("totalMark"));
                        join.add(new EntitiesPairing(student, assignment));

                    }
            }
        } catch (SQLException ex) {
            Logger.getLogger(EntitiesRelationshipDao.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            DBMS.getDBMS().closeConnection(rs, stmt, conn);
        }
        return join;
    }

    /*epistrefei tous students pou anhkoun se perissotera apo ena mathimata*/
    public ArrayList<Student> selectStudentToMoreThanOneCourses() {
        Connection conn = DBMS.getDBMS().getConnection();
        ResultSet rs = null;
        Statement stmt = null;
        ArrayList<Student> students = new ArrayList<>();
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("call allSelect('studentsToMoreCourses');");
            while (rs.next()) {//diaforetikos mathiths
                students.add(new Student(rs.getInt("id"), rs.getString("firstName"), rs.getString("lastName"), LocalDate.parse(rs.getString("dateOfBirth"), DateTimeFormatter.ofPattern("dd/MM/yyyy")), rs.getInt("tuitionFees")));

            }
        } catch (SQLException ex) {
            Logger.getLogger(EntitiesRelationshipDao.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            DBMS.getDBMS().closeConnection(rs, stmt, conn);
        }
        return students;
    }

    /*epistrefei to index ths ArrayList pou iparxei apothikeumeno to entitieId diladh h kyria ontothta*/
    public int arrayListSearch(ArrayList<EntitiesRelationship> eR, int entitieId) {
        int i = 0, index = -1;
        while ((index == -1) && (i < eR.size())) {//epistrefw to index tou entitieId pou psaxnw 
            if (eR.get(i).getEntitieId() == entitieId) {
                index = i;
            }
            i++;
        }
        return index;
    }

    /*ektypwnei tous mathites pou prepei na paradwsoun mia toulaxiston ergasia mesa sthn ebdomada pou anhkei h hmeromhnia valueDate*/
    public void assignmentsPerStudentInPeriod(String valueDate) {
        Statement stmt = null;
        ResultSet rs = null;
        int minusDays, tempId = 0;
        LocalDate date = LocalDate.parse(valueDate, DateTimeFormatter.ofPattern("dd/MM/yyyy"));//kanw localDate thn hmeromhnia pou diavasa
        minusDays = date.getDayOfWeek().getValue() - 1;//ypologizw poses meres prepei na afairesw gia na peftw panta sthn Deutera
        LocalDate startPeriod = date.minusDays(minusDays);//to start_period ths ebdomadas einai panta h deutera
        LocalDate endPeriod = startPeriod.plusDays(6);//to end_period einai Deutera+6 meres = oloklhrh h ebdomada
        System.out.println(Printer.red("Η ημερομηνία που έδωσες ανήκει στην εβδομάδα, από " + DateTimeFormatter.ofPattern("dd/MM/yyyy").format(startPeriod) + " έως " + DateTimeFormatter.ofPattern("dd/MM/yyyy").format(endPeriod) + " και"));
        System.out.println(Printer.red("μέσα σε αυτή την εβδομάδα έχουν να παραδώσουν εργασίες οι εξής μαθητές:"));
        Connection conn = DBMS.getDBMS().getConnection();
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("call sizeOfAssigPerStudInPeriod('" + startPeriod + "','" + endPeriod + "');");
            rs.next();
            if (rs.getInt("size") > 0) {//an o pinakas exei stoixeia        
                rs = stmt.executeQuery("call assignmentsPerStudentInPeriod('" + startPeriod + "','" + endPeriod + "');");
                while (rs.next()) {//diaforetikos mathiths     
                    if (tempId != rs.getInt("studentId")) {
                        tempId = rs.getInt("studentId");
                        Printer.printRelationshipsTableHeader("oneStudent");
                        System.out.println(String.format(Printer.header(Student.class
                        ), rs.getInt("studentId"), rs.getString("firstName"), rs.getString("lastName"), rs.getString("dateOfBirth"), rs.getInt("tuitionFees")));
                        System.out.println(Printer.arrayDividers(Student.class
                        ));//kleisimo tou pinaka       
                        Printer.printRelationshipsTableHeader(DateTimeFormatter.ofPattern("dd/MM/yyyy").format(startPeriod) + "," + DateTimeFormatter.ofPattern("dd/MM/yyyy").format(endPeriod));//stelnw tis hmeromhnies startPeriod,endPeriod xwrismenes me ,

                    }
                    System.out.println(String.format(Printer.header(Assignment.class
                    ), rs.getInt("assignmentId"), rs.getString("title"), rs.getString("adescription"), rs.getString("subDateTime"), rs.getInt("oralMark"), rs.getInt("totalMark")));
                    System.out.println(Printer.arrayDividers(Assignment.class
                    ));
                }
            } else {
                stmt = conn.createStatement();
                rs = stmt.executeQuery("call arraySize('assignmentsPerStudent');");
                rs.next();
                if (rs.getInt("size") > 0) {//an o pinakas exei stoixeia 
                    System.out.println(Printer.red("Δεν βρέθηκε κανένας μαθητής που έχει να παραδώσει μέσα σε αυτή την περίοδο !"));
                } else {
                    System.out.println(Printer.red("Δεν υπάρχουν συσχετίσεις assignmentsPerStudent !"));

                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(EntitiesRelationshipDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DBMS.getDBMS().closeConnection(rs, stmt, conn);
        }
    }

    /*elegxw an h sysxetish pou thelei na eisagei o xrhsths einai ok, to eRArray to stelnw apo to Input san eisodo, enw to epistrefei 
      h selectAllRelationshipAsIds giati to xreiazomai kai sthn antistoixh methodo wste na mhn kanw dio fores to query*/
    public boolean checkAddRelationshipIds(ArrayList<EntitiesRelationship> eRArray, String relationship, String label) {
        String[] parts;
        String[] parts2;
        int[] refIds;
        int entitieId, i, j;
        parts = relationship.split("->");
        boolean isValid = true;
        ArrayList<Integer> mainEntitiesIds;
        ArrayList<Integer> refEntitiesIds;
        if (parts.length == 2) {//an den spaei se dio parts analoga me thn morfh pou ypedeixa kanw return false
            entitieId = isNumberBiggerThanLimit(parts[0], 0);//to parts[0] einai to entitieId analoga thn periptwsh, tsekarw an einai arithmos kai megalyteros tou 0
            if (entitieId == -1) {
                isValid = false;
            } else {
                mainEntitiesIds = new ArrayList<>();
                parts2 = parts[1].split("-");//polla h ena id p.x. 1-2-3
                refIds = new int[parts2.length];
                i = 0;
                while ((i < parts2.length) && isValid) {//gia kathe id tha psaxw na dw an einai arithmos kai megalyteros tou 0
                    if (isNumberBiggerThanLimit(parts2[i], 0) != -1) {
                        refIds[i] = isNumberBiggerThanLimit(parts2[i], 0);
                        i++;
                    } else {
                        isValid = false;
                    }
                }
                if (isValid && (refIds.length >= 2)) {//tsekarw an o xrhsths edwse dipla ids
                    i = 0;
                    while ((i < refIds.length) && isValid) {
                        j = i + 1;
                        while ((j < refIds.length) && isValid) {
                            if (refIds[i] == refIds[j]) {//yparxei koino stoixeio ston pinaka
                                isValid = false;
                            }
                            j++;
                        }
                        i++;
                    }
                }
                if (isValid) {//thelw na elegxw an kapoio apo ta ids pou thelei o xrhsths na eisagei sthn sisxethsh den yparxei     
                    switch (label) {
                        case studPerCo:
                            mainEntitiesIds = selectAllMainEntitiesAsIds(cour);
                            refEntitiesIds = selectAllMainEntitiesAsIds(stud);
                            break;
                        case traiPerCo:
                            mainEntitiesIds = selectAllMainEntitiesAsIds(cour);
                            refEntitiesIds = selectAllMainEntitiesAsIds(trai);
                            break;
                        case assiPerCo:
                            mainEntitiesIds = selectAllMainEntitiesAsIds(cour);
                            refEntitiesIds = selectAllMainEntitiesAsIds(assig);
                            break;
                        default://assignmentsPerCourse
                            mainEntitiesIds = selectAllMainEntitiesAsIds(stud);
                            refEntitiesIds = selectAllMainEntitiesAsIds(assig);
                    }
                    if (!mainEntitiesIds.contains(entitieId)) {//zhthse o xrhsths san mainEntity kati pou den yparxei sth bash 
                        isValid = false;
                        System.out.println(Printer.red("Το main id που έδωσες δεν υπάρχει στη βάση !"));
                    } else {
                        j = 0;
                        while ((j < refIds.length) && isValid) {//edw elegxw an gia kapoio refId Pou thelw na kanw thn sisxetish den yparxei sth bash h anexarthth ontothta ara den ginetai h sysxetish
                            if (!refEntitiesIds.contains(refIds[j])) {//an estw kai ena apo ta redIds den yparxei tote den ginetai h sysxetish
                                isValid = false;
                                System.out.println(Printer.red("Κάποιο από τα refIds που έδωσες δεν υπάρχει στη βάση !"));
                            }
                            j++;
                        }
                    }
                }
                if (isValid) {
                    i = 0;
                    while ((i < eRArray.size()) && isValid) {//edw elegxw an gia kapoio apo ta ids yparxei hdh h sysxetish
                        if (eRArray.get(i).getEntitieId() == entitieId) {//an to id yparxei hdh kataxwrhmeno
                            j = 0;
                            while ((j < refIds.length) && isValid) {
                                if (eRArray.get(i).getIds().contains(refIds[j])) {//an h sysxetish yparxei hdh gia to trexwn refId
                                    isValid = false;
                                    System.out.println(Printer.red("Η συσχέτιση υπάρχει ήδη για κάποιο από τα id που έδωσες !"));
                                }
                                j++;
                            }
                        }
                        i++;
                    }
                }
            }
        } else {
            isValid = false;
        }
        return isValid;
    }

    /*kanw insert twm sysxetisewn kalwntas thn antistoixh procedure*/
    public boolean insertToDBEntitiesRelationship(int entitieId, String refIds, String label) {
        Connection conn = DBMS.getDBMS().getConnection();
        Statement stmt = null;
        boolean result;
        try {
            stmt = conn.createStatement();
            result = (stmt.executeUpdate("call insertEntitiesRelationship(" + entitieId + ",'" + refIds + "','" + label + "');") == 1);
        } catch (SQLException ex) {
            result = false;
        } finally {
            DBMS.getDBMS().closeConnection(null, stmt, conn);
        }
        return result;
    }
}
