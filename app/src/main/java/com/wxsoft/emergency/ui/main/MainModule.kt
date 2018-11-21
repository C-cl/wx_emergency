package com.wxsoft.emergency.ui.main

import android.arch.lifecycle.ViewModel
import com.wxsoft.emergency.di.ChildFragmentScoped
import com.wxsoft.emergency.di.FragmentScoped
import com.wxsoft.emergency.di.ViewModelKey
import com.wxsoft.emergency.ui.main.dialog.ShowPatientDialog
import com.wxsoft.emergency.ui.main.fragment.PatientsFragment
import com.wxsoft.emergency.ui.main.fragment.PatientsViewModel
import com.wxsoft.emergency.ui.main.fragment.profile.UserProfileFragment
import com.wxsoft.emergency.ui.main.fragment.profile.UserProfileViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
internal abstract class MainModule {
    @Binds
    @IntoMap
    @ViewModelKey(UserProfileViewModel::class)
    abstract fun bindUserProfileViewModel(viewModel: UserProfileViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PatientsViewModel::class)
    abstract fun bindPatientsViewModel(viewModel: PatientsViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindPatientViewModel(viewModel: MainViewModel): ViewModel

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeUserProfileFragment(): UserProfileFragment

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributePatientsFragment(): PatientsFragment

    @ChildFragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeShowPatientDialog(): ShowPatientDialog
}