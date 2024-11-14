package com.taild.jetcartoon.repository

import com.taild.domain.Episode
import com.taild.network.ApiOperation
import com.taild.network.KtorClient
import javax.inject.Inject

class EpisodesRepository @Inject constructor(
    private val ktorClient: KtorClient
) {
    suspend fun fetchAllEpisodes(): ApiOperation<List<Episode>> {
        return ktorClient.getAllEpisodes()
    }
}