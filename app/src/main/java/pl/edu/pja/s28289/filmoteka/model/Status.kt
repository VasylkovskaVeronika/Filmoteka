package pl.edu.pja.s28289.filmoteka.model

import androidx.compose.ui.res.stringResource
import pl.edu.pja.s28289.filmoteka.R

enum class Status(val displayName: Int) {
    SEEN(R.string.status_seen),
    NOT_SEEN(R.string.status_notSeen)
}