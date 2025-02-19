package com.motorro.fragments.menu.dish

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.motorro.fragments.data.Category
import com.motorro.fragments.data.menu

class DishContentViewModel : ViewModel() {
    val categories: LiveData<List<Category>> = MutableLiveData(menu)
}