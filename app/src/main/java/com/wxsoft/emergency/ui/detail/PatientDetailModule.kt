package com.wxsoft.emergency.ui.detail

import android.arch.lifecycle.ViewModel
import com.wxsoft.emergency.di.ChildFragmentScoped
import com.wxsoft.emergency.di.FragmentScoped
import com.wxsoft.emergency.di.ViewModelKey
import com.wxsoft.emergency.ui.detail.dialog.BindRfidDialog
import com.wxsoft.emergency.ui.detail.fragment.map.MapFragment
import com.wxsoft.emergency.ui.detail.fragment.operation.OperationMenusFragment
import com.wxsoft.emergency.ui.detail.fragment.profile.ProfileFragment
import com.wxsoft.emergency.ui.detail.fragment.timeline.TimeLineFragment
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
internal abstract class PatientDetailModule {

    @Binds
    @IntoMap
    @ViewModelKey(PatientDetailViewModel::class)
    abstract fun bindPatientDetailViewModel(viewModel: PatientDetailViewModel): ViewModel

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeTimeLineFragment(): TimeLineFragment

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeMapFragment(): MapFragment

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeOperationMenusFragment(): OperationMenusFragment

    @ChildFragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeProfileFragment(): ProfileFragment

    @ChildFragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeBindRfidDialog(): BindRfidDialog

}