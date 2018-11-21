package com.wxsoft.emergency.ui.emr.fragment.evaluation

import android.arch.lifecycle.ViewModelProvider
import android.databinding.BindingAdapter
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wxsoft.emergency.data.entity.Dictionary
import com.wxsoft.emergency.databinding.FragmentDictionaryBinding
import com.wxsoft.emergency.ui.emr.EmrViewModel
import com.wxsoft.emergency.utils.activityViewModelProvider
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class EvaluationFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: FragmentDictionaryBinding

    private lateinit var viewModel: EmrViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding= FragmentDictionaryBinding.inflate(inflater, container, false).apply {
            setLifecycleOwner (this@EvaluationFragment)
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = activityViewModelProvider(viewModelFactory)

        binding.viewModel=viewModel

    }
}


@BindingAdapter(value = ["dictionaryItems"])
fun dictionaryItems(recyclerView: RecyclerView, list: List<Dictionary>?) {
    if (recyclerView.adapter == null) {
        recyclerView.adapter = EvaluationAdapter()

    }

    (recyclerView.adapter as EvaluationAdapter).apply { submitList(list?: emptyList()) }
}
