package cat.kiwi.game.icf.generator.parser

import cat.kiwi.game.icf.generator.ext.*
import cat.kiwi.game.icf.generator.model.Content
import cat.kiwi.game.icf.generator.model.ICFInfo

class ICFDeserializer {
    fun parse(originBytes: ByteArray): ICFInfo {
        val icfBytes = originBytes.slice(8 until originBytes.size)
        val appID = icfBytes.slice(16..19).joinToString("") { it.toInt().toChar().toString() }
        val platformID = icfBytes.slice(20..22).joinToString("") { it.toInt().toChar().toString() }
        val platformGeneration = icfBytes[23].toInt().toString()
        val contentsCount = icfBytes.slice(8..15).reversed().toInt()
        val contents = mutableListOf<Content>()
        var startIndex = 56
        for (i in 0 until contentsCount) {
            val bytes = icfBytes.slice(startIndex + 4 until startIndex +6 )
            val type = bytes.contentType
            val version = icfBytes.slice(startIndex + 32 until startIndex + 36).convertToVersion(type)
            val buildDate = icfBytes.slice(startIndex + 36 until startIndex + 43).buildDate
            val requiredPlatformVersion = icfBytes.slice(startIndex + 44 until startIndex + 48).requiredPlatformVersion
            contents.add(Content(type, version, buildDate, requiredPlatformVersion))
            startIndex += 64
        }
        return ICFInfo(appID, platformID, platformGeneration, contents)
    }
}