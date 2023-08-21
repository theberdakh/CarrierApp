package com.theberdakh.carrierapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.theberdakh.carrierapp.data.model.response.seller.GetAllSellerResult
import com.theberdakh.carrierapp.databinding.ItemRecyclerSellerBinding

class TaxSellersAdapter: ListAdapter<GetAllSellerResult,TaxSellersAdapter.TaxSellersViewHolder >(TaxSellerAdapterCallback){

    inner class TaxSellersViewHolder(private val binding: ItemRecyclerSellerBinding): ViewHolder(binding.root){
        fun bind(){
            val seller = getItem(adapterPosition)
            binding.tvTitle.text = seller.karer_name
            binding.tvSubtitle.text = seller.phone_number
        }

    }

    private object TaxSellerAdapterCallback: DiffUtil.ItemCallback<GetAllSellerResult>(){
        override fun areItemsTheSame(
            oldItem: GetAllSellerResult,
            newItem: GetAllSellerResult
        ) = oldItem == newItem


        override fun areContentsTheSame(
            oldItem: GetAllSellerResult,
            newItem: GetAllSellerResult
        ) = oldItem.id == newItem.id

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = TaxSellersViewHolder(
        ItemRecyclerSellerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: TaxSellersViewHolder, position: Int)  = holder.bind()

}