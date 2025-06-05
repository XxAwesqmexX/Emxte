package dev.xxawesqmexx.emxtes.api

import dev.xxawesqmexx.emxtes.Emxtes
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Color
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.Particle.DustOptions
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.scheduler.BukkitRunnable
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import kotlin.math.round

class Emxte {
    companion object {
        private val emxteCache: MutableMap<String, List<Triple<Int,Int,Int>>> = mutableMapOf()

        fun loadAllEmotes() {
            emxteCache.clear()
            val configFile = File("plugins${File.separator}emxtes${File.separator}config.yml")
            val configYaml = YamlConfiguration.loadConfiguration(configFile)
            val emxtes = configYaml.getConfigurationSection("emotes")!!.getKeys(false)
            for (emxteID in emxtes) {
                if (!configYaml.contains("emotes.$emxteID.path")) {
                    Bukkit.getConsoleSender().sendMessage("${ChatColor.RED}Emote: $emxteID's image path is not set")
                    continue
                }
                val emxtePath = configYaml.getString("emotes.$emxteID.path")!!
                val emxteFile = File("plugins${File.separator}emxtes${File.separator}${emxtePath.replace("/", File.separator)}")
                if (!emxteFile.exists()) {
                    Bukkit.getConsoleSender().sendMessage("${ChatColor.RED}Emote: $emxteID's image does not exist.")
                    continue
                }
                val image = loadImage(emxteFile) ?: continue
                val rgbList = convertImageToRGBList(image) ?: continue

                emxteCache[emxteID] = rgbList
            }
        }

        private fun loadImage(file: File): BufferedImage? {
            return try { ImageIO.read(file) }
            catch (e: Exception) {
                Bukkit.getConsoleSender().sendMessage("${ChatColor.RED}Error reading the image file: ${e.message}")
                null
            }
        }
        private fun convertImageToRGBList(image: BufferedImage): List<Triple<Int, Int, Int>>? {
            val width = image.width
            val height = image.height
            if (width != 8 || height != 8) {
                Bukkit.getConsoleSender().sendMessage("${ChatColor.RED}File dimensions are incorrect, they should be 8x8 but they are ${width}x${height}")
                return null
            }
            val rgbPixels: MutableList<Triple<Int, Int, Int>> = mutableListOf()
            for (y in 0 until height) {
                for (x in 0 until width) {
                    val pixel = image.getRGB(x, y)
                    val alpha = (pixel shr 24) and 0xFF
                    val red = (pixel shr 16) and 0xFF
                    val green = (pixel shr 8) and 0xFF
                    val blue = pixel and 0xFF
                    if (alpha == 0)
                        rgbPixels.add(Triple(-1, -1, -1))
                    else
                        rgbPixels.add(Triple(red, green, blue))
                }
            }
            return rgbPixels
        }

        @JvmStatic
        fun createEmxteDisplay(drawLoc: Location, emxteID: String) {
            if (!emxteCache.containsKey(emxteID)) {
                Bukkit.getConsoleSender().sendMessage("${ChatColor.RED}Emote: $emxteID doesn't exist in the config! ${ChatColor.YELLOW}If you just added it you may need to reload the config. ${ChatColor.DARK_GRAY}'/emxte reload'")
                return
            }

            drawEmote(drawLoc, emxteCache[emxteID]!!)
        }

        @JvmStatic
        fun drawEmote(loc: Location, colors: List<Triple<Int, Int, Int>>) {
            val doX = isDoX(loc.yaw)

            val coord = (!doX).let { if (it) loc.x else loc.z}
            var smallestCoord = coord-1
            val largestCoord = coord+1

            val y = loc.y
            var largestY = y+2

            val drawLoc = (!doX).let { if (it) Location(loc.world, smallestCoord, largestY, loc.z, loc.yaw, 0.0f) else Location(loc.world, loc.x, largestY, smallestCoord, loc.yaw, 0.0f)}

            repeat(10) { repeat ->
                object : BukkitRunnable() {
                    override fun run() {
                        var i = 0
                        while (largestY > y) {
                            while (smallestCoord < largestCoord) {
                                if (colors.size < i)
                                    return
                                if (colors[i].first != -1)
                                    drawSquare(drawLoc, colors[i])
                                if (!doX) drawLoc.x += 0.25 else drawLoc.z += 0.25
                                smallestCoord += 0.25
                                i += 1
                            }
                            if (!doX) drawLoc.x -= 2 else drawLoc.z -= 2
                            smallestCoord -= 2
                            drawLoc.y -= 0.25
                            largestY -= 0.25
                        }
                    }
                }.runTaskLater(Emxtes.instance, (repeat*8).toLong())
            }
        }
        @JvmStatic
        fun drawSquare(loc: Location, colorRGB: Triple<Int, Int, Int>) {
            val color = Color.fromRGB(colorRGB.first, colorRGB.second, colorRGB.third)
            val dust = DustOptions(color, 0.8f)

            val doX = isDoX(loc.yaw)

            val coord = (!doX).let { if (it) loc.x else loc.z}
            var smallestCoord = coord-0.125
            val largestCoord = coord+0.125

            val y = loc.y
            var largestY = y+0.25

            val drawLoc = (!doX).let { if (it) Location(loc.world, smallestCoord, largestY, loc.z, loc.yaw, 0.0f) else Location(loc.world, loc.x, largestY, smallestCoord, loc.yaw, 0.0f)}

            while (smallestCoord < largestCoord) {
                while (largestY > y) {
                    drawLoc.world!!.spawnParticle(Particle.DUST, drawLoc, 1, 0.0, 0.0, 0.0, 0.0, dust)
                    drawLoc.y -= 0.06
                    largestY -= 0.06
                }
                smallestCoord += 0.06
                if (!doX) drawLoc.x += 0.06 else drawLoc.z += 0.06
                largestY += 0.25
                drawLoc.y += 0.25
            }
        }

        private fun isDoX(originalYaw: Float): Boolean {
            val yaw = ((originalYaw % 360) + 360) % 360
            val yawRounded = round(yaw).toInt()
            val yawList = (225..315) + (45..135)
            return yawList.contains(yawRounded)
        }
    }
}