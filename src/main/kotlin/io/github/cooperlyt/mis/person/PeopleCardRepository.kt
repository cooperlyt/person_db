package org.example.io.github.cooperlyt.mis.person

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import io.github.cooperlyt.commons.data.PeopleCardInfo
import org.example.io.github.cooperlyt.db.rocksdb.JsonRocksDBRepository
import org.springframework.stereotype.Repository


@Repository
class PeopleCardRepository(objectMapper: ObjectMapper): JsonRocksDBRepository<PeopleCardInfo>(objectMapper, "people-card") {
    override fun type(): TypeReference<PeopleCardInfo> {
        return object : TypeReference<PeopleCardInfo>() {}
    }

}