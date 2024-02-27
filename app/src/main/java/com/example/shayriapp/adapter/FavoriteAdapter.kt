package com.example.shayriapp.adapter


import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shayriapp.R
import com.example.shayriapp.modeclass.FavouriteModelClass

class FavoriteAdapter(var like: (Int, Int) -> Unit) :   //create invoke
    RecyclerView.Adapter<FavoriteAdapter.MyViewHolder>() {

    var list = ArrayList<FavouriteModelClass>() //set array list in model class

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var shayriName: TextView = itemView.findViewById(R.id.txtShayari)  //Id binding
        var imgLikeD: ImageView = itemView.findViewById(R.id.imgLikeD)  //Id binding
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var v = LayoutInflater.from(parent.context)
            .inflate(R.layout.display_category_data, parent, false)  //set xml file
        return MyViewHolder(v)
    }

    override fun getItemCount(): Int {
        return list.size   //array list size set
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.shayriName.text = list[position].shayri_item    //set model class variable in text view


        //like
        holder.imgLikeD.setImageResource(R.drawable.heartcolor)


        //like
        holder.imgLikeD.setOnClickListener {


            like.invoke(0,list[position].shayri_id)
            list[position].fav = 0



            //click button and set unlike
            deleteItem(position)  //create function and set position
        }
    }

    //pass function in activity
    fun updateList(list: ArrayList<FavouriteModelClass>) {
        this.list = list
        notifyDataSetChanged()

    }

    private fun deleteItem(position: Int) {  //click button and remove data in recycle view
        list.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, list.size)
    }
}