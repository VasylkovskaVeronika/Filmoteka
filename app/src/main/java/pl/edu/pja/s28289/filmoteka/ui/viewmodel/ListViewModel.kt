package pl.edu.pja.s28289.filmoteka.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import pl.edu.pja.s28289.filmoteka.data.FilmRepository
import pl.edu.pja.s28289.filmoteka.data.LocalFilmRepository
import pl.edu.pja.s28289.filmoteka.model.Category
import pl.edu.pja.s28289.filmoteka.model.Film
import pl.edu.pja.s28289.filmoteka.model.Status
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(): ViewModel() {
    private val _repo: FilmRepository = LocalFilmRepository
    var state by mutableStateOf(listOf<Film>())
    var _selectedCategory by mutableIntStateOf(-1)
    var _selectedStatus by mutableIntStateOf(-1)

    fun load() {
        state=_repo.films
    }
    fun setCategoryFilter(category: Int) {
        _selectedCategory=category
        filteredFilms()
    }

    fun setStatusFilter(status: Int) {
        _selectedStatus= status
        filteredFilms()
    }

    private fun filteredFilms() {
        state = _repo.filterByStatusCategory(_selectedStatus, _selectedCategory)
    }

    fun deleteFilm(film: Film) {
        state = _repo.delete(film.title)
    }
}