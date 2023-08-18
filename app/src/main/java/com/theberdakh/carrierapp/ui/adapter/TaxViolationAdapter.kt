package com.theberdakh.carrierapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.theberdakh.carrierapp.data.model.response.violation.Violation
import com.theberdakh.carrierapp.databinding.ItemRecyclerViolationBinding

class TaxViolationAdapter :
    ListAdapter<Violation, TaxViolationAdapter.TaxViolationViewHolder>(TaxViolationCallBack) {

    inner class TaxViolationViewHolder(private val binding: ItemRecyclerViolationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            val violation = getItem(adapterPosition)

            binding.apply {
                tvFullName.text = violation.driver_name
                tvReason.text = violation.reason_violation
                tvCarNumber.text = violation.car_number
                tvTimeDate.text = violation.cargo_date
            }


            binding.root.setOnClickListener {
                onViolationClick.invoke(violation)
            }


        }


    }

    private object TaxViolationCallBack : DiffUtil.ItemCallback<Violation>() {
        override fun areItemsTheSame(oldItem: Violation, newItem: Violation): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Violation, newItem: Violation): Boolean {
            return oldItem.id == newItem.id && oldItem.driver_passport_or_id == newItem.driver_passport_or_id
        }

    }


    private lateinit var onViolationClick: (Violation) -> Unit
    fun onViolationClickListener(onViolationClick: (Violation) -> Unit) {
        this.onViolationClick = onViolationClick
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = TaxViolationViewHolder(
        ItemRecyclerViolationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: TaxViolationViewHolder, position: Int) = holder.bind()
}