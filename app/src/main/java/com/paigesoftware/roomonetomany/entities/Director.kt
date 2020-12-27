package com.paigesoftware.roomonetomany.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

//Define Director

@Entity
data class Director (

    @PrimaryKey(autoGenerate = false)
    val directorName: String,
    val schoolName: String

)