package com.example.shayriapp.adpater

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shayriapp.modeclass.CategoryModelClass
import com.example.shayriapp.R

class CategoryAdapter(
    var categoryList: ArrayList<CategoryModelClass>,   // Array List in set Model Class
    var click: (CategoryModelClass) -> Unit                //create invoke set model class
) : RecyclerView.Adapter<CategoryAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var categoryName: TextView = itemView.findViewById(R.id.txtCategory)   //id binding
        var layCategory: LinearLayout = itemView.findViewById(R.id.layCategory)   //id binding

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var v = LayoutInflater.from(parent.context)
            .inflate(R.layout.category_list, parent, false)   //set xml file
        return MyViewHolder(v)
    }

    override fun getItemCount(): Int {
        return categoryList.size  //set array list size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.categoryName.text = categoryList[position].categoryName   //set model class variable in text view

        holder.layCategory.setOnClickListener {  //set click listener
            click.invoke(categoryList[position])        //set position
        }

    }
}