package dev.xxawesqmexx.emxtes

import dev.xxawesqmexx.emxtes.api.Emxte
import dev.xxawesqmexx.emxtes.command.EmxteCommand
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption

class Emxtes : JavaPlugin() {
    companion object {
        lateinit var instance: Emxtes
            private set
    }

    override fun onEnable() {
        instance = this

        getCommand("emxte")!!.setExecutor(EmxteCommand())
        getCommand("emxte")!!.tabCompleter = EmxteCommand()

        val configFile = File("plugins${File.separator}emxtes${File.separator}config.yml")
        if (!configFile.exists()) {
            var file = getFileFromResource("/config.yml")!!

            var targetDirectory = File("plugins${File.separator}emxtes")
            targetDirectory.mkdirs()
            var targetFile = File(targetDirectory, file.name)
            try {
                val newFilePath = Files.copy(file.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
                val newFile = newFilePath.toFile()
                val configYaml = YamlConfiguration.loadConfiguration(file)
                try { configYaml.save(newFile) }
                catch (ignored: IOException) {}
            }
            catch (ignored: IOException) {}

            targetDirectory = File("plugins${File.separator}emxtes${File.separator}emxtes_images")
            targetDirectory.mkdirs()
            val defaultEmxtes = listOf("angry.png", "attack.png", "gold_coin.png", "happy.png", "sad.png", "skull.png")
            defaultEmxtes.forEach { name ->
                file = getFileFromResource("/default_emxtes/$name")!!
                targetFile = File(targetDirectory, file.name)
                try {
                    val newFilePath = Files.copy(file.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
                    val newFile = newFilePath.toFile()
                    newFile.mkdirs()
                }
                catch (ignored: IOException) {}
            }
        }

        Emxte.loadAllEmotes()
    }

    override fun onDisable() {
    }

    private fun getFileFromResource(fileName: String): File? {
        val inputStream = javaClass.getResourceAsStream(fileName) ?: throw IllegalArgumentException("Resource not found: $fileName")
        try {
            val tempDir: Path = Files.createTempDirectory("tempResources")
            val tempFile: Path = tempDir.resolve(File(fileName).name)
            Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING)
            return tempFile.toFile()
        }
        catch (_: Exception) { }
        return null
    }
}