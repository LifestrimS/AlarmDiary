package com.lifestrim.alarmdiary.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lifestrim.alarmdiary.R
import com.lifestrim.alarmdiary.db.entity.Note
import com.lifestrim.alarmdiary.util.DateConverter

class NoteListAdapter : ListAdapter<Note, NoteListAdapter.NoteViewHolder>(DiffCallback()) {

    private var notes = emptyList<Note>()

    var listener: OnItemClickListener? = null

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val noteItemTitle: TextView = itemView.findViewById(R.id.twNoteTitle)
        val noteItemText: TextView = itemView.findViewById(R.id.twNoteText)
        val noteItemDate: TextView = itemView.findViewById(R.id.twNoteDate)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener!!.onItemClick(notes[position])
                }
            }
        }
    }

    private class DiffCallback: DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            if (oldItem.id != newItem.id) return false
            // check if id is the same
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            // check if content is the same
            // equals using data class
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_item, parent, false)
        return NoteViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val current = notes[position]

        holder.noteItemTitle.text = current.noteTitle
        holder.noteItemText.text = current.noteText
        holder.noteItemDate.text = DateConverter().getDayMonthHoursMinute(current.noteCreateDate)

    }

    internal fun setNotes(notes: List<Note>) {
        this.notes = notes
        notifyDataSetChanged()
    }

    override fun getItemCount() = notes.size

    fun getNoteAt(position: Int): Note {
        return notes[position]
    }

    interface OnItemClickListener {
        fun onItemClick(note: Note)
    }

    fun setOnClickListener(listener: OnItemClickListener) {
        this.listener = listener
//        this is setter method to pass the interface reference to we can use it in
//        onViewBindHolder
    }

}

