package com.wxsoft.emergency.ui.main.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wxsoft.emergency.databinding.FragmentPatientsBinding
import com.wxsoft.emergency.result.EventObserver
import com.wxsoft.emergency.ui.detail.PatientDetailActivity
import com.wxsoft.emergency.ui.fragment.patients.PatientsAdapter
import com.wxsoft.emergency.utils.activityViewModelProvider
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_patients.*
import javax.inject.Inject

class PatientsFragment : DaggerFragment() {

    private lateinit var viewModel: PatientsViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var adapter: PatientsAdapter

    lateinit var binding: FragmentPatientsBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding= FragmentPatientsBinding.inflate(inflater, container, false).apply {
            setLifecycleOwner (this@PatientsFragment)

        }

        binding.floatingActionButton.setOnClickListener { toDetail("") }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = activityViewModelProvider(viewModelFactory)

        adapter= PatientsAdapter(viewModel)
        binding.viewModel=viewModel

        list.adapter=adapter
        viewModel.patients.observe(this, Observer { it->adapter.patients=it?: emptyList() })
        viewModel.navigateToOperationAction.observe(this, EventObserver{ t->

            toDetail(t)
        })
    }


    fun toDetail(id:String){
        var intent= Intent(activity!!, PatientDetailActivity::class.java)
        intent.putExtra(PatientDetailActivity.PATIENT_ID,id)
        startActivity(intent)
    }
}
