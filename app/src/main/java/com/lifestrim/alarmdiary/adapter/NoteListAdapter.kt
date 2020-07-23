package com.lifestrim.alarmdiary.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lifestrim.alarmdiary.R
import com.lifestrim.alarmdiary.db.entity.Note
import com.lifestrim.alarmdiary.db.repository.CategoryRepository
import com.lifestrim.alarmdiary.db.repository.NoteRepository
import com.lifestrim.alarmdiary.util.DateConverter
import com.lifestrim.alarmdiary.viewmodel.NoteViewModel
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
            Log.d("TAG", "NoteViewHolder init")
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
            // check if content is the same
            // equals using data class
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_item, parent, false)
        Log.d("TAG", "onCreateViewHolder")
        return NoteViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val current = noteFilterList[position]

        Log.d("TAG", "onBindViewHolder")

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

    override fun getItemCount() = noteFilterList.size


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

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    noteFilterList = notes
                    Log.d("TAG", "Search empty work")
                } else {
                    val resultList = ArrayList<Note>()
                    for (row in notes) {
                        if (row.noteTitle.toLowerCase(Locale.ROOT)
                                .contains(charSearch.toLowerCase(Locale.ROOT))
                        ) {
                            resultList.add(row)
                            Log.d("TAG", "Search result: $row")
                        }
                    }
                    noteFilterList = resultList
                    Log.d("TAG", "Search not empty work, resultList: $resultList")
                }
                val filterResults = FilterResults()
                filterResults.values = noteFilterList
                Log.d("TAG", "Search filterResults: ${filterResults.values}")
                return filterResults
            }


            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                noteFilterList = results?.values as List<Note>
                notifyDataSetChanged()
                Log.d("TAG", "Search publish result")
            }

        }
    }

}

