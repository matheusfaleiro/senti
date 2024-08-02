import io.gitlab.arturbosch.detekt.Detekt

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.google.ksp) apply false
    alias(libs.plugins.dagger.hilt) apply false
    alias(libs.plugins.detekt)
    alias(libs.plugins.kover) apply false
}

/**
 * This file sets up Detekt for the project. Detekt is a static code analysis tool for the Kotlin language.
 * It operates on the abstract syntax tree provided by the Kotlin compiler.
 *
 * The Detekt setup includes several key configurations:
 * - source: Defines the directories that Detekt will scan for Kotlin files. This is set to the project directory.
 * - parallel: If set to true, Detekt will build the AST (abstract syntax tree) in parallel,
 * leading to potential speedups for larger projects.
 * - config: Specifies the path to the Detekt configuration file(s).
 * - buildUponDefaultConfig: If set to false, Detekt's default config file will not be applied
 * on top of the provided config files.
 * - autoCorrect: If set to true, Detekt will attempt to automatically correct some rule violations.
 * - allRules: If set to false, not all rules are active by default.
 * - disableDefaultRuleSets: If set to false, the default Detekt rule sets are used.
 * - debug: If set to false, Detekt will not output debug information during task execution.
 * - ignoreFailures: If set to false, the build will fail if the maxIssues count is reached.
 * - ignoredBuildTypes, ignoredFlavors, ignoredVariants: Lists of Android build types, flavors,
 * and variants for which Detekt tasks should not be created.
 * - basePath: Specifies the base path for file paths in the formatted reports.
 * If not set, all file paths reported will be absolute.
 *
 * Two Detekt plugins are added as dependencies:
 * - detekt-formatting: This plugin provides additional formatting rules.
 * - detekt-rules-compose: This plugin provides rules for Jetpack Compose.
 *
 * Finally, a Detekt task is configured. This task is designed to run on the entire code base without
 * the starting overhead for each module. It includes all .kt and .kts files, excluding ones from
 * the resources and build directories. It outputs XML and HTML reports of the analysis results.
 */
detekt {
    basePath = projectDir.absolutePath

    source.setFrom(files(projectDir))

    parallel = true

    config.setFrom("$rootDir/config/detekt/detekt.yml")

    buildUponDefaultConfig = false

    autoCorrect = true

    allRules = false

    disableDefaultRuleSets = false

    debug = false

    ignoreFailures = false

    ignoredBuildTypes = listOf("release")

    ignoredFlavors = emptyList()

    ignoredVariants = emptyList()
}

tasks.getByName<Detekt>("detekt").apply {
    description = "Runs Over Whole Code Base Without the Starting Overhead for Each Module."
    
    include("**/*.kt")
    include("**/*.kts")
    exclude("**/resources/**")
    exclude("**/build/**")

    reports {
        xml.required.set(false)

        txt.required.set(false)

        html {
            outputLocation.set(file("build/reports/detekt.html"))
        }
    }
}
