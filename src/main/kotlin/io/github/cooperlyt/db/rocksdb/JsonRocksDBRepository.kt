package org.example.io.github.cooperlyt.db.rocksdb

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper

abstract class JsonRocksDBRepository<V: Any>(private val objectMapper: ObjectMapper,
                                             dbFilename: String) : RocksDBRepository<V>(dbFilename){
    override fun serialize(value: V): ByteArray {
        return objectMapper.writeValueAsString(value).toByteArray()
    }

    override fun deserialize(bytes: ByteArray): V {
        return objectMapper.readValue(bytes, type())
    }

    abstract fun type(): TypeReference<V> // Class<V>

}