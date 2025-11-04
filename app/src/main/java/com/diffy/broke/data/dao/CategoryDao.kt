package com.diffy.broke.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.diffy.broke.data.entity.Category

@Dao
interface CategoryDao: GenericDao<Category> {

    @Query("SELECT * FROM category WHERE category_name LIKE :name || '%'")
    suspend fun searchCategory(name: String): List<Category>
}