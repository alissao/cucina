package org.it.business

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.QueryParam

/**
 * To use it via injection.
 *
 * ```kotlin
 *     @Inject
 *     @RestClient
 *     lateinit var myRemoteService: MyRemoteService
 *
 *     fun doSomething() {
 *         val restClientExtensions = myRemoteService.getExtensionsById("io.quarkus:quarkus-rest-client")
 *     }
 * ```
 * 
 * Note: This service is disabled in development to avoid external connection issues.
 */
@RegisterRestClient(baseUri = "https://stage.code.quarkus.io/api", configKey = "my-remote-service")
interface MyRemoteService {

    @GET
    @Path("/extensions")
    fun getExtensionsById(@QueryParam("id") id: String): Set<Extension>

    data class Extension(val id: String, val name: String, val shortName: String, val keywords: List<String>)
}
