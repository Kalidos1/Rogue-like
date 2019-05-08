package de.htwg.se.roguelike.aview

import de.htwg.se.roguelike.controller.Controller
import de.htwg.se.roguelike.model.{Enemy, Level, LevelCreator, Player}
import org.scalatest.{Matchers, WordSpec}

class TuiTest extends WordSpec with Matchers{

  "A Rogue-Like Tui" should {
    val player = new Player(name = "Player",posX = 5, posY = 5)
    val enemies = Vector(new Enemy(name = "TestE1",posX = 0, posY = 0), new Enemy(name = "TestE2",posX = 1, posY = 0), new Enemy(name = "TestE3",posX = 0, posY = 1))
    val controller = new Controller(player = player, enemies = enemies ,level = new Level(10))
    val tui = new Tui(controller)
    "do nothing  on input 'q'" in {
      tui.processInputLine("q")
    }
    "create a random Level on input 'r'" in {
      tui.processInputLine("r")
      controller.createRandomLevel
      controller.level.map.size should be(10)
      controller.enemies.size should be(10)
    }
    "create a Level on input 'n'" in {
      tui.processInputLine("r")
      controller.createLevel
      controller.level should be(new LevelCreator(10).createLevel(player,enemies))
    }
    "find interactions with input i" in {
      tui.processInputLine("i")
      controller.interaction should be(false)
    }
    "move up with input 'w'" in {
      tui.processInputLine("w")
      val old = controller.player.posY
      controller.moveUp
      controller.player.posY should be(old-1)
    }
    "find interactions with input 'a'" in {
      tui.processInputLine("a")
      val old = controller.player.posX
      controller.moveLeft
      controller.player.posX should be(old-1)
    }
    "find interactions with input 's'" in {
      tui.processInputLine("s")
      val old = controller.player.posY
      controller.moveUp
      controller.player.posY should be(old+1)
    }
    "find interactions with input 'd'" in {
      tui.processInputLine("d")
      val old = controller.player.posX
      controller.moveUp
      controller.player.posX should be(old+1)
    }

    "do nothing on bad input like'99999'" in {
      val old = controller.levelToString
      tui.processInputLine("99999")
      controller.levelToString should be(old)
    }
  }
}