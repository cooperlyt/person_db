package org.example.io.github.cooperlyt.db.rocksdb.defaultdb

import org.example.io.github.cooperlyt.db.rocksdb.RocksDBProperties
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
        rocksDBProperties.dbs.forEach { (name, options) ->
            repositories[name] = DefaultRocksDBRepository(options.filename)
        }
    }


    fun get(db: String, key: String): Optional<ByteArray> {

        val repository = repositories[db] ?: throw IllegalArgumentException("No repository found for $db")

        return repository.get(key)

    }

    fun put(db: String, key: String, value: ByteArray): Boolean {

        val repository = repositories[db] ?: throw IllegalArgumentException("No repository found for $db")

        return repository.save(key, value)

    }

    fun delete(db: String, key: String): Boolean {

        val repository = repositories[db] ?: throw IllegalArgumentException("No repository found for $db")

        return repository.delete(key)

    }


}