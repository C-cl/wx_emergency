package com.wxsoft.emergency.ui.detail.fragment.timeline

import android.arch.lifecycle.ViewModelProvider
import android.databinding.BindingAdapter
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wxsoft.emergency.R
import com.wxsoft.emergency.data.entity.EmrItem
import com.wxsoft.emergency.databinding.FragmentTimeLineBinding
import com.wxsoft.emergency.ui.detail.PatientDetailViewModel
import com.wxsoft.emergency.ui.detail.fragment.operation.OperationMenusFragment
import com.wxsoft.emergency.ui.detail.fragment.profile.ProfileFragment
import com.wxsoft.emergency.ui.fragment.patients.TimeAdapter
import com.wxsoft.emergency.utils.activityViewModelProvider
import com.wxsoft.emergency.utils.clearDecorations
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class TimeLineFragment : DaggerFragment() {

    private lateinit var viewModel: PatientDetailViewModel

    private lateinit var menuBottomSheetBehavior: BottomSheetBehavior<*>

    private lateinit var fragment: OperationMenusFragment

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var binding: FragmentTimeLineBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding= FragmentTimeLineBinding.inflate(inflater, container, false)
            .apply {
                setLifecycleOwner (this@TimeLineFragment)
            }

        binding.floatingActionButton.setOnClickListener {

            fragment.show(childFragmentManager,OperationMenusFragment.TAG)
//            menuBottomSheetBehavior=BottomSheetBehavior.from(fragment.view)
//            menuBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
        binding.bottomBar.replaceMenu(R.menu.bottom_bar_patient_time_line)
        binding.bottomBar.setOnMenuItemClickListener {
             when (it.itemId) {
                R.id.nav_profile -> {

                    var dialog= ProfileFragment()
                    dialog.show(activity!!.supportFragmentManager, ProfileFragment.DIALOG_PROFILE)
                    return@setOnMenuItemClickListener true
                }
                else-> return@setOnMenuItemClickListener  false
            }
        }
        fragment=OperationMenusFragment()

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = activityViewModelProvider(viewModelFactory)

        binding.viewModel=viewModel


    }



}

@BindingAdapter(value = ["timelineItems"])
fun timeLineItems(recyclerView: RecyclerView, list: List<EmrItem>?) {
    if (recyclerView.adapter == null) {
        recyclerView.adapter = TimeAdapter()
    }
    val adapter = (recyclerView.adapter as TimeAdapter).apply {
        this.submitList(list ?: emptyList())
    }

    // Recreate the decoration used for the sticky date headers
    recyclerView.clearDecorations()
    if (list != null && list.isNotEmpty()) {
        recyclerView.addItemDecoration(
            UserCaseDecoration(recyclerView.context, list)
        )

        recyclerView.addItemDecoration(
            ItemDecoration(recyclerView.context, list)
        )

        recyclerView.addItemDecoration(
            ItemTimeDecoration(recyclerView.context, list)
        )
    }
}
