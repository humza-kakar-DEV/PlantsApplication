package com.example.plantsservicefyp.repository

import com.example.plantsservicefyp.model.Plant
import com.example.plantsservicefyp.util.UiState

interface PlantSearchRepository {
    fun getAllPlants (callback: (UiState<List<Plant>>) -> Unit)
}