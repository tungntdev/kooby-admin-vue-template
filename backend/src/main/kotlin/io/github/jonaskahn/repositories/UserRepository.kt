package io.github.jonaskahn.repositories

import com.google.inject.ImplementedBy
import io.github.jonaskahn.entities.User
import io.github.jonaskahn.entities.enums.Status
import io.github.jonaskahn.services.user.UserDto

@ImplementedBy(UserRepositoryImpl::class)
interface UserRepository {

    fun create(user: User): User

    fun findByUsernameOrEmail(username: String, email: String): User?

    fun findActivatedUserByPreferredUsername(preferredUsername: Long): User?

    fun existsByUsernameOrEmail(username: String?, email: String?): Boolean

    fun findCustomActivatedUserByPreferredUsername(preferredUsername: Long): UserDto?

    fun countByKeywordAndStatus(keyword: String?, status: Collection<Status>): Long

    fun searchByKeywordAndStatusAndOffset(
        keyword: String?,
        status: Collection<Status>,
        offset: Long
    ): Collection<UserDto>
}