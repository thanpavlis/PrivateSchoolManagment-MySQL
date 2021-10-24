package entities.io;

import entities.*;
import entities.dao.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import privateschoolmysql.Constants;

/*thn ebala abstract gia na mhn mporei na ftiaxtei antikeimeno ths*/
public abstract class Printer extends Constants {

    private static int[] coursesWidths = null;
    private static int[] trainersWidths = null;
    private static int[] studentsWidths = null;
    private static int[] assignmentsWidths = null;

    /*arxikopoiei ta widths (thelw kathe attribute na exei to max length)*/
    public static void initializeWidths() {
        coursesWidths = ActionsInterface.maxWidthsOf(cour);
        trainersWidths = ActionsInterface.maxWidthsOf(trai);
        studentsWidths = ActionsInterface.maxWidthsOf(stud);
        assignmentsWidths = ActionsInterface.maxWidthsOf(assig);
    }

    /*einai to width gia thn ektypwsh kathe sthlhs analoga poios thn kalese*/
    public static int[] printWidths(Class c) {
        if (c == Course.class) {//an klithike apo thn klash Course
            return coursesWidths;
        } else if (c == Trainer.class) {//an klithike apo thn klash Trainer
            return trainersWidths;
        } else if (c == Student.class) {//an klithike apo thn klash Student
            return studentsWidths;
        } else {//an klithike apo thn klash Assignment   
            return assignmentsWidths;
        }
    }

    /*to header fitarei analga me to poios thn kalese*/
    public static String header(Class c) {
        int width[] = printWidths(c);
        String line;
        if (c == Course.class) {
            line = "|%1$-" + width[0] + "s|%2$-" + width[1] + "s|%3$-" + width[2] + "s|%4$-" + width[3] + "s|%5$-" + width[4] + "s|%6$-" + width[5] + "s|";
        } else if (c == Trainer.class) {
            line = "|%1$-" + width[0] + "s|%2$-" + width[1] + "s|%3$-" + width[2] + "s|%4$-" + width[3] + "s|";
        } else if (c == Student.class) {
            line = "|%1$-" + width[0] + "s|%2$-" + width[1] + "s|%3$-" + width[2] + "s|%4$-" + width[3] + "s|%5$-" + width[4] + "s|";
        } else {//einai gia to Assignment
            line = "|%1$-" + width[0] + "s|%2$-" + width[1] + "s|%3$-" + width[2] + "s|%4$-" + width[3] + "s|%5$-" + width[4] + "s|%6$-" + width[5] + "s|";
        }
        return line;
    }

    /*epistrfei tis diakekommenes grammes tou pinaka analoga me ta widths*/
    public static String arrayDividers(Class c) {
        int width[] = printWidths(c);//ta width epistrefontai analoga me to poia klash kalese thn printWidths
        String outlines[] = new String[width.length];
        String line;
        for (int i = 0; i < outlines.length; i++) {//ftiaxnei tis paules ------- gia kathe sthlh analoga me ta widths
            char[] chars = new char[width[i]];
            Arrays.fill(chars, '-');
            outlines[i] = new String(chars);
        }
        if (c == Course.class) {
            line = String.format(header(c), outlines[0], outlines[1], outlines[2], outlines[3], outlines[4], outlines[5]);
        } else if (c == Trainer.class) {
            line = String.format(header(c), outlines[0], outlines[1], outlines[2], outlines[3]);
        } else if (c == Student.class) {
            line = String.format(header(c), outlines[0], outlines[1], outlines[2], outlines[3], outlines[4]);
        } else {//einai gia to Assignment
            line = String.format(header(c), outlines[0], outlines[1], outlines[2], outlines[3], outlines[4], outlines[5]);
        }
        return line;
    }

    /*einai ta diaxwristika prints, ta xrhsimopoiw gia thn consola*/
    public static void divider(char c, int size) {
        String divider;
        int width = (size == 0) ? 200 : size;//an size=0 default timh 200
        char[] chars = new char[width];
        Arrays.fill(chars, c);
        divider = new String(chars);
        System.out.println(divider);
    }

    /*ektypwnei tis kyries ontothtes*/
    public static <T> void printEntity(String label, ArrayList<T> entities) {
        if (entities.size() > 0) {
            if (entities.get(0) instanceof Student) {//an h lista exei objects tipou student
                System.out.println(Input.getWriteToFile() ? "Λίστα Μαθητών:" : red("Λίστα Μαθητών:"));
                String dividers = arrayDividers(Student.class
                );//einai oi diakekommenes tou pinaka ------------- epistrefontai analoga me to poia klash thn kalei 
                System.out.println(dividers);
                System.out.println(String.format(header(Student.class), "Id", "FirstName", "LastName", "DateOfBirth", "TuitionFees"));
                System.out.println(dividers);
                for (int i = 0; i < entities.size(); i++) {
                    System.out.println(((Student) entities.get(i)).tableFormat());
                    System.out.println(dividers);
                }
            } else if (entities.get(0) instanceof Trainer) {//an h lista exei objects tipou trainer
                System.out.println(Input.getWriteToFile() ? "Λίστα Εκπαιδευτών:" : red("Λίστα Εκπαιδευτών:"));
                String dividers = arrayDividers(Trainer.class
                );//einai oi diakekommenes tou pinaka ------------- epistrefontai analoga me to poia klash thn kalei 
                System.out.println(dividers);
                System.out.println(String.format(header(Trainer.class), "Id", "FirstName", "LastName", "Subject"));
                System.out.println(dividers);
                for (int i = 0; i < entities.size(); i++) {
                    System.out.println(((Trainer) entities.get(i)).tableFormat());
                    System.out.println(dividers);
                }
            } else if (entities.get(0) instanceof Course) {//an h lista exei objects tipou course
                System.out.println(Input.getWriteToFile() ? "Λίστα Μαθημάτων:" : red("Λίστα Μαθημάτων:"));
                String dividers = arrayDividers(Course.class);//einai oi diakekommenes tou pinaka -------------
                System.out.println(dividers);
                System.out.println(String.format(header(Course.class), "Id", "Title", "Stream", "Type", "Start_Date", "End_Date"));
                System.out.println(dividers);
                for (int i = 0; i < entities.size(); i++) {
                    System.out.println(((Course) entities.get(i)).tableFormat());
                    System.out.println(dividers);
                }
            } else {////an h lista exei objects tipou assignment
                System.out.println(Input.getWriteToFile() ? "Λίστα Εργασιών:" : red("Λίστα Εργασιών:"));
                String dividers = arrayDividers(Assignment.class
                );//einai oi diakekommenes tou pinaka ------------- epistrefontai analoga me to poia klash thn kalei 
                System.out.println(dividers);
                System.out.println(String.format(header(Assignment.class), "Id", "Title", "Description", "SubDateTime", "OralMark", "TotalMark"));
                System.out.println(dividers);
                for (int i = 0; i < entities.size(); i++) {
                    System.out.println(((Assignment) entities.get(i)).tableFormat());
                    System.out.println(dividers);
                }
            }
        } else {
            if (label.equals(stud)) {
                echoNotFound(stud);
            } else if (label.equals(trai)) {
                echoNotFound(trai);
            } else if (label.equals(cour)) {
                echoNotFound(cour);
            } else {
                echoNotFound(assig);
            }
        }
    }

    /*ektypwnei to antistoixo minima se periptwsh pou kapoios pinakas einai adeios*/
    public static void echoNotFound(String label) {
        boolean writeToFile = Input.getWriteToFile();
        if (label.equals(stud)) {
            System.out.println(writeToFile ? "Δεν υπάρχουν μαθητές !" : Printer.red("Δεν υπάρχουν μαθητές !"));
        } else if (label.equals(trai)) {
            System.out.println(writeToFile ? "Δεν υπάρχουν εκπαιδευτές !" : Printer.red("Δεν υπάρχουν εκπαιδευτές !"));
        } else if (label.equals(cour)) {
            System.out.println(writeToFile ? "Δεν υπάρχουν μαθήματα !" : Printer.red("Δεν υπάρχουν μαθήματα !"));
        } else if (label.equals(assig)) {
            System.out.println(writeToFile ? "Δεν υπάρχουν εργασίες !" : Printer.red("Δεν υπάρχουν εργασίες !"));
        } else if (label.equals(studPerCo)) {
            System.out.println(writeToFile ? "Δεν υπάρχουν συσχετίσεις studentsPerCourse !" : Printer.red("Δεν υπάρχουν συσχετίσεις studentsPerCourse !"));
        } else if (label.equals(traiPerCo)) {
            System.out.println(writeToFile ? "Δεν υπάρχουν συσχετίσεις trainersPerCourse !" : Printer.red("Δεν υπάρχουν συσχετίσεις trainersPerCourse !"));
        } else if (label.equals(assiPerCo)) {
            System.out.println(writeToFile ? "Δεν υπάρχουν συσχετίσεις assignmentsPerCourse !" : Printer.red("Δεν υπάρχουν συσχετίσεις assignmentsPerCourse !"));
        } else {
            System.out.println(writeToFile ? "Δεν υπάρχουν συσχετίσεις assignmentsPerStudent !" : Printer.red("Δεν υπάρχουν συσχετίσεις assignmentsPerStudent !"));
        }
    }

    /*ektypwnei tis sysxetiseis se morfh ids*/
    public static void printEntitiesRealationshipAsIds(String label, ArrayList<EntitiesRelationship> eRArray) {
        switch (label) {
            case studPerCo:
                if (eRArray.size() > 0) {
                    System.out.println(Input.getWriteToFile() ? "Οι συσχετίσεις studentsPerCourse σε μορφή id είναι:" : red("Οι συσχετίσεις studentsPerCourse σε μορφή id είναι:"));
                    divider('-', 50);
                    for (EntitiesRelationship e : eRArray) {
                        System.out.println(e);
                    }
                } else {
                    echoNotFound(studPerCo);
                }
                break;
            case traiPerCo:
                if (eRArray.size() > 0) {
                    System.out.println(Input.getWriteToFile() ? "Οι συσχετίσεις trainersPerCourse σε μορφή id είναι:" : red("Οι συσχετίσεις trainersPerCourse σε μορφή id είναι:"));
                    divider('-', 50);
                    for (EntitiesRelationship e : eRArray) {
                        System.out.println(e);
                    }
                } else {
                    echoNotFound(traiPerCo);
                }
                break;
            case assiPerCo:
                if (eRArray.size() > 0) {
                    System.out.println(Input.getWriteToFile() ? "Οι συσχετίσεις assignmentsPerCourse σε μορφή id είναι:" : red("Οι συσχετίσεις assignmentsPerCourse σε μορφή id είναι:"));
                    divider('-', 53);
                    for (EntitiesRelationship e : eRArray) {
                        System.out.println(e);
                    }
                } else {
                    echoNotFound(assiPerCo);
                }
                break;
            default://assignmentsPerStudent
                if (eRArray.size() > 0) {
                    System.out.println(Input.getWriteToFile() ? "Οι συσχετίσεις assignmentsPerStudent σε μορφή id είναι:" : red("Οι συσχετίσεις assignmentsPerStudent σε μορφή id είναι:"));
                    divider('-', 54);
                    for (EntitiesRelationship e : eRArray) {
                        System.out.println(e);
                    }
                } else {
                    echoNotFound(assiPerSt);
                }
        }
    }

    /*ektypwnei tis sysxetiseis se anagnwsimh morfh*/
    public static <U, V> void printEntitiesRealationshipAsReadable(String label, ArrayList<EntitiesPairing<U, V>> join) {
        int tempId = 0;
        switch (label) {
            case studPerCo://studentsPerCourse
                System.out.println(Input.getWriteToFile() ? "Οι συσχετίσεις studentsPerCourse είναι:" : red("Οι συσχετίσεις studentsPerCourse είναι:"));
                for (EntitiesPairing<U, V> reference : join) {
                    if (tempId != ((Course) (reference.mainEntity)).getId()) {//ennow diaforetiko mathima
                        tempId = ((Course) (reference.mainEntity)).getId();
                        printRelationshipsTableHeader("oneCourse");//header tou course
                        System.out.println(((Course) (reference.mainEntity)).tableFormat());
                        System.out.println(arrayDividers(Course.class
                        ));//kleisimo tou pinaka
                        printRelationshipsTableHeader("manyStudent");//header twn Students pou prakolouthoun to mathima
                    }
                    System.out.println(((Student) (reference.refEntity)).tableFormat());
                    System.out.println(arrayDividers(Student.class
                    ));
                }
                break;
            case traiPerCo://trainersPerCourse
                System.out.println(Input.getWriteToFile() ? "Οι συσχετίσεις trainersPerCourse είναι:" : red("Οι συσχετίσεις trainersPerCourse είναι:"));
                for (EntitiesPairing<U, V> reference : join) {
                    if (tempId != ((Course) (reference.mainEntity)).getId()) {
                        tempId = ((Course) (reference.mainEntity)).getId();
                        printRelationshipsTableHeader("oneCourse");
                        System.out.println(((Course) (reference.mainEntity)).tableFormat());
                        System.out.println(arrayDividers(Course.class
                        ));
                        printRelationshipsTableHeader("manyTrainer");
                    }
                    System.out.println(((Trainer) (reference.refEntity)).tableFormat());
                    System.out.println(arrayDividers(Trainer.class
                    ));
                }
                break;
            case assiPerCo://assignmentsPerCourse
                System.out.println(Input.getWriteToFile() ? "Οι συσχετίσεις assignmentsPerCourse είναι:" : red("Οι συσχετίσεις assignmentsPerCourse είναι:"));
                for (EntitiesPairing<U, V> reference : join) {
                    if (tempId != ((Course) (reference.mainEntity)).getId()) {
                        tempId = ((Course) (reference.mainEntity)).getId();
                        printRelationshipsTableHeader("oneCourse");
                        System.out.println(((Course) (reference.mainEntity)).tableFormat());
                        System.out.println(arrayDividers(Course.class
                        ));
                        printRelationshipsTableHeader("manyAssignment");
                    }
                    System.out.println(((Assignment) (reference.refEntity)).tableFormat());
                    System.out.println(arrayDividers(Assignment.class
                    ));
                }
                break;
            default://assignmentsPerStudent
                System.out.println(Input.getWriteToFile() ? "Οι συσχετίσεις studentsPerCourse είναι:" : red("Οι συσχετίσεις studentsPerCourse είναι:"));
                for (EntitiesPairing<U, V> reference : join) {
                    if (tempId != ((Student) (reference.mainEntity)).getId()) {
                        tempId = ((Student) (reference.mainEntity)).getId();
                        printRelationshipsTableHeader("oneStudent");
                        System.out.println(((Student) (reference.mainEntity)).tableFormat());
                        System.out.println(arrayDividers(Student.class
                        ));
                        printRelationshipsTableHeader("manyAssignment");
                    }
                    System.out.println(((Assignment) (reference.refEntity)).tableFormat());
                    System.out.println(arrayDividers(Assignment.class
                    ));
                }
        }
    }

    /*ektypwnei to header kathe pinaka, mazi me ena minima, wste na ektypwsw tis sysxetiseis se anagnwsimh morfh*/
    public static void printRelationshipsTableHeader(String label) {
        String dividers;
        switch (label) {
            case "oneCourse":
                divider('=', 0);
                System.out.println(Input.getWriteToFile() ? "Το μάθημα:" : red("Το μάθημα:"));
                dividers
                        = arrayDividers(Course.class
                        );//einai oi diakekommenes tou pinaka -------------
                System.out.println(dividers);
                System.out.println(String.format(header(Course.class
                ), "Id", "Title", "Stream", "Type", "Start_Date", "End_Date"));
                System.out.println(dividers);
                break;
            case "manyStudent":
                System.out.println(Input.getWriteToFile() ? "Παρακολουθούν οι εξής μαθητές:" : red("Παρακολουθούν οι εξής μαθητές:"));
                dividers
                        = arrayDividers(Student.class
                        );//einai oi diakekommenes tou pinaka ------------- epistrefontai analoga me to poia klash thn kalei 
                System.out.println(dividers);
                System.out.println(String.format(header(Student.class
                ), "Id", "FirstName", "LastName", "DateOfBirth", "TuitionFees"));
                System.out.println(dividers);
                break;
            case "manyTrainer":
                System.out.println(Input.getWriteToFile() ? "Διδάσκουν οι εξής εκπαιδευτές:" : red("Διδάσκουν οι εξής εκπαιδευτές:"));
                dividers
                        = arrayDividers(Trainer.class
                        );//einai oi diakekommenes tou pinaka ------------- epistrefontai analoga me to poia klash thn kalei 
                System.out.println(dividers);
                System.out.println(String.format(header(Trainer.class
                ), "Id", "FirstName", "LastName", "Subject"));
                System.out.println(dividers);
                break;
            case "manyAssignment":
                System.out.println(Input.getWriteToFile() ? "Έχει τις εξής εργασίες:" : red("Έχει τις εξής εργασίες:"));
                dividers
                        = arrayDividers(Assignment.class
                        );//einai oi diakekommenes tou pinaka ------------- epistrefontai analoga me to poia klash thn kalei 
                System.out.println(dividers);
                System.out.println(String.format(header(Assignment.class
                ), "Id", "Title", "Description", "SubDateTime", "OralMark", "TotalMark"));
                System.out.println(dividers);
                break;
            case "oneStudent"://oneStudent
                divider('=', 0);
                System.out.println(Input.getWriteToFile() ? "Ο μαθητής:" : red("Ο μαθητής:"));
                dividers
                        = arrayDividers(Student.class
                        );//einai oi diakekommenes tou pinaka ------------- epistrefontai analoga me to poia klash thn kalei 
                System.out.println(dividers);
                System.out.println(String.format(header(Student.class
                ), "Id", "FirstName", "LastName", "DateOfBirth", "TuitionFees"));
                System.out.println(dividers);
                break;
            case "studentsToMoreThanOneCourse":
                divider('=', 0);
                System.out.println(red("Οι μαθητές που ανήκουν σε περισσότερα από ένα μαθήματα είναι:"));
                dividers
                        = arrayDividers(Student.class
                        );//einai oi diakekommenes tou pinaka ------------- epistrefontai analoga me to poia klash thn kalei 
                System.out.println(dividers);
                System.out.println(String.format(header(Student.class
                ), "Id", "FirstName", "LastName", "DateOfBirth", "TuitionFees"));
                System.out.println(dividers);
                break;
            default://einai to minima gia ta assignments ths sygkekrimenhs ebdomadas (stelnw tis hmeromhnies xwrismenes me ,)
                String[] dates = label.split(",");
                System.out.println(red("Έχει να παραδώσει στο διάστημα από " + dates[0] + " έως και " + dates[1] + " τις εξής εργασίες:"));
                dividers
                        = arrayDividers(Assignment.class
                        );//einai oi diakekommenes tou pinaka ------------- epistrefontai analoga me to poia klash thn kalei 
                System.out.println(dividers);
                System.out.println(String.format(header(Assignment.class
                ), "Id", "Title", "Description", "SubDateTime", "OralMark", "TotalMark"));
                System.out.println(dividers);
        }
    }

    /*kanei export to stigmiotypo ths bashs, se arxeio ths morfhs (PrivateSchoolDB_dd-MM-yyyy_HH_mm_ss.txt) 
      kai to dhmiourgei mesa ston fakelo PrivateSchoolDBExports, mesa sto fakelo tou project*/
    public static void exportReadableDB() {
        File f;
        LocalDateTime date;
        PrintStream out, console = System.out;
        String day, month, year, hour, minute, second, fileName, folder, now;
        String[] relations = {studPerCo, traiPerCo, assiPerCo, assiPerSt};
        CourseDao cdao = new CourseDao();
        StudentDao sdao = new StudentDao();
        TrainerDao tdao = new TrainerDao();
        AssignmentDao adao = new AssignmentDao();
        EntitiesRelationshipDao edao = new EntitiesRelationshipDao();
        try {
            now = LocalDateTime.now().toString();
            folder = "PrivateSchoolDBExports";
            Input.setWriteToFile(true);//thn xrisimopoiw wste na xerw pote thelw na grapsw se arxeio gia na vgalw to xrwma apo tis kefalides
            day = now.substring(8, 10);//diaxwrizw mera, mhna, xrono, wra, lepta, deuterolepta
            month = now.substring(5, 7);
            year = now.substring(0, 4);
            hour = now.substring(11, 13);
            minute = now.substring(14, 16);
            second = now.substring(17, 19);
            now = day + "-" + month + "-" + year + "_" + hour + "_" + minute + "_" + second;
            date = LocalDateTime.parse(now, DateTimeFormatter.ofPattern("dd-MM-yyyy_HH_mm_ss"));
            fileName = "PrivateSchoolDB_" + DateTimeFormatter.ofPattern("dd-MM-yyyy_HH_mm_ss").format(date) + ".txt";
            f = new File(folder);//ftiaxnw ton fakelo PrivateSchoolDBExports ston opoio tha apothikeuontai ta stigmiotypa ths bashs
            if (!f.exists()) {
                f.mkdir();
            }
            out = new PrintStream(new File(folder + "\\" + fileName));
            System.setOut(out);//pleon h roh einai pros to arxeio
            //ektypwsh main entities
            divider('*', 0);
            printEntity(cour, cdao.selectAll());
            divider('*', 0);
            printEntity(stud, sdao.selectAll());
            divider('*', 0);
            printEntity(trai, tdao.selectAll());
            divider('*', 0);
            printEntity(assig, adao.selectAll());
            //ektypwsh relationships
            for (String s : relations) {
                divider('*', 0);
                if (ActionsInterface.sizeOf(s) > 0) {
                    printEntitiesRealationshipAsIds(s, edao.selectAllRelationshipAsIds(s));
                    printEntitiesRealationshipAsReadable(s, edao.selectAllAsJoin(s));
                } else {
                    System.out.println(Input.getWriteToFile() ? "Δεν υπάρχουν συσχετίσεις " + s + " !" : Printer.red("Δεν υπάρχουν συσχετίσεις " + s + " !"));
                }
            }
            divider('*', 0);
            System.setOut(console);//afou grapsw to arxeio h roh xanagyrizei sthn konsola     
            Input.setWriteToFile(false);
            System.out.println(Printer.green("Το στιγμιότυπο της βάσης δεδομένων έγινε επιτυχώς export στο αρχείο : " + Printer.red(fileName)));
        } catch (FileNotFoundException ex) {
            System.out.println(Printer.red("Μη αποδεκτό όνομα αρχείου !"));
        }
    }

    /*epistrefei ena string me ton kwdiko gia kokkino xrwma*/
    public static String red(String s) {
        return "\u001B[31m" + s + "\u001B[0m";
    }

    /*epistrefei ena string me ton kwdiko gia prasino xrwma*/
    public static String green(String s) {
        return "\u001B[32m" + s + "\u001B[0m";
    }
}
