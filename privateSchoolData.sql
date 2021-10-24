use school;

/*insert to courses*/
insert into courses(title,cstream,ctype,startDate,endDate) values
('CB13','Java','Part Time',str_to_date('15/02/2021','%d/%m/%Y'),str_to_date('30/08/2021','%d/%m/%Y')),
('CB13','C++','Part Time',str_to_date('15/02/2021','%d/%m/%Y'),str_to_date('15/07/2021','%d/%m/%Y')),
('CB13','C#','Full Time',str_to_date('15/03/2021','%d/%m/%Y'),str_to_date('30/09/2021','%d/%m/%Y')),
('CB13','Python','Full Time',str_to_date('15/06/2020','%d/%m/%Y'),str_to_date('30/12/2021','%d/%m/%Y')),
('CB12','Php','Part Time',str_to_date('15/06/2020','%d/%m/%Y'),str_to_date('30/12/2021','%d/%m/%Y')),
('CB12','Java','Full Time',str_to_date('15/02/2020','%d/%m/%Y'),str_to_date('30/12/2021','%d/%m/%Y'));

/*insert to trainers*/
insert into trainers(firstName,lastName,tsubject) values
('Giwrgos','Kalogyrou','Teaches only Java.'),
('Giannhs','Papadopoulos','Teaches only C#.'),
('Hara','Gkritsi','Teaches Sql and C++.'),
('Kwnstantina','Kopanari','Teaches HTML, CSS and JS.'),
('Dimitrhs','Nikoalou','Teaches Python and Php.');

/*insert to students*/
insert into students(firstName,lastName,dateOfBirth,tuitionFees) values
('Thodoris','Michalopoulos',str_to_date('17/06/1993','%d/%m/%Y'),'4000'),
('Nikos','Arxontos',str_to_date('15/08/1990','%d/%m/%Y'),'3000'),
('Giannhs','Marinakis',str_to_date('18/12/1992','%d/%m/%Y'),'2500'),
('Iwanna','Sanida',str_to_date('13/10/1981','%d/%m/%Y'),'2000'),
('Athina','Papadopoulou',str_to_date('05/03/2000','%d/%m/%Y'),'5000'),
('Gewrgia','Zotou',str_to_date('11/01/1985','%d/%m/%Y'),'1300'),
('Poph','Rhna',str_to_date('15/09/2002','%d/%m/%Y'),'4500');

/*insert to assignments*/
insert into assignments(title,adescription,subDateTime,oralMark,totalMark) values
('Sql','Design Part A',str_to_date('30/04/2021 15:30:00','%d/%m/%Y %H:%i:%s'),'15','85'),
('Sql','Web Design Part B',str_to_date('31/05/2021 23:59:00','%d/%m/%Y %H:%i:%s'),'15','85'),
('Java','Individual Project Part A',str_to_date('30/04/2021 22:00:00','%d/%m/%Y %H:%i:%s'),'0','100'),
('Java','Individual Project Part B',str_to_date('28/06/2021 20:00:00','%d/%m/%Y %H:%i:%s'),'30','70'),
('HTML_CSS_JS','Design Part A',str_to_date('31/03/2021 22:00:00','%d/%m/%Y %H:%i:%s'),'0','100'),
('HTML_CSS_JS','Design Part B',str_to_date('30/04/2021 23:59:00','%d/%m/%Y %H:%i:%s'),'50','50'),
('HTML_CSS_JS','Design Part C',str_to_date('31/05/2021 23:00:00','%d/%m/%Y %H:%i:%s'),'20','80'),
('HTML_CSS_JS','Design Total',str_to_date('30/06/2021 23:59:00','%d/%m/%Y %H:%i:%s'),'40','60'),
('Python','Web Design Project',str_to_date('30/06/2021 21:00:00','%d/%m/%Y %H:%i:%s'),'80','20'),
('Php','Web Design Project',str_to_date('30/06/2021 23:59:00','%d/%m/%Y %H:%i:%s'),'70','30'),
('C#','Web Design Project Part A',str_to_date('30/07/2021 23:59:00','%d/%m/%Y %H:%i:%s'),'10','90'),
('C#','Web Design Project Part B',str_to_date('30/09/2021 23:59:00','%d/%m/%Y %H:%i:%s'),'45','55');

/*insert to trainersPerCourse*/
insert into trainersPerCourse(courseID,trainerID) values
(1,1),(1,3),(1,4),
(2,3),
(3,2),(3,3),(3,4),
(4,3),(4,4),(4,5),
(5,3),(5,4),(5,5),
(6,1),(6,3),(6,4);

/*insert to studentsPerCourse*/
insert into studentsPerCourse(courseID,studentID) values
(1,1),(1,5),
(3,2),(3,4),
(4,6),(4,7),
(5,1),(5,5),
(6,3);

/*insert to assignmentsPerCourse*/
insert into assignmentsPerCourse(courseID,assignmentID) values
(1,1),(1,2),(1,3),(1,4),(1,5),(1,6),(1,7),(1,8),
(2,1),(2,2),(2,11),
(3,1),(3,2),(3,5),(3,6),(3,7),(3,8),(3,11),(3,12),
(4,1),(4,2),(4,5),(4,6),(4,7),(4,8),(4,9),
(5,1),(5,2),(5,5),(5,6),(5,7),(5,8),(5,10),
(6,1),(6,2),(6,3),(6,4),(6,5),(6,6),(6,7),(6,8);

/*insert to assignmentsPerStudent*/
insert into assignmentsPerStudent(studentID,assignmentID) values
(1,1),(1,2),(1,3),(1,4),(1,5),(1,10),
(2,1),(2,2),(2,5),(2,11),
(3,1),(3,2),(3,3),(3,4),(3,5),(3,6),(3,7),(3,8),
(4,1),(4,2),(4,5),(4,6),(4,11),
(5,1),(5,2),(5,3),(5,4),(5,5),(5,10),
(6,1),(6,5),(6,6),(6,9),
(7,1),(7,5),(7,6),(7,9);