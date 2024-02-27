package com.example.shayriapp.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shayriapp.MyDatabase
import com.example.shayriapp.adpater.CategoryAdapter
import com.example.shayriapp.databinding.ActivityMainBinding
import com.example.shayriapp.modeclass.CategoryModelClass
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {


    lateinit var binding: ActivityMainBinding

    lateinit var adapter: CategoryAdapter

    lateinit var db: MyDatabase


    var categoryList = ArrayList<CategoryModelClass>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)   //set xml file

        db = MyDatabase(this)

       // navigationView()
        initView()
    }



    private fun initView() {


        binding.imgLikePage.setOnClickListener {                    //move to one activity to second activity
            var fav = Intent(this, FavoriteActivity::class.java)
            startActivity(fav)
        }


        categoryList = db.categoryData()

        adapter =
            CategoryAdapter(categoryList) {

                var i = Intent(this, DisplayCategoryActivity::class.java)
                i.putExtra("Title", it.categoryName)
                i.putExtra("Id", it.id)


                startActivity(i)
            }
        var manger = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rcvCategory.layoutManager = manger
        binding.rcvCategory.adapter = adapter


    }
    private var doubleBackToExitPressedOnce: Boolean=false
    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()

        Handler(Looper.getMainLooper()).postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
    }
}
