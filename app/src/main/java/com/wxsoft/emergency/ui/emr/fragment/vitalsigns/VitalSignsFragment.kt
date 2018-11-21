package com.wxsoft.emergency.ui.emr.fragment.vitalsigns

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wxsoft.emergency.databinding.DialogVitalSignsBinding
import com.wxsoft.emergency.di.ViewModelFactory
import com.wxsoft.emergency.ui.emr.EmrViewModel
import com.wxsoft.emergency.utils.activityViewModelProvider
import com.wxsoft.emergency.widget.CustomDimDialogFragment
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class VitalSignsFragment : CustomDimDialogFragment(), HasSupportFragmentInjector {



    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    @Inject lateinit var factory: ViewModelFactory

    private lateinit var viewModel: EmrViewModel
    private lateinit var binding: DialogVitalSignsBinding

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return fragmentInjector
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        viewModel=activityViewModelProvider(factory)

        binding=DialogVitalSignsBinding.inflate(inflater,container, false)



        return binding.root
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.viewModel=viewModel
    }


    companion object {
        const val DIALOG_VITAL_SIGNS = "dialog_vital_signs"
        const val VITAL_SIGNS_ACTIVITY_REQUEST_CODE = 42
    }
}
