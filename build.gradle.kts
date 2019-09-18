import com.bmuschko.gradle.clover.CloverContextsConvention
import com.bmuschko.gradle.clover.CloverMethodContextConvention
import com.bmuschko.gradle.clover.CloverReportConvention

buildscript {
  repositories {
    jcenter()
  }
  dependencies {
    classpath("com.bmuschko:gradle-clover-plugin:2.2.3")
  }
}

plugins {
  groovy
  `java-library`
  `maven-publish`
  `project-report`
  id("io.spring.dependency-management") version "1.0.8.RELEASE"
  id("com.bmuschko.clover") version "2.2.3"
}



tasks.register("clover") {
  group = "verification"
  dependsOn(":cloverAggregateReports")
  doLast {
    println("See the report at: file:///${project.buildDir}/reports/clover/html/index.html")
  }
}

tasks.register("maven") {
  group = "publishing"
  dependsOn(":publishToMavenLocal")
}

tasks.register("updateReadmeVersion") {
  group = "documentation"
  doLast {
    ant.withGroovyBuilder {
      "replaceregexp"(
          "match" to "^\\:spork-version\\: ([0-9\\.]+(-SNAPSHOT)?)$",
          "replace" to ":spork-version: ${project.version}",
          "flags" to "g",
          "byline" to true) {
        "fileset"("dir" to ".", "includes" to "**/*.adoc")
      }
    }
  }
}



allprojects {
  repositories {
    mavenCentral()
  }

  apply(plugin = "groovy")
  apply(plugin = "java-library")
  apply(plugin = "maven-publish")
  apply(plugin = "project-report")
  apply(plugin = "io.spring.dependency-management")
  apply(plugin = "com.bmuschko.clover")

  group = "spork"
  version = "0.1.0"

  dependencyManagement {
    dependencies {
      dependency("org.codehaus.groovy:groovy-all:2.5.8")
      dependency("org.spockframework:spock-core:1.3-groovy-2.5")
      dependency("org.reflections:reflections:0.9.11")
      dependency("org.yaml:snakeyaml:1.25")
      dependency("org.slf4j:slf4j-api:1.7.28")
      dependency("ch.qos.logback:logback-classic:1.2.3")
      dependency("org.mock-server:mockserver-client-java:5.6.1")
      dependency("org.mock-server:mockserver-netty:5.6.1")
      dependency("com.squareup.okhttp3:okhttp:4.2.0")
      dependency("net.bytebuddy:byte-buddy:1.10.1")
      dependency("org.objenesis:objenesis:3.0.1")
      dependency("org.openclover:clover:4.3.1")
      dependency("com.bmuschko:gradle-clover-plugin:2.2.0")
    }
  }

  dependencies {
    compileOnly("com.bmuschko:gradle-clover-plugin")
    clover("org.openclover:clover")
  }

  tasks.register<Jar>("sourcesJar") {
    from(sourceSets.main.get().allSource)
    archiveClassifier.set("sources")
  }

  tasks.register<Jar>("testJar") {
    dependsOn(":testClasses")
    from(sourceSets.test.get().output)
    archiveClassifier.set("tests")
  }

  configurations {
    create("test")
  }

  artifacts {
    add("test", tasks["testJar"])
  }

  publishing {
    publications {
      create<MavenPublication>("maven") {
        from(components["java"])
        artifact(tasks["sourcesJar"])
      }
    }
  }

  clover {
    contexts(closureOf<CloverContextsConvention> {
      method(delegateClosureOf<CloverMethodContextConvention> {
        name = "toString"
        regexp = "public String toString\\(\\)"
      })
    })
    report(closureOf<CloverReportConvention> {
      html = true
      filter = "toString"
    })
  }
}
