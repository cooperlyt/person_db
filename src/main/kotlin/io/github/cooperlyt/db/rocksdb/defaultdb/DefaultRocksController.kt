package org.example.io.github.cooperlyt.db.rocksdb.defaultdb

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.HttpClientErrorException
import reactor.core.publisher.Mono

@ConditionalOnProperty(prefix = "rocks", name = ["enabled"])
@RestController
@RequestMapping("db/{db}")
class DefaultRocksController(private val rocksDbService: DefaultRocksDbService) {


    @GetMapping("{key}")
    fun get(@PathVariable("db") db: String,
            @PathVariable("key")  key: String): Mono<ByteArray> {
        return Mono.justOrEmpty(rocksDbService.get(db, key))
            .switchIfEmpty(Mono.error(HttpClientErrorException(HttpStatus.NOT_FOUND)))
    }

    @PutMapping("{key}")
    fun put(@PathVariable("db") db: String,
            @PathVariable("key") key: String,
            @RequestBody value: ByteArray): Mono<Void> {
        return Mono.just(rocksDbService.put(db, key, value))
            .filter{ it }
            .switchIfEmpty(Mono.error(HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR)))
            .then()
    }

    @RequestMapping("{key}")
    fun delete(@PathVariable("db") db: String,
               @PathVariable("key") key: String): Mono<Void> {
        return Mono.just(rocksDbService.delete(db, key))
            .filter{ it }
            .switchIfEmpty(Mono.error(HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR)))
            .then()
    }
}