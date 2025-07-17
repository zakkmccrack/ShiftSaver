package com.example.shiftsaver.ui

import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shiftsaver.R
import com.example.shiftsaver.data.AppDatabase
import com.example.shiftsaver.data.entity.ShiftEntity
import com.example.shiftsaver.databinding.ActivityMonthDetailBinding
import com.example.shiftsaver.viewmodel.MonthDetailViewModel
import com.example.shiftsaver.viewmodel.MonthDetailViewModelFactory
import java.time.Duration
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.temporal.Temporal

class MonthDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMonthDetailBinding
    private lateinit var viewModel: MonthDetailViewModel
    private lateinit var adapter: ShiftAdapter

    private var monthId: Int = -1
    private var monthName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMonthDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        monthId = intent.getIntExtra("MONTH_ID", -1)
        monthName = intent.getStringExtra("MONTH_NAME") ?: "Mese"

        binding.monthTitleText.text = monthName

        val dao = AppDatabase.getDatabase(applicationContext).shiftDao()
        val factory = MonthDetailViewModelFactory(dao)
        viewModel = ViewModelProvider(this, factory)[MonthDetailViewModel::class.java]

        adapter = ShiftAdapter(emptyList()) { shift ->
            confirmDeleteShift(shift)
        }

        binding.shiftRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.shiftRecyclerView.adapter = adapter

        binding.addShiftButton.setOnClickListener {
            showAddShiftDialog()
        }

        viewModel.getShiftsForMonth(monthId).observe(this) { shifts ->
            adapter.updateData(shifts)
            binding.totalHoursText.text = "Totale ore: ${calculateTotalHours(shifts)}"
        }
    }

    private fun calculateTotalHours(shifts: List<ShiftEntity>): String {
        var totalMinutes = 0L
        val formatter = DateTimeFormatter.ofPattern("HH:mm")

        for (shift in shifts) {
            val start = try {
                LocalTime.parse(shift.startTime, formatter)
            } catch (e: DateTimeParseException) {
                continue // salta turno malformato
            }

            val end = try {
                LocalTime.parse(shift.endTime, formatter)
            } catch (e: DateTimeParseException) {
                continue
            }

            val duration = Duration.between(start, end)
            if (!duration.isNegative) {
                totalMinutes += duration.toMinutes()
            }else{
                totalMinutes += convertNegative(start,end)
            }
        }

        val hours = totalMinutes / 60
        val minutes = totalMinutes % 60
        return if (minutes == 0L) "$hours h" else "$hours h $minutes min"
    }

    private fun showAddShiftDialog() {

        val dialogView = layoutInflater.inflate(R.layout.dialog_add_shift, null)
        val dateInput = dialogView.findViewById<EditText>(com.example.shiftsaver.R.id.editDate)
        val startInput = dialogView.findViewById<EditText>(com.example.shiftsaver.R.id.editStartTime)
        val endInput = dialogView.findViewById<EditText>(com.example.shiftsaver.R.id.editEndTime)

        AlertDialog.Builder(this)
            .setTitle("Aggiungi turno")
            .setView(dialogView)
            .setPositiveButton("Aggiungi") { _, _ ->
                val formatter = DateTimeFormatter.ofPattern("HH:mm")

                val date = dateInput.text.toString()
                val start = startInput.text.toString()
                val end = endInput.text.toString()

                val startFormatted = LocalTime.parse(start, formatter)
                val endFormatted = LocalTime.parse(end, formatter)

                val minutes = if (endFormatted.isAfter(startFormatted)) {
                    Duration.between(startFormatted, endFormatted).toMinutes()
                } else {
                    convertNegative(startFormatted, endFormatted)
                }
                val hours = minutes / 60
                val remainingMinutes = minutes % 60
                val strTotal = String.format("%02d:%02d", hours, remainingMinutes)

                if (date.isNotBlank() && start.isNotBlank() && end.isNotBlank()) {
                    val shift = ShiftEntity(
                        id = 0,
                        monthId = monthId,
                        date = date,
                        startTime = start,
                        endTime = end,
                        totalTime = strTotal
                    )
                    viewModel.insertShift(shift)
                } else {
                    Toast.makeText(this, "Compila tutti i campi", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Annulla", null)
            .show()
    }

    private fun convertNegative(start: Temporal, end: Temporal) : Long{
        val mid = LocalTime.parse("23:00")
        val mid2 = LocalTime.parse("00:00")
        val half = (Duration.between(start, mid))
        val half2 = (Duration.between(mid2, end))
        return ((half + half2).plusHours(1)).toMinutes()
    }

    private fun confirmDeleteShift(shift: ShiftEntity) {
        AlertDialog.Builder(this)
            .setTitle("Elimina turno")
            .setMessage("Vuoi eliminare questo turno?")
            .setPositiveButton("Elimina") { _, _ ->
                viewModel.deleteShift(shift)
            }
            .setNegativeButton("Annulla", null)
            .show()
    }
}
