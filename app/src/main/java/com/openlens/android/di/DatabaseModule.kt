package com.openlens.android.di

import android.content.Context
import com.openlens.android.data.local.OpenLensDatabase
import com.openlens.android.data.local.dao.ClusterDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    @Provides
    @Singleton
    fun provideOpenLensDatabase(@ApplicationContext context: Context): OpenLensDatabase {
        return OpenLensDatabase.getDatabase(context)
    }
    
    @Provides
    fun provideClusterDao(database: OpenLensDatabase): ClusterDao {
        return database.clusterDao()
    }
}