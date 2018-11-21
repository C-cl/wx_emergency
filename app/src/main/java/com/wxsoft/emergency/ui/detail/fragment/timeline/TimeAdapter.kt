package com.wxsoft.emergency.ui.fragment.patients


import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.wxsoft.emergency.data.entity.Patient
import com.wxsoft.emergency.BR
import com.wxsoft.emergency.R
import com.wxsoft.emergency.data.entity.EmrItem


class TimeAdapter: ListAdapter<EmrItem, TimeAdapter.ItemViewHolder>(TimeDiff) {

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        var binding=holder.binding

        binding.setVariable(BR.emr,getItem(position))

        binding.executePendingBindings()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val binding: ViewDataBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_emr_detail,parent, false)

        return ItemViewHolder(binding)
    }

    class ItemViewHolder(binding:ViewDataBinding ) : RecyclerView.ViewHolder(binding.root) {
        var binding: ViewDataBinding
            private set

        init {
            this.binding = binding
        }
    }

    object TimeDiff: DiffUtil.ItemCallback<EmrItem>(){
        override fun areContentsTheSame(oldItem: EmrItem, newItem: EmrItem): Boolean {
            return oldItem.id==newItem.id
        }

        override fun areItemsTheSame(oldItem: EmrItem, newItem: EmrItem): Boolean {
            return oldItem == newItem
        }

    }
}