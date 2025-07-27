package com.diffy.broke.domain.repository

import com.diffy.broke.core.Result
import kotlinx.coroutines.flow.Flow


interface CommonDbRepo {
    fun backup(): Flow<Result<String>>
    fun restore(): Flow<Result<String>>
}