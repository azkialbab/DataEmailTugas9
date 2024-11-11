package com.example.dataemail

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.dataemail.database.MovieDao
import com.example.dataemail.database.UserDao
import com.example.dataemail.model.Movie
import com.example.dataemail.model.Users

@Database(entities = [Users::class, Movie::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun movieDao(): MovieDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

            fun getInstance(context: Context): AppDatabase {
                return INSTANCE ?: synchronized(this) {
                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "movie_database"
                    ).build()
                    INSTANCE = instance
                    instance
                }
            }
        }

        // Migration from version 2 to version 3
        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // SQL command to add the new column 'role' with default value 'user'
                database.execSQL("ALTER TABLE users ADD COLUMN role TEXT NOT NULL DEFAULT 'user'")
            }
        }

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .addMigrations(MIGRATION_2_3) // Add migrations
                    .fallbackToDestructiveMigration() // Optional: Delete data if migration fails
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

