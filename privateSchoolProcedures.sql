use school;
/*------------------------------------------------------------------------------------------------------------------------------------------------------*/
/*all select procedure*/
drop procedure if exists allSelect;
delimiter $
create procedure allSelect(in label varchar(50))
begin
	if (label='courses') then
		select id,title,cstream,ctype, 
			   date_format(startDate,'%d/%m/%Y') as startDate,
			   date_format(endDate,'%d/%m/%Y') as endDate 
		from courses;
	elseif (label='students') then
		select id, firstName,lastName,date_format(dateOfBirth,'%d/%m/%Y') as dateOfBirth,tuitionFees 
		from students;
	elseif (label='trainers') then
		select * from trainers;
	elseif (label='assignments') then
		select id,title,adescription,date_format(subDateTime,'%d/%m/%Y %H:%i:%s') as subDateTime,oralMark,totalMark 
		from assignments;
	elseif (label='studentsPerCourse') then
		select co.id as courseId,co.title,co.cstream,co.ctype,
			   date_format(co.startDate,'%d/%m/%Y') as startDate,
			   date_format(co.endDate,'%d/%m/%Y') as endDate,
			   st.id as studentId,st.firstName,st.lastName,date_format(st.dateOfBirth,'%d/%m/%Y') as dateOfBirth,
			   st.tuitionFees from courses co
		join studentsPerCourse sPC on co.id=sPC.courseID
		join students st on st.id=sPC.studentID
		order by co.id asc;
	elseif (label='trainersPerCourse') then
		select co.id as courseId,co.title,co.cstream,co.ctype,
			   date_format(startDate,'%d/%m/%Y') as startDate,
			   date_format(endDate,'%d/%m/%Y') as endDate,
			   tr.id as trainerId,tr.firstName,tr.lastName,tr.tsubject 
		from courses co
		join trainersPerCourse tPC on co.id=tPC.courseID
		join trainers tr on tr.id=tPC.trainerID
		order by co.id asc;
	elseif (label='assignmentsPerCourse') then
		select co.id as courseId,co.title,co.cstream,co.ctype,
			   date_format(startDate,'%d/%m/%Y') as startDate,
			   date_format(endDate,'%d/%m/%Y') as endDate,
			   assi.id as assignmentId,assi.title,assi.adescription,
			   date_format(assi.subDateTime,'%d/%m/%Y %H:%i:%s') as subDateTime,
			   assi.oralMark,assi.totalMark
		from courses co
		join assignmentsPerCourse aPC on co.id=aPC.courseID
		join assignments assi on assi.id=aPC.assignmentID
		order by co.id asc;
	elseif (label='assignmentsPerStudent') then
		select st.id as studentId,st.firstName,st.lastName,date_format(st.dateOfBirth,'%d/%m/%Y') as dateOfBirth,
			   st.tuitionFees,assi.id as assignmentId,assi.title,assi.adescription,
			   date_format(assi.subDateTime,'%d/%m/%Y %H:%i:%s') as subDateTime,assi.oralMark,assi.totalMark
		from students st
		join assignmentsPerStudent aPS on st.id=aPS.studentID
		join assignments assi on assi.id=aPS.assignmentID
		order by st.id asc;
	elseif (label='studentsToMoreCourses') then
		select st.id,st.firstName,st.lastName,date_format(st.dateOfBirth,'%d/%m/%Y') as dateOfBirth,
			   st.tuitionFees 
		from students st
		join studentsPerCourse sPC on st.id=sPC.studentID
		group by sPC.studentID
		having count(*)>1
		order by st.id asc;	
    end if;
end$
delimiter ;
/*------------------------------------------------------------------------------------------------------------------------------------------------------*/
/*all assignments per student must submit in period(startPeriod,endPeriod)*/
drop procedure if exists assignmentsPerStudentInPeriod;
delimiter $
create procedure assignmentsPerStudentInPeriod(in startPeriod date,in endPeriod date)
begin
	select st.id as studentId,firstName,lastName,date_format(dateOfBirth,'%d/%m/%Y') as dateOfBirth,
		   tuitionFees,assi.id as assignmentId,assi.title,adescription,
           date_format(subDateTime,'%d/%m/%Y %H:%i:%s') as subDateTime,oralMark,totalMark
	from students st
	join assignmentsPerStudent aPS on st.id=aPS.studentID
	join assignments assi on assi.id=aPS.assignmentID
	where (date(subDateTime)>=startPeriod
    and date(subDateTime)<=endPeriod)
    order by st.id asc;
end $
delimiter ;
/*------------------------------------------------------------------------------------------------------------------------------------------------------*/
drop procedure if exists sizeOfAssigPerStudInPeriod;
delimiter $
create procedure sizeOfAssigPerStudInPeriod(in startPeriod date,in endPeriod date)
begin 
	select count(*) as size
	from students st
	join assignmentsPerStudent aPS on st.id=aPS.studentID
	join assignments assi on assi.id=aPS.assignmentID
	where (date(subDateTime)>=startPeriod
	and date(subDateTime)<=endPeriod); 
end$
delimiter ;
/*------------------------------------------------------------------------------------------------------------------------------------------------------*/
/*arrays size procedure*/
drop procedure if exists arraySize;
delimiter $
create procedure arraySize(in arName varchar(50))
begin 
	if (arName='courses') then
		select count(*) as size from courses;
	elseif (arName='students') then
		select count(*) as size from students;
	elseif (arName='trainers') then
		select count(*) as size from trainers;
	elseif (arName='assignments') then
		select count(*) as size from assignments;
	elseif (arName='studentsPerCourse') then
		select count(*) as size from studentsPerCourse;
	elseif (arName='trainersPerCourse') then
		select count(*) as size from trainersPerCourse;
	elseif (arName='assignmentsPerCourse') then
		select count(*) as size from assignmentsPerCourse;
	elseif (arName='assignmentsPerStudent') then
		select count(*) as size from assignmentsPerStudent;
	elseif (arName='studentsToMoreCourses') then
		select count(*) as size from (
		select *
		from students st
		join studentsPerCourse sPC on st.id=sPC.studentID
		group by sPC.studentID
		having count(*)>1 ) subquery;
    end if;
end$
delimiter ;
/*------------------------------------------------------------------------------------------------------------------------------------------------------*/
/*insertToStudents procedure*/
drop procedure if exists insertToStudents;
delimiter $
create procedure insertToStudents(in firstName varchar(100),in lastName varchar(100),in dateOfBirth date,in tuitionFees int)
begin 
	insert into students(firstName,lastName,dateOfBirth,tuitionFees) values (firstName,lastName,dateOfBirth,tuitionFees);
end$
delimiter ;
/*------------------------------------------------------------------------------------------------------------------------------------------------------*/
/*insertToTrainers procedure*/
drop procedure if exists insertToTrainers;
delimiter $
create procedure insertToTrainers(in firstName varchar(100),in lastName varchar(100),in tsubject text)
begin 
	insert into trainers(firstName,lastName,tsubject) values (firstName,lastName,tsubject);
end$
delimiter ;
/*------------------------------------------------------------------------------------------------------------------------------------------------------*/
/*insertToCourses procedure*/
drop procedure if exists insertToCourses;
delimiter $
create procedure insertToCourses(in title varchar(100),in cstream varchar(100),in ctype varchar(100),in startDate date,in endDate date)
begin 
	insert into courses (title,cstream,ctype,startDate,endDate) values (title,cstream,ctype,startDate,endDate);
end$
delimiter ;
/*------------------------------------------------------------------------------------------------------------------------------------------------------*/
/*insertToAssignments procedure*/
drop procedure if exists insertToAssignments;
delimiter $
create procedure insertToAssignments(in title varchar(100),in adescription text,in subDateTime datetime,in oralMark int,in totalMark int)
begin 
	insert into assignments (title,adescription,subDateTime,oralMark,totalMark) values (title,adescription,subDateTime,oralMark,totalMark);
end$
delimiter ;
/*------------------------------------------------------------------------------------------------------------------------------------------------------*/
/*entitiesRelationship procedure se morfh ids*/
drop procedure if exists entitiesRelationship;
delimiter $
create procedure entitiesRelationship(in label varchar(100))
begin 	
	if (label='studentsPerCourse') then
		select * from studentsPerCourse order by courseID asc;
    elseif (label='trainersPerCourse') then
		select * from trainersPerCourse order by courseID asc;
    elseif (label='assignmentsPerCourse') then
		select * from assignmentsPerCourse order by courseID asc;
    elseif (label='assignmentsPerStudent') then
		select * from assignmentsPerStudent order by studentID asc;
    end if;
end$
delimiter ;
/*------------------------------------------------------------------------------------------------------------------------------------------------------*/
/*mainEntitiesIds procedure*/
drop procedure if exists mainEntitiesIds;
delimiter $
create procedure mainEntitiesIds(in label varchar(100))
begin 
	if (label='students') then
		select id from students;
	elseif (label='courses') then
		select id from courses;
	elseif (label='trainers') then
		select id from trainers;
	elseif (label='assignments') then
		select id from assignments;
    end if;
end$
delimiter ;
/*------------------------------------------------------------------------------------------------------------------------------------------------------*/
/*findById procedure*/
drop procedure if exists findById;
delimiter $
create procedure findById(in label varchar(30),in id int)
begin 
	if (label='courses') then
		select id,title,cstream,ctype, 
			   date_format(startDate,'%d/%m/%Y') as startDate,
			   date_format(endDate,'%d/%m/%Y') as endDate 
		from courses
        where courses.id=id;
	elseif (label='students') then
		select id, firstName,lastName,date_format(dateOfBirth,'%d/%m/%Y') as dateOfBirth,tuitionFees 
		from students
        where students.id=id;
	elseif (label='trainers') then
		select * from trainers
        where trainers.id=id;
	elseif (label='assignments') then
		select id,title,adescription,date_format(subDateTime,'%d/%m/%Y %H:%i:%s') as subDateTime,oralMark,totalMark 
		from assignments
        where assignments.id=id;
    end if;
end$
delimiter ;
/*------------------------------------------------------------------------------------------------------------------------------------------------------*/
/*deleteMainEntityById procedure*/
drop procedure if exists deleteMainEntityById;
delimiter $
create procedure deleteMainEntityById(in label varchar(30),in id int)
begin 
	if (label='courses') then
		delete from courses where courses.id=id;
	elseif (label='students') then
		delete from students where students.id=id;
	elseif (label='trainers') then
		delete from trainers where trainers.id=id;
	elseif (label='assignments') then
		delete from assignments where assignments.id=id;
    end if;
end$
delimiter ;
/*------------------------------------------------------------------------------------------------------------------------------------------------------*/
/*insertEntitiesRelationship procedure pairnei ws parametros to entityID, ta redIds pou tha deixei kai to label gia na xerw se poion pinaka tha kanw insert */
drop procedure if exists insertEntitiesRelationship;
delimiter $
create procedure insertEntitiesRelationship(in entityID int,in refIds varchar(255),in label varchar(100))
begin 
	declare indx int;
    declare id int;
	declare part varchar(255); 
	set indx = 1;
	SplitLoop: while indx != 0 do
	set indx = LOCATE("-",refIds); #thesh tou prwtou '-'
	if indx !=0 then #apothikeuw apo thn arxh mexri to prwto '-' sto part
		set part = LEFT(refIds,indx - 1);
	else #an einai 0 dhladh den exw poia xarakthra '-' pairnw to ipoloipo refIds5
		set part = refIds;
	end if;
	set id = cast(part as unsigned); #kanw casting to id se integer
    if (label='studentsPerCourse') then
		insert into studentsPerCourse(courseID,studentID) values (entityID,id);
    elseif (label='trainersPerCourse') then
		insert into trainersPerCourse(courseID,trainerID) values (entityID,id);
    elseif (label='assignmentsPerCourse') then
		insert into assignmentsPerCourse(courseID,assignmentID) values (entityID,id);
    elseif (label='assignmentsPerStudent') then
		insert into assignmentsPerStudent(studentID,assignmentID) values (entityID,id);
    end if;
	set refIds = right(refIds,LENGTH(refIds) - indx); #diwxnw to id pou molis phra apo to refIds kai krataw ta ipoloipa 
	if LENGTH(refIds) = 0 then #otan den exw pia kati sto refIds teleiwnei h while
		leave SplitLoop;
	end if; 
	end while;
end$
delimiter ;
/*------------------------------------------------------------------------------------------------------------------------------------------------------*/
/*insertEntitiesRelationship procedure pairnei ws parametros to entityID, ta redIds pou tha diagrapsw kai to label gia na xerw se poion pinaka tha kanw insert */
drop procedure if exists deleteEntitiesRelationship;
delimiter $
create procedure deleteEntitiesRelationship(in entityID int,in refIds varchar(255),in label varchar(100))
begin 
	declare indx int;
    declare id int;
	declare part varchar(255); 
	set indx = 1;
	SplitLoop: while indx != 0 do
	set indx = LOCATE("-",refIds); #thesh tou prwtou '-'
	if indx !=0 then #apothikeuw apo thn arxh mexri to prwto '-' sto part
		set part = LEFT(refIds,indx - 1);
	else #an einai 0 dhladh den exw poia xarakthra '-' pairnw to ipoloipo refIds5
		set part = refIds;
	end if;
	set id = cast(part as unsigned); #kanw casting to id se integer
    if (label='studentsPerCourse') then
		delete from studentsPerCourse where (courseID=entityID) and (studentID=id);
    elseif (label='trainersPerCourse') then
		delete from trainersPerCourse where (courseID=entityID) and (trainerID=id);
    elseif (label='assignmentsPerCourse') then
		delete from assignmentsPerCourse where (courseID=entityID) and (assignmentID=id);
    elseif (label='assignmentsPerStudent') then
		delete from assignmentsPerStudent where (studentID=entityID) and (assignmentID=id);
    end if;
	set refIds = right(refIds,LENGTH(refIds) - indx); #diwxnw to id pou molis phra apo to refIds kai krataw ta ipoloipa 
	if LENGTH(refIds) = 0 then leave SplitLoop;end if; #otan den exw pia kati sto refIds teleiwnei h while
	end while;
end$
delimiter ;
/*------------------------------------------------------------------------------------------------------------------------------------------------------*/
/*deleteAllDatabase procedure gia na adeiasw th bash an o xrhsths thelei na balei apo to pliktrologio times*/
/*kanw delete mono tis kyries ontothtes kathws exw kanei on delete cascade kai sbhnontai ola*/
drop procedure if exists deleteAll;
delimiter $
create procedure deleteAll(in label varchar(50))
begin 
	if (label = 'students') then
		delete from students;
	elseif (label='trainers') then
		delete from trainers;
	elseif (label='assignments') then
		delete from assignments;
	elseif (label='courses') then
		delete from courses;
	end if;
end$
delimiter ;
/*------------------------------------------------------------------------------------------------------------------------------------------------------*/
/*maxWidthsOf procedure epistrefei ta maxWidths kathe sthlhs gia ta prints*/
drop procedure if exists maxWidthsOf;
delimiter $
create procedure maxWidthsOf(in label varchar(50))
begin 
	if (label = 'students') then
		select max(length(id)) as id,max(length(firstName)) as firstName,
               max(length(lastName)) as lastName,max(length(tuitionFees)) as tuitionFees
	    from students;
	elseif (label='trainers') then
		select max(length(id)) as id,max(length(firstName)) as firstName,
               max(length(lastName)) as lastName,max(length(tsubject)) as tsubject
	    from trainers;
	elseif (label='assignments') then
		select max(length(id)) as id,max(length(title)) as title,max(length(adescription)) as adescription,
               max(length(oralMark)) as oralMark,max(length(totalMark)) as totalMark
	    from assignments;
	elseif (label='courses') then
		select max(length(id)) as id,max(length(title)) as title,
               max(length(cstream)) as cstream,max(length(ctype)) as ctype
	    from courses; 
	end if;
end$
delimiter ;
/*------------------------------------------------------------------------------------------------------------------------------------------------------*/
/*calling procedures statements examples*/
call allSelect('courses');
call allSelect('trainers');
call allSelect('students');
call allSelect('assignments');
call allSelect('studentsPerCourse');
call allSelect('trainersPerCourse');
call allSelect('assignmentsPerCourse');
call allSelect('assignmentsPerStudent');
call allSelect('studentsToMoreCourses');
call assignmentsPerStudentInPeriod('2021-04-26','2021-04-30');
call deleteAll('courses');
call insertEntitiesRelationship(1,"2-3","studentsPerCourse");
call deleteEntitiesRelationship(1,"1-5-","studentsPerCourse");
/*------------------------------------------------------------------------------------------------------------------------------------------------------*/
call arraySize('courses');
call arraySize('students');
call arraySize('trainers');
call arraySize('assignments');
call arraySize('studentsPerCourse');
call arraySize('trainersPerCourse');
call arraySize('assignmentsPerCourse');
call arraySize('assignmentsPerStudent');
call arraySize('studentsToMoreCourses');
call sizeOfAssigPerStudInPeriod('2021-04-26','2021-04-30');
call entitiesRelationship('studentsPerCourse');
/*------------------------------------------------------------------------------------------------------------------------------------------------------*/