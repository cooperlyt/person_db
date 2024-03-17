package org.example.io.github.cooperlyt.db.rocksdb

import org.springframework.boot.context.properties.ConfigurationProperties


@ConfigurationProperties(prefix = "rocks")
class RocksDBProperties {

    class RocksDbOptions {
        var enabled: Boolean = true
        var filename: String = "rocksdb"
    }

    var dbs: Map<String, RocksDbOptions> = mutableMapOf()

    var path: String = "/var/lib/rocksdb"

}