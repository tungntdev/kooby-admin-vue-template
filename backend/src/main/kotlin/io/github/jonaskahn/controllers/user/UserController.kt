package io.github.jonaskahn.controllers.user

import io.github.jonaskahn.assistant.PageData
import io.github.jonaskahn.constants.Roles
import io.github.jonaskahn.middlewares.role.AccessVerifier
import io.github.jonaskahn.services.user.UserDto
import io.github.jonaskahn.services.user.UserService
import io.jooby.annotation.GET
import io.jooby.annotation.POST
import io.jooby.annotation.Path
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.inject.Inject

@Tag(name = "User", description = "APIs for user management and user-related operations")
@Path
class UserController @Inject constructor(
    private val userService: UserService,
    private val accessVerifier: AccessVerifier
) {

    @GET("/secure/user/info")
    fun info(): UserDto {
        return userService.getCurrentUserInfo()
    }

    @GET("/secure/user/info-with-executor")
    fun getInfo(): UserDto {
        return userService.getCurrentUserInfoWithExecutor()
    }

    @POST("/user/register")
    fun register(request: UserRegisterRequest) {
        userService.createUser(request)
    }

    @POST("/user/searchRecord")
    fun search(request: UserSearchRequest): PageData<UserDto> {
        return userService.search(request.keyword, request.statuses, request.pageNo)
    }

    @GET("/secure/user/find-copy-users")
    fun findCopyUsers(): List<UserDto> {
        accessVerifier.requireRole(Roles.DATA_ENTRY_PERSON)
        return userService.findAllCopyUsers()
    }
}