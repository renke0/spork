plugins {
  groovy
  `java-library`
  `maven-publish`
  jacoco
}

allprojects {
  repositories {
    mavenCentral()
  }

  apply(plugin = "groovy")
  apply(plugin = "java-library")
  apply(plugin = "maven-publish")
  apply(plugin = "jacoco")

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

  jacoco {
    toolVersion = "0.8.4"
    reportsDir = file("$buildDir/jacoco")
  }
}

subprojects {
  tasks.jacocoTestReport {
    additionalSourceDirs.setFrom(files(sourceSets.main.get().allSource.srcDirs))
    sourceDirectories.setFrom(files(sourceSets.main.get().allSource.srcDirs))
    classDirectories.setFrom(files(sourceSets.main.get().output))
    reports {
      xml.isEnabled = false
      csv.isEnabled = false
    }
  }
}

tasks.register<JacocoReport>("codeCoverageReport") {
  dependsOn(subprojects.map { it.getTasksByName("test", true) })
  additionalSourceDirs.setFrom(files(subprojects.map {
    it.sourceSets.main.get().allSource.srcDirs
  }))
  sourceDirectories.setFrom(files(subprojects.map {
    it.sourceSets.main.get().allSource.srcDirs
  }))
  classDirectories.setFrom(files(subprojects.map {
    it.sourceSets.main.get().output
  }))
  executionData.setFrom(files(subprojects.flatMap {
    it.getTasksByName("jacocoTestReport", true).map { r ->
      (r as JacocoReport).executionData
    }
  }))
  reports {
    csv.isEnabled = false
    xml.isEnabled = true
    xml.destination = file("$buildDir/reports/jacoco/report.xml")
    html.isEnabled = true
    html.destination = file("$buildDir/reports/jacoco/report")
  }
}

