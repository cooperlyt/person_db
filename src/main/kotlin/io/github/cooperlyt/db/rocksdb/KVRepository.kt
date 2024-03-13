package org.example.io.github.cooperlyt.db.rocksdb

import java.util.*

interface KVRepository<K,V> {

    fun save(key: K, value: V): Boolean

    fun get(key: K): Optional<V>

    fun delete(key: K): Boolean
}