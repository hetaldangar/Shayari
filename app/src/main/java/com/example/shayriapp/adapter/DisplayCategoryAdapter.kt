package com.example.shayriapp.adpater

import android.app.DownloadManager
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.shayriapp.R
import com.example.shayriapp.modeclass.DisplayCategoryModelClass
import java.io.File
import java.io.FileWriter


class DisplayCategoryAdapter(
    var context: Context,
    var click: (DisplayCategoryModelClass) -> Unit,     // set invoke
    var like: (Int, Int) -> Unit                  // set invoke
) : RecyclerView.Adapter<DisplayCategoryAdapter.MyViewHolder>() {

    var shariList= ArrayList<DisplayCategoryModelClass>()

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ShariItem: TextView = itemView.findViewById(R.id.txtShayari)
        var imgLikeD: ImageView = itemView.findViewById(R.id.imgLikeD)
        var imgShareD: ImageView = itemView.findViewById(R.id.imgShareD)
        var imgCopyD: ImageView = itemView.findViewById(R.id.imgCopyD)
        var imgDownload: ImageView = itemView.findViewById(R.id.imgDownload)
        var layout: LinearLayout = itemView.findViewById(R.id.layout)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var v = LayoutInflater.from(parent.context)
            .inflate(R.layout.display_category_data, parent, false)
        return MyViewHolder(v)
    }

    override fun getItemCount(): Int {
        return shariList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var shayariText = shariList[position].shayri
        holder.ShariItem.text =
            shariList[position].shayri

        //click page
        holder.layout.setOnClickListener {
            click.invoke(shariList[position])
        }

        holder.imgCopyD.setOnClickListener {
            copyToClipboard(it.context, shayariText)
        }

        holder.imgShareD.setOnClickListener {
            shareQuote(it.context, shayariText)
           }

        holder.imgDownload.setOnClickListener {
            downloadShayari(it.context, shayariText)
        }



        //like
        if (shariList[position].fav == 1) {
            holder.imgLikeD.setImageResource(R.drawable.heartcolor)

        } else {
            holder.imgLikeD.setImageResource(R.drawable.heartwhite)
        }

//like
        holder.imgLikeD.setOnClickListener {

            if (shariList[position].fav == 1) {

                like.invoke(0,shariList[position].shayri_id)
                holder.imgLikeD.setImageResource(R.drawable.heartwhite)
                shariList[position].fav = 0

            } else {

                like.invoke(1,shariList[position].shayri_id)
                holder.imgLikeD.setImageResource(R.drawable.heartcolor)

                shariList[position].fav = 1

            }

        }

    }

    private fun copyToClipboard(context: Context, text: String) {
        val clipboardManager =
            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("Quote", text)
        clipboardManager.setPrimaryClip(clipData)
        Toast.makeText(context, "Quote copied to clipboard !", Toast.LENGTH_SHORT).show()
    }

    private fun shareQuote(context: Context, text: String) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.setType("text/plain")
        shareIntent.putExtra(Intent.EXTRA_TEXT, text)
        context.startActivity(Intent.createChooser(shareIntent, "Share using"))
        }




    private fun downloadShayari(context: Context, text: String) {
        try {
            // Create a file to store the shayari text
            val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "shayari.txt")
            FileWriter(file).use { writer ->
                writer.write(text)
            }

            // Create a URI for the saved file
            val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)

            // Create a DownloadManager request using the URI of the saved file
            val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val request = DownloadManager.Request(uri)
                .setTitle("Shayari")
                .setDescription("Downloading Shayari...")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "shayari.txt")

            // Enqueue the download request
            downloadManager.enqueue(request)
            Toast.makeText(context, "Downloading Shayari...", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Log.e("Download Error", "Error downloading shayari: ${e.message}")
            Toast.makeText(context, "Error downloading Shayari", Toast.LENGTH_SHORT).show()
        }
    }

//    fun updateList(shariList: ArrayList<DisplayCategoryModelClass>) {
//        this.shariList=ArrayList()
//        this.shariList.addAll(shariList)
//        notifyDataSetChanged()
//    }

    fun updateList(shariList: ArrayList<DisplayCategoryModelClass>) {
        this.shariList.clear()
        this.shariList.addAll(shariList)
        notifyDataSetChanged()
    }


}
