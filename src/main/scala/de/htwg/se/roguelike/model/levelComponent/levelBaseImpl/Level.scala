package de.htwg.se.roguelike.model.levelComponent.levelBaseImpl

import de.htwg.se.roguelike.model.levelComponent.{PlayerInterface, LevelInterface}

case class Level(map: Land[Tile]) extends LevelInterface {
  def this(sizeY: Int,sizeX:Int) = this(new Land[Tile](sizeY,sizeX, Tile(0)))

  def moveUp(player: PlayerInterface): (Level, PlayerInterface) = {
    if (player.posY - 1 < 0) {
      return (this, player)
    }

    val player1 = player.nextPlayer(posY = player.posY - 1)
    var level = Level(map.replaceTile(player1.posY, player1.posX, Tile(5 + map.tile(player1.posY, player1.posX).value)))
    level = Level(level.map.replaceTile(player.posY, player.posX, Tile(level.map.tile(player.posY, player.posX).value - 5)))

    (level, player1)
  }

  def moveDown(player: PlayerInterface): (Level, PlayerInterface) = {
    if (player.posY + 1 > map.sizeX - 1) {
      return (this, player)
    }

    val player1 = player.nextPlayer(posY = player.posY + 1)
    var level = Level(map.replaceTile(player1.posY, player1.posX, Tile(5 + map.tile(player1.posY, player1.posX).value)))
    level = Level(level.map.replaceTile(player.posY, player.posX, Tile(level.map.tile(player.posY, player.posX).value - 5)))

    (level, player1)
  }

  def moveLeft(player: PlayerInterface): (Level, PlayerInterface) = {
    if (player.posX - 1 < 0) {
      return (this, player)
    }

    val player1 = player.nextPlayer(posX = player.posX - 1)
    var level = Level(map.replaceTile(player1.posY, player1.posX, Tile(5 + map.tile(player1.posY, player1.posX).value)))
    level = Level(level.map.replaceTile(player.posY, player.posX, Tile(level.map.tile(player.posY, player.posX).value - 5)))

    (level, player1)
  }

  def moveRight(player: PlayerInterface): (Level, PlayerInterface) = {
    if (player.posX + 1 > map.sizeY - 1) {
      return (this, player)
    }

    val player1 = player.nextPlayer(posX = player.posX + 1)
    var level = Level(map.replaceTile(player1.posY, player1.posX, Tile(5 + map.tile(player1.posY, player1.posX).value)))
    level = Level(level.map.replaceTile(player.posY, player.posX, Tile(level.map.tile(player.posY, player.posX).value - 5)))

    (level, player1)
  }

  def removeElement(col: Int, row: Int, value: Int): Level = {
    Level(this.map.replaceTile(col, row, Tile(value)))
  }

  override def toString: String = {
    val sb = new StringBuilder
    for (x <- 0 until map.sizeX) {
      for (y <- 0 until map.sizeY) {
        sb ++= (map.tile(x, y).value + " ")
      }
      sb ++= "\n"
    }
    sb.toString
  }
}
