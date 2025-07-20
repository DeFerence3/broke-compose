package com.diffy.broke.presentation.core.search

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Parcelable
import androidx.activity.result.contract.ActivityResultContract
import kotlin.reflect.KClass

class SearchContract<T: Any>(private val resultTypeClass: KClass<T>): ActivityResultContract<Unit, T?>(){
    override fun createIntent(context: Context, input: Unit): Intent {
        return Intent(context, SearchActivity::class.java).apply {
            val extra = resultTypeClass.qualifiedName
            putExtra("type", extra)
        }
    }

    override fun parseResult(resultCode: Int, intent: Intent?): T? {
        return if (resultCode == Activity.RESULT_OK) {
            intent?.parcelable("item") as T?
        } else null
    }

    inline fun <reified T : Parcelable> Intent.parcelable(key: String): T? = when {
        Build.VERSION.SDK_INT >= 33 -> getParcelableExtra(key, T::class.java)
        else -> @Suppress("DEPRECATION") getParcelableExtra(key) as? T
    }
}
