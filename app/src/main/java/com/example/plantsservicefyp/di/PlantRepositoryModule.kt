package com.example.plantsservicefyp.di

import com.example.plantsservicefyp.repository.plant.PlantRepository
import com.example.plantsservicefyp.repository.plant.PlantRepositoryImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PlantRepositoryModule {

    @Binds
    @Singleton
    abstract fun getPlantRepository (plantRepositoryImp: PlantRepositoryImp): PlantRepository

}