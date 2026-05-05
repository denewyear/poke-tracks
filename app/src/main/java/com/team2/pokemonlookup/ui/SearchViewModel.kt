package com.team2.pokemonlookup.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team2.pokemonlookup.data.model.PokemonResponse
import com.team2.pokemonlookup.data.repository.PokemonRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class SearchViewModel(
    private val repository: PokemonRepository
) : ViewModel() {

    private val _pokemonData = MutableLiveData<PokemonResponse?>()
    val pokemonData: LiveData<PokemonResponse?> = _pokemonData

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun searchPokemon(name: String) {
        if (name.isBlank()) {
            _errorMessage.value = "Please enter a Pokemon name"
            _pokemonData.value = null
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            val result = repository.getPokemon(name)
            result
                .onSuccess { pokemon ->
                    _pokemonData.value = pokemon
                    _errorMessage.value = null
                }
                .onFailure { error ->
                    _pokemonData.value = null
                    _errorMessage.value = friendlyError(error)
                }

            _isLoading.value = false
        }
    }

    private fun friendlyError(error: Throwable): String = when (error) {
        is HttpException ->
            if (error.code() == 404) "Pokemon not found"
            else "Server error (${error.code()})"
        is IOException -> "Network error — check your connection"
        else -> error.message ?: "Something went wrong"
    }
}
