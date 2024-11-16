package com.taild.jetcartoon.repository

import com.taild.domain.Character
import com.taild.domain.CharacterPage
import com.taild.network.ApiOperation
import com.taild.network.KtorClient
import javax.inject.Inject

class CharacterRepository @Inject constructor(
    private val ktorClient: KtorClient
) {
    suspend fun fetchCharacter(characterId: Int): ApiOperation<Character> {
        return ktorClient.getCharacter(characterId)
    }

    suspend fun fetchCharacterPage(page: Int): ApiOperation<CharacterPage> {
        return ktorClient.getCharacterByPage(page)
    }

    suspend fun fetchCharactersByName(
        name: String
    ): ApiOperation<List<Character>> {
        return ktorClient.getCharacterByName(name)
    }
}