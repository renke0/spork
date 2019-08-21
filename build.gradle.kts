plugins {
  groovy
  `java-library`
  `maven-publish`
  jacoco
  id("com.github.kt3k.coveralls") version "2.8.4"
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
      html.destination = file("$buildDir/jacoco/html")
    }
  }
}

tasks.register<JacocoReport>("jacocoRootReport") {
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
    xml.isEnabled = true
    csv.isEnabled = false
    html.isEnabled = true
    xml.destination = file("$buildDir/jacoco/report.xml")
  }
}

coveralls {
  jacocoReportPath = "$buildDir/jacoco/report.xml"
}
