package com.example.assignment3

import android.graphics.Typeface
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class RecyclerViewAdapter(private val mList : List<Game>) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolderClass>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerViewAdapter.ViewHolderClass {
        var itemView = LayoutInflater.from(parent.context).inflate(R.layout.game_layout, parent, false)
        return ViewHolderClass(itemView);
    }

    override fun onBindViewHolder(holder: RecyclerViewAdapter.ViewHolderClass, position: Int) {
        val Elements = mList[position];
        holder.name.setText((Elements.name))
        holder.rating.setText((Elements.rating).toString())


        var dollars = "$"
        if(Elements.price < 10) dollars = " " + dollars
        holder.price.setText(dollars +(Elements.price).toString())
        Glide.with(holder.cover)
            .load(Elements.coverURL)
            .into(holder.cover)
        //holder.imageView.setImageResource(Elements.image);
        holder.taglist.removeAllViews()
        for (tag in Elements.tags) {
            val chip = Chip(holder.itemView.context, null, R.style.chipStyle).apply {
                text = tag
                textSize = 22f
                isClickable = true

                val heightInDp = 52
                val heightInPx = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    heightInDp.toFloat(),
                    resources.displayMetrics
                ).toInt()

                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, // Width
                    heightInPx // Height in pixels (or use dp)
                )

                typeface = Typeface.defaultFromStyle(Typeface.BOLD)

                val cornerInDp = 10
                val cornerInPx = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    cornerInDp.toFloat(),
                    resources.displayMetrics
                ).toFloat() // Set your desired corner radius
                shapeAppearanceModel = shapeAppearanceModel.toBuilder()
                    .setAllCornerSizes(cornerInPx) // Set corner radius for all corners
                    .build()
            }
            chip.setChipBackgroundColorResource(R.color.third)
            chip.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.secondary))
            holder.taglist.addView(chip)
        }

        val params = holder.itemView.layoutParams as MarginLayoutParams
        params.bottomMargin = 40 // Reset to 0 by default

        // Apply additional margin for the last item only
        if (position == itemCount - 1) {
            params.bottomMargin = 250 // Set the desired margin size
        }

        holder.itemView.layoutParams = params
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolderClass(view : View) : RecyclerView.ViewHolder(view){
        val name : TextView = view.findViewById(R.id.name)
        val rating : TextView = view.findViewById(R.id.rating)
        val price : TextView = view.findViewById(R.id.price)
        val cover : ImageView = view.findViewById(R.id.cover)
        val taglist : ChipGroup = view.findViewById(R.id.taglist)
    }
}

