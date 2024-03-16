package org.example.io.github.cooperlyt.db.rocksdb.defaultdb

import kotlinx.coroutines.reactor.mono
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
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
        return mono { rocksDbService.get(db, key) }
            .filter{ it.isPresent }
            .switchIfEmpty(Mono.error(HttpClientErrorException(HttpStatus.NOT_FOUND)))
            .map { it.get() }
    }

    @PutMapping("{key}")
    fun put(@PathVariable("db") db: String,
            @PathVariable("key") key: String,
            @RequestBody value: ByteArray): Mono<Void> {
        return mono {rocksDbService.put(db, key, value)}
            .filter{ it }
            .switchIfEmpty(Mono.error(HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR)))
            .then()
    }

    @DeleteMapping("{key}")
    fun delete(@PathVariable("db") db: String,
               @PathVariable("key") key: String): Mono<Void> {
        return mono { rocksDbService.delete(db, key) }
            .filter{ it }
            .switchIfEmpty(Mono.error(HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR)))
            .then()
    }
}