package com.studios.ciprian.students.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.studios.ciprian.students.R
import com.studios.ciprian.students.model.Group

open class GroupsAdapter(
        options: FirestoreRecyclerOptions<Group>,
        private val clickListener: (Group) -> Unit,
) : FirestoreRecyclerAdapter<Group, GroupHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            GroupHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_grid_view, parent, false))

    override fun onBindViewHolder(holder: GroupHolder, position: Int, model: Group) {
        holder.bind(model)
        holder.itemView.setOnClickListener {
            clickListener(model)
        }
    }
}

class GroupHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(group: Group) {
        val textView = itemView.findViewById<TextView>(R.id.grid_text)
        textView.text = group.toString()
    }
}