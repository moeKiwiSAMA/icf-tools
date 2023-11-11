package cat.kiwi.game.icf.generator.parser

import cat.kiwi.game.icf.generator.ext.buildDateByteList
import cat.kiwi.game.icf.generator.ext.optVersionByteList
import cat.kiwi.game.icf.generator.ext.platformVersionByteList
import cat.kiwi.game.icf.generator.ext.versionByteList
import cat.kiwi.game.icf.generator.model.Content
import cat.kiwi.game.icf.generator.model.ContentType

/**
 * @email me@kiwi.cat
 * @date 2023/6/26 11:29
 */
object ContentParser {
    var allAPPVer = arrayListOf<ArrayList<Byte>>()
    var patchCount:Byte = 0
    fun parse(content: Content): ArrayList<Byte> {
        val appVer = arrayListOf<Byte>()
        val contentBytes = arrayListOf<Byte>()
        contentBytes.addAll(arrayListOf(0x02, 0x01, 0x00, 0x00))
        when (content.type) {
            ContentType.APP -> {
                contentBytes.addAll(arrayListOf(0x01, 0x00, 0x00, 0x00))
            }

            ContentType.PATCH -> {
                patchCount++
                contentBytes.addAll(arrayListOf(0x01, patchCount, 0x00, 0x00))
            }

            ContentType.OPTION -> {
                contentBytes.addAll(arrayListOf(0x02, 0x00, 0x00, 0x00))
            }

            ContentType.PLATFORM -> {
                contentBytes.addAll(arrayListOf(0x00, 0x00, 0x00, 0x00))
            }
        }
        contentBytes.addAll((1..0x18).map { 0x00 })
        when (content.type) {
            ContentType.OPTION -> {
                contentBytes.addAll(content.version.optVersionByteList)
            }

            ContentType.PLATFORM -> {
                contentBytes.addAll(content.version.versionByteList)
            }

            ContentType.APP -> {
                val lvb = content.version.versionByteList
                appVer.addAll(lvb)
                contentBytes.addAll(lvb)
            }

            ContentType.PATCH -> {
                val lvb = content.version.versionByteList
                appVer.addAll(lvb)
                contentBytes.addAll(lvb)
            }
        }
        val bdbl = content.buildDate.buildDateByteList

        if (content.type == ContentType.APP || content.type == ContentType.PATCH) {
            appVer.addAll(bdbl)
            appVer.add(0x00)
        }
        contentBytes.addAll(bdbl)

        contentBytes.add(0x00)

        if (content.type == ContentType.OPTION) {
            contentBytes.addAll((1..0x04).map { 0x00 })
        } else {
            contentBytes.addAll(content.requiredPlatformVersion.platformVersionByteList)
        }
        if (content.type == ContentType.APP) {
            val pvbl = content.requiredPlatformVersion.platformVersionByteList
            appVer.addAll(pvbl)
        }

        if (content.type == ContentType.PATCH) {
            val pvbl = content.requiredPlatformVersion.platformVersionByteList
            appVer.addAll(pvbl)
            contentBytes.addAll(allAPPVer.last())
        } else {
            contentBytes.addAll((1..0x10).map { 0x00 })
        }
        if (content.type == ContentType.APP || content.type == ContentType.PATCH) {
            allAPPVer.add(appVer)
        }
        return contentBytes
    }
}