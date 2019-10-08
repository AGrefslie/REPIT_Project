package com.example.repit_project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.repit_project.Data.TestData
import com.example.repit_project.RecyclerViewAdapter.QuizAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.recycler_view


class MainActivity : AppCompatActivity() {

    private lateinit var quizAdapter: QuizAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        setUpBottomNavMenu(navController)

        initRecyclerView()
        addDataSet()
    }

    private fun setUpBottomNavMenu(navController : NavController) {
        bottom_navigation?.let{
            NavigationUI.setupWithNavController(it, navController)
        }
    }

    //Legger til data fra testklassen
    private fun addDataSet() {
        val data = TestData.createDataSet()
        quizAdapter.submitList(data)
    }

    //Initsialiserer recyckerViewt
    private fun initRecyclerView() {
        recycler_view.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            quizAdapter = QuizAdapter()
            adapter = quizAdapter
        }
    }




}
