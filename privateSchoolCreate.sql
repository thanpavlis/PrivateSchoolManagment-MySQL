use school;

drop table if exists assignmentsPerCourse,assignmentsPerStudent,trainersPerCourse,studentsPerCourse,courses,students,trainers,assignments;

/*table courses*/
create table courses(
	id int unsigned auto_increment not null,
    title varchar(100) not null,
    cstream varchar(100) not null,
    ctype varchar(100) not null,
    startDate date not null,
    endDate date not null,
    primary key(id)
);

/*table trainers*/
create table trainers(
	id int unsigned auto_increment not null,
    firstName varchar(100) not null,
    lastName varchar(100) not null,
    tsubject text not null, 
    primary key(id)
);

/*table students*/
create table students(
	id int unsigned auto_increment not null,
    firstName varchar(100) not null,
    lastName varchar(100) not null,
    dateOfBirth date not null,
    tuitionFees int unsigned not null,
    primary key (id)
);

/*table assignments*/
create table assignments(
	id int unsigned auto_increment not null,
    title varchar(100) not null,
    adescription text not null,
    subDateTime datetime not null,
    oralMark int unsigned not null,
    totalMark int unsigned not null,
    primary key(id)
);

/*table trainersPerCourse*/
create table trainersPerCourse(
	courseID int unsigned not null,
    trainerID int unsigned not null,
    primary key(courseID,trainerID),
    foreign key(courseID) references courses(id) on delete cascade,
    foreign key(trainerID) references trainers(id) on delete cascade
);

/*table studentsPerCourse*/
create table studentsPerCourse(
	courseID int unsigned not null,
    studentID int unsigned not null,
    primary key(courseID,studentID),
    foreign key (courseID) references courses(id) on delete cascade,
    foreign key (studentID) references students(id) on delete cascade
);

/*table assignmentsPerCourse*/
create table assignmentsPerCourse(
	courseID int unsigned not null,
    assignmentID int unsigned not null,
    primary key(courseID,assignmentID),
    foreign key (courseID) references courses(id) on delete cascade,
    foreign key (assignmentID) references assignments(id) on delete cascade
);

/*table assignmentsPerStudent*/
create table assignmentsPerStudent(
	studentID int unsigned not null,
    assignmentID int unsigned not null,
    primary key(studentID,assignmentID),
    foreign key(studentID) references students(id) on delete cascade,
    foreign key(assignmentID) references assignments(id) on delete cascade
);