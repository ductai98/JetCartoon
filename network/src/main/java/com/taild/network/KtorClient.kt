package com.taild.network

import com.taild.domain.Character
import com.taild.domain.CharacterPage
import com.taild.domain.Episode
import com.taild.domain.EpisodePage
import com.taild.remote.RemoteCharacter
import com.taild.remote.RemoteCharacterPage
import com.taild.remote.RemoteEpisode
import com.taild.remote.RemoteEpisodePage
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

const val baseUrl = "https://rickandmortyapi.com/api/"
class KtorClient {
    private val client = HttpClient(OkHttp) {
        defaultRequest { url(baseUrl) }

        install(Logging) {
            logger = Logger.SIMPLE
        }

        install(ContentNegotiation) {
            json(
                Json {
                   ignoreUnknownKeys = true
                }
            )
        }
    }

    private var characterCache = mutableMapOf<Int, Character>()
    private var characterPageCache = mutableMapOf<Int, CharacterPage>()

    suspend fun getCharacter(id: Int): ApiOperation<Character> {
        characterCache[id]?.let{
            return ApiOperation.Success(data = it)
        }
        return safeApiCall {
            client.get("character/$id")
                .body<RemoteCharacter>()
                .toDomainCharacter()
                .also {
                    characterCache[id] = it
                }
        }
    }

    suspend fun getEpisodes(episodeIds: List<Int>): ApiOperation<List<Episode>> {
        if (episodeIds.size > 1) {
            val ids = episodeIds.joinToString(separator = ", ")
            return safeApiCall {
                client.get("episode/$ids")
                    .body<List<RemoteEpisode>>()
                    .map { it.toDomainEpisode() }
            }
        } else {
            return safeApiCall {
                listOf(
                    client.get("episode/${episodeIds[0]}")
                        .body<RemoteEpisode>()
                        .toDomainEpisode()
                )
            }
        }
    }

    suspend fun getEpisodesByPage(pageIndex: Int): ApiOperation<EpisodePage> {
        return safeApiCall {
            client.get("episode/?page=$pageIndex")
                .body<RemoteEpisodePage>()
                .toDomain()
        }
    }

    suspend fun getAllEpisodes(): ApiOperation<List<Episode>> {
        val data = mutableListOf<Episode>()
        var exception: Exception? = null
        var totalPageCount = 0
        getEpisodesByPage(1).onSuccess { firstPage ->
            totalPageCount = firstPage.info.pages
            data.addAll(firstPage.results)
        }.onFailure { e ->
            exception = e
        }

        for (index in 2..totalPageCount) {
            getEpisodesByPage(index).onSuccess {
                data.addAll(it.results)
            }.onFailure { e ->
                exception = e
            }
        }

        return exception?.let { ex ->
            ApiOperation.Failure(ex)
        } ?: ApiOperation.Success(data = data)

    }

    suspend fun getCharacterByPage(pageNumber: Int): ApiOperation<CharacterPage> {
        return safeApiCall {
            client.get("character/?page=$pageNumber")
                .body<RemoteCharacterPage>()
                .toDomain()
        }
    }

    private inline fun <T> safeApiCall(apiCall: () -> T): ApiOperation<T> {
        try {
            return ApiOperation.Success(data = apiCall())
        } catch (e: Exception) {
            return ApiOperation.Failure(exception = e)
        }
    }
}

sealed interface ApiOperation<T> {
    data class Success<T>(val data: T): ApiOperation<T>
    data class Failure<T>(val exception: Exception): ApiOperation<T>

    fun onSuccess(block: (T) -> Unit): ApiOperation<T> {
        if (this is Success) block(data)
        return this
    }

    fun onFailure(block: (Exception) -> Unit): ApiOperation<T> {
        if (this is Failure) block(exception)
        return this
    }
}
