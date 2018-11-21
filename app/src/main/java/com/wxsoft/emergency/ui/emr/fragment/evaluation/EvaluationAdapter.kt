package com.wxsoft.emergency.ui.emr.fragment.evaluation


import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

import com.wxsoft.emergency.BR
import com.wxsoft.emergency.R
import com.wxsoft.emergency.data.entity.Dictionary


class EvaluationAdapter :  ListAdapter<Dictionary, EvaluationAdapter.ItemViewHolder>(EvaluationDiff) {

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        var binding=holder.binding

        binding.setVariable(BR.dictionary, getItem(position))
        binding.executePendingBindings()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val binding: ViewDataBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_dictionary,parent, false)

        return ItemViewHolder(binding)
    }

    class ItemViewHolder(binding:ViewDataBinding ) : RecyclerView.ViewHolder(binding.root) {
        var binding: ViewDataBinding
            private set

        init {
            this.binding = binding
        }
    }

    object EvaluationDiff: DiffUtil.ItemCallback<Dictionary>(){
        override fun areContentsTheSame(oldItem: Dictionary, newItem: Dictionary): Boolean {
            return oldItem.id==newItem.id
        }

        override fun areItemsTheSame(oldItem: Dictionary, newItem: Dictionary): Boolean {
            return oldItem == newItem
        }

    }
}