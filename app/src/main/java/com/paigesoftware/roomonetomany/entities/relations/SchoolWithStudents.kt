package com.paigesoftware.roomonetomany.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.paigesoftware.roomonetomany.entities.School
import com.paigesoftware.roomonetomany.entities.Student

data class SchoolWithStudents(
    @Embedded val school: School,
    @Relation(
        parentColumn = "schoolName",
        entityColumn = "schoolName"
    )
    val students: List<Student>
)