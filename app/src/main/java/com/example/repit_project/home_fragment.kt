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


    private val QuestionList = listOf(
        Question("ok", "nei"),
        Question("ok", "nei"),
        Question("ok", "nei"),
        Question("ok", "nei")
    )

    val QuizList = listOf(
        Quiz("Test Quiz", "This is just a test", "https://images.unsplash.com/photo-1524646432175-d58115a8a854?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1050&q=80", false, QuestionList),
        Quiz("Test Quiz", "This is just a test", "https://images.unsplash.com/photo-1524646432175-d58115a8a854?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1050&q=80", false, QuestionList)
        //Quiz("Test Quiz", "This is just a test", "https://images.unsplash.com/photo-1524646432175-d58115a8a854?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1050&q=80", false, QuestionList),
        //Quiz("Test Quiz", "This is just a test", "https://images.unsplash.com/photo-1524646432175-d58115a8a854?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1050&q=80", false, QuestionList),
        //Quiz("Test Quiz", "This is just a test", "https://images.unsplash.com/photo-1524646432175-d58115a8a854?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1050&q=80", false, QuestionList)
    )



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler_view.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = QuizAdapter(QuizList)
        }
    }

}
