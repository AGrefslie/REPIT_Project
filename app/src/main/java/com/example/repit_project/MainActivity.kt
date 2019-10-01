package com.example.repit_project

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.repit_project.Models.Question
import com.example.repit_project.Models.Quiz
import com.example.repit_project.Models.TestData
import com.example.repit_project.RecyclerViewAdapter.QuizAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var quizAdapter : QuizAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initRecyclerView()
        addDataSet()
    }

    private fun addDataSet() {
        val data = TestData.createDataSet()
        quizAdapter.submitList(data)
    }

    private fun initRecyclerView() {
        recycler_view.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            quizAdapter = QuizAdapter()
            adapter = quizAdapter
            
        }
    }



    @SuppressLint("WrongConstant")
    fun startTest(view: View) {
        Toast.makeText(applicationContext, "Test Started", 4).show()



    }
}
