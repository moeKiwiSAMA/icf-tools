package cat.kiwi.game.icf.generator.parser

import cat.kiwi.game.icf.generator.ext.crc32
import cat.kiwi.game.icf.generator.ext.reversedOctaBytes
import cat.kiwi.game.icf.generator.ext.reversedQuadBytes
import cat.kiwi.game.icf.generator.model.ICFInfo

/**
 * @email me@kiwi.cat
 * @date 2023/6/26 11:43
 */
object ICFSerializer {
    fun parse(icfInfo: ICFInfo): ArrayList<Byte> {
        if (icfInfo.appID.length != 4) throw Exception("appID must be 4 characters")
        if (icfInfo.platformID.length != 3) throw Exception("platformID must be 3 characters")
        val icfBytes = arrayListOf<Byte>()
        icfBytes.addAll((1..0x10).map { 0x00 })
        icfBytes.addAll(icfInfo.contents.size.toLong().reversedOctaBytes)
        icfBytes.addAll(icfInfo.appID.map { it.code.toByte() })
        icfBytes.addAll(icfInfo.platformID.map { it.code.toByte() })
        icfBytes.add(icfInfo.platformGeneration.toInt().toByte())
        var crcContent = 0U

        icfInfo.contents.forEach {
            crcContent = crcContent xor ContentParser.parse(it).crc32
        }
        icfBytes.addAll(crcContent.reversedQuadBytes)
        icfBytes.addAll((1..0x1c).map { 0x00 })

        ContentParser.allAPPVer.clear()
        ContentParser.patchCount = 0
        icfInfo.contents.forEach {
            icfBytes.addAll(ContentParser.parse(it))
        }
        val size = icfBytes.size.reversedQuadBytes
        icfBytes[0x04] = size[0]
        icfBytes[0x05] = size[1]
        icfBytes[0x06] = size[2]
        icfBytes[0x07] = size[3]
        val crc32 = icfBytes.subList(0x04, icfBytes.size).crc32.reversedQuadBytes
        icfBytes[0x00] = crc32[0]
        icfBytes[0x01] = crc32[1]
        icfBytes[0x02] = crc32[2]
        icfBytes[0x03] = crc32[3]
        return icfBytes
    }
}