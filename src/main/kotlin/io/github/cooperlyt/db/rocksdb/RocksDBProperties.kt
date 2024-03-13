package org.example.io.github.cooperlyt.db.rocksdb

import org.springframework.boot.context.properties.ConfigurationProperties


@ConfigurationProperties(prefix = "rocks")
class RocksDBProperties {

    class RocksDbOptions {
        var filename: String = "rocksdb"
    }

    var dbs: Map<String, RocksDbOptions> = mutableMapOf()

}