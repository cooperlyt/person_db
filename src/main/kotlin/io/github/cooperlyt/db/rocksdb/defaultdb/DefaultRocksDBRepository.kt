package io.github.cooperlyt.db.rocksdb.defaultdb

import io.github.cooperlyt.db.rocksdb.RocksDBRepository

class DefaultRocksDBRepository(dbFilename: String): RocksDBRepository<ByteArray>(dbFilename) {
    override fun serialize(value: ByteArray): ByteArray {
        return value
    }

    override fun deserialize(bytes: ByteArray): ByteArray {
        return bytes
    }
}