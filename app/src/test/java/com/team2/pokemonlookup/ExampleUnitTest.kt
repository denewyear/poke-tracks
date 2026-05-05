package com.team2.pokemonlookup

import com.team2.pokemonlookup.data.remote.RetrofitClient
import com.team2.pokemonlookup.data.repository.PokemonRepository
import kotlinx.coroutines.runBlocking
import org.junit.Test

class PokemonRepositoryTest {

    @Test
    fun testGetPokemon() = runBlocking {
        val repo = PokemonRepository(RetrofitClient.api)

        val result = repo.getPokemon("pikachu")

        println(result)
        assert(result.isSuccess)
    }
}
