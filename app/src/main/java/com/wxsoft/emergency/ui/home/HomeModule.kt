package com.wxsoft.emergency.ui.home

import android.arch.lifecycle.ViewModel
import com.wxsoft.emergency.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class HomeModule {
    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindPatientViewModel(viewModel: HomeViewModel): ViewModel



}