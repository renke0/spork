plugins {
  groovy
  `java-library`
  `maven-publish`
  id("org.sonarqube") version "2.7"
}

allprojects {
  repositories {
    mavenCentral()
  }

  apply(plugin = "groovy")
  apply(plugin = "java-library")
  apply(plugin = "maven-publish")
  apply(plugin = "org.sonarqube")

  group = "org.renke"
  version = "1.0-SNAPSHOT"

  dependencies {
    api("org.codehaus.groovy:groovy-all:2.5.8")
    api("org.spockframework:spock-core:1.3-groovy-2.5")
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

  sonarqube {
    properties {
      property("sonar.projectKey", "org.renke.spork")
      property("sonar.organization", "renke0-github")
      property("sonar.host.url", "https://sonarcloud.io")
      property("sonar.groovy.binaries", sourceSets.main.get().output.classesDirs)
    }
  }
}
