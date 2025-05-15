package pl.edu.pja.s28289.filmoteka.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.lifecycle.HiltViewModel
import pl.edu.pja.s28289.filmoteka.data.FilmRepository
import pl.edu.pja.s28289.filmoteka.data.LocalFilmRepository

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindFilmRepository(
        filmRepositoryImpl: LocalFilmRepository
    ): FilmRepository
}