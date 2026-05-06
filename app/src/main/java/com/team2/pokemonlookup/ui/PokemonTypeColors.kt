package com.team2.pokemonlookup.ui

import android.content.Context
import androidx.core.content.ContextCompat
import com.team2.pokemonlookup.R

object PokemonTypeColors {

    fun colorFor(context: Context, typeName: String): Int {
        val resId = when (typeName.lowercase()) {
            "normal" -> R.color.type_normal
            "fire" -> R.color.type_fire
            "water" -> R.color.type_water
            "electric" -> R.color.type_electric
            "grass" -> R.color.type_grass
            "ice" -> R.color.type_ice
            "fighting" -> R.color.type_fighting
            "poison" -> R.color.type_poison
            "ground" -> R.color.type_ground
            "flying" -> R.color.type_flying
            "psychic" -> R.color.type_psychic
            "bug" -> R.color.type_bug
            "rock" -> R.color.type_rock
            "ghost" -> R.color.type_ghost
            "dragon" -> R.color.type_dragon
            "dark" -> R.color.type_dark
            "steel" -> R.color.type_steel
            "fairy" -> R.color.type_fairy
            else -> R.color.brand_primary
        }
        return ContextCompat.getColor(context, resId)
    }

    fun darken(color: Int, factor: Float = 0.78f): Int {
        val a = android.graphics.Color.alpha(color)
        val r = (android.graphics.Color.red(color) * factor).toInt().coerceIn(0, 255)
        val g = (android.graphics.Color.green(color) * factor).toInt().coerceIn(0, 255)
        val b = (android.graphics.Color.blue(color) * factor).toInt().coerceIn(0, 255)
        return android.graphics.Color.argb(a, r, g, b)
    }

    fun lighten(color: Int, factor: Float = 0.4f): Int {
        val a = android.graphics.Color.alpha(color)
        val r = (android.graphics.Color.red(color) + (255 - android.graphics.Color.red(color)) * factor).toInt().coerceIn(0, 255)
        val g = (android.graphics.Color.green(color) + (255 - android.graphics.Color.green(color)) * factor).toInt().coerceIn(0, 255)
        val b = (android.graphics.Color.blue(color) + (255 - android.graphics.Color.blue(color)) * factor).toInt().coerceIn(0, 255)
        return android.graphics.Color.argb(a, r, g, b)
    }
}
