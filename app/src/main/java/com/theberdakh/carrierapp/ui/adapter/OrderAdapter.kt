package com.theberdakh.carrierapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.theberdakh.carrierapp.data.model.response.order.Order
import com.theberdakh.carrierapp.databinding.ItemRecyclerOrderCarrierBinding
import okhttp3.internal.trimSubstring

class OrderAdapter :
    ListAdapter<Order, OrderAdapter.OrderViewHolder>(WordsCallBack) {

    inner class OrderViewHolder(private val binding: ItemRecyclerOrderCarrierBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            val order = getItem(adapterPosition)

            binding.apply {
                tvCarCode.text = order.car_number.take(2)
                tvCarNumber.text = order.car_number.trimSubstring(2, 8)
                tvFullName.text = order.driver_name
                tvCargoType.text = order.cargo_type
                tvTimeDate.text = order.date
                tvCargoValue.text = "${order.cargo_value} ${if(order.cargo_unit ==1 ) "m3" else "kg"}"
            }


            binding.root.setOnClickListener{
                onOrderClick.invoke(order)
            }




        }
    }

    private object WordsCallBack : DiffUtil.ItemCallback<Order>() {
        override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem.id == newItem.id && oldItem.driver_passport_or_id == newItem.driver_passport_or_id
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

    private lateinit var onOrderClick: (Order) -> Unit
    fun onOrderClickListener(onOrderClick:(Order) -> Unit ){
        this.onOrderClick = onOrderClick
    }

}