dependencies {
  api(project(":spork-http-mock"))
  api("org.mock-server:mockserver-client-java")

  testImplementation(project(":spork-test"))
  testImplementation(project(path = ":spork-http-mock", configuration = "test"))
  testImplementation("org.mock-server:mockserver-netty")
}
