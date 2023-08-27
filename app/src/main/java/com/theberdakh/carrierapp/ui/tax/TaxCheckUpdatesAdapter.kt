package com.theberdakh.carrierapp.ui.tax

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.theberdakh.carrierapp.data.model.fake.Updates
import com.theberdakh.carrierapp.databinding.ItemRecyclerUpdateViolationBinding

class TaxCheckUpdatesAdapter: ListAdapter<Updates,TaxCheckUpdatesAdapter.TaxCheckAdapter>(UpdateCallBack) {
    inner class TaxCheckAdapter(val binding: ItemRecyclerUpdateViolationBinding): ViewHolder(binding.root){
        fun bind(){
            val update = getItem(adapterPosition)
            binding.tvDate.text = update.date
            binding.tvTaxName.text = update.name
        }
    }
    private object UpdateCallBack: DiffUtil.ItemCallback<Updates>(){
        override fun areItemsTheSame(oldItem: Updates, newItem: Updates) = oldItem == newItem

        override fun areContentsTheSame(oldItem: Updates, newItem: Updates) = oldItem.id == newItem.id

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = TaxCheckAdapter(ItemRecyclerUpdateViolationBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    override fun onBindViewHolder(holder: TaxCheckAdapter, position: Int) = holder.bind()
}