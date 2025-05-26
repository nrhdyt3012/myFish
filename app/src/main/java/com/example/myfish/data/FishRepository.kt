package com.example.myfish.data

import com.example.myfish.model.Fish
import com.example.myfish.model.FishData

class FishRepository {
    fun getFish(): List<Fish> {
        return FishData.fishes
    }
    fun searchFishes(query: String): List<Fish>{
        return FishData.fishes.filter {
            it.name.contains(query, ignoreCase = true)
        }
    }

}