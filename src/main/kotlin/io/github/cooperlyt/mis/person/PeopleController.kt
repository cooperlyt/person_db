package org.example.io.github.cooperlyt.mis.person

import io.github.cooperlyt.commons.data.PeopleCardInfo
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.HttpClientErrorException
import reactor.core.publisher.Mono

@RestController
@RequestMapping("people")
class PeopleController(private val peopleService: PeopleService) {

    @PutMapping("card")
    fun putPeopleCard(@RequestBody peopleCardInfo: PeopleCardInfo): Mono<Void> {
        return Mono.just(peopleService.putPeopleCard(peopleCardInfo))
            .filter{ it }
            .switchIfEmpty(Mono.error(HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR)))
            .then()
    }

    @GetMapping("card/{id}")
    fun getPeopleCard(@PathVariable("id") id: String): Mono<PeopleCardInfo> {
        return Mono.just(peopleService.getPeopleCard(id))
            .filter{ it.isPresent }
            .switchIfEmpty(Mono.error(HttpClientErrorException(HttpStatus.NOT_FOUND)))
            .map{ it.get() }
    }

    @GetMapping("card/{id}/picture")
    fun getPeoplePicture(@PathVariable("id") id: String, response: ServerHttpResponse): Mono<Void> {
        response.headers.contentType = MediaType.IMAGE_JPEG
        return response.writeWith(Mono.just(peopleService.getPeoplePicture(id))
            .map{ response.bufferFactory().wrap(it) }
        )
    }


}