package com.example.shiftsaver

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shiftsaver.data.entity.MonthEntity
import com.example.shiftsaver.ui.MonthAdapter
import com.example.shiftsaver.ui.MonthDetailActivity
import com.example.shiftsaver.viewmodel.MonthListViewModel
import java.time.LocalDate
import kotlin.getValue

class MainActivity : ComponentActivity() {

    private val viewModel: MonthListViewModel by viewModels()
    private lateinit var adapter: MonthAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView: RecyclerView = findViewById(R.id.monthRecyclerView)
        val addButton: Button = findViewById(R.id.addMonthButton)

        adapter = MonthAdapter(
            emptyList(),
            onClick = { selectedMonth ->
                val intent = Intent(this, MonthDetailActivity::class.java).apply {
                    putExtra("MONTH_ID", selectedMonth.id)
                    putExtra("MONTH_NAME", "${selectedMonth.month}/${selectedMonth.year}")
                }
                startActivity(intent)
            },
            onDelete = { month ->
                confirmDeleteMonth(month)
            }
        )



        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        viewModel.allMonths.observe(this) { months ->
            adapter.updateData(months)
        }

        addButton.setOnClickListener {
            val today = LocalDate.now()
            viewModel.addMonth(today.year, today.monthValue)
        }
    }
    private fun confirmDeleteMonth(month: MonthEntity) {
        val dialog = androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Elimina mese")
            .setMessage("Vuoi eliminare ${month.month}/${month.year}?")
            .setPositiveButton("Elimina") { _, _ ->
                viewModel.delMonth(month.id)
            }
            .setNegativeButton("Annulla", null)
            .create()

        dialog.show()
    }
}