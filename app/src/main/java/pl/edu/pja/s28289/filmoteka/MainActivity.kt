package pl.edu.pja.s28289.filmoteka

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import pl.edu.pja.s28289.filmoteka.ui.theme.FilmotekaTheme
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import pl.edu.pja.s28289.filmoteka.ui.viewmodel.AddEditViewModel
import pl.edu.pja.s28289.filmoteka.ui.viewmodel.ListViewModel
import pl.edu.pja.s28289.filmoteka.ui.views.AddEditScreen
import pl.edu.pja.s28289.filmoteka.ui.views.Screen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FilmotekaTheme {
                Home()
                //Screen(films = films)
            }
        }
    }
}

object Destinations {
    val argTitle = "title"

    val listDestinations = "list"
    val addEditDestination ="add_edit/{$argTitle}"

    fun getPathToAddEdit(title: String): String {
        return addEditDestination.replace("{$argTitle}", title)
    }
}

@Composable
fun Home() {
    val navController = rememberNavController()
    NavHost(
        navController,
        Destinations.listDestinations
    ) {
        composable(Destinations.listDestinations) {
            val vm: ListViewModel = viewModel()
            LaunchedEffect(Unit) {
                vm.load()
            }

            Screen(
                films = vm.state,
                onFilmClicked = { navController.navigate(Destinations.getPathToAddEdit(it)) },
                onFilterStatusSet = { vm.setStatusFilter(it) },
                onFilterCategorySet = { vm.setCategoryFilter(it)},
                onFilmDelete = {vm.deleteFilm(it)},
                onFilmAdd = {navController.navigate(Destinations.getPathToAddEdit(""))}
            )
//                vm.setCategoryFilter(-1)
//                vm.setStatusFilter(-1)

        }
        composable(Destinations.addEditDestination,
            arguments = listOf(
                navArgument(Destinations.argTitle) {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val title = backStackEntry.arguments?.getString(Destinations.argTitle) ?: ""
            val vm: AddEditViewModel = viewModel()
           LaunchedEffect(title) {
                vm.load(title)
            }

                AddEditScreen(
                    film = vm.state,
                    onNavigationSave = { vm.update(it) },
                    {navController.popBackStack()}
                )

        }
    }
}