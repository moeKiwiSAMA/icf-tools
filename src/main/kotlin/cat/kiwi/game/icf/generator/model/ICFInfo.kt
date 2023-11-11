package cat.kiwi.game.icf.generator.model


/**
 * @email me@kiwi.cat
 * @date 2023/6/26 10:49
 */
data class ICFInfo(
    val appID: String,
    val platformID: String,
    val platformGeneration: String,
    val contents: List<Content>
)
data class Content(
    val type: ContentType,
    val version: String,
    val buildDate: String,
    val requiredPlatformVersion: String
)
enum class ContentType {
    PLATFORM, APP, PATCH, OPTION
}