package com.paigesoftware.roomonetomany.entities.dao

import androidx.room.*
import com.paigesoftware.roomonetomany.entities.Director
import com.paigesoftware.roomonetomany.entities.School
import com.paigesoftware.roomonetomany.entities.Student
import com.paigesoftware.roomonetomany.entities.Subject
import com.paigesoftware.roomonetomany.entities.relations.*

@Dao
interface SchoolDao {

    /* One to One */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSchool(school: School)

    /* One to One */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDirector(director: Director)

    /* One to Many */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudent(student: Student)

    /* Many to Many */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubject(subject: Subject)

    /* Many to Many */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudentSubjectCrossRef(coressRef: StudentSubjectCrossRef)

    /* One to One */
    @Transaction //Thread Safe Manner
    @Query("SELECT * FROM school WHERE schoolName = :schoolName")
    suspend fun getSchoolAndDirectorWithSchoolName(schoolName: String): List<SchoolAndDirector> //join

    /* One to Many */
    @Transaction //Thread Safe Manner
    @Query("SELECT * FROM school WHERE schoolName = :schoolName")
    suspend fun getSchoolWithStudents(schoolName: String): List<SchoolWithStudents>

    /* Many to Many */
    @Transaction //Thread Safe Manner
    @Query("SELECT * FROM subject WHERE subjectName = :subjectName")
    suspend fun getStudentsOfSubject(subjectName: String): List<SubjectWithStudents> //join

    @Transaction //Thread Safe Manner
    @Query("SELECT * FROM student WHERE studentName = :studentName")
    suspend fun getSubjectsOfStudent(studentName: String): List<StudentWithSubjects> //join

}