package com.paigesoftware.roomonetomany.entities.relations

import androidx.room.Entity

@Entity(primaryKeys = ["studentName", "subjectName"])
data class StudentSubjectCrossRef(
    val studentName: String,  //primary key of student Table
    val subjectName: String   //primary key of subject Table
)