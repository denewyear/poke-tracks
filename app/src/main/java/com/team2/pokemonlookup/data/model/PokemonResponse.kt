package com.team2.pokemonlookup.data.model

import com.google.gson.annotations.SerializedName

data class PokemonResponse(
    val id: Int = 0,
    val name: String,
    val height: Int = 0,
    val weight: Int = 0,
    val sprites: Sprites,
    val types: List<TypeSlot>,
    val stats: List<StatInfo>
)

data class Sprites(
    @SerializedName("front_default")
    val frontDefault: String?,
    @SerializedName("other")
    val other: OtherSprites? = null
)

data class OtherSprites(
    @SerializedName("official-artwork")
    val officialArtwork: OfficialArtwork? = null
)

data class OfficialArtwork(
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
