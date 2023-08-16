package di

import org.koin.dsl.module
import presentation.main.MainViewModel
import presentation.sidebar.SidebarViewModel

val viewModelModule = module {
    single { SidebarViewModel(get()) }
    single { MainViewModel() }
}
