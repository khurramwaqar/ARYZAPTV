package com.google.jetstream.data.repositories

import Favorites
import SeriesList
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoritesRepository(private val realm: Realm) {

    // Create or Update a Favorite
    suspend fun addOrUpdateFavorite(favorite: Favorites) {
        realm.write {
            copyToRealm(favorite)
        }
    }

    // Read All Favorites
    fun getAllFavorites(): Flow<List<Favorites>> {
        return realm.query<Favorites>().asFlow().map { it.list }
    }

    // Delete a Favorite by ID
    suspend fun deleteFavoriteById(id: String) {
        realm.write {
            val favoriteToDelete = query<Favorites>("_id == $0", id).first().find()
            if (favoriteToDelete != null) {
                delete(favoriteToDelete)
            }
        }
    }

    // Create or Update a SeriesList
    suspend fun addOrUpdateSeriesList(seriesList: SeriesList) {
        realm.write {
            copyToRealm(seriesList)
        }
    }

    // Read All SeriesLists
    fun getAllSeriesLists(): Flow<List<SeriesList>> {
        return realm.query<SeriesList>().asFlow().map { it.list }
    }

    // Delete a SeriesList by ID
    suspend fun deleteSeriesListById(id: String) {
        realm.write {
            val seriesListToDelete = query<SeriesList>("_id == $0", id).first().find()
            if (seriesListToDelete != null) {
                delete(seriesListToDelete)
            }
        }
    }
}