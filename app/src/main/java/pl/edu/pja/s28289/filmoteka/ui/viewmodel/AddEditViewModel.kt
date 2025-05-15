package pl.edu.pja.s28289.filmoteka.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import pl.edu.pja.s28289.filmoteka.data.FilmRepository
import pl.edu.pja.s28289.filmoteka.data.LocalFilmRepository
import pl.edu.pja.s28289.filmoteka.model.Film
import javax.inject.Inject

@HiltViewModel
class AddEditViewModel @Inject constructor(): ViewModel() {
    private val _repo: FilmRepository = LocalFilmRepository
    var state by mutableStateOf<Film?>(null)

    fun load(title: String) {
        state=_repo.findByTitle(title)
    }

    fun update(newFilm: Film) {
        state=_repo.update(state, newFilm)
    }

//    fun add(film: Film) {
//        if(_repo.findByTitle(film.title)==null) {
//            _repo.add(film)
//        }
//    }
}