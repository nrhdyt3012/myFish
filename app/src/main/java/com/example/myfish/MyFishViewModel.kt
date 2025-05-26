package com.example.myfish

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myfish.data.FishRepository
import com.example.myfish.model.Fish
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MyFishViewModel(private val repository: FishRepository) : ViewModel() {
     private val _groupedFishes = MutableStateFlow (
         repository.getFish()
             .sortedBy { it.name }
             .groupBy { it.name[0] }

     )
    val groupedFishes: StateFlow<Map<Char, List<Fish>>> get() = _groupedFishes


    private val _query = mutableStateOf("")
    val query: State<String> get() = _query
    fun search(newQuery: String) {
        _query.value = newQuery
        _groupedFishes.value = repository.searchFishes(_query.value)
            .sortedBy { it.name }
            .groupBy { it.name[0] }
    }

    fun getFishById(id: Int?): Fish? {
        return repository.getFish().find { it.id == id }
    }

}
class ViewModelFactory(private val repository: FishRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MyFishViewModel::class.java)) {
            return MyFishViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}