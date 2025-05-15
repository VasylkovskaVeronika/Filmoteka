package pl.edu.pja.s28289.filmoteka.data

import android.util.Log
import pl.edu.pja.s28289.filmoteka.model.Category
import pl.edu.pja.s28289.filmoteka.model.Film
import pl.edu.pja.s28289.filmoteka.model.Status
import java.time.LocalDate

interface FilmRepository {
    val films: MutableList<Film>

    fun findByTitle(title: String): Film? {
        return films.find { it.title.equals(title) }
    }

    fun filterByStatus(status: Int): List<Film> {
        return films.filter { it.status.displayName==status }.sortedWith(compareBy {
            it.releaseDate
        })
    }
    fun filterByCategory(category: Int): List<Film> {
        return films.filter { it.category.displayName==category }
            .sortedWith(compareBy {
                it.releaseDate
            })
    }
    fun filterByStatusCategory(status: Int, category: Int): List<Film> {
        if(category!=-1 && status!=-1) {
            return films
                .filter { it.status.displayName == status && it.category.displayName == category }
                .sortedWith(compareBy {
                    it.releaseDate
                })
        }
        if(category!=-1) {
            return filterByCategory(category)
        }
        if(status!=-1) {
            return filterByStatus(status)
        }
        else {
            return films.sortedWith(compareBy {
                it.releaseDate
            })
        }
    }

    fun delete(title: String): List<Film> {
        films.remove(findByTitle(title))
        return films.sortedWith(compareBy {
            it.releaseDate
        })
    }

//    fun add(film: Film) {
//        var newList: MutableList<Film> = films.toMutableList()
//        newList[newList.lastIndex+1]=film
//        films = newList
//    }

    fun update(oldFilm: Film?, newFilm: Film): Film? {
        if(oldFilm!=null && films.contains(oldFilm)) {
            val filmToUpdate: Film =films[films.indexOf(oldFilm)]
            filmToUpdate.title=newFilm.title
            filmToUpdate.releaseDate=newFilm.releaseDate
            filmToUpdate.category=newFilm.category
            filmToUpdate.status=newFilm.status
            //if seen
            filmToUpdate.poster=newFilm.poster
            return films[films.indexOf(filmToUpdate)]
        }
        else if(oldFilm==null) {
            films.add(newFilm)
            return films[films.indexOf(newFilm)]
        }
        return null
    }
}