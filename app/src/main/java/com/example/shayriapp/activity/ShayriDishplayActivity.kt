package com.example.shayriapp.activity

import android.Manifest
import android.app.Activity
import android.app.Instrumentation
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.shayriapp.R
import com.example.shayriapp.databinding.ActivityShayriDishplayBinding



class ShayriDishplayActivity : AppCompatActivity() {


    lateinit var binding: ActivityShayriDishplayBinding   //Activity Binding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShayriDishplayBinding.inflate(layoutInflater)
        setContentView(binding.root)    //set xml file

        binding.imgAdd.setOnClickListener {
            showImageSourceDialog()
        }


        //showCameraGalleryDialog()
        intiView()
    }

    private fun showImageSourceDialog() {

        val dialogView = layoutInflater.inflate(R.layout.dialog_item_file, null)
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle("Select Image Source")
            .create()

        dialogView.findViewById<LinearLayout>(R.id.layCamera).setOnClickListener {
            // Handle camera selection
            openCamera()
            dialog.dismiss()
        }

        dialogView.findViewById<LinearLayout>(R.id.layGallery).setOnClickListener {
            // Handle gallery selection
            openGallery()
            dialog.dismiss()
        }

        dialog.show()
    }


    private fun intiView() {

         fun copyShayariToClipboard(shayariText: String) {
            val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Shayari", shayariText)
            clipboardManager.setPrimaryClip(clip)
            Toast.makeText(this, "Shayari copied to clipboard", Toast.LENGTH_SHORT).show()
        }

// Place this code outside the OnClickListener
        val shayariName: String? = intent.getStringExtra("shariItem")    // Set key in variable
        binding.txtShariDisplay.text = shayariName                       // Variable set in text view

        binding.imgBack.setOnClickListener {   // Move to one activity to second activity
            onBackPressed()
        }

        binding.imgSaveD.setOnClickListener {
            val z: View = binding.relLayout
            z.isDrawingCacheEnabled = true
            val totalHeight: Int = z.height
            val totalWidth: Int = z.width
            z.layout(0, 0, totalWidth, totalHeight)
            z.buildDrawingCache(true)
            val bm: Bitmap = Bitmap.createBitmap(z.getDrawingCache())
            z.isDrawingCacheEnabled = false
            MediaStore.Images.Media.insertImage(contentResolver, bm, null, null)
            //    takeScreenshot(this, it)
            Toast.makeText(this, "Image saved", Toast.LENGTH_SHORT).show()
        }



        binding.imgShare.setOnClickListener(View.OnClickListener { s: View? ->
            val ShareIntent = Intent(Intent.ACTION_SEND)
            ShareIntent.type = "text/plain"
            ShareIntent.putExtra(Intent.EXTRA_TEXT, shayariName)
            startActivity(ShareIntent)
        })

// Place this code inside the OnClickListener for imgCopyD
        binding.imgCopyD.setOnClickListener {
            val shayariText = binding.txtShariDisplay.text.toString()
            copyShayariToClipboard(shayariText)

            // Write permission to access the storage
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                1
            )
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                1
            )
        }
    }

    //-------------------------------
//    fun showCameraGalleryDialog() {
//        val dialogView = layoutInflater.inflate(R.layout.dialog_item_file, null)
//        val dialog = AlertDialog.Builder(this)
//            .setView(dialogView)
//            .setTitle("Select Image Source")
//            .create()
//
//        dialogView.findViewById<LinearLayout>(R.id.layCamera).setOnClickListener {
//            // Handle camera selection
//            openCamera()
//            dialog.dismiss()
//        }
//
//        dialogView.findViewById<LinearLayout>(R.id.layGallery).setOnClickListener {
//            // Handle gallery selection
//            openGallery()
//            dialog.dismiss()
//        }
//
//        dialog.show()
//    }
//-----------------------------------
    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE)
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (cameraIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
        } else {
            Toast.makeText(this, "No camera app found", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val GALLERY_REQUEST_CODE = 100
        private const val CAMERA_REQUEST_CODE = 1001
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                GALLERY_REQUEST_CODE -> {
                    // Gallery image selected, handle it here
                    val selectedImageUri: Uri? = data?.data
                    // Now you can do whatever you want with the selected image URI
                    // For example, display it in an ImageView
                    binding.imgShow.setImageURI(selectedImageUri)
                }
                CAMERA_REQUEST_CODE -> {
                    // Camera image captured, handle it here
                    val imageBitmap = data?.extras?.get("data") as Bitmap
                    // Set the captured image to ImageView
                    binding.imgShow.setImageBitmap(imageBitmap)
                    // Do something with the imageBitmap, like displaying it in an ImageView
                }
            }
        }
    }

//          fun copyShayariToClipboard(shayariText: String) {
//            val clipboardManager =
//                getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
//            val clip = ClipData.newPlainText("Shayari", shayariText)
//            clipboardManager.setPrimaryClip(clip)
//            Toast.makeText(this, "Shayari copied to clipboard", Toast.LENGTH_SHORT).show()
//        }
    }


