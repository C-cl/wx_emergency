package com.wxsoft.emergency.ui.detail.fragment.operation

import android.arch.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wxsoft.emergency.data.entity.OperationMenu
import com.wxsoft.emergency.databinding.FragmentOperationMenusBinding
import com.wxsoft.emergency.di.ViewModelFactory
import com.wxsoft.emergency.ui.detail.PatientDetailViewModel
import com.wxsoft.emergency.ui.fragment.patients.OpertionMenuAdapter
import com.wxsoft.emergency.utils.activityViewModelProvider
import com.wxsoft.emergency.widget.CustomDimDialogFragment
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.DaggerFragment
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject


class OperationMenusFragment : BottomSheetDialogFragment(), HasSupportFragmentInjector {

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var factory: ViewModelFactory

    private lateinit var viewModel: PatientDetailViewModel
    private lateinit var binding: FragmentOperationMenusBinding
    private lateinit var adapter: OpertionMenuAdapter

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return fragmentInjector
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding= FragmentOperationMenusBinding.inflate(inflater,container, false)
        binding.apply {


        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel=activityViewModelProvider(factory)
        adapter = OpertionMenuAdapter(viewModel)

        binding.list.adapter = adapter
//        binding.viewModel=viewModel
        viewModel.menuItems.observe(this@OperationMenusFragment, Observer { t->

            adapter.menus.clear()
            adapter.menus.addAll(t!!)
            adapter.notifyDataSetChanged()

        })

        viewModel.getMenus()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    companion object {
        final val TAG = "OperationMenusFragment"
    }
}
