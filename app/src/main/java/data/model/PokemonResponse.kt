package com.team2.pokemonlookup.data.model

import com.google.gson.annotations.SerializedName

// Fields used from PokéAPI:
// - name
// - sprites.front_default
// - types[].type.name
// - stats[].base_stat
// - stats[].stat.name
//
// Ignored for Sprint 3:
// - abilities
// - moves
// - forms
// - species
// - game_indices
// - other sprite variations

data class PokemonResponse(
    val name: String,
    val sprites: Sprites,
    val types: List<TypeSlot>,
    val stats: List<StatInfo>
)

data class Sprites(
    @SerializedName("front_default")
    val frontDefault: String?
)

data class TypeSlot(
    val type: Type
)

data class Type(
    val name: String
)

data class StatInfo(
    @SerializedName("base_stat")
    val baseStat: Int,
    val stat: Stat
)

data class Stat(
    val name: String
)