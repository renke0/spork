dependencies {
  api("org.codehaus.groovy:groovy-all")
  api("org.spockframework:spock-core")
  api("org.reflections:reflections")
  api("org.yaml:snakeyaml")
  api("org.slf4j:slf4j-api")
  api("ch.qos.logback:logback-classic")

  testImplementation(project(":spork-test"))
}
