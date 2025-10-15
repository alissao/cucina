plugins {
    kotlin("jvm") version "2.2.20"
    kotlin("plugin.allopen") version "2.2.20"
    id("io.quarkus")
}

repositories {
    mavenCentral()
    mavenLocal()
}

val quarkusPlatformGroupId: String by project
val quarkusPlatformArtifactId: String by project
val quarkusPlatformVersion: String by project

dependencies {
    implementation(enforcedPlatform("${quarkusPlatformGroupId}:${quarkusPlatformArtifactId}:${quarkusPlatformVersion}"))
    implementation("io.quarkus:quarkus-rest")
    implementation("io.quarkus:quarkus-oidc-client")
    implementation("io.quarkus:quarkus-smallrye-fault-tolerance")
    implementation("io.quarkus:quarkus-smallrye-jwt")
    implementation("io.quarkus:quarkus-jdbc-postgresql")
    implementation("io.quarkus:quarkus-keycloak-authorization")
    implementation("io.quarkus:quarkus-rest-jackson")
    implementation("io.quarkus:quarkus-kotlin")
    implementation("io.quarkus:quarkus-micrometer-opentelemetry")
    implementation("io.quarkus:quarkus-oidc-redis-token-state-manager")
    implementation("io.quarkus:quarkus-info")
    implementation("io.quarkus:quarkus-container-image-docker")
    implementation("io.quarkus:quarkus-hibernate-orm-panache-kotlin")
    implementation("io.quarkus:quarkus-rest-client-jackson")
    implementation("io.quarkiverse.openapi.generator:quarkus-openapi-generator-server:2.12.0")
    implementation("io.quarkiverse.docker:quarkus-docker-client:0.0.5")
    implementation("io.quarkus:quarkus-kafka-client")
    implementation("io.quarkus:quarkus-config-yaml")
    implementation("io.quarkus:quarkus-keycloak-admin-rest-client")
    implementation("io.quarkus:quarkus-jfr")
    implementation("io.quarkiverse.vault:quarkus-vault")
    implementation("io.quarkus:quarkus-smallrye-health")
    implementation("io.quarkus:quarkus-micrometer")
    implementation("io.quarkus:quarkus-hibernate-validator")
    implementation("io.quarkus:quarkus-oidc")
    implementation("io.quarkus:quarkus-jackson")
    implementation("io.quarkus:quarkus-liquibase")
    // implementation("io.quarkiverse.hibernatetypes:quarkus-hibernate-types:2.2.0")
    implementation("org.yaml:snakeyaml:2.2")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("io.quarkus:quarkus-arc")
    implementation("io.quarkus:quarkus-hibernate-orm")
    testImplementation("io.quarkus:quarkus-junit5")
    testImplementation("io.rest-assured:rest-assured")
}

group = "org.it.business"
version = "1.0.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

tasks.withType<Test> {
    systemProperty("java.util.logging.manager", "org.jboss.logmanager.LogManager")
}
allOpen {
    annotation("jakarta.ws.rs.Path")
    annotation("jakarta.enterprise.context.ApplicationScoped")
    annotation("jakarta.persistence.Entity")
    annotation("io.quarkus.test.junit.QuarkusTest")
}

kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21
        javaParameters = true
    }
}
