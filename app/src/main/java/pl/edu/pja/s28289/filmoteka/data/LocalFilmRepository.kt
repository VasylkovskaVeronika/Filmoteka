package pl.edu.pja.s28289.filmoteka.data

import androidx.compose.ui.res.painterResource
import pl.edu.pja.s28289.filmoteka.R
import pl.edu.pja.s28289.filmoteka.model.Category
import pl.edu.pja.s28289.filmoteka.model.Film
import pl.edu.pja.s28289.filmoteka.model.Poster
import pl.edu.pja.s28289.filmoteka.model.Status
import java.time.LocalDate
import javax.inject.Inject

object LocalFilmRepository : FilmRepository {
    private val _films: MutableList<Film>
    override val films: MutableList<Film>
        get() = _films

    init {
        _films = """
            Mad Max: Na drodze furii|15.05.2015|ACTION|SEEN|MAD_MAX|3|Too mad)
            Parasite|11.10.2019|DRAMA|NOT_SEEN|PARASITE
            Dune: Część druga|29.02.2024|SCI_FI|SEEN|DUNE|5|world-known classic
            Death Race: Wyścig śmierci|08.08.2008|ACTION|NOT_SEEN|DEATH_RACE
            Avatar|18.12.2009|SCI_FI|SEEN|AVATAR|4|too fiction to be science
            Incepcja|16.07.2010|THRILLER|SEEN|INTERCEPTION|4|-
            """
            .trimIndent()
            .split("\n")
            .filter {it.isNotBlank()}
            .map {
                val parts = it.split("|")
                val dateParts=parts[1].split(".")
                if(parts.size==7) {
                    Film(
                        Poster.valueOf(parts[4]),
                        parts[0], LocalDate.of(
                            dateParts[2].toInt(),
                            dateParts[1].toInt(), dateParts[0].toInt()
                        ), Category.valueOf(parts[2]),
                        Status.valueOf(parts[3]), parts[5].toDouble(), parts[6]
                    )
                }
                else {
                    Film(
                        Poster.valueOf(parts[4]),
                        parts[0], LocalDate.of(
                            dateParts[2].toInt(),
                            dateParts[1].toInt(), dateParts[0].toInt()
                        ), Category.valueOf(parts[2]),
                        Status.valueOf(parts[3]), null, null
                    )
                }
            }
            .sortedWith(compareBy {
                it.releaseDate
            })
            .toMutableList()
    }
}