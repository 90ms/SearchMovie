package com.a90ms.domain.usecase

import com.a90ms.domain.repository.NaverRepository
import javax.inject.Inject

class GetFavoriteListUseCase @Inject constructor(
    private val repository: NaverRepository,
) {
    fun getFavoriteList() = repository.getFavoriteList()
}
