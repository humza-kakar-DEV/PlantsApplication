package com.example.plantsservicefyp.di

import com.example.plantsservicefyp.repository.plant.PlantSearchRepository
import com.example.plantsservicefyp.repository.plant.PlantSearchRepositoryImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PlantSearchRepositoryModule {

    @Binds
    @Singleton
    abstract fun getPlantSearchRepository(plantSearchRepositoryImp: PlantSearchRepositoryImp): PlantSearchRepository

}