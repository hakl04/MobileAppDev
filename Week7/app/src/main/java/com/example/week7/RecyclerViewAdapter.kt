package com.example.week7

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewAdapter(private val mList : List<DataClass>) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolderClass>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerViewAdapter.ViewHolderClass {
        var itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return ViewHolderClass(itemView);
    }

    override fun onBindViewHolder(holder: RecyclerViewAdapter.ViewHolderClass, position: Int) {
        val Elements = mList[position];
        holder.name.setText((Elements.name))
        holder.rating.setText("Rating: " + (Elements.rating.toString()))
        holder.imageView.setImageResource(Elements.image);
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolderClass(view : View) : RecyclerView.ViewHolder(view){
        val name : TextView = view.findViewById(R.id.name)
        val rating : TextView = view.findViewById(R.id.rating)
        val imageView : ImageView = view.findViewById(R.id.imageView)
    }
}

