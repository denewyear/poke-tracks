package com.team2.pokemonlookup

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.team2.pokemonlookup.data.model.PokemonResponse
import com.team2.pokemonlookup.data.remote.RetrofitClient
import com.team2.pokemonlookup.data.repository.PokemonRepository
import com.team2.pokemonlookup.databinding.ActivityMainBinding
import com.team2.pokemonlookup.ui.SearchViewModel

class MainActivity : ComponentActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: SearchViewModel by viewModels {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                SearchViewModel(PokemonRepository(RetrofitClient.api)) as T
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.searchButton.setOnClickListener {
            viewModel.searchPokemon(binding.searchInput.text.toString())
        }

        viewModel.pokemonData.observe(this) { pokemon ->
            if (pokemon != null) showPokemon(pokemon) else hidePokemon()
        }

        viewModel.isLoading.observe(this) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
            binding.searchButton.isEnabled = !loading
        }

        viewModel.errorMessage.observe(this) { error ->
            if (error.isNullOrBlank()) {
                binding.errorMessage.visibility = View.GONE
            } else {
                binding.errorMessage.text = error
                binding.errorMessage.visibility = View.VISIBLE
            }
        }
    }

    private fun showPokemon(pokemon: PokemonResponse) {
        binding.pokemonName.text = pokemon.name.replaceFirstChar { it.titlecase() }

        binding.pokemonSprite.load(pokemon.sprites.frontDefault) {
            crossfade(true)
            placeholder(R.drawable.ic_launcher_foreground)
            error(R.drawable.ic_launcher_foreground)
        }

        val typesText = pokemon.types.joinToString(", ") {
            it.type.name.replaceFirstChar { c -> c.titlecase() }
        }
        binding.pokemonTypes.text = "Type: $typesText"

        val byName = pokemon.stats.associateBy { it.stat.name }
        binding.statHp.text      = "HP: ${byName["hp"]?.baseStat ?: "?"}"
        binding.statAttack.text  = "Attack: ${byName["attack"]?.baseStat ?: "?"}"
        binding.statDefense.text = "Defense: ${byName["defense"]?.baseStat ?: "?"}"
        binding.statSpAtk.text   = "Sp. Atk: ${byName["special-attack"]?.baseStat ?: "?"}"
        binding.statSpDef.text   = "Sp. Def: ${byName["special-defense"]?.baseStat ?: "?"}"
        binding.statSpeed.text   = "Speed: ${byName["speed"]?.baseStat ?: "?"}"

        binding.statsContainer.visibility = View.VISIBLE
    }

    private fun hidePokemon() {
        binding.pokemonName.text = ""
        binding.pokemonTypes.text = ""
        binding.statsContainer.visibility = View.GONE
        binding.pokemonSprite.setImageDrawable(null)
    }
}
