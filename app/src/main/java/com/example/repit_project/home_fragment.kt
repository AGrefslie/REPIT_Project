package com.example.repit_project

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.repit_project.Models.Quiz
import com.google.firebase.firestore.DocumentChange.Type
import com.example.repit_project.RecyclerViewAdapter.QuizAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.android.synthetic.main.fragment_home.*

class home_fragment : Fragment() {

    private lateinit var db : FirebaseFirestore
    private lateinit var collectionQuizes : CollectionReference

    private lateinit var fireStoreListenerRegistration: ListenerRegistration

    private var swipeBackground: ColorDrawable = ColorDrawable(Color.parseColor("#FF0000"))
    private lateinit var deleteIcon: Drawable

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        db = FirebaseFirestore.getInstance()
        collectionQuizes = db.collection("Quizes")
        val privacyQuery = collectionQuizes.whereEqualTo("public", false)

        deleteIcon = ContextCompat.getDrawable(this.requireContext(), R.drawable.ic_delete)!!

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

        recycler_view.adapter = QuizAdapter(quizList)
        recycler_view.layoutManager = LinearLayoutManager(activity)

        /*DELETE FUNCTION WITH SWIPE FUNCTIONALITY AND DRAWER. (removeItem function is located in QuizAdapter)*/
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, position: Int) {
                (recycler_view.adapter as QuizAdapter).removeItem(viewHolder)
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

                val itemView = viewHolder.itemView
                val iconMargin = (itemView.height - deleteIcon.intrinsicHeight) / 2

                if (dX < 0) {
                    swipeBackground.setBounds(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
                    deleteIcon.setBounds(itemView.right - iconMargin - deleteIcon.intrinsicWidth, itemView.top + iconMargin,
                        itemView.right - iconMargin, itemView.bottom - iconMargin)
                }

                swipeBackground.draw(c)

                c.save()

                if (dX < 0 ) {
                    c.clipRect(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
                }

                deleteIcon.draw(c)

                c.restore()
            }


        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recycler_view)
        /*DELETE FUNCTION END*/
    }

    private fun createFireStoreReadListner() {
        val firebaseUser = FirebaseAuth.getInstance().currentUser

        fireStoreListenerRegistration = collectionQuizes
            .whereEqualTo("public", false)
            .whereEqualTo("userId", firebaseUser?.uid)
            .addSnapshotListener { querySnapshot, exception ->
            if (exception != null) {
                Log.w(LOGTAG, "Listen failed", exception)
                return@addSnapshotListener
            }
            for (documentChange in querySnapshot?.documentChanges!!) {
                val documentSnapshot = documentChange.document
                val Quiz = documentSnapshot.toObject(Quiz::class.java)
                Quiz.uid = documentSnapshot.id

                val pos = quizListUid.indexOf(Quiz.uid)

                when (documentChange.type) {
                    Type.ADDED -> {
                        quizList.add(Quiz)
                        quizListUid.add(Quiz.uid)
                        recycler_view.adapter?.notifyItemInserted(quizList.size-1)
                    }
                    Type.REMOVED -> {
                        quizList.removeAt(pos)
                        quizListUid.removeAt(pos)
                        recycler_view.adapter?.notifyItemRemoved(pos)
                    }
                    Type.MODIFIED -> {
                        quizList[pos] = Quiz
                        recycler_view.adapter?.notifyItemChanged(pos)
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        createFireStoreReadListner()
    }

    override fun onPause() {
        super.onPause()
        quizList = ArrayList<Quiz>()
        quizListUid = ArrayList<String>()
        fireStoreListenerRegistration.remove()
    }

    companion object {
        var quizList : MutableList<Quiz> = ArrayList()
        var quizListUid : MutableList<String> = ArrayList()

        private val LOGTAG = MainActivity::class.java.simpleName
    }


}

