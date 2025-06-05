package dev.xxawesqmexx.emxtes.command

import dev.xxawesqmexx.emxtes.api.Emxte
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor

class EmxteCommand: CommandExecutor, TabExecutor {
    @Override
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (args.isEmpty()) {
            sender.sendMessage(ChatColor.RED.toString() + "Usage: /emxte <arg>")
            return true
        }
        if (args[0] == "reload") {
            sender.sendMessage("${ChatColor.YELLOW}Reloading Emxtes...")
            Emxte.loadAllEmotes()
            sender.sendMessage("${ChatColor.GREEN}Reloaded Emxtes!")
            return true
        }
        if (args[0] == "author") {
            sender.sendMessage("${ChatColor.GREEN}Made by XxAwesqmexX <3")
            return true
        }
        return true
    }

    @Override
    override fun onTabComplete(sender: CommandSender, command: Command, label: String, args: Array<String?>): List<String> {
        val completions: MutableList<String> = ArrayList()

        if (args.size == 1) completions.addAll(listOf("reload", "author"))

        return completions
    }
}