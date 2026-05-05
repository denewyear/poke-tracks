package com.team2.pokemonlookup.data.repository

import com.team2.pokemonlookup.data.model.PokemonResponse
import com.team2.pokemonlookup.data.remote.PokemonApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PokemonRepository(
    private val apiService: PokemonApiService
){
    suspend fun getPokemon(name: String): Result<PokemonResponse>{
        return withContext(Dispatchers.IO){
            try {
                val formattedName = name.trim().lowercase()

                if (formattedName.isEmpty()) {
                    return@withContext Result.failure(
                        IllegalArgumentException("Pokemon name cannot be empty")
                    )
                }

                val response = apiService.getPokemon(formattedName)
                Result.success(response)

            } catch (e: Exception){
                Result.failure(e)
            }
        }
    }
}