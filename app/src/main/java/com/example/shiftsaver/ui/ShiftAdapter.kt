package com.example.shiftsaver.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shiftsaver.R
import com.example.shiftsaver.data.entity.ShiftEntity
import java.time.Duration
import java.time.LocalTime
import java.time.format.DateTimeFormatter


class ShiftAdapter(
    private var shifts: List<ShiftEntity>,
    private val onDelete: (ShiftEntity) -> Unit
) : RecyclerView.Adapter<ShiftAdapter.ShiftViewHolder>() {

    class ShiftViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val shiftText: TextView = view.findViewById(R.id.shiftText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShiftViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_shift, parent, false)
        return ShiftViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShiftViewHolder, position: Int) {
        val shift = shifts[position]
        val start = LocalTime.parse(shift.startTime)
        val end = LocalTime.parse(shift.endTime)
        val duration = LocalTime.parse(shift.totalTime)

        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

        val text = "${shift.date.format(dateFormatter)} — ${start.format(formatter)} - ${end.format(formatter)} | $duration h"

        holder.shiftText.text = text

        holder.itemView.setOnLongClickListener {
            onDelete(shift)
            true
        }
    }

    override fun getItemCount(): Int = shifts.size

    fun updateData(newList: List<ShiftEntity>) {
        shifts = newList
        notifyDataSetChanged()
    }

}