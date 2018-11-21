package com.wxsoft.emergency.ui.fragment.patients


import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.wxsoft.emergency.BR
import com.wxsoft.emergency.R
import com.wxsoft.emergency.data.entity.OperationMenu
import com.wxsoft.emergency.ui.detail.PatientDetailViewModel


class OpertionMenuAdapter constructor(private val viewModel: PatientDetailViewModel): RecyclerView.Adapter<OpertionMenuAdapter.ItemViewHolder>() {

    var menus: MutableList<OperationMenu> = ArrayList()
    override fun getItemCount(): Int {
        return menus.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        var binding=holder.binding

        binding.setVariable(BR.menu,menus[position])
        binding.setVariable(BR.viewModel,viewModel)

        binding.executePendingBindings()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val binding: ViewDataBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.fragment_operation_menu_item,parent, false)

        return ItemViewHolder(binding)
    }

    class ItemViewHolder(binding:ViewDataBinding ) : RecyclerView.ViewHolder(binding.root) {
        var binding: ViewDataBinding
            private set

        init {
            this.binding = binding
        }
    }

}