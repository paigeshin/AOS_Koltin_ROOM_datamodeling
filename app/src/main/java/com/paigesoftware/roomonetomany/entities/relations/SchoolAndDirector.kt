package com.paigesoftware.roomonetomany.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.paigesoftware.roomonetomany.entities.Director
import com.paigesoftware.roomonetomany.entities.School

//SchoolAndDirector
//DirectorAndSchool

data class SchoolAndDirector (
    @Embedded val school: School,
    @Relation(
        parentColumn = "schoolName",
        entityColumn = "schoolName"
    )
    val director: Director
)