package com.example.shayriapp.modeclass

data class CategoryModelClass(var id: Int, val categoryName: String)

data class DisplayCategoryModelClass(var shayri_id:Int,var shayri:String,var category_Id:Int,var fav:Int)

data class FavouriteModelClass(var shayri_id:Int,var shayri_item:String,var fav:Int)