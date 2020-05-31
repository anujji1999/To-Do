package com.anuj.to_do.Adapters

import android.graphics.Color
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.anuj.to_do.R
import com.anuj.to_do.clickListeners.ItemClickListener
import com.anuj.to_do.db.Note

class NotesAdapter(val list : List<Note>, val itemClickListener : ItemClickListener) : RecyclerView.Adapter<NotesAdapter.ViewHolder>() {

    val colorList = arrayOf("#FFCCCC", "#FFFFCC", "#E5FFCC", "#CCFFFF", "#CCCCFF", "#FFCCE5")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.notes_adapter_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.constraintLayoutItem.setBackgroundColor(Color.parseColor(colorList[position% colorList.size]))
        val note = list[position]
        val description = note.description
        holder.textViewDescription.text = description
        holder.checkBoxMarkStatus.isChecked = note.isTaskCompleted

        if(note.isTaskCompleted){
            holder.textViewDescription.apply {
                paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                text = note.description
            }
            holder.checkBoxMarkStatus.visibility = CheckBox.INVISIBLE
            holder.imageViewDelete.visibility = ImageView.VISIBLE
            holder.imageViewUndo.visibility = ImageView.VISIBLE
            holder.imageViewDelete.isClickable = true
            holder.imageViewUndo.isClickable = true
        }

        if(note.isPriority){
            holder.imageViewPriority.visibility = ImageView.VISIBLE
        }

        holder.imageViewDelete.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                itemClickListener.onDelete(note)
            }
        })

        holder.imageViewUndo.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                note.isTaskCompleted = false
                itemClickListener.onUndo(note)
            }
        })

        holder.checkBoxMarkStatus.setOnCheckedChangeListener(object:
            CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                note.isTaskCompleted = isChecked
                itemClickListener.onCheck((note))
            }
        })
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val constraintLayoutItem : ConstraintLayout = itemView.findViewById(R.id.constraintLayoutItem)
        val cardViewItem : CardView = itemView.findViewById(R.id.cardViewItem)
        val imageViewPriority : ImageView = itemView.findViewById(R.id.imageViewPriority)
        val textViewDescription : TextView = itemView.findViewById(R.id.textViewDescription)
        val checkBoxMarkStatus : CheckBox = itemView.findViewById(R.id.checkboxMarkStatus)
        val imageViewDelete : ImageView = itemView.findViewById(R.id.imageViewDelete)
        val imageViewUndo : ImageView = itemView.findViewById(R.id.imageViewUndo)
    }
}