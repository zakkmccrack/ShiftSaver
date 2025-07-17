package com.example.shiftsaver.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shiftsaver.R
import com.example.shiftsaver.data.entity.MonthEntity
import java.time.Month

class MonthAdapter (
    private var months: List<MonthEntity>,
    private val onClick: (MonthEntity) -> Unit,
    private val onDelete: (MonthEntity) -> Unit
) : RecyclerView.Adapter<com.example.shiftsaver.ui.MonthAdapter.MonthViewHolder>(){

    class MonthViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val monthText: TextView = view.findViewById(R.id.monthText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_month, parent, false)
        return MonthViewHolder(view)
    }

    override fun onBindViewHolder(holder: MonthViewHolder, position: Int) {
        val item = months[position]
        val monthName = Month.of(item.month).name.lowercase().replaceFirstChar { it.uppercase() }
        holder.monthText.text = "$monthName ${item.year}"

        holder.itemView.setOnClickListener { onClick(item) }
        holder.itemView.setOnLongClickListener {
            onDelete(item)
            true
        }
    }
    override fun getItemCount(): Int = months.size

    fun updateData(newList: List<MonthEntity>) {
        months = newList
        notifyDataSetChanged()
    }
}