plugins {
    java
    id("org.springframework.boot") version "4.1.0"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com.sourav"
version = "0.0.1-SNAPSHOT"
description = "TrasactionExtractor"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("dev.langchain4j:langchain4j:1.17.0")
    implementation("dev.langchain4j:langchain4j-pgvector:1.17.0-beta27")
    implementation("dev.langchain4j:langchain4j-document-parser-apache-pdfbox:1.17.0-beta27")
    implementation("dev.langchain4j:langchain4j-open-ai:1.17.0")
    implementation("org.projectlombok:lombok")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testAnnotationProcessor("org.projectlombok:lombok")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
