package entities.io;

import entities.*;
import entities.dao.*;
import java.util.Scanner;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import privateschoolmysql.Constants;

public abstract class Input extends Constants {

    private static Scanner scan = null;
    private static CourseDao cdao = null;
    private static StudentDao sdao = null;
    private static TrainerDao tdao = null;
    private static AssignmentDao adao = null;
    private static EntitiesRelationshipDao edao = null;
    private static boolean writeToFile;//thn xrhsimopoiw gia thn eggrafh sto arxeio 

    private static void initialVars() {//arxikopoish twn antikeimenwn
        scan = new Scanner(System.in);
        cdao = new CourseDao();
        sdao = new StudentDao();
        tdao = new TrainerDao();
        adao = new AssignmentDao();
        edao = new EntitiesRelationshipDao();
        setWriteToFile(false);
    }

    public static boolean getWriteToFile() {
        return writeToFile;
    }

    public static void setWriteToFile(boolean wrToFile) {
        writeToFile = wrToFile;
    }

    public static void initializeDB() {
        initialVars();//arxikopoihsh
        Printer.initializeWidths();//arixikopoihsh twn maxWidths
        String choice;
        int ch;
        do {
            Printer.divider('=', 0);
            System.out.println(Printer.red("ΕΙΣΑΓΩΓΗ ΔΕΔΟΜΕΝΩΝ:"));
            System.out.println("1-ΠΛΗΚΤΡΟΛΟΓΙΟ");
            System.out.println("2-ΧΡΗΣΗ ΥΠΑΡΧΟΥΣΑΣ ΒΑΣΗΣ");
            System.out.println("0-Έξοδος");
            System.out.print("Δώσε αριθμό: ");
            choice = scan.nextLine().trim();
            ch = isNumberInLimits(choice, 0, 2);
            System.out.print((ch == -1) ? Printer.red("Μη αποδεκτή τιμή ξαναδώσε !\n") : "");
        } while (ch == -1);//-1 mh apodekth timh eite gt den einai arithmos eite giati einai ektos oriwn
        switch (ch) {
            case 1://eisagwgh apo to pliktrologio
                System.out.println(Printer.red("Διαγραφή βάσης δεδομένων:"));
                Printer.divider('-', 24);
                System.out.println(cdao.deleteAllData() ? Printer.green("Τα μαθήματα και οι συσχετίσεις τους διαγράφηκαν !") : Printer.red("Τα μαθήματα και οι συσχετίσεις τους δεν διαγράφηκαν !"));
                System.out.println(sdao.deleteAllData() ? Printer.green("Οι μαθητές και οι συσχετίσεις τους διαγράφηκαν !") : Printer.red("Οι μαθητές και οι συσχετίσεις τους δεν διαγράφηκαν !"));
                System.out.println(tdao.deleteAllData() ? Printer.green("Οι εκπαιδευτές και οι συσχετίσεις τους διαγράφηκαν !") : Printer.red("Οι εκπαιδευτές και οι συσχετίσεις τους δεν διαγράφηκαν !"));
                System.out.println(adao.deleteAllData() ? Printer.green("Οι εργασίες και οι συσχετίσεις τους διαγράφηκαν !") : Printer.red("Οι εργασίες και οι συσχετίσεις τους δεν διαγράφηκαν !"));
                inputKeyboardData();
                break;
            case 2://xrhsh yparxousas bashs
                mainMenu();
                break;
            default://exodos
                System.out.println(Printer.red("Έξοδος προγράμματος!"));
                System.exit(0);
        }
    }

    private static void inputKeyboardData() {//kalw tis methodous gia thn eisagwgh twn dedomenwn apo to pliktrologio
        addCourses();
        addTrainers();
        addStudents();
        addAssignments();
        addStudentsPerCourse();
        addTrainersPerCourse();
        addAssignmentsPerCourse();
        addAssignmentsPerStudent();
        mainMenu();
    }

    private static void addCourses() {
        //EISAGWGH TWN COURSES
        int pl, i;
        boolean valid, result;
        String plithos, title, stream, type, start_date, end_date;
        do {
            Printer.divider('=', 0);
            System.out.println("Πόσα courses θέλεις να εισάγεις ?");
            System.out.println("Δώσε αριθμό (μικρότερο του 1000): ");
            plithos = scan.nextLine().trim();
            pl = isNumberInLimits(plithos, 1, 1000);
            System.out.print((pl == -1) ? Printer.red("Μη αποδεκτή τιμή ξαναδώσε !\n") : "");
        } while (pl == -1);
        for (i = 1; i <= pl; i++) {//osa courses epelexe o xrhsths na prosthesei sthn arrayList
            Printer.divider('-', 0);
            do {
                System.out.println("Δώσε τίτλο για το " + i + "ο μάθημα (έως 100 χαρακτήρες): ");
                title = scan.nextLine().trim().replaceAll("\\s+", " ");
                System.out.print(title.isEmpty() || (title.length() > 100) ? Printer.red("Μη αποδεκτή τιμή ξαναδώσε !\n") : "");
            } while (title.isEmpty() || (title.length() > 100));
            do {
                System.out.println("Δώσε το stream για το " + i + "ο μάθημα (έως 100 χαρακτήρες): ");
                stream = scan.nextLine().trim().replaceAll("\\s+", " ");
                System.out.print(stream.isEmpty() || (stream.length() > 100) ? Printer.red("Μη αποδεκτή τιμή ξαναδώσε !\n") : "");
            } while (stream.isEmpty() || (stream.length() > 100));
            do {
                System.out.println("Δώσε τον τύπο για το " + i + "ο μάθημα (έως 100 χαρακτήρες): ");
                type = scan.nextLine().trim().replaceAll("\\s+", " ");
                System.out.print(type.isEmpty() || (type.length() > 100) ? Printer.red("Μη αποδεκτή τιμή ξαναδώσε !\n") : "");
            } while (type.isEmpty() || (type.length() > 100));
            do {//elegxos hmeromhnias start_date
                System.out.println("Δώσε την ημερομηνία έναρξης για το " + i + "ο μάθημα (να είναι της μορφής dd/mm/yyyy): ");
                start_date = scan.nextLine().trim();
                valid = isValidDateStart(start_date);
                System.out.print((!valid) ? Printer.red("Μη αποδεκτή τιμή ξαναδώσε !\n") : "");
            } while (!valid);
            do {//elegxos hmeromhnias end_date
                System.out.println("Δώσε την ημερομηνία λήξης για το " + i + "ο μάθημα (να είναι της μορφής dd/mm/yyyy): ");
                end_date = scan.nextLine().trim();
                valid = isValidDate(end_date);
                System.out.print((!valid) ? Printer.red("Μη αποδεκτή τιμή ξαναδώσε !\n") : "");
                if (valid) {//an h hmeromhnia lixhs einai valid tote thn sigkrinw me thn enarxhs
                    valid = isEndDateAfterStart(start_date, end_date);
                    System.out.print((!valid) ? Printer.red("Η ημερομηνία λήξης δεν μπορεί να είναι ίδια ή πιο παλιά από την έναρξης !\n") : "");
                }
            } while (!valid);
            //insert sth bash
            result = cdao.insert(new Course(title, stream, type, LocalDate.parse(start_date, DateTimeFormatter.ofPattern("dd/MM/yyyy")), LocalDate.parse(end_date, DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
            System.out.println(result ? Printer.green("Το " + i + "ο μάθημα αποθηκεύτηκε επιτυχώς !") : Printer.red("Το " + i + "ο μάθημα δεν αποθηκεύτηκε επιτυχώς !"));
        }
        Printer.initializeWidths();
    }

    private static void addTrainers() {
        //EISAGWGH TWN TRAINERS
        int pl, i;
        boolean result;
        String plithos, firstName, lastName, subject;
        do {
            Printer.divider('=', 0);
            System.out.println("Πόσους trainers θέλεις να εισάγεις ?");
            System.out.println("Δώσε αριθμό (μικρότερο του 1000): ");
            plithos = scan.nextLine().trim();
            pl = isNumberInLimits(plithos, 1, 1000);
            System.out.print((pl == -1) ? Printer.red("Μη αποδεκτή τιμή ξαναδώσε !\n") : "");
        } while (pl == -1);
        for (i = 1; i <= pl; i++) {//osous trainers epelexe o xrhsths na prosthesei sthn arrayList
            Printer.divider('-', 0);
            do {
                System.out.println("Δώσε όνομα για το " + i + "ο εκπαιδευτή (έως 100 χαρακτήρες): ");
                firstName = scan.nextLine().trim().replaceAll("\\s+", " ");
                System.out.print(firstName.isEmpty() || (firstName.length() > 100) ? Printer.red("Μη αποδεκτή τιμή ξαναδώσε !\n") : "");
            } while (firstName.isEmpty() || (firstName.length() > 100));
            do {
                System.out.println("Δώσε το επώνυμο για το " + i + "ο εκπαιδευτή (έως 100 χαρακτήρες):");
                lastName = scan.nextLine().trim().replaceAll("\\s+", " ");
                System.out.print(lastName.isEmpty() || (lastName.length() > 100) ? Printer.red("Μη αποδεκτή τιμή ξαναδώσε !\n") : "");
            } while (lastName.isEmpty() || (lastName.length() > 100));
            do {
                System.out.println("Δώσε το θέμα για το " + i + "ο εκπαιδευτή:");
                subject = scan.nextLine().trim().replaceAll("\\s+", " ");
                System.out.print(subject.isEmpty() ? Printer.red("Μη αποδεκτή τιμή ξαναδώσε !\n") : "");
            } while (subject.isEmpty());
            //insert sth bash
            result = tdao.insert(new Trainer(firstName, lastName, subject));
            System.out.println(result ? Printer.green("Ο " + i + "ος εκπαιδευτής αποθηκεύτηκε επιτυχώς !") : Printer.red("Ο " + i + "ος εκπαιδευτής δεν αποθηκεύτηκε επιτυχώς !"));
        }
        Printer.initializeWidths();
    }

    private static void addStudents() {
        //EISAGWGH TWN STUDENTS
        boolean valid, result;
        int pl, i, tuitionFees;
        String fees, plithos, firstName, lastName, dateOfBirth;
        do {
            Printer.divider('=', 0);
            System.out.println("Πόσους students θέλεις να εισάγεις ?");
            System.out.println("Δώσε αριθμό (μικρότερο του 1000): ");
            plithos = scan.nextLine().trim();
            pl = isNumberInLimits(plithos, 1, 1000);
            System.out.print((pl == -1) ? Printer.red("Μη αποδεκτή τιμή ξαναδώσε !\n") : "");
        } while (pl == -1);
        for (i = 1; i <= pl; i++) {//osous students epelexe o xrhsths na prosthesei sthn arrayList
            Printer.divider('-', 0);
            do {
                System.out.println("Δώσε όνομα για το " + i + "ο μαθητή (έως 100 χαρακτήρες): ");
                firstName = scan.nextLine().trim().replaceAll("\\s+", " ");
                System.out.print(firstName.isEmpty() || (firstName.length() > 100) ? Printer.red("Μη αποδεκτή τιμή ξαναδώσε !\n") : "");
            } while (firstName.isEmpty() || (firstName.length() > 100));
            do {
                System.out.println("Δώσε επώνυμο για το " + i + "ο μαθητή (έως 100 χαρακτήρες): ");
                lastName = scan.nextLine().trim().replaceAll("\\s+", " ");
                System.out.print(lastName.isEmpty() || (lastName.length() > 100) ? Printer.red("Μη αποδεκτή τιμή ξαναδώσε !\n") : "");
            } while (lastName.isEmpty() || (lastName.length() > 100));
            do {//elegxos hmeromhnias dateOfBirth
                System.out.println("Δώσε ημερομηνία γέννησης για το " + i + "ο μαθητή (να είναι της μορφής dd/mm/yyyy): ");
                dateOfBirth = scan.nextLine().trim();
                valid = isValidDateBirth(dateOfBirth);
                System.out.print((!valid) ? Printer.red("Μη αποδεκτή τιμή ξαναδώσε !\n") : "");
            } while (!valid);
            do {
                System.out.println("Δώσε δίδακτρα για το " + i + "ο μαθητή: ");
                fees = scan.nextLine().trim();
                tuitionFees = isNumberBiggerThanLimit(fees, 0);
                System.out.print((tuitionFees == -1) ? Printer.red("Μη αποδεκτή τιμή ξαναδώσε !\n") : "");
            } while (tuitionFees == -1);
            //insert sth bash
            result = sdao.insert(new Student(firstName, lastName, LocalDate.parse(dateOfBirth, DateTimeFormatter.ofPattern("dd/MM/yyyy")), tuitionFees));
            System.out.println(result ? Printer.green("Ο " + i + "ος μαθητής αποθηκεύτηκε επιτυχώς !") : Printer.red("Ο " + i + "ος μαθητής δεν αποθηκεύτηκε επιτυχώς !"));
        }
        Printer.initializeWidths();
    }

    private static void addAssignments() {
        //EISAGWGH TWN ASSIGNMENTS
        boolean valid, result;
        int pl, i, oralMark, totalMark;
        String mark, plithos, title, description, subDateTime;
        do {
            Printer.divider('=', 0);
            System.out.println("Πόσα assignemnts θέλεις να εισάγεις ?");
            System.out.println("Δώσε αριθμό (μικρότερο του 1000): ");
            plithos = scan.nextLine().trim();
            pl = isNumberInLimits(plithos, 1, 1000);
            System.out.print((pl == -1) ? Printer.red("Μη αποδεκτή τιμή ξαναδώσε !\n") : "");
        } while (pl == -1);
        for (i = 1; i <= pl; i++) {//osous students epelexe o xrhsths na prosthesei
            Printer.divider('-', 0);
            do {
                System.out.println("Δώσε τίτλο για την " + i + "η εργασία (έως 100 χαρακτήρες): ");
                title = scan.nextLine().trim().replaceAll("\\s+", " ");
                System.out.print(title.isEmpty() || (title.length() > 100) ? Printer.red("Μη αποδεκτή τιμή ξαναδώσε !\n") : "");
            } while (title.isEmpty() || (title.length() > 100));
            do {
                System.out.println("Δώσε περιγραφή για την " + i + "η εργασία: ");
                description = scan.nextLine().trim().replaceAll("\\s+", " ");
                System.out.print(description.isEmpty() ? Printer.red("Μη αποδεκτή τιμή ξαναδώσε !\n") : "");
            } while (description.isEmpty());
            do {//elegxos hmeromhnias subDateTime
                System.out.println("Δώσε ημερομηνία και ώρα υποβολής για την " + i + "η εργασία (να είναι της μορφής dd/mm/yyyy HH:mm:ss): ");
                subDateTime = scan.nextLine().trim().replaceAll("\\s+", " ");
                valid = isValidDateTime(subDateTime);//elexei an einai se morfh pou exw orisei ws dateTime
                System.out.print((!valid) ? Printer.red("Μη αποδεκτή τιμή ξαναδώσε !\n") : "");
            } while (!valid);
            do {
                System.out.println("Ο συνολικός βαθμός της εργασίας είναι με μέγιστο το 100.");
                System.out.println("π.χ. Αν δώσεις προφορικό 20/100, τότε αυτόματα το γραπτό μετράει για 80/100.");
                System.out.println("Δώσε το πόσο μετράει ο προφορικός βαθμός (στα 100) για την " + i + "η εργασία (τα προφορικά μπορεί να είναι και 0/100): ");
                mark = scan.nextLine().trim();
                oralMark = isNumberInLimits(mark, 0, 100);//mporei to oralMark na mhn paizei kapoio rolo
                System.out.print((oralMark == -1) ? Printer.red("Μη αποδεκτή τιμή ξαναδώσε !\n") : "");
            } while (oralMark == -1);
            System.out.println("Εφόσον ο προφορικός μετράει για " + oralMark + "/100, o γραπτός βαθμός μετράει για " + (100 - oralMark) + "/100");
            totalMark = 100 - oralMark;
            //insert sth bash
            result = adao.insert(new Assignment(title, description, LocalDateTime.parse(subDateTime, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")), oralMark, totalMark));
            System.out.println(result ? Printer.green("Η " + i + "η εργασία αποθηκεύτηκε επιτυχώς !") : Printer.green("Η " + i + "η εργασία δεν αποθηκεύτηκε επιτυχώς !"));
        }
        Printer.initializeWidths();
    }

    private static void addStudentsPerCourse() {
        //EISAGWGH TWN SYSXETISEWN studentsPerCourse
        int pl, i;
        boolean valid, result;
        String[] parts;
        String relationship, plithos;
        ArrayList<EntitiesRelationship> eRArray = new ArrayList<>();
        Printer.divider('=', 0);
        eRArray = edao.selectAllRelationshipAsIds(studPerCo);//pairnw thn lista me ta ids thn opoia dinw san input sthn checkAddRelationshipIds
        Printer.printEntity(cour, cdao.selectAll());//ektypwsh mathimatwn
        Printer.printEntity(stud, sdao.selectAll());//ektypwsh mathitwn
        Printer.printEntitiesRealationshipAsIds(studPerCo, eRArray);
        if (ActionsInterface.sizeOf(cour) > 0 && ActionsInterface.sizeOf(stud) > 0) {
            System.out.println(Printer.red("Δώσε συσχετίσεις studentsPerCourse."));
            do {
                Printer.divider('=', 0);
                System.out.println("Σε πόσα μαθήματα θέλεις να εισάγεις μαθητές (ο αριθμός δεν μπορεί να ξεπερνάει τα μαθήματα που έχεις εισάγει), δηλαδή " + ActionsInterface.sizeOf(cour) + ":");
                plithos = scan.nextLine().trim();
                pl = isNumberInLimits(plithos, 1, ActionsInterface.sizeOf(cour));
                System.out.print((pl == -1) ? Printer.red("Μη αποδεκτή τιμή ξαναδώσε !\n") : "");
            } while (pl == -1);
            for (i = 1; i <= pl; i++) {//poses fores tha diavasw apo ton xrhsth
                do {
                    System.out.println("Δώσε την " + i + "η συσχέτιση -> id_Μαθήματος (π.χ. 10) και τα id_Μαθητών(π.χ. 1-2-3) της μορφής (π.χ. 10->1-2-3) ή δώσε 0 για έξοδο από την τρέχουσα συσχέτιση:");
                    relationship = scan.nextLine().trim().replaceAll("\\s+", "");
                    valid = (isNumberInLimits(relationship, 0, 0) == 0) ? false : edao.checkAddRelationshipIds(eRArray, relationship, studPerCo);
                    System.out.print((!valid && (isNumberInLimits(relationship, 0, 0) != 0)) ? Printer.red("Μη αποδεκτή τιμή ξαναδώσε (δες αν τα id είναι διαθέσιμα, η μορφή τους είναι σωστή και είναι μοναδικά) !\n") : "");
                } while (!valid && (isNumberInLimits(relationship, 0, 0) != 0));
                if (valid) {
                    parts = relationship.split("->");
                    result = edao.insertToDBEntitiesRelationship(Integer.parseInt(parts[0]), parts[1], studPerCo);
                    System.out.println(result ? Printer.green("Η " + i + "η συσχέτιση studentsPerCourse αποθηκεύτηκε επιτυχώς !") : Printer.red("Η " + i + "η συσχέτιση studentsPerCourse δεν αποθηκεύτηκε επιτυχώς !"));
                    eRArray = edao.selectAllRelationshipAsIds(studPerCo);//fernw ta enhmerwmena ids
                    if (eRArray.size() > 0) {
                    } else {
                    }
                    Printer.printEntitiesRealationshipAsIds(studPerCo, eRArray);
                }
            }
        }
    }

    private static void addTrainersPerCourse() {
        //EISAGWGH TWN SYSXETISEWN trainersPerCourse
        int pl, i;
        boolean valid, result;
        String[] parts;
        String relationship, plithos;
        ArrayList<EntitiesRelationship> eRArray = new ArrayList<>();
        Printer.divider('=', 0);
        eRArray = edao.selectAllRelationshipAsIds(traiPerCo);//pairnw thn lista me ta ids thn opoia dinw san input sthn checkAddRelationshipIds
        Printer.printEntity(cour, cdao.selectAll());//ektypwsh mathimatwn
        Printer.printEntity(trai, tdao.selectAll());//ektypwsh trainers
        Printer.printEntitiesRealationshipAsIds(traiPerCo, eRArray);
        if (ActionsInterface.sizeOf(cour) > 0 && ActionsInterface.sizeOf(trai) > 0) {
            System.out.println(Printer.red("Δώσε συσχετίσεις trainersPerCourse."));
            do {
                Printer.divider('=', 0);
                System.out.println("Σε πόσα μαθήματα θέλεις να εισάγεις εκπαιδευτές (ο αριθμός δεν μπορεί να ξεπερνάει τα μαθήματα που έχεις εισάγει), δηλαδή " + ActionsInterface.sizeOf(cour) + ":");
                plithos = scan.nextLine().trim();
                pl = isNumberInLimits(plithos, 1, ActionsInterface.sizeOf(cour));
                System.out.print((pl == -1) ? Printer.red("Μη αποδεκτή τιμή ξαναδώσε !\n") : "");
            } while (pl == -1);
            for (i = 1; i <= pl; i++) {//poses fores tha diavasw apo ton xrhsth
                do {
                    System.out.println("Δώσε την " + i + "η συσχέτιση -> id_Μαθήματος (π.χ. 10) και τα id_Εκπαιδευτών(π.χ. 1-2-3) της μορφής (π.χ. 10->1-2-3) ή δώσε 0 για έξοδο από την τρέχουσα συσχέτιση:");
                    relationship = scan.nextLine().trim().replaceAll("\\s+", "");
                    valid = (isNumberInLimits(relationship, 0, 0) == 0) ? false : edao.checkAddRelationshipIds(eRArray, relationship, traiPerCo);
                    System.out.print((!valid && (isNumberInLimits(relationship, 0, 0) != 0)) ? Printer.red("Μη αποδεκτή τιμή ξαναδώσε (δες αν τα id είναι διαθέσιμα, η μορφή τους είναι σωστή και είναι μοναδικά) !\n") : "");
                } while (!valid && (isNumberInLimits(relationship, 0, 0) != 0));
                if (valid) {
                    parts = relationship.split("->");
                    result = edao.insertToDBEntitiesRelationship(Integer.parseInt(parts[0]), parts[1], traiPerCo);
                    System.out.println(result ? Printer.green("Η " + i + "η συσχέτιση trainersPerCourse αποθηκεύτηκε επιτυχώς !") : Printer.red("Η " + i + "η συσχέτιση trainersPerCourse δεν αποθηκεύτηκε επιτυχώς !"));
                    eRArray = edao.selectAllRelationshipAsIds(traiPerCo);//fernw ta enhmerwmena ids
                    Printer.printEntitiesRealationshipAsIds(traiPerCo, eRArray);
                }
            }
        }
    }

    private static void addAssignmentsPerCourse() {
        //EISAGWGH TWN SYSXETISEWN assignmentsPerCourse
        int pl, i;
        boolean valid, result;
        String[] parts;
        String relationship, plithos;
        ArrayList<EntitiesRelationship> eRArray = new ArrayList<>();
        Printer.divider('=', 0);
        eRArray = edao.selectAllRelationshipAsIds(assiPerCo);//pairnw thn lista me ta ids thn opoia dinw san input sthn checkAddRelationshipIds
        Printer.printEntity(cour, cdao.selectAll());//ektypwsh mathimatwn
        Printer.printEntity(assig, adao.selectAll());//ektypwsh assignments
        Printer.printEntitiesRealationshipAsIds(assiPerCo, eRArray);
        if (ActionsInterface.sizeOf(cour) > 0 && ActionsInterface.sizeOf(assig) > 0) {
            System.out.println(Printer.red("Δώσε συσχετίσεις assignmentsPerCourse."));
            do {
                Printer.divider('=', 0);
                System.out.println("Σε πόσα μαθήματα θέλεις να εισάγεις εργασίες (ο αριθμός δεν μπορεί να ξεπερνάει τα μαθήματα που έχεις εισάγει), δηλαδή " + ActionsInterface.sizeOf(cour) + ":");
                plithos = scan.nextLine().trim();
                pl = isNumberInLimits(plithos, 1, ActionsInterface.sizeOf(cour));
                System.out.print((pl == -1) ? Printer.red("Μη αποδεκτή τιμή ξαναδώσε !\n") : "");
            } while (pl == -1);
            for (i = 1; i <= pl; i++) {//poses fores tha diavasw apo ton xrhsth
                do {
                    System.out.println("Δώσε την " + i + "η συσχέτιση -> id_Μαθήματος (π.χ. 10) και τα id_Εργασιών(π.χ. 1-2-3) της μορφής (π.χ. 10->1-2-3) ή δώσε 0 για έξοδο από την τρέχουσα συσχέτιση:");
                    relationship = scan.nextLine().trim().replaceAll("\\s+", "");
                    valid = (isNumberInLimits(relationship, 0, 0) == 0) ? false : edao.checkAddRelationshipIds(eRArray, relationship, assiPerCo);
                    System.out.print((!valid && (isNumberInLimits(relationship, 0, 0) != 0)) ? Printer.red("Μη αποδεκτή τιμή ξαναδώσε (δες αν τα id είναι διαθέσιμα, η μορφή τους είναι σωστή και είναι μοναδικά) !\n") : "");
                } while (!valid && (isNumberInLimits(relationship, 0, 0) != 0));
                if (valid) {
                    parts = relationship.split("->");
                    result = edao.insertToDBEntitiesRelationship(Integer.parseInt(parts[0]), parts[1], assiPerCo);
                    System.out.println(result ? Printer.green("Η " + i + "η συσχέτιση assignmentsPerCourse αποθηκεύτηκε επιτυχώς !") : Printer.red("Η " + i + "η συσχέτιση assignmentsPerCourse δεν αποθηκεύτηκε επιτυχώς !"));
                    eRArray = edao.selectAllRelationshipAsIds(assiPerCo);//fernw ta enhmerwmena ids
                    Printer.printEntitiesRealationshipAsIds(assiPerCo, eRArray);
                }
            }
        }
    }

    private static void addAssignmentsPerStudent() {
        //EISAGWGH TWN SYSXETISEWN assignmentsPerStudent
        int pl, i;
        boolean valid, result;
        String[] parts;
        String relationship, plithos;
        ArrayList<EntitiesRelationship> eRArray = new ArrayList<>();
        Printer.divider('=', 0);
        eRArray = edao.selectAllRelationshipAsIds(assiPerSt);//pairnw thn lista me ta ids thn opoia dinw san input sthn checkAddRelationshipIds
        Printer.printEntity(stud, sdao.selectAll());//ektypwsh mathitwn
        Printer.printEntity(assig, adao.selectAll());//ektypwsh assignments G
        Printer.printEntitiesRealationshipAsIds(assiPerSt, eRArray);
        if (ActionsInterface.sizeOf(stud) > 0 && ActionsInterface.sizeOf(assig) > 0) {
            System.out.println(Printer.red("Δώσε συσχετίσεις assignmentsPerStudent."));
            do {
                Printer.divider('=', 0);
                System.out.println("Σε πόσους μαθητές θέλεις να εισάγεις εργασίες (ο αριθμός δεν μπορεί να ξεπερνάει τους μαθητές που έχεις εισάγει), δηλαδή " + ActionsInterface.sizeOf(stud) + ":");
                plithos = scan.nextLine().trim();
                pl = isNumberInLimits(plithos, 1, ActionsInterface.sizeOf(stud));
                System.out.print((pl == -1) ? Printer.red("Μη αποδεκτή τιμή ξαναδώσε !\n") : "");
            } while (pl == -1);
            for (i = 1; i <= pl; i++) {//poses fores tha diavasw apo ton xrhsth
                do {
                    System.out.println("Δώσε την " + i + "η συσχέτιση -> id_Μαθητή (π.χ. 10) και τα id_Εργασιών(π.χ. 1-2-3) της μορφής (π.χ. 10->1-2-3) ή δώσε 0 για έξοδο από την τρέχουσα συσχέτιση:");
                    relationship = scan.nextLine().trim().replaceAll("\\s+", "");
                    valid = (isNumberInLimits(relationship, 0, 0) == 0) ? false : edao.checkAddRelationshipIds(eRArray, relationship, assiPerSt);
                    System.out.print((!valid && (isNumberInLimits(relationship, 0, 0) != 0)) ? Printer.red("Μη αποδεκτή τιμή ξαναδώσε (δες αν τα id είναι διαθέσιμα, η μορφή τους είναι σωστή και είναι μοναδικά) !\n") : "");
                } while (!valid && (isNumberInLimits(relationship, 0, 0) != 0));
                if (valid) {
                    parts = relationship.split("->");
                    result = edao.insertToDBEntitiesRelationship(Integer.parseInt(parts[0]), parts[1], assiPerSt);
                    System.out.println(result ? Printer.green("Η " + i + "η συσχέτιση assignmentsPerStudent αποθηκεύτηκε επιτυχώς !") : Printer.red("Η " + i + "η συσχέτιση assignmentsPerStudent δεν αποθηκεύτηκε επιτυχώς !"));
                    eRArray = edao.selectAllRelationshipAsIds(assiPerSt);//fernw ta enhmerwmena ids
                    Printer.printEntitiesRealationshipAsIds(assiPerSt, eRArray);
                }
            }
        }
    }

    private static void mainMenu() {
        String choice;
        int ch;
        do {
            do {
                Printer.divider('=', 0);
                System.out.println(Printer.red("ΕΚΤΥΠΩΣΗ ΔΕΔΟΜΕΝΩΝ:"));
                System.out.println("1--ΟΛΟΙ ΟΙ ΜΑΘΗΤΕΣ");
                System.out.println("2--ΟΛΟΙ ΟΙ ΕΚΠΑΙΔΕΥΤΕΣ");
                System.out.println("3--ΟΛΑ ΤΑ ΜΑΘΗΜΑΤΑ");
                System.out.println("4--ΟΛΕΣ ΟΙ ΕΡΓΑΣΙΕΣ");
                System.out.println("5--ΟΛΟΙ ΟΙ ΜΑΘΗΤΕΣ ΑΝΑ ΜΑΘΗΜΑ");
                System.out.println("6--ΟΛΟΙ ΟΙ ΕΚΠΑΙΔΕΥΤΕΣ ΑΝΑ ΜΑΘΗΜΑ");
                System.out.println("7--ΟΛΕΣ ΟΙ ΕΡΓΑΣΙΕΣ ΑΝΑ ΜΑΘΗΜΑ");
                System.out.println("8--ΟΛΕΣ ΟΙ ΕΡΓΑΣΙΕΣ ΑΝΑ ΜΑΘΗΤΗ");
                System.out.println("9--ΜΑΘΗΤΕΣ ΠΟΥ ΑΝΗΚΟΥΝ ΣΕ ΠΕΡΙΣΣΟΤΕΡΑ ΑΠΟ ΕΝΑ ΜΑΘΗΜΑΤΑ");
                System.out.println("10-ΜΑΘΗΤΕΣ ΠΟΥ ΠΡΕΠΕΙ ΝΑ ΠΑΡΑΔΩΣΟΥΝ ΤΟΥΛΑΧΙΣΤΟΝ ΜΙΑ ΕΡΓΑΣΙΑ ΜΕΣΑ ΣΤΗΝ ΕΒΟΔΟΜΑΔΑ ΠΟΥ ΑΝΗΚΕΙ Η ΗΜΕΡΟΜΗΝΙΑ ΠΟΥ ΘΑ ΔΩΣΕΙΣ");
                System.out.println("11--ΕΞΑΓΩΓΗ ΣΤΙΓΜΙΟΤΥΠΟΥ ΒΑΣΗΣ ΔΕΔΟΜΕΝΩΝ ΣΕ ΑΝΑΓΝΩΣΙΜΟ ΑΡΧΕΙΟ PrivateSchoolDB_dd-MM-yyyy_HH_mm_ss.txt");
                System.out.println(Printer.red("ΠΡΟΣΘΗΚΗ - ΔΙΑΓΡΑΦΗ ΔΕΔΟΜΕΝΩΝ:"));
                System.out.println("12--ΠΡΟΣΘΗΚΗ ΔΕΔΟΜΕΝΩΝ");
                System.out.println("13--ΔΙΑΓΡΑΦΗ ΔΕΔΟΜΕΝΩΝ");
                System.out.println("0--Έξοδος");
                System.out.print("Δώσε αριθμό: ");
                choice = scan.nextLine();
                choice = choice.trim();
                ch = isNumberInLimits(choice, 0, 13);
                System.out.print((ch == -1) ? Printer.red("Μη αποδεκτή τιμή ξαναδώσε !\n") : "");
            } while (ch == -1);//-1 mh apodekth timh eite gt den einai arithmos eite giati einai ektos oriwn
            switch (ch) {//analoga me thn epilogh tou xrhsth
                case 1://ektypwsh olwn twn mathitwn    
                    if (ActionsInterface.sizeOf(stud) > 0) {
                        Printer.printEntity(stud, sdao.selectAll());
                    } else {
                        Printer.echoNotFound(stud);
                    }
                    break;
                case 2://etkypwsh olwn twn ekpaideutwn
                    if (ActionsInterface.sizeOf(trai) > 0) {
                        Printer.printEntity(trai, tdao.selectAll());
                    } else {
                        Printer.echoNotFound(trai);
                    }
                    break;
                case 3://ektypwsh olwn twn mathimatwn
                    if (ActionsInterface.sizeOf(cour) > 0) {
                        Printer.printEntity(cour, cdao.selectAll());
                    } else {
                        Printer.echoNotFound(cour);
                    }
                    break;
                case 4://ektypwsh olws twn assignments
                    if (ActionsInterface.sizeOf(assig) > 0) {
                        Printer.printEntity(assig, adao.selectAll());
                    } else {
                        Printer.echoNotFound(assig);
                    }
                    break;
                case 5://oloi oi mathites ana mathima
                    if (ActionsInterface.sizeOf(studPerCo) > 0) {
                        Printer.printEntitiesRealationshipAsIds(studPerCo, edao.selectAllRelationshipAsIds(studPerCo));
                        Printer.printEntitiesRealationshipAsReadable(studPerCo, edao.selectAllAsJoin(studPerCo));
                    } else {
                        Printer.echoNotFound(studPerCo);
                    }
                    break;
                case 6://oloi oi ekpaideutes an mathima
                    if (ActionsInterface.sizeOf(traiPerCo) > 0) {
                        Printer.printEntitiesRealationshipAsIds(traiPerCo, edao.selectAllRelationshipAsIds(traiPerCo));
                        Printer.printEntitiesRealationshipAsReadable(traiPerCo, edao.selectAllAsJoin(traiPerCo));
                    } else {
                        Printer.echoNotFound(traiPerCo);
                    }
                    break;
                case 7://oles oi ergasies ana mathima
                    if (ActionsInterface.sizeOf(assiPerCo) > 0) {
                        Printer.printEntitiesRealationshipAsIds(assiPerCo, edao.selectAllRelationshipAsIds(assiPerCo));
                        Printer.printEntitiesRealationshipAsReadable(assiPerCo, edao.selectAllAsJoin(assiPerCo));
                    } else {
                        Printer.echoNotFound(assiPerCo);
                    }
                    break;
                case 8://oles oi ergasies ana mathith
                    if (ActionsInterface.sizeOf(assiPerSt) > 0) {
                        Printer.printEntitiesRealationshipAsIds(assiPerSt, edao.selectAllRelationshipAsIds(assiPerSt));
                        Printer.printEntitiesRealationshipAsReadable(assiPerSt, edao.selectAllAsJoin(assiPerSt));
                    } else {
                        Printer.echoNotFound(assiPerSt);
                    }
                    break;
                case 9://oloi oi mathites pou anhkoun se perissotera apo ena mathimata                           
                    if (ActionsInterface.sizeOf("studentsToMoreCourses") > 0) {//an exw apotelesmata apo to query
                        Printer.printRelationshipsTableHeader("studentsToMoreThanOneCourse");
                        for (Student student : edao.selectStudentToMoreThanOneCourses()) {
                            System.out.println(student.tableFormat());
                            System.out.println(Printer.arrayDividers(Student.class));
                        }
                    } else {
                        if (ActionsInterface.sizeOf(studPerCo) > 0) {//an o pinakas exei stoixeia
                            System.out.println(Printer.red("Δεν υπάρχουν μαθητές να ανήκουν σε περισσότερα από ένα μαθήματα !"));
                        } else {
                            Printer.echoNotFound(studPerCo);
                        }
                    }
                    break;
                case 10://ektypwsh mathitwn pou exoun na paradwsoun toulaxiston mia ergasia mesa sthn ebdomada pou anhkei h hmeromhnia pou tha dwsei o xrhsths
                    String date;
                    boolean valid;
                    do {
                        System.out.println("Δώσε ημερομηνία (dd/mm/yyyy) για την οποία θα εκυπωθούν οι μαθητές, οι οποίοι πρέπει να\n"
                                + "παραδώσουν τουλάχιστον μια εργασία, εντός της εβδομάδας που ανήκει η ημερομηνία αυτή:");
                        date = scan.nextLine().trim().replaceAll("\\s+", "");;
                        valid = isValidDate(date);
                        System.out.print((!valid) ? Printer.red("Μη αποδεκτή τιμή ξαναδώσε !\n") : "");
                    } while (!valid);
                    edao.assignmentsPerStudentInPeriod(date);
                    break;
                case 11://export bashs se .txt arxeio
                    Printer.exportReadableDB();
                    break;
                case 12://prosthiki dedomenwn
                    addMenu();
                    break;
                case 13://diagrafh dedomenwn
                    removeMenu();
                    break;
                default:
                    System.out.println(Printer.red("Έξοδος προγράμματος!"));
                    System.exit(0);
            }

        } while (ch != 0);
    }

    private static void addMenu() {
        String choice;
        int ch;
        do {
            do {
                Printer.divider('=', 0);
                System.out.println(Printer.red("ΠΡΟΣΘΗΚΗ ΔΕΔΟΜΕΝΩΝ:"));
                System.out.println("1--ΜΑΘΗΤΕΣ");
                System.out.println("2--ΕΚΠΑΙΔΕΥΤΕΣ");
                System.out.println("3--ΜΑΘΗΜΑΤΑ");
                System.out.println("4--ΕΡΓΑΣΙΕΣ");
                System.out.println("5--ΜΑΘΗΤΕΣ ΑΝΑ ΜΑΘΗΜΑ");
                System.out.println("6--ΕΚΠΑΙΔΕΥΤΕΣ ΑΝΑ ΜΑΘΗΜΑ");
                System.out.println("7--ΕΡΓΑΣΙΕΣ ΑΝΑ ΜΑΘΗΜΑ");
                System.out.println("8--ΕΡΓΑΣΙΕΣ ΑΝΑ ΜΑΘΗΤΗ");
                System.out.println("0--ΠΙΣΩ");
                System.out.print("Δώσε αριθμό: ");
                choice = scan.nextLine();
                choice = choice.trim();
                ch = isNumberInLimits(choice, 0, 8);
                System.out.print((ch == -1) ? Printer.red("Μη αποδεκτή τιμή ξαναδώσε !\n") : "");
            } while (ch == -1);//-1 mh apodekth timh eite gt den einai arithmos eite giati einai ektos oriwn
            switch (ch) {//analoga me thn epilogh tou xrhsth
                case 1://prosthiki mathitwn
                    Printer.printEntity(stud, sdao.selectAll());
                    addStudents();
                    break;
                case 2://prosthiki ekpaideutwn             
                    Printer.printEntity(trai, tdao.selectAll());
                    addTrainers();
                    break;
                case 3://prosthiki mathimatwn      
                    Printer.printEntity(cour, cdao.selectAll());
                    addCourses();
                    break;
                case 4://prosthiki assignments
                    Printer.printEntity(assig, adao.selectAll());
                    addAssignments();
                    break;
                case 5://prosthiki mathites ana mathima
                    addStudentsPerCourse();
                    break;
                case 6://prosthiki ekpaideutes an mathima
                    addTrainersPerCourse();
                    break;
                case 7://prosthiki ergasies ana mathima
                    addAssignmentsPerCourse();
                    break;
                case 8://prosthiki ergasies ana mathith
                    addAssignmentsPerStudent();
                    break;
                default://pisw
                    System.out.println(Printer.red("ΠΙΣΩ"));
            }
        } while (ch != 0);
    }

    private static void removeMenu() {
        String choice;
        int ch;
        do {
            do {
                Printer.divider('=', 0);
                System.out.println(Printer.red("ΔΙΑΓΡΑΦΗ ΔΕΔΟΜΕΝΩΝ:"));
                System.out.println("1--ΜΑΘΗΤΕΣ");
                System.out.println("2--ΕΚΠΑΙΔΕΥΤΕΣ");
                System.out.println("3--ΜΑΘΗΜΑΤΑ");
                System.out.println("4--ΕΡΓΑΣΙΕΣ");
                System.out.println("5--ΜΑΘΗΤΕΣ ΑΝΑ ΜΑΘΗΜΑ");
                System.out.println("6--ΕΚΠΑΙΔΕΥΤΕΣ ΑΝΑ ΜΑΘΗΜΑ");
                System.out.println("7--ΕΡΓΑΣΙΕΣ ΑΝΑ ΜΑΘΗΜΑ");
                System.out.println("8--ΕΡΓΑΣΙΕΣ ΑΝΑ ΜΑΘΗΤΗ");
                System.out.println("0--ΠΙΣΩ");
                System.out.print("Δώσε αριθμό: ");
                choice = scan.nextLine();
                choice = choice.trim();
                ch = isNumberInLimits(choice, 0, 8);
                System.out.print((ch == -1) ? Printer.red("Μη αποδεκτή τιμή ξαναδώσε !\n") : "");
            } while (ch == -1);//-1 mh apodekth timh eite gt den einai arithmos eite giati einai ektos oriwn
            switch (ch) {//analoga me thn epilogh tou xrhsth
                case 1://diagrafh mathitwn
                    removeStudents();
                    break;
                case 2://diagrafh ekpaideutwn
                    removeTrainers();
                    break;
                case 3://diagrafh mathimatwn
                    removeCourses();
                    break;
                case 4://diagrafh assignments
                    removeAssignments();
                    break;
                case 5://diagrafh mathites ana mathima
                    removeStudentsPerCourse();
                    break;
                case 6://diagrafh ekpaideutes an mathima
                    removeTrainersPerCourse();
                    break;
                case 7://diagrafh ergasies ana mathima
                    removeAssignmentsPerCourse();
                    break;
                case 8://diagrafh ergasies ana mathith
                    removeAssignmentsPerStudent();
                    break;
                default://pisw
                    System.out.println(Printer.red("ΠΙΣΩ"));
            }
        } while (ch != 0);
    }

    private static void removeStudents() {
        //DIAGRAFH MATHITWN
        boolean valid, result;
        String relationship;
        Printer.divider('=', 0);
        Printer.printEntity(stud, sdao.selectAll());
        if (ActionsInterface.sizeOf(stud) > 0) {
            do {
                System.out.println("Δώσε τα id_Μαθητών(π.χ. 1-2-3) που θες να διαγράψεις) ή δώσε 0 για έξοδο από την διαγραφή:");
                relationship = scan.nextLine().trim().replaceAll("\\s+", "");
                valid = (isNumberInLimits(relationship, 0, 0) == 0) ? false : edao.checkRemoveIds(stud, relationship);
                System.out.print((!valid && (isNumberInLimits(relationship, 0, 0) != 0)) ? Printer.red("Μη αποδεκτή τιμή ξαναδώσε (δες αν τα id είναι διαθέσιμα, η μορφή τους είναι σωστή και είναι μοναδικά) !\n") : "");
            } while (!valid && (isNumberInLimits(relationship, 0, 0) != 0));
            if (valid) {
                result = edao.deleteFromDB(stud, relationship);
                System.out.println(result ? Printer.green("Η διαγραφή μαθητών έγινε επιτυχώς !") : Printer.red("Η διαγραφή μαθητών έγινε επιτυχώς !"));
            }
            Printer.initializeWidths();
        }
    }

    private static void removeTrainers() {
        //DIAGRAFH EKPAIDEYTWN
        boolean valid, result;
        String relationship;
        Printer.divider('=', 0);
        Printer.printEntity(trai, tdao.selectAll());
        if (ActionsInterface.sizeOf(trai) > 0) {
            do {
                System.out.println("Δώσε τα id_Εκπαιδευτών(π.χ. 1-2-3) που θες να διαγράψεις) ή δώσε 0 για έξοδο από την διαγραφή:");
                relationship = scan.nextLine().trim().replaceAll("\\s+", "");
                valid = (isNumberInLimits(relationship, 0, 0) == 0) ? false : edao.checkRemoveIds(trai, relationship);
                System.out.print((!valid && (isNumberInLimits(relationship, 0, 0) != 0)) ? Printer.red("Μη αποδεκτή τιμή ξαναδώσε (δες αν τα id είναι διαθέσιμα, η μορφή τους είναι σωστή και είναι μοναδικά) !\n") : "");
            } while (!valid && (isNumberInLimits(relationship, 0, 0) != 0));
            if (valid) {
                result = edao.deleteFromDB(trai, relationship);
                System.out.println(result ? Printer.green("Η διαγραφή εκπαιδευτών έγινε επιτυχώς !") : Printer.red("Η διαγραφή εκπαιδευτών δεν έγινε επιτυχώς !"));
            }
            Printer.initializeWidths();
        }
    }

    private static void removeCourses() {
        //DIAGRAFH MATHIMATWN
        boolean valid, result;
        String relationship;
        Printer.divider('=', 0);
        Printer.printEntity(cour, cdao.selectAll());
        if (ActionsInterface.sizeOf(cour) > 0) {
            do {
                System.out.println("Δώσε τα id_Μαθημάτων(π.χ. 1-2-3) που θες να διαγράψεις) ή δώσε 0 για έξοδο από την διαγραφή:");
                relationship = scan.nextLine().trim().replaceAll("\\s+", "");
                valid = (isNumberInLimits(relationship, 0, 0) == 0) ? false : edao.checkRemoveIds(cour, relationship);
                System.out.print((!valid && (isNumberInLimits(relationship, 0, 0) != 0)) ? Printer.red("Μη αποδεκτή τιμή ξαναδώσε (δες αν τα id είναι διαθέσιμα, η μορφή τους είναι σωστή και είναι μοναδικά) !\n") : "");
            } while (!valid && (isNumberInLimits(relationship, 0, 0) != 0));
            if (valid) {
                result = edao.deleteFromDB(cour, relationship);
                System.out.println(result ? Printer.green("Η διαγραφή μαθημάτων έγινε επιτυχώς !") : Printer.red("Η διαγραφή μαθημάτων δεν έγινε επιτυχώς !"));
            }
            Printer.initializeWidths();
        }
    }

    private static void removeAssignments() {
        //DIAGRAFH ERGASIWN
        boolean valid, result;
        String relationship;
        Printer.divider('=', 0);
        Printer.printEntity(assig, adao.selectAll());
        if (ActionsInterface.sizeOf(assig) > 0) {
            do {
                System.out.println("Δώσε τα id_Εργασιών(π.χ. 1-2-3) που θες να διαγράψεις) ή δώσε 0 για έξοδο από την διαγραφή:");
                relationship = scan.nextLine().trim().replaceAll("\\s+", "");
                valid = (isNumberInLimits(relationship, 0, 0) == 0) ? false : edao.checkRemoveIds(assig, relationship);
                System.out.print((!valid && (isNumberInLimits(relationship, 0, 0) != 0)) ? Printer.red("Μη αποδεκτή τιμή ξαναδώσε (δες αν τα id είναι διαθέσιμα, η μορφή τους είναι σωστή και είναι μοναδικά) !\n") : "");
            } while (!valid && (isNumberInLimits(relationship, 0, 0) != 0));
            if (valid) {
                result = edao.deleteFromDB(assig, relationship);
                System.out.println(result ? Printer.green("Η διαγραφή εργασιών έγινε επιτυχώς !") : Printer.red("Η διαγραφή εργασιών δεν έγινε επιτυχώς !"));
            }
            Printer.initializeWidths();
        }
    }

    private static void removeStudentsPerCourse() {
        //DIAGRAFH SYSXETISEWN studentsPerCourse
        boolean valid, result;
        String relationship;
        Printer.divider('=', 0);
        Printer.printEntitiesRealationshipAsIds(studPerCo, edao.selectAllRelationshipAsIds(studPerCo));
        Printer.printEntity(cour, cdao.selectAll());
        Printer.printEntity(stud, sdao.selectAll());
        if (ActionsInterface.sizeOf(studPerCo) > 0) {//exw sysxetiseis gia diagrafh     
            System.out.println(Printer.red("Δώσε την συσχέτιση που θέλεις να διαγράψεις."));
            do {
                System.out.println("Δώσε την συσχέτιση -> id_Μαθήματος (π.χ. 10) και τα id_Μαθητών(π.χ. 1-2-3) της μορφής (π.χ. 10->1-2-3) ή δώσε 0 για έξοδο από την διαγραφή:");
                relationship = scan.nextLine().trim().replaceAll("\\s+", "");
                valid = (isNumberInLimits(relationship, 0, 0) == 0) ? false : edao.checkRemoveIds(studPerCo, relationship);
                System.out.print((!valid && (isNumberInLimits(relationship, 0, 0) != 0)) ? Printer.red("Μη αποδεκτή τιμή ξαναδώσε (δες αν τα id είναι διαθέσιμα, η μορφή τους είναι σωστή και είναι μοναδικά) !\n") : "");
            } while (!valid && (isNumberInLimits(relationship, 0, 0) != 0));
            if (valid) {
                result = edao.deleteFromDB(studPerCo, relationship);
                System.out.println(result ? Printer.green("Η διαγραφή της συσχέτισης studentsPerCourse έγινε επιτυχώς !") : Printer.red("Η διαγραφή της συσχέτισης studentsPerCourse δεν έγινε επιτυχώς !"));
            }
        }
    }

    private static void removeTrainersPerCourse() {
        //DIAGRAFH SYSXETISEWN trainersPerCourse
        boolean valid, result;
        String relationship;
        Printer.divider('=', 0);
        Printer.printEntitiesRealationshipAsIds(traiPerCo, edao.selectAllRelationshipAsIds(traiPerCo));
        Printer.printEntity(cour, cdao.selectAll());
        Printer.printEntity(trai, tdao.selectAll());
        if (ActionsInterface.sizeOf(traiPerCo) > 0) {
            System.out.println(Printer.red("Δώσε την συσχέτιση που θέλεις να διαγράψεις."));
            do {
                System.out.println("Δώσε την συσχέτιση -> id_Μαθήματος (π.χ. 10) και τα id_Εκπαιδευτών(π.χ. 1-2-3) της μορφής (π.χ. 10->1-2-3) ή δώσε 0 για έξοδο από την διαγραφή:");
                relationship = scan.nextLine().trim().replaceAll("\\s+", "");
                valid = (isNumberInLimits(relationship, 0, 0) == 0) ? false : edao.checkRemoveIds(traiPerCo, relationship);
                System.out.print((!valid && (isNumberInLimits(relationship, 0, 0) != 0)) ? Printer.red("Μη αποδεκτή τιμή ξαναδώσε (δες αν τα id είναι διαθέσιμα, η μορφή τους είναι σωστή και είναι μοναδικά) !\n") : "");
            } while (!valid && (isNumberInLimits(relationship, 0, 0) != 0));
            if (valid) {
                result = edao.deleteFromDB(traiPerCo, relationship);
                System.out.println(result ? Printer.green("Η διαγραφή της συσχέτισης trainersPerCourse έγινε επιτυχώς !") : Printer.red("Η διαγραφή της συσχέτισης trainersPerCourse δεν έγινε επιτυχώς !"));
            }
        }
    }

    private static void removeAssignmentsPerCourse() {
        //DIAGRAFH SYSXETISEWN assignmentsPerCourse
        boolean valid, result;
        String relationship;
        Printer.divider('=', 0);
        Printer.printEntitiesRealationshipAsIds(assiPerCo, edao.selectAllRelationshipAsIds(assiPerCo));
        Printer.printEntity(cour, cdao.selectAll());
        Printer.printEntity(assig, adao.selectAll());
        if (ActionsInterface.sizeOf(assiPerCo) > 0) {
            System.out.println(Printer.red("Δώσε την συσχέτιση που θέλεις να διαγράψεις."));
            do {
                System.out.println("Δώσε την συσχέτιση -> id_Μαθήματος (π.χ. 10) και τα id_Εργασιών(π.χ. 1-2-3) της μορφής (π.χ. 10->1-2-3) ή δώσε 0 για έξοδο από την διαγραφή:");
                relationship = scan.nextLine().trim().replaceAll("\\s+", "");
                valid = (isNumberInLimits(relationship, 0, 0) == 0) ? false : edao.checkRemoveIds(assiPerCo, relationship);
                System.out.print((!valid && (isNumberInLimits(relationship, 0, 0) != 0)) ? Printer.red("Μη αποδεκτή τιμή ξαναδώσε (δες αν τα id είναι διαθέσιμα, η μορφή τους είναι σωστή και είναι μοναδικά) !\n") : "");
            } while (!valid && (isNumberInLimits(relationship, 0, 0) != 0));
            if (valid) {
                result = edao.deleteFromDB(assiPerCo, relationship);
                System.out.println(result ? Printer.green("Η διαγραφή της συσχέτισης assignmentsPerCourse έγινε επιτυχώς !") : Printer.red("Η διαγραφή της συσχέτισης assignmentsPerCourse δεν έγινε επιτυχώς !"));
            }
        }
    }

    private static void removeAssignmentsPerStudent() {
        //DIAGRAFH SYSXETISEWN assignmentsPerStudent
        boolean valid, result;
        String relationship;
        Printer.divider('=', 0);
        Printer.printEntitiesRealationshipAsIds(assiPerSt, edao.selectAllRelationshipAsIds(assiPerSt));
        Printer.printEntity(stud, sdao.selectAll());
        Printer.printEntity(assig, adao.selectAll());
        if (ActionsInterface.sizeOf(assiPerSt) > 0) {
            System.out.println(Printer.red("Δώσε την συσχέτιση που θέλεις να διαγράψεις."));
            do {
                System.out.println("Δώσε την συσχέτιση -> id_Μαθητή (π.χ. 10) και τα id_Εργασιών(π.χ. 1-2-3) της μορφής (π.χ. 10->1-2-3) ή δώσε 0 για έξοδο από την διαγραφή:");
                relationship = scan.nextLine().trim().replaceAll("\\s+", "");
                valid = (isNumberInLimits(relationship, 0, 0) == 0) ? false : edao.checkRemoveIds(assiPerSt, relationship);
                System.out.print((!valid && (isNumberInLimits(relationship, 0, 0) != 0)) ? Printer.red("Μη αποδεκτή τιμή ξαναδώσε (δες αν τα id είναι διαθέσιμα, η μορφή τους είναι σωστή και είναι μοναδικά) !\n") : "");
            } while (!valid && (isNumberInLimits(relationship, 0, 0) != 0));
            if (valid) {
                result = edao.deleteFromDB(assiPerSt, relationship);
                System.out.println(result ? Printer.green("Η διαγραφή της συσχέτισης assignmentsPerStudent έγινε επιτυχώς !") : Printer.red("Η διαγραφή της συσχέτισης assignmentsPerStudent δεν έγινε επιτυχώς !"));
            }
        }
    }

    private static boolean isValidDateBirth(String date) {//elegxei thn morfh ths 
        boolean valid = true;
        LocalDate d;
        try {
            d = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            if ((LocalDate.now().getYear() - d.getYear() <= 18) || (LocalDate.now().getYear() - d.getYear() >= 60)) {//ilikia apo [18 . . . 60]
                valid = false;
                System.out.println(Printer.red("Η ηλικία πρέπει να είναι από 18 έως και 60 χρονών !"));
            }
        } catch (DateTimeParseException e) {
            valid = false;
        }
        return valid;
    }

    private static boolean isValidDateStart(String date) {
        boolean valid = true;
        LocalDate d;
        try {
            d = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            if (d.isBefore(LocalDate.now())) {
                System.out.println(Printer.red("Η ημερομηνία έναρξης δεν μπορεί να είναι πριν από την σημερινή !"));
                valid = false;
            }
        } catch (DateTimeParseException e) {
            valid = false;
        }
        return valid;
    }

    private static boolean isValidDate(String date) {
        boolean valid = true;
        try {
            LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        } catch (DateTimeParseException e) {
            valid = false;
        }
        return valid;
    }

    private static boolean isValidDateTime(String dateTime) {//elegxei thn morfh ths
        boolean valid = true;
        LocalDateTime d;
        try {
            d = LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
            if (d.isBefore(LocalDateTime.now())) {//den mporei h ypobolh na einai prin apo twra          
                valid = false;
                System.out.println(Printer.red("Δεν γίνεται η ημερομηνία υποβολής να είναι πιο πριν από την σημερινή !"));
            }
        } catch (DateTimeParseException e) {
            valid = false;
        }
        return valid;
    }

    private static boolean isEndDateAfterStart(String start_date, String end_date) {//elegxei an h end_date einai pio meta apo thn start_date
        boolean valid = true;
        LocalDate start = LocalDate.parse(start_date, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        LocalDate end = LocalDate.parse(end_date, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        if (end.isBefore(start) || end.isEqual(start)) {//an to end einai prin to start h' an einai idia
            valid = false;
        }
        return valid;
    }

    private static int isNumberInLimits(String number, int limitA, int limitB) {//elegxei an to number einai arithmos kai brisketai sto [limitA,limitB] an isxyei ton epistrefei alliws epistrefei -1
        int num;
        try {
            num = Integer.parseInt(number);
            if ((num < limitA) || (num > limitB)) {//exw apo ta limits
                num = -1;
            }
        } catch (NumberFormatException e) {//den einai arithmos
            num = -1;
        }
        return num;
    }

    public static int isNumberBiggerThanLimit(String number, int limitA) {//elegxei an to number einai arithmos kai einai megalyteros tou limitA alliws epistrefei -1
        int num;
        try {
            num = Integer.parseInt(number);
            if (num <= limitA) {//an einai katw apo to limitA
                num = -1;
            }
        } catch (NumberFormatException e) {//den einai arithmos
            num = -1;
        }
        return num;
    }
}
