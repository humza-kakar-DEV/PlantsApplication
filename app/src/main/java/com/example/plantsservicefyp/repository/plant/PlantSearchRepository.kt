package com.example.plantsservicefyp.repository

import com.example.plantsservicefyp.model.Plant
import com.example.plantsservicefyp.util.UiState
import com.google.firebase.firestore.DocumentSnapshot

interface PlantSearchRepository {
    fun getAllPlants (callback: (UiState<List<DocumentSnapshot>>) -> Unit)
}