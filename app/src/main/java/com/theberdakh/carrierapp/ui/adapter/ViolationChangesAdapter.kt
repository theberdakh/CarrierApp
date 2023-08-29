package com.theberdakh.carrierapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.theberdakh.carrierapp.data.local.SharedPrefStorage
import com.theberdakh.carrierapp.data.model.response.violation.ViolationByUnique
import com.theberdakh.carrierapp.databinding.ItemRecyclerUpdateViolationBinding

class ViolationChangesAdapter: ListAdapter<ViolationByUnique, ViolationChangesAdapter.ViolationChangesViewHolder>(ViolationChangesCallback) {
    inner class ViolationChangesViewHolder(val binding: ItemRecyclerUpdateViolationBinding): ViewHolder(binding.root){

        fun bind(){
            val change = getItem(adapterPosition)
            binding.tvTaxName.text = change.tax_officer.full_name
            binding.tvDate.text = change.created_at

            binding.root.setOnClickListener {
                onClick.invoke(change)
            }
        }

    }
    private object ViolationChangesCallback: DiffUtil.ItemCallback<ViolationByUnique>(){
        override fun areItemsTheSame(oldItem: ViolationByUnique, newItem: ViolationByUnique) = oldItem == newItem
        override fun areContentsTheSame(oldItem: ViolationByUnique, newItem: ViolationByUnique) = oldItem.id == newItem.id
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViolationChangesViewHolder(ItemRecyclerUpdateViolationBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    override fun onBindViewHolder(holder: ViolationChangesViewHolder, position: Int) = holder.bind()

    private lateinit var onClick: (ViolationByUnique) -> Unit
    fun onClickListener(onClick:(ViolationByUnique) -> Unit ){
        this.onClick = onClick
    }
}