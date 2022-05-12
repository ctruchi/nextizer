import java.nio.file.Paths
import kotlin.io.path.readText

val html = Paths.get("/home/ctruchi/dev/wkspace/bme/MaRenov-Proto/src/index.html").readText()
val css = Paths.get("/home/ctruchi/dev/wkspace/bme/MaRenov-Proto/styles/cssComponents/footer.css").readText()

fun main() {
    var content = html
    val regex = Regex("class=\"([^\"]+)\"")

    var result = regex.find(content)
    while (result != null) {
        val classContent = result.groupValues[1]
        val classes = classContent.split(" ")
        var fixedClassContent = ""
        if (classes.size == 1) {
            fixedClassContent = "${fixSingleClass(classes.single())}"
        } else {
            fixedClassContent = "{`${classes.joinToString(" ") { fixClass(it) }}`}"
        }
        content = content.replaceRange(result.range, "className=$fixedClassContent")
        result = regex.find(content)
    }

    println(content)
}

fun fixSingleClass(content: String): String {
    if (css.contains(".$content"))
        return "{styles.${content.camelCase()}}"
    else
        return "\"${content.camelCase()}\""
}

fun fixClass(content: String): String {
    if (css.contains(".$content")) {
        return "\${styles.${content.camelCase()}}"
    } else {
        return content.camelCase()
    }
}

private fun String.camelCase() = split("-")
    .joinToString("") {
        it.replaceFirstChar { it.uppercaseChar() }
    }
    .replaceFirstChar { it.lowercaseChar() }
