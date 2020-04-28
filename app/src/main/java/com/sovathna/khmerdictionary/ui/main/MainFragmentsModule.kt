package com.sovathna.khmerdictionary.ui.main

import com.sovathna.khmerdictionary.ui.definition.DefinitionFragment
import com.sovathna.khmerdictionary.ui.definition.DefinitionModule
import com.sovathna.khmerdictionary.ui.wordlist.WordListFragment
import com.sovathna.khmerdictionary.ui.wordlist.WordListModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainFragmentsModule {

  @ContributesAndroidInjector(modules = [WordListModule::class])
  abstract fun wordListFragment(): WordListFragment

  @ContributesAndroidInjector(modules = [DefinitionModule::class])
  abstract fun definitionFragment(): DefinitionFragment

}