package org.example.io.github.cooperlyt.db.rocksdb

import org.rocksdb.Options
import org.rocksdb.RocksDB
import org.rocksdb.RocksDBException
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.util.*


abstract class RocksDBRepository<V: Any>(dbFilename: String, path: String = "/var/lib/rocksdb"): KVRepository<String, V> {

    companion object {
        private val log = org.slf4j.LoggerFactory.getLogger(RocksDBRepository::class.java)

        //private const val DB_FILE_PATH = "/var/lib/rocksdb"
    }

    abstract fun serialize(value: V): ByteArray;
    abstract fun deserialize(bytes: ByteArray): V;

    private lateinit var db: RocksDB

    init {
        RocksDB.loadLibrary()
        val options = Options()
        options.setCreateIfMissing(true)
        val dbDir = File(path, dbFilename)
        try {
            Files.createDirectories(dbDir.getParentFile().toPath())
            Files.createDirectories(dbDir.getAbsoluteFile().toPath())
            db = RocksDB.open(options, dbDir.absolutePath)
            log.info("RocksDB initialized: {}", dbFilename)
        } catch (e: IOException) {
            log.error("Error initializng RocksDB. Exception: '{}', message: '{}'", e.cause, e.message, e)
        } catch (e: RocksDBException) {
            log.error("Error initializng RocksDB. Exception: '{}', message: '{}'", e.cause, e.message, e)
        }
    }

    @Synchronized
    override fun save(key: String, value: V): Boolean {
        log.info("save")
        try {
            db.put(key.toByteArray(), serialize(value))
        } catch (e: RocksDBException) {
            log.error("Error saving entry in RocksDB, cause: {}, message: {}", e.cause, e.message)
            return false
        }
        return true
    }

    @Synchronized
    override fun get(key: String): Optional<V> {
        try {
            val bytes = db[key.toByteArray()]
            return if (bytes != null) Optional.of(deserialize(bytes)) else Optional.empty()
        } catch (e: RocksDBException) {
            log.error(
                "Error retrieving the entry with key: {}, cause: {}, message: {}",
                key,
                e.cause,
                e.message
            )
            return Optional.empty()
        }
    }

    @Synchronized
    override fun delete(key: String): Boolean {
        log.info("deleting key '{}'", key)
        try {
            db.delete(key.toByteArray())
        } catch (e: RocksDBException) {
            log.error("Error deleting entry, cause: '{}', message: '{}'", e.cause, e.message)
            return false
        }
        return true
    }

}