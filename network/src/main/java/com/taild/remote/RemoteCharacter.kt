package com.taild.remote

import com.taild.domain.Character
import com.taild.domain.CharacterGender
import com.taild.domain.CharacterStatus
import kotlinx.serialization.Serializable

@Serializable
data class RemoteCharacter(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val origin: Origin,
    val location: Location,
    val image: String,
    val episode: List<String>,
    val url: String,
    val created: String
) {
    @Serializable
    data class Origin(
        val name: String,
        val url: String
    )

    @Serializable
    data class Location(
        val name: String,
        val url: String
    )

    fun toDomainCharacter(): Character {
        val characterGender = when (gender.lowercase()) {
            "female" -> CharacterGender.Female
            "male" -> CharacterGender.Male
            "genderless" -> CharacterGender.Genderless
            else -> CharacterGender.Unknown
        }
        val characterStatus = when (status.lowercase()) {
            "alive" -> CharacterStatus.Alive
            "dead" -> CharacterStatus.Dead
            else -> CharacterStatus.Unknown
        }
        return Character(
            id = id,
            name = name,
            status = characterStatus,
            species = species,
            type = type,
            gender = characterGender,
            origin = Character.Origin(origin.name, origin.url),
            location = Character.Location(location.name, location.url),
            image = image,
            episode = episode,
            url = url,
            created = created
        )
    }
}