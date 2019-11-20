package com.example.repit_project.RecyclerViewAdapter


import android.content.ContentValues.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.repit_project.Models.Quiz
import com.example.repit_project.R
import com.example.repit_project.collections_fragmentDirections
import com.example.repit_project.home_fragment
import com.example.repit_project.home_fragmentDirections
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.layout_listitem.view.*

class QuizAdapter(private val list: MutableList<Quiz>, whichFragment : Int) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var removedPosition: Int = 0
    private var removedItem: Quiz = Quiz()

    private var items : MutableList<Quiz> = list
    private var WhichFragment = whichFragment

    private lateinit var db : FirebaseFirestore
    private lateinit var collectionQuizes : CollectionReference


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        db = FirebaseFirestore.getInstance()
        collectionQuizes = db.collection("Quizes")

        return QuizViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.layout_listitem, parent, false)
        )

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is QuizViewHolder -> {
                holder.bind(items.get(position))


                val currentQuizItem = items[position]
                holder.bind(currentQuizItem)

                holder.startTestButton.setOnClickListener{ view ->
                    if (WhichFragment == 1) {
                        val action = home_fragmentDirections.actionDestinationHomeToAnswerTestFragment(list[position])
                        findNavController(view).navigate(action)
                    }
                    if (WhichFragment == 2) {
                        val action = collections_fragmentDirections.actionDestinationCollectionsToAnswerTestFragment(list[position])
                        findNavController(view).navigate(action)
                    }

                }
            }
        }
    }

    fun removeItem(viewHolder: RecyclerView.ViewHolder){

        removedPosition = viewHolder.adapterPosition
        removedItem = list[viewHolder.adapterPosition]

        Snackbar.make(viewHolder.itemView, "Test deleted. Press UNDO if you regret your decision.", Snackbar.LENGTH_LONG).setAction("UNDO") {

            collectionQuizes.add(removedItem)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot written with ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }
        }.show()

        /* Add logic for firestoe delete */
        collectionQuizes.document(removedItem.uid)
            .delete()
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun submitList(QuizList : MutableList<Quiz>) {
        items = QuizList
    }

    class QuizViewHolder constructor (itemView : View): RecyclerView.ViewHolder(itemView) {

        val quizImage = itemView.quiz_image
        val quizTitle = itemView.quiz_title
        val quizDescription = itemView.quiz_description
        val startTestButton = itemView.startTestBtn

        fun bind(QuizObject : Quiz) {

            quizTitle.setText(QuizObject.title)
            quizDescription.setText(QuizObject.description)

            val requestOptions = RequestOptions()
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)

            Glide.with(itemView.context)
                .applyDefaultRequestOptions(requestOptions)
                .load(QuizObject.image)
                .into(quizImage)
        }
    }
}