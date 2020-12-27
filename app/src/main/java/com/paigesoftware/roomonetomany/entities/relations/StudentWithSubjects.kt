package com.paigesoftware.roomonetomany.entities.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.paigesoftware.roomonetomany.entities.Student
import com.paigesoftware.roomonetomany.entities.Subject

//find subjects with one student
data class StudentWithSubjects(
    @Embedded val student: Student,
    @Relation(
        parentColumn = "studentName",
        entityColumn = "subjectName",
        associateBy = Junction(StudentSubjectCrossRef::class)
    )
    val subjects: List<Subject>
)