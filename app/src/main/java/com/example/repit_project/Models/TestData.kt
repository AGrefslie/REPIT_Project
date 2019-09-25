package com.example.repit_project.Models

class TestData {

    companion object {

        fun createDataSet(): ArrayList<Quiz> {
            val list = ArrayList<Quiz>()
            list.add(
                Quiz(
                    "Spanish Test!",
                    "Test for friday 23.6.2019!",
                    "https://images.unsplash.com/photo-1434030216411-0b793f4b4173?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1950&q=80"
                )
            )
            list.add(
                Quiz(
                    "Anatomy Test!",
                    "Test for Tuesday 04.2.2019!",
                    "https://images.unsplash.com/photo-1524646432175-d58115a8a854?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1050&q=80"
                )
            )
            list.add(
                Quiz(
                    "Spanish Test!",
                    "Test for friday 23.6.2019!",
                    "https://images.unsplash.com/photo-1434030216411-0b793f4b4173?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1950&q=80"
                )
            )
            list.add(
                Quiz(
                    "Anatomy Test!",
                    "Test for Tuesday 04.2.2019!",
                    "https://images.unsplash.com/photo-1524646432175-d58115a8a854?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1050&q=80"
                )
            )



            return list
        }
    }
}