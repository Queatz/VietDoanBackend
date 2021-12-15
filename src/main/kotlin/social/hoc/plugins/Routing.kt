package social.hoc.plugins

import com.arangodb.ArangoDB
import com.arangodb.velocypack.module.jdk8.VPackJdk8Module
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.serialization.Serializable

@Serializable
class Entry {
    var tu: String? = null
    var eng: List<String>? = null
    var definitions: List<String>? = null
    var examples: List<String>? = null
}

fun Application.configureRouting() {
    val db = ArangoDB.Builder()
        .user("viet")
        .password("viet")
        .acquireHostList(false)
        .registerModule(VPackJdk8Module())
        .build()
        .db("viet")

    routing {
        get("/tu/{tu}") {
            val results = db.query("""
                for tu in append(

                (for x in `tu` filter x.tu == lower(@tu) return unset(x, '_lower')),

                (for x in fulltext(`tu`, 'tu', @tu, 10) return unset(x, '_lower'))

                ) return tu
            """.trimIndent(), mapOf("tu" to (call.parameters["tu"] ?: "")), Entry::class.java).asListRemaining()

            call.respond(results)
        }
    }
}
