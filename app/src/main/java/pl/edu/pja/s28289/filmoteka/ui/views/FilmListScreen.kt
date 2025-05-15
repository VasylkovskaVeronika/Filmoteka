package pl.edu.pja.s28289.filmoteka.ui.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material.icons.outlined.RemoveRedEye
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pl.edu.pja.s28289.filmoteka.R
import pl.edu.pja.s28289.filmoteka.data.LocalFilmRepository
import pl.edu.pja.s28289.filmoteka.model.Category
import pl.edu.pja.s28289.filmoteka.model.Film
import pl.edu.pja.s28289.filmoteka.model.Status
import pl.edu.pja.s28289.filmoteka.ui.theme.FilmotekaTheme
import pl.edu.pja.s28289.filmoteka.ui.viewmodel.ListViewModel
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Screen(films: List<Film>, onFilmClicked: (String) -> Unit,
           onFilterStatusSet: (Int) -> Unit,
           onFilterCategorySet: (Int) -> Unit,
           onFilmDelete: (Film) -> Unit,
           onFilmAdd: () -> Unit
) {
    Scaffold (
        topBar = {
            CenterAlignedTopAppBar(

                title = { Text(stringResource(R.string.title_list))},
                actions = {
                    var chosenStatus=-1
                    var chosenCategory=-1
                    var expanded by remember { mutableStateOf(false) }
                    var expanded2 by remember { mutableStateOf(false) }
                    Row(
                        modifier = Modifier
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box {
                            IconButton(onClick = { expanded2 = !expanded2 }) {
                                Icon(
                                    Icons.Outlined.RemoveRedEye, contentDescription =
                                        stringResource(R.string.content_description_status)
                                )
                            }
                            DropdownMenu(
                                expanded = expanded2,
                                onDismissRequest = { expanded2 = false }
                            ) {
                                Status.entries.toTypedArray().asList().forEach {
                                    DropdownMenuItem(
                                        text = { Text(stringResource(it.displayName)) },
                                        onClick = {
                                            if (chosenStatus == it.displayName) {
                                                chosenStatus = -1
                                            } else {
                                                chosenStatus = it.displayName
                                            }
                                            onFilterStatusSet(chosenStatus)
                                        },
                                    )
                                }
                            }
                        }
                        Box {
                            IconButton(onClick = { expanded = !expanded }) {
                                Icon(
                                    Icons.Default.FilterList, contentDescription =
                                        stringResource(R.string.content_description_category)
                                )
                            }
                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                Category.entries.toTypedArray().asList().forEach {
                                    DropdownMenuItem(
                                        text = { Text(stringResource(it.displayName)) },
                                        onClick = {
                                            if (chosenCategory == it.displayName) {
                                                chosenCategory = -1
                                            } else {
                                                chosenCategory = it.displayName
                                            }
                                            onFilterCategorySet(chosenCategory)
                                        },
                                    )
                                }
                            }
                        }
                        Text(text = films.size.toString() + " positions")
                    }
                    IconButton(
                        onClick = onFilmAdd
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Add,
                            contentDescription = stringResource(R.string.content_description_add),
                        )
                    }
                }
            )
        }
    ){ innerPaddings ->
        val sysPaddings = WindowInsets.systemBars.asPaddingValues()
        var showDialog by remember { mutableStateOf(false) }
        var showDeleteDialog by remember { mutableStateOf(false)}
        var filmToDelete by remember { mutableStateOf<Film?>(null) }
        LazyColumn ( modifier = Modifier.fillMaxSize()
            .padding(horizontal =8.dp)
            .padding(bottom = sysPaddings.calculateBottomPadding()),
            contentPadding = innerPaddings, verticalArrangement =
                Arrangement.spacedBy(12.dp)) {
            items(films, key = { it.title }) {
                FilmItemView(it, onClick = {
                    if (it.status == Status.NOT_SEEN) {
                        onFilmClicked(it.title)
                    } else {
                        showDialog = true
                    } },
                    onLongClick = { filmToDelete=it
                        showDeleteDialog=true}
                )
            }
        }
        if (showDialog) {
            AlertDialog(
                onDismissRequest = {
                    showDialog = false
                },
                title = {
                    Text(stringResource(R.string.alert_noEdit_title))
                },
                text = {
                    Text(stringResource(R.string.alert_noEdit_text))
                },
                confirmButton = {
                    Button(onClick = {
                        showDialog = false
                    }) {
                        Text("OK")
                    }
                }
            )
        }
        if (showDeleteDialog && filmToDelete != null) {
            AlertDialog(
                onDismissRequest = {
                    showDeleteDialog = false
                    filmToDelete = null
                },
                title = {
                    Text(stringResource(R.string.remove_title))
                },
                text = {
                    Text(stringResource(R.string.remove_text)+" '${filmToDelete?.title}'")
                },
                confirmButton = {
                    Button(onClick = {
                        filmToDelete?.let { onFilmDelete(it) }
                        showDeleteDialog = false
                        filmToDelete = null
                    }) {
                        Text("Remove")
                    }
                },
                dismissButton = {
                    Button(onClick = {
                        showDeleteDialog = false
                        filmToDelete = null
                    }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilmItemView(film: Film, onClick: () -> Unit, onLongClick: (Film) -> Unit) {
    Card (
        modifier = Modifier.fillMaxWidth()
            //.clickable (onClick = onClick)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { onClick() },
                    onLongPress = { onLongClick(film) }
                )
            }
    ) {
        Row (
            verticalAlignment = Alignment.CenterVertically,
        ){
//            Icon(
//                imageVector =
//            )
            Column (modifier = Modifier.weight(1f)) {
                Image(
                    painter = painterResource(film.poster.image),
                    contentDescription = film.title
                )
            }
            Column (modifier=Modifier.weight(2f).padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,) {
                Text(
                    text=film.title.trim(),
                    style= MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text=stringResource(film.category.displayName)
                            +" | "+film.releaseDate
                                .format(DateTimeFormatter.ofPattern("dd-MM-yyyy")),
                    style= MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text= stringResource(film.status.displayName),
                    style= MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Normal
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    FilmotekaTheme {
        Screen(
            films = LocalFilmRepository.films,
            onFilmClicked = {},
            onFilterStatusSet = {},
            onFilterCategorySet = {},
            onFilmDelete = {},
            onFilmAdd = {})
    }
}
@Preview(showBackground = true)
@Composable
fun FilmItemPreview() {
    FilmotekaTheme {
        FilmItemView(film = LocalFilmRepository.films[0], {}, {})
    }
}