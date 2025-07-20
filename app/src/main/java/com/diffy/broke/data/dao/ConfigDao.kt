package com.diffy.broke.data.dao

import androidx.room.Dao
import com.diffy.broke.data.entity.Config

@Dao
interface ConfigDao: GenericDao<Config> {
}