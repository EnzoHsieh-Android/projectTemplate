package com.citrus.projecttemplate.util.base

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

@ExperimentalCoroutinesApi
abstract class BaseUseCase<T> {
    private val _trigger = MutableStateFlow(true)

    /**
     * Exposes result of this use case
     */
    val resultFlow: Flow<T> = _trigger.flatMapLatest {
        performAction()
    }
    /**
     * Triggers the execution of this use case
     */
    suspend fun launch() {
        _trigger.emit(!(_trigger.value))
    }

    protected abstract fun performAction() : Flow<T>

}