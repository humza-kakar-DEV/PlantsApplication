package com.example.plantsservicefyp.di

import com.example.plantsservicefyp.repository.authentication.AuthenticationRepository
import com.example.plantsservicefyp.repository.authentication.AuthenticationRepositoryImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthenticationRepositoryModule {

    @Binds
    @Singleton
    abstract fun getRepository(
        authenticationRepositoryImp: AuthenticationRepositoryImp
    ): AuthenticationRepository

}