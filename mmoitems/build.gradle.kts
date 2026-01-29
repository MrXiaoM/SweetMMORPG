subprojects {
    apply(plugin="java")
    dependencies {
        add("compileOnly", "org.spigotmc:spigot-api:1.20-R0.1-SNAPSHOT")
        add("compileOnly", "org.jetbrains:annotations:24.0.0")
    }
}
