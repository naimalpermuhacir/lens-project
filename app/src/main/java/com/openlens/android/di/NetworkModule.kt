package com.openlens.android.di

import com.openlens.android.data.remote.KubernetesApiService
import com.openlens.android.data.remote.KubernetesApiServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkModule {
    
    @Binds
    @Singleton
    abstract fun bindKubernetesApiService(
        kubernetesApiServiceImpl: KubernetesApiServiceImpl
    ): KubernetesApiService
}