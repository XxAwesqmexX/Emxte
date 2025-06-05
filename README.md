# Emxte
**Emxte** is a small API/library Spigot plugin that allows you to add emotes — however, wherever, and whichever you like — to your Minecraft server.

---

## 📦 Features

- Easy-to-use emote API
- Lightweight and modular
- Works with Minecraft 1.21+
- No config required (unless you want to add your emotes, this process is explained within the config.yml located in the Plugin's folder)
- Comes With Default Emotes (Scroll Down To View)

---

## 🔧 Installation

1. Download the latest release from [GitHub Releases](https://github.com/XxAwesqmexX/Emxte/releases)
2. Drop the `.jar` into your `plugins/` folder
3. Restart your server
4. Done!

---

## ⚙️ Using The API

### Java Plugin

1. Including the API in your project (pom.xml):
```xml
  <repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
  </repositories>
  
  <dependency>
      <groupId>com.github.XxAwesqmexX</groupId>
      <artifactId>Emxte</artifactId>
      <version>v1.0.0</version>
  </dependency>
```
- Do NOT shade the plugin unless you understand how to use it properly.
- If it is not working properly and you are using the maven shade plugin include this in your pom.xml
```xml
  <plugin>
      <groupId>org.apache.maven.plugins</groupId>
      <artifactId>maven-shade-plugin</artifactId>
      <version>3.5.3</version>
      <executions>
          <execution>
              <phase>package</phase>
              <goals>
                  <goal>shade</goal>
              </goals>
              <configuration>
                  <artifactSet>
                      <excludes>
                          <exclude>com.github.XxAwesqmexX:Emxte</exclude>
                      </excludes>
                  </artifactSet>
              </configuration>
          </execution>
      </executions>
  </plugin>
```
2. How To Use It:
- The Two Main Functions You Will Utilize Are `createEmxteDisplay(drawLoc: Location, emxteID: String)` and `getAllLoadedEmotes()`.
- Example In Java, This Will Draw a Happy Face Everytime a Player Breaks a Block (Note: You Must Register The Event In Your Main Class):
```java
public class Example implements Listener {
    @EventHandler
    public void onMine(BlockBreakEvent e) {
        Location loc = e.getPlayer().getLocation();
        loc.setY(loc.getY() + 2);
        Emxte.createEmxteDisplay(loc, "happy");
    }
}
```
- In Kotlin:
```kt
class Example : Listener {
    @EventHandler
    fun onMine(e: BlockBreakEvent) {
        val loc: Location = e.player.location
        loc.y += 2
        Emxte.createEmxteDisplay(loc, "happy")
    }
}
```
- The Function `getAllLoadedEmotes()` Will Simply Do As It Says and Return a List of All The Loaded Emotes.

### Skript

1. Including The API (You Must Have The Plugin Installed on Your Server and You Must Have The Skript-Reflect Addon)
- Include This Somewhere In The Skript Where You're Using The API:
```
import:
    dev.xxawesqmexx.emxtes.api.Emxte
```
2. Example Usage:
- This Will Create a Happy Face Whenever a Player Breaks a Block
```
on mine:
    Emxte.createEmxteDisplay(location 2 meters above location of player, "happy")
```
- You Can Also Use The Following To Get a List of all The Loaded Emotes:
```
command /getLoadedEmotes:
    permission: op
    trigger:
        send "%getLoadedEmotesAsList()%" to player

function getLoadedEmotesAsList() :: strings:
    set {_emotes} to "%Emxte.getAllLoadedEmotes()%"
    replace all "[", "]", " " with "" in {_emotes}
    return split {_emotes} by ","
```
