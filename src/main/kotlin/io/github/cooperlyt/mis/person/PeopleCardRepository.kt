package io.github.cooperlyt.mis.person

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import io.github.cooperlyt.commons.data.PeopleCardInfo
import io.github.cooperlyt.db.rocksdb.JsonRocksDBRepository
import io.github.cooperlyt.db.rocksdb.RocksDBProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Repository


@Repository
@EnableConfigurationProperties(PeopleProperties::class, RocksDBProperties::class)
class PeopleCardRepository(objectMapper: ObjectMapper,
                           peopleProperties: PeopleProperties,
                           rocksDBProperties: RocksDBProperties): JsonRocksDBRepository<PeopleCardInfo>(objectMapper, peopleProperties.card.db, rocksDBProperties.path) {
    override fun type(): TypeReference<PeopleCardInfo> {
        return object : TypeReference<PeopleCardInfo>() {}
    }

}