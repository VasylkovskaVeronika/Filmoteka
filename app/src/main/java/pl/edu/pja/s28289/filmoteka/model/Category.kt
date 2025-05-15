package pl.edu.pja.s28289.filmoteka.model

import pl.edu.pja.s28289.filmoteka.R
import java.time.LocalDate

enum class Category(val displayName: Int) {
    ACTION(R.string.category_action),
    SCI_FI(R.string.category_scifi),
    DRAMA(R.string.category_drama),
    THRILLER(R.string.category_thriller)
}

