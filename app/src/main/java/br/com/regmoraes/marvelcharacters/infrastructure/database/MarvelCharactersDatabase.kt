package br.com.regmoraes.marvelcharacters.infrastructure.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [CharacterEntity::class], version = 1, exportSchema = true)
abstract class MarvelCharactersDatabase : RoomDatabase() {

    abstract fun characterDao(): CharacterDao

    companion object {
        @Volatile
        private var INSTANCE: MarvelCharactersDatabase? = null

        fun getDatabase(context: Context): MarvelCharactersDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MarvelCharactersDatabase::class.java,
                    "marvel_characters_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}