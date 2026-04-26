package com.example.onetasker

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.onetasker.databinding.ActivityAddTaskBinding

class AddTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddTaskBinding
    private var taskIndex: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarAddTask)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        taskIndex = intent.getIntExtra("TASK_INDEX", -1)

        if (taskIndex != -1) {
            val existing = DashboardActivity.taskList[taskIndex]
            val parts = existing.split(" - ", limit = 2)
            binding.etTaskName.setText(parts[0])
            if (parts.size > 1) binding.etTaskDesc.setText(parts[1])
            supportActionBar?.title = getString(R.string.title_edit_task)
            binding.btnSaveTask.text = getString(R.string.edit_task)
        } else {
            supportActionBar?.title = getString(R.string.title_add_task)
        }

        binding.btnSaveTask.setOnClickListener {
            val taskName = binding.etTaskName.text.toString().trim()
            val taskDesc = binding.etTaskDesc.text.toString().trim()

            if (taskName.isEmpty()) {
                Toast.makeText(this, "Nama tugas tidak boleh kosong!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val taskString = if (taskDesc.isEmpty()) taskName else "$taskName - $taskDesc"

            if (taskIndex != -1) {
                DashboardActivity.taskList[taskIndex] = taskString
                Toast.makeText(this, "Tugas berhasil diperbarui!", Toast.LENGTH_SHORT).show()
            } else {
                DashboardActivity.taskList.add(taskString)
                Toast.makeText(this, "Tugas berhasil disimpan!", Toast.LENGTH_SHORT).show()
            }

            finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
