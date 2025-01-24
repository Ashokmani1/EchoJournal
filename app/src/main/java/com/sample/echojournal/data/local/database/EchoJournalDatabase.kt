package com.sample.echojournal.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sample.echojournal.data.local.entity.AudioEntryEntity
import com.sample.echojournal.data.local.dao.AudioEntryDao
import com.sample.echojournal.data.local.dao.SettingsDao
import com.sample.echojournal.data.local.dao.TopicDao
import com.sample.echojournal.data.local.entity.SettingsEntity
import com.sample.echojournal.data.local.entity.TopicEntity

@Database(
    entities = [
        AudioEntryEntity::class,
        TopicEntity::class,
        SettingsEntity::class
    ],
    version = 1
)
@TypeConverters(Converters::class)
abstract class EchoJournalDatabase : RoomDatabase()
{
    abstract fun audioEntryDao(): AudioEntryDao
    abstract fun topicDao(): TopicDao
    abstract fun settingsDao(): SettingsDao
}