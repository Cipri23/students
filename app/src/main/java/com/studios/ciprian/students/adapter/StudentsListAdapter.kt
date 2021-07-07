package com.studios.ciprian.students.adapter;

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.studios.ciprian.students.R
import com.studios.ciprian.students.model.Student
import com.studios.ciprian.students.util.Validator

/**
 * Created with Love by Lucian and Pi on 29.03.2016.
 */
open class StudentsListAdapter(
        options: FirestoreRecyclerOptions<Student>,
        private val deleteListener: (Student) -> Unit,
        private val clickListener: (Student) -> Unit,
        private val addPresenceClick: (Student) -> Unit,
        private val addGradeClick: (Student) -> Unit,
) : FirestoreRecyclerAdapter<Student, StudentsListAdapter.StudentHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentHolder {
        return StudentHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_student, parent, false))
    }

    override fun onBindViewHolder(holder: StudentHolder, position: Int, model: Student) {
        holder.bind(model, addPresenceClick, addGradeClick)
        holder.itemView.setOnClickListener {
            clickListener(model)
        }
        holder.itemView.setOnLongClickListener {
            deleteListener(model)
            return@setOnLongClickListener true
        }
    }

    class StudentHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @SuppressLint("SetTextI18n")
        fun bind(
                student: Student,
                addPresenceClick: (Student) -> Unit,
                addGradeClick: (Student) -> Unit,
        ) {
            FirebaseFirestore.getInstance()
                    .collection("presences")
                    .whereEqualTo("matricol", student.matricol)
                    .addSnapshotListener { value, e ->
                        if (e != null) {
                            Log.w("logg", "Listen failed.", e)
                            return@addSnapshotListener
                        }

                        val presencesPerStudent = value?.size() ?: 0
                        itemView.findViewById<TextView>(R.id.student_presences_status).text = "$presencesPerStudent/14"
                    }

            val textViewName = itemView.findViewById<TextView>(R.id.student_name_layout)
            val textViewUsername = itemView.findViewById<TextView>(R.id.student_username_layout)
            val buttonAdd = itemView.findViewById<Button>(R.id.student_list_button_1)
            val buttonPresence = itemView.findViewById<Button>(R.id.student_list_button_2)

            textViewName.text = student.name + " " + student.surname
            textViewUsername.text = student.matricol

            if (Validator.isCurrentUser(student.name + " " + student.surname)) {
                textViewUsername.visibility = View.VISIBLE
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    (itemView as CardView).setCardBackgroundColor(
                            itemView.context.resources.getColor(R.color.button_color, null)
                    )
                }
            } else {
                if (Validator.userIsStudent()) {
                    textViewUsername.visibility = View.GONE
                } else {
                    textViewUsername.visibility = View.VISIBLE
                }
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    (itemView as CardView).setCardBackgroundColor(
                            itemView.context.resources.getColor(android.R.color.white, null)
                    )
                }
            }

            buttonAdd.visibility = if (Validator.userIsStudent()) View.GONE else View.VISIBLE
            buttonPresence.visibility = if (Validator.userIsStudent()) View.GONE else View.VISIBLE
            buttonAdd.setOnClickListener { addGradeClick(student) }
            buttonPresence.setOnClickListener { addPresenceClick(student) }
        }
    }
}
