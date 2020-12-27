package com.paigesoftware.roomonetomany.entities.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.paigesoftware.roomonetomany.entities.Student
import com.paigesoftware.roomonetomany.entities.Subject

//find subjects with one student
data class SubjectWithStudents(
    @Embedded val subject: Subject,
    @Relation(
        parentColumn = "subjectName",
        entityColumn = "studentName",
        associateBy = Junction(StudentSubjectCrossRef::class)
    )
    val students: List<Student>
)