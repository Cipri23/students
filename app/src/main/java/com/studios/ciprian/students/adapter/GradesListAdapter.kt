package com.studios.ciprian.students.adapter;

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.studios.ciprian.students.R
import com.studios.ciprian.students.model.Grade

open class GradesListAdapter(
        options: FirestoreRecyclerOptions<Grade>
) : FirestoreRecyclerAdapter<Grade, GradesListAdapter.GradesHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GradesHolder {
        return GradesHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_grade, parent, false))
    }

    override fun onBindViewHolder(holder: GradesHolder, position: Int, model: Grade) {
        holder.bind(model)
    }

    class GradesHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(model: Grade) {
            itemView.findViewById<TextView>(R.id.textViewGrade).text = model.grade.toString()
            itemView.findViewById<TextView>(R.id.textViewDate).text = model.date
            itemView.findViewById<TextView>(R.id.textViewLabNumber).text = model.labNumber.toString()
        }
    }
}
