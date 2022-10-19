package com.a90ms.domain.usecase

import com.a90ms.common.dto.CommonDto
import com.a90ms.domain.di.DefaultDispatcher
import com.a90ms.domain.repository.NaverRepository
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher

class UpdateFavoriteUseCase @Inject constructor(
    private val repository: NaverRepository,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher
) : CommonDtoUseCase<FavoriteParams, Unit>(coroutineDispatcher = dispatcher) {
    override suspend fun execute(parameters: FavoriteParams): CommonDto<Unit> =
        repository.updateFavorite(
            parameters.id,
            parameters.favorite
        )
}

data class FavoriteParams(
    val id: String,
    val favorite: Boolean
)
