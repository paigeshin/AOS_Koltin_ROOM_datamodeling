# AOS_Koltin_ROOM_datamodeling

# School Table

- Student
- Subject
- School
- Director

# Relationship

- One to One
- One to Many
- Many to Many

# Gradle

```java
plugins {
    id 'com.android.application'
    id 'kotlin-android'
    id 'kotlin-kapt'
}

android {
    compileSdkVersion 30
    buildToolsVersion "30.0.2"

    defaultConfig {
        applicationId "com.paigesoftware.roomonetomany"
        minSdkVersion 16
        targetSdkVersion 30
        versionCode 1
        versionName "1.0"

        testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = '1.8'
    }
}

dependencies {
    implementation "org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version"
    implementation 'androidx.core:core-ktx:1.3.2'
    implementation 'androidx.appcompat:appcompat:1.2.0'
    implementation 'com.google.android.material:material:1.2.1'
    implementation 'androidx.constraintlayout:constraintlayout:2.0.4'
    testImplementation 'junit:junit:4.+'
    androidTestImplementation 'androidx.test.ext:junit:1.1.2'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.3.0'

    // Room
    implementation "androidx.room:room-runtime:2.2.6"
    kapt "androidx.room:room-compiler:2.2.6"

    // Kotlin Extensions and Coroutines support for Room
    implementation "androidx.room:room-ktx:2.2.6"

    // Coroutines
    implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.7'
    implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9'

    // Coroutine Lifecycle Scopes
    implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0"
    implementation "androidx.lifecycle:lifecycle-runtime-ktx:2.2.0"
}
```

# One to One

- One school has one director

⇒ SchoolAndDirector 또는 DirectorAndSchool 식으로 변경 가능.

- Director.kt

```kotlin
@Entity
data class Director (

    @PrimaryKey(autoGenerate = false)
    val directorName: String,
    val schoolName: String

)
```

- School.kt

```kotlin
@Entity
data class School (
    @PrimaryKey(autoGenerate = false)
    val schoolName: String
)
```

- SchoolAndDirector

```kotlin
data class SchoolAndDirector (
    @Embedded val school: School,
    @Relation(
        parentColumn = "schoolName",
        entityColumn = "schoolName"
    )
    val director: Director
)
```

- Define Dao

```kotlin
@Dao
interface SchoolDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSchool(school: School)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDirector(director: Director)

    @Transaction
    @Query("SELECT * FROM school WHERE schoolName = :schoolName")
    suspend fun getSchoolAndDirectorWithSchoolName(schoolName: String): List<SchoolAndDirector> //join

}
```

# One to Many Relationship

- One school has many students

- School.kt

```kotlin
@Entity
data class School (
    @PrimaryKey(autoGenerate = false)
    val schoolName: String
)
```

- Student.kt

```kotlin
package com.paigesoftware.roomonetomany.entities

import androidx.room.PrimaryKey

data class Student(
    @PrimaryKey(autoGenerate = false)
    val studentName: String,
    val semester: Int,
    val schoolName: String
)
```

- SchoolWithStudents.kt

```kotlin
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
```

- SchoolDao.kt

```kotlin
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

    /* One to One */
    @Transaction //Thread Safe Manner
    @Query("SELECT * FROM school WHERE schoolName = :schoolName")
    suspend fun getSchoolAndDirectorWithSchoolName(schoolName: String): List<SchoolAndDirector> //join

    /* One to Many */
    @Transaction //Thread Safe Manner
    @Query("SELECT * FROM school WHERE schoolName = :schoolName")
    suspend fun getSchoolWithStudents(schoolName: String): List<SchoolWithStudents>

}
```

# Many to Many Relationship

- Students have many Subjects

- Student.kt

```kotlin
@Entity
data class Student(
    @PrimaryKey(autoGenerate = false)
    val studentName: String,
    val semester: Int,
    val schoolName: String
)
```

- Subject.kt

```kotlin
@Entity
data class Subject(
    @PrimaryKey(autoGenerate = false)
    val subjectName: String
)
```

- StudentSubjectCrossRef

```kotlin
@Entity(primaryKeys = ["studentName", "subjectName"])
data class StudentSubjectCrossRef(
    val studentName: String,  //primary key of student Table
    val subjectName: String   //primary key of subject Table
)
```

- StudentWithSubjects

```kotlin
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
```

- SubjectWithStudents

```kotlin
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
```

- SchoolDao.kt

```kotlin
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
```

# Define Database

```kotlin
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
```