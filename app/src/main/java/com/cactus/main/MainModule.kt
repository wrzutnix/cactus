package com.cactus.main

import android.app.Activity
import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class MainModule {
    @Provides fun context(view: MainContract.View): Context = view as Context
    @Provides fun activity(view: MainContract.View): Activity = view as Activity
}
