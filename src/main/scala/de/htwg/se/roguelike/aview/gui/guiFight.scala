package de.htwg.se.roguelike.aview.gui

import java.awt.{Color, Font, Graphics2D}
import java.awt.image.BufferedImage

import de.htwg.se.roguelike.controller.{Controller, GameStatus}

import scala.swing.event.ButtonClicked
import scala.swing.{Button, Dimension, FlowPanel, Label, Panel}

case class guiFight(controller: Controller, gui: SwingGui) extends StateGui {
  override def processInputLine(input: String): Unit = {
    input match {
      case "q" =>
      case "1" => controller.attack()
      case "2" => controller.block()
      case "3" => controller.special()
      case "r" => controller.run()
      //case "enter" match
      case "i" => {
        controller.setGameStatus(GameStatus.INVENTORY)
        controller.inventoryGameStatus = GameStatus.FIGHT
      }
      case _ =>
        print("Wrong Input!!!")
    }
  }

  override def handle(): Unit = {
    val e = controller.gameStatus
    e match {
      case GameStatus.LEVEL => gui.state = new guiLevel(controller, gui)
      case GameStatus.FIGHT => gui.state = this
      case GameStatus.FIGHTSTATUS => gui.state = this
      case GameStatus.INVENTORY => gui.state = new guiInventoryMain(controller, gui)
      case GameStatus.GAMEOVER => gui.state = new guiGameOver(controller, gui)
      case GameStatus.PLAYERLEVELUP => gui.state = new guiPlayerLevelUp(controller, gui)
      case GameStatus.LOOTENEMY => gui.state = new guiLootEnemy(controller, gui)
      case _ =>
        print("Wrong GameStatus!!!")
    }
  }

  override def drawPanel(SCALE: Int): Panel = {

    val panel = new FlowPanel() {

      peer.setLayout(null)

      //ersma so aber eig eigene texturen für fihgt
      val playerSpriteSheet = new SpriteSheet("resources/Player.png")
      val fightSpriteSheet = new SpriteSheet("resources/TextBar.png")
      val fightBackgroundSpriteSheet = new SpriteSheet("resources/FightBackground1.png")
      val enemiesSpriteSheet = new SpriteSheet("resources/Enemy.png")
      val backgroundSpriteSheet = new SpriteSheet("resources/16bitSpritesBackground.png")

      val playerTexture = playerSpriteSheet.getSprite(16, 0,16)
      val enemyTextureBlue = enemiesSpriteSheet.horizontalFlip(enemiesSpriteSheet.getSprite(0, 32,16)) //zum flippen vll in eigene klasse?!?!?!?
      val enemyTextureRed = enemiesSpriteSheet.horizontalFlip(enemiesSpriteSheet.getSprite(0, 16,16))
      val enemyTextureGreen = enemiesSpriteSheet.horizontalFlip(enemiesSpriteSheet.getSprite(0, 0,16))
      val errorTexture = backgroundSpriteSheet.getSprite(32, 16,16)
      val fight = fightSpriteSheet.getImage()
      val fightBackground = fightBackgroundSpriteSheet.getImage()

      preferredSize = new Dimension(256 * SCALE, 144 * SCALE)

      val attack = new Button()
      attack.peer.setBounds(10 * SCALE,110 * SCALE, 30 * SCALE, 30 * SCALE)
      listenTo(attack)
      contents += attack

      reactions += {
        case ButtonClicked(a) if a == attack => controller.attack()
      }

      override def paintComponent(g: Graphics2D): Unit = {

        g.drawImage(fightBackground, 0, 0, 256 * SCALE, 144 * SCALE, null)
        g.drawImage(playerTexture, 10 * SCALE, 60 * SCALE, 32 * SCALE, 32 * SCALE, null)

        controller.currentEnemy.enemyType match {
          case 1 => g.drawImage(enemyTextureBlue, 210 * SCALE, 60 * SCALE, 32 * SCALE, 32 * SCALE, null)
          case 2 => g.drawImage(enemyTextureRed, 210 * SCALE, 60 * SCALE, 32 * SCALE, 32 * SCALE, null)
          case 3 => g.drawImage(enemyTextureGreen, 210 * SCALE, 60 * SCALE, 32 * SCALE, 32 * SCALE, null)
          case _ => g.drawImage(errorTexture, 210 * SCALE, 60 * SCALE, 32 * SCALE, 32 * SCALE, null)
        }


        g.drawImage(fight, 0, 0, 256 * SCALE, 144 * SCALE, null)

        g.setFont(new Font("TimesRoman", Font.BOLD, 4 * SCALE))
        //HealthBar
        g.setColor(Color.BLACK)
        g.fillRect(4 * SCALE, 22 * SCALE, 25 * SCALE, 5 * SCALE)
        g.setColor(Color.RED)

        var HealthHelp1 = 0
        var healthbarWidth = 0
        if (controller.player.maxHealth < 200) {
          HealthHelp1 = controller.player.maxHealth / 100
          val HealthHelp2 = controller.player.health / HealthHelp1
          healthbarWidth = HealthHelp2 * 25 / controller.player.maxHealth
        } else {
          HealthHelp1 = controller.player.maxHealth / 100
          healthbarWidth = controller.player.health * 25 / controller.player.maxHealth
        }

        g.fillRect(4 * SCALE, 22 * SCALE, healthbarWidth * SCALE, 5 * SCALE)
        g.setColor(Color.BLACK)
        g.drawRect(4 * SCALE, 22 * SCALE, 25 * SCALE, 5 * SCALE)
        g.drawString("Health", 4 * SCALE, 21 * SCALE)
        g.setColor(Color.WHITE)
        g.drawString(controller.player.health + "/" + controller.player.maxHealth, 8 * SCALE, 26 * SCALE)
        //g.drawString(help2 + "%", 8 * SCALE, 24 * SCALE) für prozentAnzeige

        g.setColor(Color.BLACK)
        g.fillRect(226 * SCALE, 22 * SCALE, 25 * SCALE, 5 * SCALE)
        g.setColor(Color.RED)

        var EHealthHelp1 = 0
        var EhealthbarWidth = 0
        if (controller.currentEnemy.maxHealth < 200) {
          EHealthHelp1 = controller.currentEnemy.maxHealth / 100
          val EHealthHelp2 = controller.currentEnemy.health / EHealthHelp1
          EhealthbarWidth = EHealthHelp2 * 25 / controller.currentEnemy.maxHealth
        } else {
          EHealthHelp1 = controller.currentEnemy.maxHealth / 100
          EhealthbarWidth = controller.currentEnemy.health * 25 / controller.currentEnemy.maxHealth
        }

        g.fillRect(226 * SCALE, 22 * SCALE, EhealthbarWidth * SCALE, 5 * SCALE)
        g.setColor(Color.BLACK)
        g.drawRect(226 * SCALE, 22 * SCALE, 25 * SCALE, 5 * SCALE)
        g.drawString("Health", 226 * SCALE, 21 * SCALE)
        g.setColor(Color.WHITE)
        g.drawString(controller.currentEnemy.health + "/" + controller.currentEnemy.maxHealth, 230 * SCALE, 26 * SCALE)

        //ManaBar
        g.setColor(Color.BLACK)
        g.fillRect(4 * SCALE, 35 * SCALE, 25 * SCALE, 5 * SCALE)
        g.setColor(Color.BLUE)
        var ManaHelp1 = 0
        var manabarWidth = 0
        if (controller.player.maxMana < 200) {
          ManaHelp1 = controller.player.maxMana / 100
          val ManaHelp2 = controller.player.mana / ManaHelp1
          manabarWidth = ManaHelp2 * 25 / controller.player.maxMana
        } else {
          ManaHelp1 = controller.player.maxMana / 100
          manabarWidth = controller.player.mana * 25 / controller.player.maxMana
        }
        g.fillRect(4 * SCALE, 35 * SCALE, manabarWidth * SCALE, 5 * SCALE)
        g.setColor(Color.BLACK)
        g.drawRect(4 * SCALE, 35 * SCALE, 25 * SCALE, 5 * SCALE)
        g.drawString("Mana", 4 * SCALE, 34 * SCALE)
        g.setColor(Color.WHITE)
        g.drawString(controller.player.mana + "/" + controller.player.maxMana, 8 * SCALE, 39 * SCALE)

        g.setColor(Color.BLACK)
        g.fillRect(226 * SCALE, 35 * SCALE, 25 * SCALE, 5 * SCALE)
        g.setColor(Color.BLUE)
        var EManaHelp1 = 0
        var EmanabarWidth = 0
        if (controller.currentEnemy.maxMana < 200) {
          EManaHelp1 = controller.currentEnemy.maxMana / 100
          val EManaHelp2 = controller.currentEnemy.mana / EManaHelp1
          EmanabarWidth = EManaHelp2 * 25 / controller.currentEnemy.maxMana
        } else {
          EManaHelp1 = controller.currentEnemy.maxMana / 100
          EmanabarWidth = controller.currentEnemy.mana * 25 / controller.currentEnemy.maxMana
        }
        g.fillRect(226 * SCALE, 35 * SCALE, EmanabarWidth * SCALE, 5 * SCALE)
        g.setColor(Color.BLACK)
        g.drawRect(226 * SCALE, 35 * SCALE, 25 * SCALE, 5 * SCALE)
        g.drawString("Mana", 226 * SCALE, 34 * SCALE)
        g.setColor(Color.WHITE)
        g.drawString(controller.currentEnemy.mana + "/" + 100, 230 * SCALE, 39 * SCALE)

        //--Name--LEVEL--
        g.setColor(Color.BLACK)
        g.setFont(new Font("TimesRoman", Font.BOLD, 5 * SCALE))
        g.drawString(controller.player.name, 5 * SCALE, 10 * SCALE)
        g.drawString("Level:  " + controller.player.lvl, 5 * SCALE, 15 * SCALE)
        g.drawString(controller.currentEnemy.name, 226 * SCALE, 10 * SCALE)
        g.drawString("Level:  " + controller.currentEnemy.lvl, 226 * SCALE, 15 * SCALE)

        g.setFont(new Font("TimesRoman", Font.PLAIN, 10 * SCALE))
        //g.drawString("Health", 5 * SCALE, 10 * SCALE)
        g.drawString("[1]Attack   [2]:Block   [3]:Special   [i]Inventory    [r]:Run", 5 * SCALE, 125 * SCALE)

      }

      repaint()
    }
    panel
  }

}
