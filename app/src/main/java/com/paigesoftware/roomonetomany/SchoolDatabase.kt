package com.paigesoftware.roomonetomany

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.paigesoftware.roomonetomany.entities.Director
import com.paigesoftware.roomonetomany.entities.School
import com.paigesoftware.roomonetomany.entities.Student
import com.paigesoftware.roomonetomany.entities.Subject
import com.paigesoftware.roomonetomany.entities.dao.SchoolDao
import com.paigesoftware.roomonetomany.entities.relations.StudentSubjectCrossRef

@Database(
    entities = [
        School::class,
        Student::class,
        Director::class,
        Subject::class,
        StudentSubjectCrossRef::class
    ],
    version = 1  /* When you change schema, increment this version. Room automatically recognizes it */
)
abstract class SchoolDatabase: RoomDatabase() {

    abstract val schoolDao: SchoolDao

    companion object {
        @Volatile
        private var INSTANCE: SchoolDatabase? = null

        fun getInstance(context: Context): SchoolDatabase {
            synchronized(this) {
                return INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    SchoolDatabase::class.java,
                    "school_db"
                ).build().also {
                    INSTANCE = it
                }
            }
        }

    }

}