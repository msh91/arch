import org.gradle.api.Plugin
import org.gradle.api.Project

class MetroLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("dev.zacsweers.metro")
            }
        }
    }
}
