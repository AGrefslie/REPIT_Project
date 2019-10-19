package com.example.repit_project

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.repit_project.Models.Question
import com.example.repit_project.Models.Quiz
import com.example.repit_project.RecyclerViewAdapter.QuizAdapter
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.layout_listitem.*
import kotlinx.android.synthetic.main.layout_listitem.view.*

class home_fragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Setup FAB navigation
        FAB_CreateTestButton.setOnClickListener {
            val action = home_fragmentDirections.actionDestinationHomeToDestinationCreateTest()
            findNavController().navigate(action)
        }

        recycler_view.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = QuizAdapter(QuizList)
        }
    }

    companion object {
        private val QuestionList1 = listOf(
            Question("Hva er Hei på Spansk?", "Hola"),
            Question("Hva er Hei på Engelsk?", "Hello")
        )

        private val QuestionList2 = listOf(
            Question("Hvem er kaptein for Manchester United?", "Ashley Young"),
            Question("Hvem er norges største tallent i golf i dag?", "Viktor Hovland")
        )


        val QuizList = listOf(
            Quiz("Hello Quiz", "Hello in different languages, Hello in different languages, Hello in different languages", "https://i.ytimg.com/vi/kJ2dr9jAThY/maxresdefault.jpg", false, QuestionList1),
            Quiz("MUFC & GOLF", "No description needed", "https://www.soccerpro.com/wp-content/uploads/2018/02/ManchesterUnited_1280x800.jpg", false, QuestionList2)
            //Quiz("Test Quiz", "This is just a test", "https://images.unsplash.com/photo-1524646432175-d58115a8a854?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1050&q=80", false, QuestionList),
            //Quiz("Test Quiz", "This is just a test", "https://images.unsplash.com/photo-1524646432175-d58115a8a854?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1050&q=80", false, QuestionList),
            //Quiz("Test Quiz", "This is just a test", "https://images.unsplash.com/photo-1524646432175-d58115a8a854?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1050&q=80", false, QuestionList)
        )
    }

}
