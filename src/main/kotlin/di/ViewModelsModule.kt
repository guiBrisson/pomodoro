package di

import org.koin.dsl.module
import presentation.sidebar.SidebarViewModel

val viewModelsModule = module {
    single { SidebarViewModel(get()) }
}
