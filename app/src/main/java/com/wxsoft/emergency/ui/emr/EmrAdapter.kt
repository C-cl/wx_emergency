package com.wxsoft.emergency.ui.fragment.patients


import android.support.v7.recyclerview.extensions.AsyncListDiffer
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wxsoft.emergency.BR
import com.wxsoft.emergency.R
import com.wxsoft.emergency.data.entity.EmrItem
import com.wxsoft.emergency.data.entity.Patient
import com.wxsoft.emergency.databinding.ItemEmrDetailBinding
import com.wxsoft.emergency.databinding.ItemPatientDetailBinding
import com.wxsoft.emergency.ui.emr.EmrViewModel


class EmrAdapter(val viewModel: EmrViewModel): RecyclerView.Adapter<EmrDetailViewHolder>() {

    private val differ = AsyncListDiffer<Any>(this, DiffCallback)


    var patient: Patient = Patient("")
        set(value) {
            field = value
            differ.submitList(buildMergedList(pat = value))
        }

    var related: List<EmrItem> = emptyList()
        set(value) {
            field = value
            differ.submitList(buildMergedList(relatedEmrs = value))
        }


    private var _merged = mutableListOf<Any>()

    val merged:MutableList<Any>
            get() {return _merged}
    private fun buildMergedList(
            pat: Patient = patient,
            relatedEmrs: List<EmrItem> = related
    ): List<Any> {

        _merged=mutableListOf()
        _merged.add(pat)
        if (relatedEmrs.isNotEmpty()) {
//            _merged += emr
            _merged.addAll(relatedEmrs)
        }
        return _merged
    }

    init {
        differ.submitList(buildMergedList())
    }


    override fun getItemViewType(position: Int): Int {

        return when (differ.currentList[position]) {
            is Patient -> R.layout.item_patient_detail
            is EmrItem -> R.layout.item_emr_detail
            else -> throw IllegalStateException("Unknown view type at position $position")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmrDetailViewHolder {


        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            R.layout.item_patient_detail -> EmrDetailViewHolder.PatientInfoViewHolder(
                    ItemPatientDetailBinding.inflate(inflater, parent, false)
            )
            R.layout.item_emr_detail -> EmrDetailViewHolder.EmrItemViewHolder(
                    ItemEmrDetailBinding.inflate(inflater, parent, false)
            )

            else -> throw IllegalStateException("Unknown viewType $viewType")
        }

    }

    override fun getItemCount() = differ.currentList.size

    override fun onBindViewHolder(holder: EmrDetailViewHolder, position: Int) {


        when (holder) {
            is EmrDetailViewHolder.PatientInfoViewHolder -> holder.binding.apply {
                val presenter = differ.currentList[position] as Patient
                holder.binding.setVariable(BR.patient,presenter)
                executePendingBindings()
            }
            is EmrDetailViewHolder.EmrItemViewHolder -> holder.binding.apply {
                val presenter = differ.currentList[position] as EmrItem

                holder.binding.setVariable(BR.emr,presenter)
                holder.binding.setVariable(BR.listener,viewModel)
                executePendingBindings()
            }
            is EmrDetailViewHolder.HeaderViewHolder -> Unit // no-op
        }

    }


}

//object p
//object emr

object DiffCallback : DiffUtil.ItemCallback<Any>() {
    override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
        return when {
//            oldItem === p && newItem === p -> true
//            oldItem === emr && newItem === emr -> true
            oldItem is Patient && newItem is Patient-> oldItem.id==newItem.id
            oldItem is EmrItem && newItem is EmrItem-> oldItem.id==newItem.id
            else -> false
        }
    }

    override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
        return when {
//            oldItem is p && newItem is p -> oldItem == newItem
//            oldItem is emr && newItem is emr -> oldItem == newItem

            oldItem is Patient && newItem is Patient-> oldItem.id==newItem.id
            oldItem is EmrItem && newItem is EmrItem-> oldItem.id==newItem.id
            else -> true
        }
    }
}

sealed class EmrDetailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    class PatientInfoViewHolder(
            val binding: ItemPatientDetailBinding
    ) : EmrDetailViewHolder(binding.root)

    class EmrItemViewHolder(
            val binding: ItemEmrDetailBinding
    ) : EmrDetailViewHolder(binding.root)


    class HeaderViewHolder(
            itemView: View
    ) : EmrDetailViewHolder(itemView)
}