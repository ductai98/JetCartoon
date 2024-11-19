package com.taild.jetcartoon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.taild.jetcartoon.navigation.CharacterDetailRoute
import com.taild.jetcartoon.navigation.CharacterEpisodeRoute
import com.taild.jetcartoon.navigation.EpisodesRoute
import com.taild.jetcartoon.navigation.HomeRoute
import com.taild.jetcartoon.navigation.SearchRoute
import com.taild.jetcartoon.ui.screens.allepisodes.AllEpisodesScreen
import com.taild.jetcartoon.ui.screens.characterepisode.CharacterEpisodeScreen
import com.taild.jetcartoon.ui.screens.charaterdetail.CharacterDetailScreen
import com.taild.jetcartoon.ui.screens.home.HomeScreen
import com.taild.jetcartoon.ui.screens.search.SearchScreen
import com.taild.jetcartoon.ui.theme.JetCartoonTheme
import com.taild.jetcartoon.ui.theme.RickPrimary
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
            val items = listOf(
                NavDestination.Home,
                NavDestination.Episodes,
                NavDestination.Search
            )

            JetCartoonTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        NavigationBar(
                            containerColor = RickPrimary
                        ) {
                            val navBackStackEntry by navController.currentBackStackEntryAsState()
                            val currentDestination = navBackStackEntry?.destination
                            items.forEach { screen ->
                                NavigationBarItem(
                                    icon = {
                                        Icon(
                                            imageVector = screen.icon,
                                            contentDescription = null
                                        )
                                    },
                                    label = { Text(screen.title) },
                                    selected = currentDestination?.hierarchy?.any {
                                        it.route?.contains(screen.route.javaClass.simpleName) ?: false
                                    } == true,
                                    onClick = {
                                        navController.navigate(screen.route) {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    Surface(
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        NavHost(
                            navController = navController,
                            startDestination = HomeRoute,
                            enterTransition = { EnterTransition.None },
                            exitTransition = { ExitTransition.None }
                        ) {
                            composable<HomeRoute> {
                                HomeScreen(
                                    onCharacterClick = { characterId ->
                                        navController.navigate(
                                            CharacterDetailRoute(characterId)
                                        )
                                    }
                                )
                            }
                            composable<CharacterDetailRoute> {
                                val id = it.toRoute<CharacterDetailRoute>().characterId
                                CharacterDetailScreen(
                                    characterId = id,
                                    onAllEpisodesClick = { characterId ->
                                        navController.navigate(CharacterEpisodeRoute(characterId))
                                    },
                                    onBackClick = {
                                        navController.navigateUp()
                                    }
                                )
                            }
                            composable<CharacterEpisodeRoute> {
                                val characterId = it.toRoute<CharacterEpisodeRoute>().characterId
                                CharacterEpisodeScreen(
                                    characterId = characterId,
                                    ktorClient = ktorClient,
                                    onBackClick = {
                                        navController.navigateUp()
                                    }
                                )
                            }
                            composable<EpisodesRoute> {
                                AllEpisodesScreen()
                            }
                            composable<SearchRoute> {
                                SearchScreen(
                                    onCharacterClicked = {
                                        navController.navigate(CharacterDetailRoute(it))
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

sealed class NavDestination(
    val title: String,
    val route: Any,
    val icon: ImageVector
) {
    object Home: NavDestination(
        title = "Home",
        route = HomeRoute,
        icon = Icons.Filled.Home
    )
    object Episodes: NavDestination(
        title = "Episodes",
        route = EpisodesRoute,
        icon = Icons.Filled.PlayArrow
    )
    object Search: NavDestination(
        title = "Search",
        route = SearchRoute,
        icon = Icons.Filled.Search
    )
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