package com.wxsoft.emergency.ui.emr.fragment.firstdiagnosis

import android.arch.lifecycle.ViewModelProvider
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wxsoft.emergency.R
import com.wxsoft.emergency.ui.emr.EmrViewModel
import com.wxsoft.emergency.utils.activityViewModelProvider
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class FirstDiagnosisFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: ViewDataBinding

    private lateinit var viewModel: EmrViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        var view= inflater.inflate(R.layout.fragment_profile, container, false)
        binding= DataBindingUtil.bind(view)!!

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = activityViewModelProvider(viewModelFactory)
//        binding.setVariable(BR.submit, View.OnClickListener{v->viewModel.savePatientInfo(viewModel.patient)})
//        binding.setVariable(BR.patient, viewModel.patient)
    }

}
