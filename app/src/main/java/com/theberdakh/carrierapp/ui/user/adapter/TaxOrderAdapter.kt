package com.theberdakh.carrierapp.ui.user.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.theberdakh.carrierapp.data.model.response.order.Order
import com.theberdakh.carrierapp.databinding.ItemRecyclerOrderCarrierBinding
import com.theberdakh.carrierapp.databinding.ItemRecyclerOrderTaxBinding

class TaxOrderAdapter: ListAdapter<Order, TaxOrderAdapter.TaxOrderViewHolder>(TaxOrderCallBack)  {

    inner class TaxOrderViewHolder(private val binding: ItemRecyclerOrderTaxBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            val order = getItem(adapterPosition)

            binding.apply {
                tvCarNumber.text = order.car_number
                tvFullName.text = order.driver_name
                tvTimeDate.text = order.date
                tvCargoType.text = if (order.cargo_type == 1)"Sheben" else "Topıraq (default)"
                tvCargoValue.text = "${order.weight} ${if(order.cargo_unit == 1) "m3" else "Kg"}"

            }


            binding.root.setOnClickListener{
                onOrderClick.invoke(order)
            }

            binding.btnFine.setOnClickListener {
                onFineClick.invoke(order)
            }

        }


    }

    private object TaxOrderCallBack : DiffUtil.ItemCallback<Order>() {
        override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem.id == newItem.id && oldItem.driver_passport_or_id == newItem.driver_passport_or_id
        }

    }



    private lateinit var onOrderClick: (Order) -> Unit
    fun onOrderClickListener(onOrderClick:(Order) -> Unit ){
        this.onOrderClick = onOrderClick
    }

    private lateinit var onFineClick: (Order) -> Unit
    fun onOrderFineClickListener(onFineClick:(Order) -> Unit ){
        this.onFineClick = onFineClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = TaxOrderViewHolder(ItemRecyclerOrderTaxBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: TaxOrderViewHolder, position: Int) = holder.bind()


}