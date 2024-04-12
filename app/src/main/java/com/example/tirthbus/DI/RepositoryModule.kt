package com.example.tirthbus.DI

import com.example.tirthbus.Data.AuthRepo
import com.example.tirthbus.Data.AuthRepoImpl
import com.example.tirthbus.Data.YatraRepo
import com.example.tirthbus.Data.YatraRepoImpl
import com.example.tirthbus.ui.theme.User.User.ViewModel.UserAuthViewModel
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun providesYatraRepo(repo: YatraRepoImpl
    ): YatraRepo

    @Binds
    abstract fun providesAuthRepo(
        repo: AuthRepoImpl
    ): AuthRepo

}