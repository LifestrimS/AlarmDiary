package com.lifestrim.alarmdiary.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lifestrim.alarmdiary.R
import com.lifestrim.alarmdiary.db.entity.Note
import com.lifestrim.alarmdiary.util.DateConverter
import java.util.*
import kotlin.collections.ArrayList

class NoteListAdapter : ListAdapter<Note, NoteListAdapter.NoteViewHolder>(DiffCallback()),
    Filterable {


    private var notes = emptyList<Note>()
    var noteFilterList = notes

    var listener: OnItemClickListener? = null

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val noteItemTitle: TextView = itemView.findViewById(R.id.twNoteTitle)
        val noteItemText: TextView = itemView.findViewById(R.id.twNoteText)
        val noteItemDate: TextView = itemView.findViewById(R.id.twNoteDate)
        val noteBackStrip: View = itemView.findViewById(R.id.noteBackStrip)

        init {
            noteFilterList = notes
            itemView.setOnClickListener {
                val position = adapterPosition
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener!!.onItemClick(notes[position])
                }
            }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            if (oldItem.id != newItem.id) return false
            // check if id is the same
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_item, parent, false)
        return NoteViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val current = noteFilterList[position]

        holder.noteItemTitle.text = current.noteTitle
        holder.noteItemText.text = current.noteText
        holder.noteItemDate.text = DateConverter().getDayMonthHoursMinute(current.noteCreateDate)

        val color: Int
        color = current.category.colorCategory ?: Color.rgb(255, 255, 255)
        holder.noteBackStrip.setBackgroundColor(color)

    }

    internal fun setNotes(notes: List<Note>) {
        this.notes = notes
        this.noteFilterList = notes
        notifyDataSetChanged()
    }

    fun setNotesByCategory(notes: List<Note>, categoryName: String) {
        val notesWithCategory: MutableList<Note> = mutableListOf()
        notes.forEach() {
            if (it.category.nameCategory == categoryName) {
                notesWithCategory.add(it)
            }
        }
        this.notes = notesWithCategory
        this.noteFilterList = notesWithCategory
        notifyDataSetChanged()
    }



    override fun getItemCount() = noteFilterList.size


    fun getNoteAt(position: Int): Note {
        return notes[position]
    }

    interface OnItemClickListener {
        fun onItemClick(note: Note)
    }

    fun setOnClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    noteFilterList = notes
                } else {
                    val resultList = ArrayList<Note>()
                    for (row in notes) {
                        if (row.noteTitle.toLowerCase(Locale.ROOT)
                                .contains(charSearch.toLowerCase(Locale.ROOT)) ||
                            row.noteText.toLowerCase(Locale.ROOT)
                                .contains(charSearch.toLowerCase(Locale.ROOT))
                        ) {
                            resultList.add(row)
                        }
                    }
                    noteFilterList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = noteFilterList
                return filterResults
            }


            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                noteFilterList = results?.values as List<Note>
                notifyDataSetChanged()
            }

        }
    }

}

