package org.example.io.github.cooperlyt.mis.person

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "person.people")
class PeopleProperties {

    class PeopleCardOptions {

        var validExpire: Boolean = true

        var db: String = "people-card"
    }


    var card: PeopleCardOptions = PeopleCardOptions()
}