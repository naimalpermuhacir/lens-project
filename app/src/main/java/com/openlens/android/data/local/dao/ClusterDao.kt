package com.openlens.android.data.local.dao

import androidx.room.*
import com.openlens.android.data.model.Cluster
import kotlinx.coroutines.flow.Flow

@Dao
interface ClusterDao {
    @Query("SELECT * FROM clusters")
    fun getAllClusters(): Flow<List<Cluster>>
    
    @Query("SELECT * FROM clusters WHERE id = :clusterId")
    suspend fun getCluster(clusterId: String): Cluster?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCluster(cluster: Cluster)
    
    @Update
    suspend fun updateCluster(cluster: Cluster)
    
    @Delete
    suspend fun deleteCluster(cluster: Cluster)
    
    @Query("DELETE FROM clusters WHERE id = :clusterId")
    suspend fun deleteCluster(clusterId: String)
}