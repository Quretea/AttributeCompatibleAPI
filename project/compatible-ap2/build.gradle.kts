dependencies {
    compileOnly(project(":project:common"))
    compileOnly(fileTree("libs") { include("*.jar") })
}