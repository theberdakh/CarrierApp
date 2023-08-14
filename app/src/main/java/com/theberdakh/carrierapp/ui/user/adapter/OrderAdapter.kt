package com.theberdakh.carrierapp.ui.user.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.theberdakh.carrierapp.databinding.ItemRecyclerOrderCarrierBinding
import com.theberdakh.carrierapp.data.model.response.Result

class OrderAdapter :
    ListAdapter<Result, OrderAdapter.OrderViewHolder>(WordsCallBack) {

    inner class OrderViewHolder(private val binding: ItemRecyclerOrderCarrierBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            val order = getItem(adapterPosition)
            binding.apply {
                tvCarNumber.text = order.car_number
                tvFullName.text = order.driver_name
                tvTimeDate.text = order.date
                tvCargoType.text = order.cargo_type.toString()
                tvCargoValue.text = "${order.cargo_value} ${order.cargo_unit}"
            }


        }
    }

    private object WordsCallBack : DiffUtil.ItemCallback<Result>() {
        override fun areItemsTheSame(oldItem: com.theberdakh.carrierapp.data.model.response.Result, newItem: Result): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem.id == newItem.id
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        return OrderViewHolder(
            ItemRecyclerOrderCarrierBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) = holder.bind()

    private lateinit var onOrderClick: (Result) -> Unit
    fun onShareClickListener(onOrderClick:(Result) -> Unit ){
        this.onOrderClick = onOrderClick
    }

}