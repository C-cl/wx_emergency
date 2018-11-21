package com.wxsoft.emergency.ui.emr

import android.arch.lifecycle.ViewModel
import com.wxsoft.emergency.di.ChildFragmentScoped
import com.wxsoft.emergency.di.FragmentScoped
import com.wxsoft.emergency.di.ViewModelKey
import com.wxsoft.emergency.ui.detail.fragment.profile.ProfileFragment
import com.wxsoft.emergency.ui.emr.fragment.evaluation.EvaluationFragment
import com.wxsoft.emergency.ui.emr.fragment.map.MapFragment
import com.wxsoft.emergency.ui.emr.fragment.vitalsigns.VitalSignsFragment
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
internal abstract class EmrModule {


    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeEvaluationFragment(): EvaluationFragment

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeMapFragment(): MapFragment

    @ChildFragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeBasicFragment(): ProfileFragment

    @ChildFragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeVitalSignsFragment(): VitalSignsFragment

    @Binds
    @IntoMap
    @ViewModelKey(EmrViewModel::class)
    abstract fun bindEmrViewModel(viewModel: EmrViewModel): ViewModel

}