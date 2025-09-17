package com.openlens.android.di

import com.openlens.android.data.repository.KubernetesRepository
import com.openlens.android.data.repository.KubernetesRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    
    @Binds
    @Singleton
    abstract fun bindKubernetesRepository(
        kubernetesRepositoryImpl: KubernetesRepositoryImpl
    ): KubernetesRepository
}