package io.github.cooperlyt.db.rocksdb.defaultdb

import io.github.cooperlyt.db.rocksdb.RocksDBProperties
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Service
import java.util.*


@ConditionalOnProperty(prefix = "rocks", name = ["enabled"])
@EnableConfigurationProperties(RocksDBProperties::class)
@Service
class DefaultRocksDbService(rocksDBProperties: RocksDBProperties) {

    private val repositories: MutableMap<String, DefaultRocksDBRepository> = mutableMapOf()

    init {
        rocksDBProperties.dbs
            .filter { it.value.enabled }
            .forEach { (name, options) -> repositories[name] = DefaultRocksDBRepository(options.filename) }
    }


    suspend fun get(db: String, key: String): Optional<ByteArray> {

        val repository = repositories[db] ?: throw IllegalArgumentException("No repository found for $db")

        return repository.get(key)

    }

    suspend fun put(db: String, key: String, value: ByteArray): Boolean {

        val repository = repositories[db] ?: throw IllegalArgumentException("No repository found for $db")

        return repository.save(key, value)

    }

    suspend fun delete(db: String, key: String): Boolean {

        val repository = repositories[db] ?: throw IllegalArgumentException("No repository found for $db")

        return repository.delete(key)

    }


}