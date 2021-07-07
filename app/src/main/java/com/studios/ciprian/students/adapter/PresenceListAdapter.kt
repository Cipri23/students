package com.studios.ciprian.students.adapter;

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.studios.ciprian.students.R
import com.studios.ciprian.students.model.Presence

open class PresenceListAdapter(
        options: FirestoreRecyclerOptions<Presence>
) : FirestoreRecyclerAdapter<Presence, PresenceListAdapter.PresenceHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PresenceHolder {
        return PresenceHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_presence, parent, false))
    }

    override fun onBindViewHolder(holder: PresenceHolder, position: Int, model: Presence) {
        holder.bind(model)
    }

    class PresenceHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(model: Presence) {
            itemView.findViewById<TextView>(R.id.textViewDate).text = model.date
            itemView.findViewById<TextView>(R.id.textViewLabNumber).text = model.labNumber.toString()
        }
    }
}
