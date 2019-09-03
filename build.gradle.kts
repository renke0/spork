plugins {
  groovy
  `java-library`
  `maven-publish`
  id("io.spring.dependency-management") version "1.0.8.RELEASE"
}

allprojects {
  repositories {
    mavenCentral()
  }

  apply(plugin = "groovy")
  apply(plugin = "java-library")
  apply(plugin = "maven-publish")
  apply(plugin = "io.spring.dependency-management")

  group = "spork"
  version = "1.0-SNAPSHOT"

  dependencyManagement {
    dependencies {
      dependency("org.codehaus.groovy:groovy-all:2.5.3")
      dependency("org.spockframework:spock-core:1.3-groovy-2.5")
      dependency("org.reflections:reflections:0.9.11")
      dependency("org.yaml:snakeyaml:1.25")
      dependency("javax.servlet:servlet-api:2.3")
      dependency("org.slf4j:slf4j-api:1.7.28")
      dependency("ch.qos.logback:logback-classic:1.2.3")
      dependency("org.mock-server:mockserver-client-java:5.6.0")
    }
  }

  tasks.register<Jar>("sourcesJar") {
    from(sourceSets.main.get().allSource)
    archiveClassifier.set("sources")
  }

  publishing {
    publications {
      create<MavenPublication>("maven") {
        from(components["java"])
        artifact(tasks["sourcesJar"])
      }
    }
  }
}
