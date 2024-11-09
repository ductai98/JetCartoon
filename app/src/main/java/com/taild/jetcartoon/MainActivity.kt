package com.taild.jetcartoon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.taild.jetcartoon.navigation.CharacterDetailRoute
import com.taild.jetcartoon.navigation.CharacterEpisodeRoute
import com.taild.jetcartoon.ui.screens.characterepisode.CharacterEpisodeScreen
import com.taild.jetcartoon.ui.screens.charaterdetail.CharacterDetailScreen
import com.taild.jetcartoon.ui.theme.JetCartoonTheme
import com.taild.network.KtorClient
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var ktorClient: KtorClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            JetCartoonTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Surface(
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        NavHost(
                            navController = navController,
                            startDestination = CharacterDetailRoute,
                            enterTransition = { EnterTransition.None },
                            exitTransition = { ExitTransition.None }
                        ) {
                            composable<CharacterDetailRoute> {
                                CharacterDetailScreen(
                                    ktorClient = ktorClient,
                                    characterId = 5,
                                    onAllEpisodesClick = {
                                        navController.navigate(CharacterEpisodeRoute(it))
                                    }
                                )
                            }
                            composable<CharacterEpisodeRoute> {
                                val characterId = it.toRoute<CharacterEpisodeRoute>().characterId
                                CharacterEpisodeScreen(
                                    characterId = characterId,
                                    ktorClient = ktorClient
                                )
                            }
                        }

                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    JetCartoonTheme {
        Greeting("Android")
    }
}