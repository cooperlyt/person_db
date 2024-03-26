package io.github.cooperlyt.mis.person

import io.github.cooperlyt.commons.data.PeopleCardInfo
import jakarta.validation.Valid
import kotlinx.coroutines.reactor.mono
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.web.ErrorResponse
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.context.request.WebRequest
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import java.time.LocalDateTime

@RestController
@RequestMapping("people")
class PeopleController(private val peopleService: PeopleService) {

    @ExceptionHandler(HttpClientErrorException::class)
    fun notFoundExceptionHandler(ex: HttpClientErrorException, request: ServerHttpRequest): ResponseEntity<Map<String,Any>> {
        val body: MutableMap<String, Any> = LinkedHashMap()
        body["timestamp"] = LocalDateTime.now()
        body["status"] = ex.statusCode.value()
        body["error"] = ex.statusCode.toString()
        body["message"] = ex.message ?: "No message available"
        body["path"] = request.path.toString()

        return ResponseEntity(body, ex.statusCode)
    }

    @PutMapping(value = ["/card"], consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun putPeopleCard(@Valid @RequestBody peopleCardInfo: PeopleCardInfo): Mono<Void> {
        return mono { peopleService.putPeopleCard(peopleCardInfo) }
            .filter{ it }
            .switchIfEmpty(Mono.error(HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR)))
            .then()
    }

    @GetMapping("/card/{id}")
    fun getPeopleCard(@PathVariable("id") id: String): Mono<PeopleCardInfo> {
        return mono { peopleService.getPeopleCard(id.uppercase()) }
            .filter{ it.isPresent }
            .map{ it.get() }
            .switchIfEmpty(Mono.error(HttpClientErrorException(HttpStatus.NOT_FOUND)))
    }

    @GetMapping("/card/{id}/picture")
    fun getPeoplePicture(@PathVariable("id") id: String, response: ServerHttpResponse): Mono<Void> {
        response.headers.contentType = MediaType.IMAGE_JPEG
        return response.writeWith(mono { peopleService.getPeoplePicture(id.uppercase()) }
            .filter{ it.isPresent }
            .map { it.get() }
            .map{ response.bufferFactory().wrap(it) }
            .switchIfEmpty { Mono.error(HttpClientErrorException(HttpStatus.NOT_FOUND)) }
        )
    }


}