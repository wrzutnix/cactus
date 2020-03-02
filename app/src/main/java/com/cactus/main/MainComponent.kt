package com.cactus.main

import dagger.BindsInstance
import dagger.Component

@Component
interface MainComponent {

    @Component.Factory interface Factory {
        fun create(@BindsInstance view: MainContract.View): MainComponent
    }

    fun inject(view: MainActivity)
}
