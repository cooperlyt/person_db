import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("org.springframework.boot") version "3.2.3"
    id("io.spring.dependency-management") version "1.1.4"
    id("com.palantir.docker") version "0.35.0"
    kotlin("jvm") version "1.9.22"
    kotlin("plugin.spring") version "1.9.22"
}

group = "io.github.cooperlyt"
version = "0.0.1"

repositories {
    mavenCentral()
    mavenLocal()
}


dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    implementation("io.github.cooperlyt.mis:commons-cloud-spring-boot-starter:2.1.8")

    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions:1.2.2")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.8.0")
    //implementation("io.projectreactor:reactor-core:3.5.14")

    // implementation("org.apache.commons:commons-lang3:3.14.0")

    implementation("org.springframework.boot:spring-boot-starter-webflux")

    implementation("io.github.cooperlyt.mis:commons-utils:2.1.8")

    implementation("org.rocksdb:rocksdbjni:8.11.3")

    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")

    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "21"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

docker {
    //val jarFile = tasks.getByName<BootJar>("bootJar").archiveFileName.get()
    //val archiveBaseName = tasks.getByName<BootJar>("bootJar").outputs.files.singleFile
    //.archiveBaseName.get()

    val repositoryUrl = "dgsspfdjw.org.cn:443/person-db"
    val push = !version.toString().uppercase().contains("SNAPSHOT")
    val bootJar = tasks.getByName<BootJar>("bootJar")
//    def branchName = System.getenv("BRANCH_NAME")


//    if (push) {
////        tag("-latestTask","$repositoryUrl:latest")
//        tag("-version", "$repositoryUrl:$version")
//    }else{
//        tag("-snapshotTask", "$repositoryUrl:snapshot")
//    }

//    name = "docker.io/cooperSoft"
    name = if (push) "$repositoryUrl:$version" else repositoryUrl
//    dockerfile file('src/docker/Dockerfile')

//    files('src/docker/entrypoint.sh', bootJar.archiveFile.get())
    platform("linux/amd64", "linux/arm64")
//    load(true) //ERROR: docker exporter does not currently support exporting manifest lists
    push(push)
    buildx(push)
    copySpec.from(bootJar.outputs.files.singleFile).into("/")

    buildArgs(mapOf("JAR_FILE" to bootJar.archiveFileName.get()))
}


kotlin {
    jvmToolchain(21)
}