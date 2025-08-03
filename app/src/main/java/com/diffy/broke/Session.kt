package com.diffy.broke

import com.diffy.broke.domain.model.OrderBy
import com.diffy.broke.domain.model.SortView
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Session @Inject constructor() {
    val orderBy: OrderBy = OrderBy.ASCENDING
    val sortView: SortView = SortView.ALL
}