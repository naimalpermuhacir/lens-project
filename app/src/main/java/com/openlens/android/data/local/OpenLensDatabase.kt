package com.openlens.android.data.local

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.openlens.android.data.local.dao.ClusterDao
import com.openlens.android.data.local.entity.ClusterEntity

@Database(
    entities = [ClusterEntity::class],
    version = 1,
    exportSchema = false
)
abstract class OpenLensDatabase : RoomDatabase() {
    abstract fun clusterDao(): ClusterDao
    
    companion object {
        @Volatile
        private var INSTANCE: OpenLensDatabase? = null
        
        fun getDatabase(context: Context): OpenLensDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    OpenLensDatabase::class.java,
                    "openlens_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}