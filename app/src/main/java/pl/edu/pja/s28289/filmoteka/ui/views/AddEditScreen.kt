package pl.edu.pja.s28289.filmoteka.ui.views

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pl.edu.pja.s28289.filmoteka.R
import pl.edu.pja.s28289.filmoteka.data.LocalFilmRepository
import pl.edu.pja.s28289.filmoteka.model.Category
import pl.edu.pja.s28289.filmoteka.model.Film
import pl.edu.pja.s28289.filmoteka.model.Poster
import pl.edu.pja.s28289.filmoteka.model.Status
import pl.edu.pja.s28289.filmoteka.ui.theme.FilmotekaTheme
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditScreen(
    film: Film?,
    onNavigationSave: (Film) -> Unit,
    onNavigationUp: () -> Unit,
    modifier: Modifier = Modifier,
    ) {
    Scaffold (
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.title_add_edit))},
                navigationIcon = {
                    Icon(
                        modifier = Modifier
                            .clickable(onClick = onNavigationUp)
                            .padding(12.dp),
                        imageVector = Icons.Outlined.Close,
                        contentDescription = stringResource(R.string.content_description_cancel),
                    )
                },
                actions = {

                }
            )
        }
    ){ innerPaddings ->
        val scrollState = rememberScrollState()
        var title by remember { mutableStateOf(film?.title?:"") }
        var poster by remember {mutableStateOf(film?.poster?:Poster.DEATH_RACE)}
        var selectedDate by remember { mutableStateOf<LocalDate?>(film?.releaseDate) }
        var category by remember { mutableStateOf(film?.category?.displayName?:-1) }
        var status by remember { mutableStateOf(film?.status?.displayName?:-1) }
        var rating by remember { mutableStateOf(0.0) }
        var comment by remember { mutableStateOf("") }
        var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
        var selectedImageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
        val datePickerState = rememberDatePickerState()
        val context = LocalContext.current

        var showBlankTitleDialog by remember { mutableStateOf(false) }
        var showFarDateDialog by remember { mutableStateOf(false) }
        var showNoCategoryDialog by remember { mutableStateOf(false) }
        var showNoRatingDialog by remember { mutableStateOf(false) }

        var showRatingCommentOptions by remember { mutableStateOf(false) }

        val imagePickerLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia(),
            onResult = { uri ->
                selectedImageUri = uri
            }
        )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPaddings)
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text(stringResource(R.string.add_title)) },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.weight(1f))
                DatePicker(
                    state = datePickerState,
                    title = { Text(text = stringResource(R.string.add_date)) },
                    headline = {
                        Text(
                            selectedDate?.toString()?: stringResource(R.string.add_noDate)
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                LaunchedEffect(datePickerState.selectedDateMillis) {
                    datePickerState.selectedDateMillis?.let { millis ->
                        selectedDate = Instant.ofEpochMilli(millis)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                var expanded2 by remember { mutableStateOf(false) }
                var expanded by remember { mutableStateOf(false) }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box {
                        Button(onClick = { expanded2 = !expanded2 },
                            shape = ButtonDefaults.elevatedShape,
                            colors = ButtonColors(
                                contentColor = Color.DarkGray,
                                containerColor = Color.Gray,
                                disabledContainerColor = Color.Gray,
                                disabledContentColor = Color.DarkGray,
                            )) {
                            Text(
                                text =
                                    stringResource(
                                        if(status != -1)
                                            status
                                        else
                                            R.string.content_description_status
                                    )
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
                                        status = it.displayName
                                        if(status==Status.SEEN.displayName) {
                                            showRatingCommentOptions=true
                                        }
                                        else {
                                            showRatingCommentOptions = false
                                        }
                                    },
                                )
                            }
                        }
                    }
                    Box {
                        Button(
                            onClick = { expanded = !expanded },
                            content = {
                                Text(
                                    text = stringResource(
                                        if (category != -1)
                                            category
                                        else
                                            R.string.content_description_category
                                    )
                                )
                            },
                            shape = ButtonDefaults.elevatedShape,
                            colors = ButtonColors(
                                contentColor = Color.DarkGray,
                                containerColor = Color.Gray,
                                disabledContainerColor = Color.Gray,
                                disabledContentColor = Color.DarkGray,
                            )
                        )
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            Category.entries.toTypedArray().asList().forEach {
                                DropdownMenuItem(
                                    text = { Text(stringResource(it.displayName)) },
                                    onClick = {
                                        category = it.displayName
                                    },
                                )
                            }
                        }
                    }
                }
                LaunchedEffect(selectedImageUri) {
                    selectedImageUri?.let { uri ->
                        selectedImageBitmap = uri.toBitmap(context)?.asImageBitmap()
                    }
                }

                Column(modifier = Modifier
                    .fillMaxSize()
                    .padding(5.dp)) {
                    Button(onClick = {
                        imagePickerLauncher.launch(
                            PickVisualMediaRequest(
                                ActivityResultContracts.PickVisualMedia.ImageOnly
                            )
                        )
                    }, shape = ButtonDefaults.elevatedShape,
                        colors = ButtonColors(
                            contentColor = Color.DarkGray,
                            containerColor = Color.Gray,
                            disabledContainerColor = Color.Gray,
                            disabledContentColor = Color.DarkGray,
                        )) {
                        Text(stringResource(R.string.add_poster))
                    }

                    selectedImageBitmap?.let { bitmap ->
                        Image(
                            bitmap = bitmap,
                            contentDescription = stringResource(R.string.added_poster),
                            modifier = Modifier.fillMaxSize()
                        )
                    } ?: run {
                        Text(stringResource(R.string.noPoster_added))
                    }
                }
                //rating and comment
                var expanded3 by remember { mutableStateOf(false) }
                if(showRatingCommentOptions) {
                    Box {
                        Button (
                            onClick = { expanded3 = !expanded3 },
                            content = { Text(text= stringResource(R.string.add_rating))}
                        )
                        DropdownMenu(
                            expanded = expanded3,
                            onDismissRequest = { expanded3 = false }
                        ) {
                            for(i in 1..5) {
                                DropdownMenuItem(
                                    text = { Text(i.toString()) },
                                    onClick = {
                                        rating = i.toDouble()
                                    },
                                )
                            }
                        }
                    }
                    OutlinedTextField(
                        value = comment,
                        onValueChange = { comment = it },
                        label = { Text(stringResource(R.string.add_comment)) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Button(onClick = {
                    if(title.isBlank()) {
                        showBlankTitleDialog=true
                    }
                    else if(selectedDate== null || selectedDate!!.year -LocalDate.now().year>2) {
                        showFarDateDialog=true
                    }
                    else if(category == -1) {
                        showNoCategoryDialog=true
                    }
                    else if(status==Status.SEEN.displayName && rating==0.0) {
                        showNoRatingDialog=true
                    }
                    else {
                        val updFilm: Film
                        if(status==-1)
                            status=Status.NOT_SEEN.displayName
                        if(status==Status.SEEN.displayName) {
                            updFilm = Film(
                                poster, title,
                                selectedDate ?: LocalDate.now(),
                                Category.entries.first { it.displayName == category },
                                Status.entries.first { it.displayName == status }, rating, comment)
                        }
                        else {
                            updFilm = Film(
                                poster, title,
                                selectedDate ?: LocalDate.now(),
                                Category.entries.first { it.displayName == category },
                                Status.entries.first { it.displayName == status },
                                null, null)
                        }
                        onNavigationSave(updFilm)
                        onNavigationUp()
                    }
                }) {
                    Icon(
                        Icons.Filled.Done,
                        contentDescription = null
                    )
                    Text(text = stringResource(R.string.content_description_saved))
                }

                //alerts
                if (showBlankTitleDialog) {
                    AlertDialog(
                        onDismissRequest = {
                            showBlankTitleDialog = false
                        },
                        title = {
                            Text(stringResource(R.string.alert_blankTitle_title))
                        },
                        text = {
                            Text(stringResource(R.string.alert_blankTitle_text))
                        },
                        confirmButton = {
                            Button(onClick = {
                                showBlankTitleDialog = false
                            }) {
                                Text("OK")
                            }
                        }
                    )
                }
                if (showFarDateDialog) {
                    AlertDialog(
                        onDismissRequest = {
                            showFarDateDialog = false
                        },
                        title = {
                            Text(stringResource(R.string.alert_farDate_title))
                        },
                        text = {
                            Text(stringResource(R.string.alert_farDate_text))
                        },
                        confirmButton = {
                            Button(onClick = {
                                showFarDateDialog = false
                            }) {
                                Text("OK")
                            }
                        }
                    )
                }
                if (showNoCategoryDialog) {
                    AlertDialog(
                        onDismissRequest = {
                            showNoCategoryDialog = false
                        },
                        title = {
                            Text(stringResource(R.string.alert_noCategory_title))
                        },
                        text = {
                            Text(stringResource(R.string.alert_noCategory_text))
                        },
                        confirmButton = {
                            Button(onClick = {
                                showNoCategoryDialog = false
                            }) {
                                Text("OK")
                            }
                        }
                    )
                }
                if (showNoRatingDialog) {
                    AlertDialog(
                        onDismissRequest = {
                            showNoRatingDialog = false
                        },
                        title = {
                            Text(stringResource(R.string.alert_noRating_title))
                        },
                        text = {
                            Text(stringResource(R.string.alert_noRating_text))
                        },
                        confirmButton = {
                            Button(onClick = {
                                showNoRatingDialog = false
                            }) {
                                Text("OK")
                            }
                        }
                    )
                }

        }
        }
}

fun Uri.toBitmap(context: Context): Bitmap? {
    return try {
        if (Build.VERSION.SDK_INT < 28) {
            MediaStore.Images.Media.getBitmap(context.contentResolver, this)
        } else {
            val source = ImageDecoder.createSource(context.contentResolver, this)
            ImageDecoder.decodeBitmap(source)
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

@Preview(showBackground = true)
@Composable
fun AddEditPreview() {
    FilmotekaTheme {
        AddEditScreen(film = LocalFilmRepository.films[0], {}, {})
    }
}