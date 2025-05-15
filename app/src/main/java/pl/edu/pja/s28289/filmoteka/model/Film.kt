package pl.edu.pja.s28289.filmoteka.model

import pl.edu.pja.s28289.filmoteka.R
import java.time.LocalDate

data class Film (
    var poster: Poster,
    var title: String,
    var releaseDate: LocalDate,
    var category: Category,
    var status: Status,
    var rating: Double?,
    var comment: String?
)
enum class Poster(val image: Int) {
   DEATH_RACE(R.drawable.death_race),
    AVATAR(R.drawable.avatar),
    DUNE(R.drawable.dune),
    INTERCEPTION(R.drawable.interception),
    MAD_MAX(R.drawable.mad_max),
    PARASITE(R.drawable.parasite)
}

//val films = """
//    Death Race: Wyścig śmierci|08.08.2008|ACTION|NOT_SEEN
//    Avatar|18.12.2009|SCI_FI|SEEN
//    Incepcja|16.07.2010|THRILLER|SEEN
//    Mad Max: Na drodze furii|15.05.2015|ACTION|SEEN
//    Parasite|11.10.2019|DRAMA|NOT_SEEN
//    Dune: Część druga|29.02.2024|SCI_FI|SEEN
//"""
//    .split("\n")
//    .filter {it.isNotBlank()}
//    .map {
//        val parts = it.split("|")
//        val dateParts=parts[1].split(".");
//        Film(parts[0], LocalDate.of(dateParts[2].toInt(),
//            dateParts[1].toInt(), dateParts[0].toInt()), Category.valueOf(parts[2]),
//            Status.valueOf(parts[3]))
//    }
//    .sortedWith(compareBy {
//        it.releaseDate
//    })