package io.github.jonaskahn.repositories

import com.google.inject.ImplementedBy
import io.github.jonaskahn.entities.Province
import io.github.jonaskahn.repositories.impl.ProvinceRepositoryImpl

@ImplementedBy(ProvinceRepositoryImpl::class)
interface ProvinceRepository: BaseRepository<Province, Long> {
}