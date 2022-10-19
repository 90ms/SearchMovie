package com.a90ms.domain.usecase

import com.a90ms.common.dto.CommonDto
import com.a90ms.domain.di.DefaultDispatcher
import com.a90ms.domain.repository.NaverRepository
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher

class ClearDatabaseUseCase @Inject constructor(
    private val repository: NaverRepository,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher
) : NoParamCommonDtoUseCase<Unit>(coroutineDispatcher = dispatcher) {
    override suspend fun execute(): CommonDto<Unit> =
        repository.clearDatabase()
}
