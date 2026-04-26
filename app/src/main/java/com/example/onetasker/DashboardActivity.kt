package com.example.onetasker

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.onetasker.databinding.ActivityDashboardBinding

class DashboardActivity : AppCompatActivity() {

    companion object {
        val taskList = ArrayList<String>()
    }

    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.fabAdd.setOnClickListener {
            startActivity(Intent(this, AddTaskActivity::class.java))
        }

        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    binding.containerHome.visibility = View.VISIBLE
                    binding.containerProfile.visibility = View.GONE
                    true
                }
                R.id.nav_profile -> {
                    binding.containerHome.visibility = View.GONE
                    binding.containerProfile.visibility = View.VISIBLE
                    true
                }
                else -> false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loadTasks()
    }

    private fun loadTasks() {
        binding.llTaskList.removeAllViews()

        if (taskList.isEmpty()) {
            binding.llEmptyState.visibility = View.VISIBLE
            return
        }

        binding.llEmptyState.visibility = View.GONE

        for ((index, task) in taskList.withIndex()) {
            val itemView = layoutInflater.inflate(R.layout.item_task, binding.llTaskList, false)

            val parts = task.split(" - ", limit = 2)
            itemView.findViewById<TextView>(R.id.tvTaskTitle).text = parts[0]

            val descView = itemView.findViewById<TextView>(R.id.tvTaskDesc)
            if (parts.size > 1 && parts[1].isNotEmpty()) {
                descView.text = parts[1]
                descView.visibility = View.VISIBLE
            } else {
                descView.visibility = View.GONE
            }

            itemView.findViewById<ImageButton>(R.id.btnEdit).setOnClickListener {
                val intent = Intent(this, AddTaskActivity::class.java)
                intent.putExtra("TASK_INDEX", index)
                startActivity(intent)
            }

            itemView.findViewById<ImageButton>(R.id.btnDelete).setOnClickListener {
                AlertDialog.Builder(this)
                    .setTitle(getString(R.string.delete_task))
                    .setMessage(getString(R.string.confirm_delete))
                    .setPositiveButton(getString(R.string.delete_task)) { _, _ ->
                        taskList.removeAt(index)
                        loadTasks()
                    }
                    .setNegativeButton("Batal", null)
                    .show()
            }

            binding.llTaskList.addView(itemView)
        }
    }
}
