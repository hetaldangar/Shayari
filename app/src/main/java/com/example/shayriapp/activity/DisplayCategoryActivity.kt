package com.example.shayriapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shayriapp.MyDatabase
import com.example.shayriapp.adpater.DisplayCategoryAdapter
import com.example.shayriapp.databinding.ActivityDisplayCategoryBinding
import com.example.shayriapp.modeclass.DisplayCategoryModelClass

class DisplayCategoryActivity : AppCompatActivity() {

    lateinit var binding: ActivityDisplayCategoryBinding

    lateinit var dbD: MyDatabase

    var shariList = ArrayList<DisplayCategoryModelClass>()
    lateinit var adapter: DisplayCategoryAdapter

    var c_ID: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDisplayCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)    //set Xml File

        dbD = MyDatabase(this)   //Database in set context Class

        initview()
    }

    private fun initview() {

        binding.imgBack.setOnClickListener {                // one activity to second activity move
            onBackPressed()
        }



        var categoryName: String? = intent.getStringExtra("Title")    // set key data in variable
        binding.txtDisplayTitle.text =
            categoryName                 //variable set in textview

        c_ID = intent.getIntExtra("Id", 0)  // set key data in variable


        adapter = DisplayCategoryAdapter(
            this,
            {   //create adapter class object and pass parameter
                var i = Intent(this, ShayriDishplayActivity::class.java)
                i.putExtra("shariItem", it.shayri)
                startActivity(i)
                finish()

            },
            {  fav ,shayri_id->
                dbD.Fav_updateRecord(fav,shayri_id)  //record update

            }
        )

        var mangar = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rcvCategoryData.layoutManager = mangar
        binding.rcvCategoryData.adapter = adapter


    }

    override fun onResume() {
        super.onResume()
        shariList = dbD.shayriData(c_ID)   //variable set in textview
        adapter.updateList(shariList)

    }
}