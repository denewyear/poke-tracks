package com.team2.pokemonlookup.data.remote

import com.team2.pokemonlookup.data.model.PokemonResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface PokemonApiService {

    @GET("pokemon/{name}")
    suspend fun getPokemon(@Path("name") name: String): PokemonResponse
}
