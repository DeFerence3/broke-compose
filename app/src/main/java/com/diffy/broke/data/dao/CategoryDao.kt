package com.diffy.broke.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.diffy.broke.data.entity.Category
import com.diffy.broke.domain.model.CategorySpendM
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao: GenericDao<Category> {

    @Query("SELECT * FROM category WHERE category_name LIKE :name || '%'")
    suspend fun searchCategory(name: String): List<Category>

    @Query("""
        select c.category_name name,SUM(amount) spent,cb.budget from `transaction` t
        inner join category c on c.id = t.category_id
        left join category_budget cb on cb.category_id = c.id
        left join monthly_budget mb on mb.id = cb.monthly_budget_id
        WHERE CAST(strftime('%m', t.date) AS INTEGER) = :month AND CAST(strftime('%Y', t.date) AS INTEGER) = :year
        group by c.category_name
    """)
    fun getSpendsByCategory(month: Int,year: Int): Flow<List<CategorySpendM>>
}