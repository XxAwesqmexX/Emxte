# Emxte
**Emxte** is a small API/library Spigot plugin that allows you to add emotes — however, wherever, and whichever you like — to your Minecraft server.

---

## 📦 Features

- Easy-to-use emote API
- Lightweight and modular
- Works with Minecraft 1.21+
- No config required (unless you want to add your emotes, this process is explained within the config.yml located in the Plugin's folder)

---

## 🔧 Installation

1. Download the latest release from [GitHub Releases](https://github.com/xxawesqmexx/Emxtes/releases)
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
Do NOT shade the plugin unless you understand how to use it properly.
If it is not working properly and you are using the maven shade plugin include this in your pom.xml
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

2. Example Usage:


### Skript
