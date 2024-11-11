package com.taild.jetcartoon.navigation

import kotlinx.serialization.Serializable

@Serializable
object CharacterDetailRoute

@Serializable
data class CharacterEpisodeRoute(val characterId: Int)

@Serializable
object HomeRoute