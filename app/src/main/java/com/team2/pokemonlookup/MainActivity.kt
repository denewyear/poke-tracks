package com.team2.pokemonlookup

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.core.content.getSystemService
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.team2.pokemonlookup.data.model.PokemonResponse
import com.team2.pokemonlookup.data.remote.RetrofitClient
import com.team2.pokemonlookup.data.repository.PokemonRepository
import com.team2.pokemonlookup.databinding.ActivityMainBinding
import com.team2.pokemonlookup.databinding.ItemStatRowBinding
import com.team2.pokemonlookup.ui.PokemonTypeColors
import com.team2.pokemonlookup.ui.SearchViewModel
import kotlin.math.roundToInt

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

        applyBackground(PokemonTypeColors.colorFor(this, "default"))
        setStatLabels()

        binding.searchButton.setOnClickListener { triggerSearch() }
        binding.searchInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                triggerSearch(); true
            } else false
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
                binding.heroCard.visibility = View.GONE
                binding.statsCard.visibility = View.GONE
                binding.emptyState.visibility = View.GONE
            }
        }
    }

    private fun triggerSearch() {
        val query = binding.searchInput.text.toString().trim()
        viewModel.searchPokemon(query)
        getSystemService<InputMethodManager>()
            ?.hideSoftInputFromWindow(binding.searchInput.windowToken, 0)
    }

    private fun setStatLabels() {
        ItemStatRowBinding.bind(binding.rowHp.root).statLabel.text = getString(R.string.stat_hp)
        ItemStatRowBinding.bind(binding.rowAttack.root).statLabel.text = getString(R.string.stat_attack)
        ItemStatRowBinding.bind(binding.rowDefense.root).statLabel.text = getString(R.string.stat_defense)
        ItemStatRowBinding.bind(binding.rowSpAtk.root).statLabel.text = getString(R.string.stat_sp_atk)
        ItemStatRowBinding.bind(binding.rowSpDef.root).statLabel.text = getString(R.string.stat_sp_def)
        ItemStatRowBinding.bind(binding.rowSpeed.root).statLabel.text = getString(R.string.stat_speed)
    }

    private fun showPokemon(pokemon: PokemonResponse) {
        binding.errorMessage.visibility = View.GONE
        binding.emptyState.visibility = View.GONE
        binding.heroCard.visibility = View.VISIBLE
        binding.statsCard.visibility = View.VISIBLE

        val primaryType = pokemon.types.firstOrNull()?.type?.name ?: "default"
        val typeColor = PokemonTypeColors.colorFor(this, primaryType)

        applyBackground(typeColor)
        applySpriteHalo(typeColor)

        binding.pokemonId.text = "#%03d".format(pokemon.id)
        binding.pokemonName.text = pokemon.name.replaceFirstChar { it.titlecase() }

        val artworkUrl = pokemon.sprites.other?.officialArtwork?.frontDefault
            ?: pokemon.sprites.frontDefault
        binding.pokemonSprite.load(artworkUrl) {
            crossfade(true)
            placeholder(R.drawable.ic_pokeball)
            error(R.drawable.ic_pokeball)
        }

        renderTypeChips(pokemon)
        renderStats(pokemon, typeColor)
    }

    private fun hidePokemon() {
        binding.heroCard.visibility = View.GONE
        binding.statsCard.visibility = View.GONE
        binding.emptyState.visibility = View.VISIBLE
        applyBackground(PokemonTypeColors.colorFor(this, "default"))
    }

    private fun applyBackground(typeColor: Int) {
        val gradient = GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM,
            intArrayOf(
                PokemonTypeColors.darken(typeColor, 0.92f),
                typeColor,
                PokemonTypeColors.lighten(typeColor, 0.55f)
            )
        )
        binding.rootLayout.background = gradient
        window.statusBarColor = PokemonTypeColors.darken(typeColor, 0.85f)
    }

    private fun applySpriteHalo(typeColor: Int) {
        val halo = GradientDrawable().apply {
            shape = GradientDrawable.OVAL
            colors = intArrayOf(
                Color.argb(64, Color.red(typeColor), Color.green(typeColor), Color.blue(typeColor)),
                Color.argb(0, Color.red(typeColor), Color.green(typeColor), Color.blue(typeColor))
            )
            gradientType = GradientDrawable.RADIAL_GRADIENT
            gradientRadius = 320f
        }
        binding.spriteHalo.background = halo
    }

    private fun renderTypeChips(pokemon: PokemonResponse) {
        binding.typesContainer.removeAllViews()
        val density = resources.displayMetrics.density
        val padH = (16 * density).toInt()
        val padV = (6 * density).toInt()
        val gap = (8 * density).toInt()

        pokemon.types.forEachIndexed { index, slot ->
            val color = PokemonTypeColors.colorFor(this, slot.type.name)
            val chip = TextView(this).apply {
                text = slot.type.name.uppercase()
                setTextColor(Color.WHITE)
                textSize = 12f
                letterSpacing = 0.12f
                setPadding(padH, padV, padH, padV)
                background = GradientDrawable().apply {
                    cornerRadius = 999f
                    setColor(color)
                }
                typeface = android.graphics.Typeface.DEFAULT_BOLD
            }
            val lp = ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            if (index > 0) lp.leftMargin = gap
            chip.layoutParams = lp
            binding.typesContainer.addView(chip)
        }
    }

    private fun renderStats(pokemon: PokemonResponse, typeColor: Int) {
        val byName = pokemon.stats.associateBy { it.stat.name }
        fun statValue(key: String) = byName[key]?.baseStat ?: 0

        bindStatRow(binding.rowHp.root, statValue("hp"), typeColor)
        bindStatRow(binding.rowAttack.root, statValue("attack"), typeColor)
        bindStatRow(binding.rowDefense.root, statValue("defense"), typeColor)
        bindStatRow(binding.rowSpAtk.root, statValue("special-attack"), typeColor)
        bindStatRow(binding.rowSpDef.root, statValue("special-defense"), typeColor)
        bindStatRow(binding.rowSpeed.root, statValue("speed"), typeColor)
    }

    private fun bindStatRow(row: View, value: Int, typeColor: Int) {
        val rowBinding = ItemStatRowBinding.bind(row)
        rowBinding.statValue.text = value.toString()
        rowBinding.statBar.setIndicatorColor(typeColor)
        val capped = value.coerceAtMost(rowBinding.statBar.max)
        rowBinding.statBar.post {
            rowBinding.statBar.setProgressCompat(capped, true)
        }
    }
}
