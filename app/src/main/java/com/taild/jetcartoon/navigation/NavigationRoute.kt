package com.taild.jetcartoon.navigation

import kotlinx.serialization.Serializable

@Serializable
data class CharacterDetailRoute(val characterId: Int)

@Serializable
data class CharacterEpisodeRoute(val characterId: Int)

@Serializable
object HomeRoute