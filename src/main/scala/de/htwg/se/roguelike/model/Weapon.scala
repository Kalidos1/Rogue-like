package de.htwg.se.roguelike.model

import scala.io.Source
import scala.util.Random

trait Weapon extends Item {
  val name: String
  val value: Int
  val usable: Boolean
  val dmg: Int
  val block: Int
  val oneHanded:Boolean
  val rarity: String

  /*
  def getAttack():(Int,Int,Int) = { //normalDmg,magicDmg,critDmg/trueDmg,etc
  }
  def special():(Int,Int,Int) = {
  }
  */

  def getScaledWeapon(lvl:Int): Weapon //vll nur lvl übergeben um nicht zu viel zu übergeben wen später components gibt bei anderen ethoden auch schauen und eventuel refactorn!!!
}

object Weapon {
  def apply(kind: String): Weapon = kind match {
    case "rightFist" => Sword(name = "RightFist", value = 0, usable = false, dmg = 5, block = 5,oneHanded = true, rarity = "")
    case "leftFist" => Sword(name = "LeftFist", value = 0, usable = false, dmg = 5, block = 5, oneHanded = true, rarity = "")
    case "Sword" => Sword(name = "Sword", value = 10, usable = false, dmg = 10, block = 5, oneHanded = true, rarity = "Common")
    case "random" =>
      val name:String = RandomWeapon.getWeaponName()
      val rarity:String = RandomWeapon.getRarity()
      val (value,dmg,block) = RandomWeapon.getStats(rarity)

      //hier match case mit allen waffen arten und dann zufällig ein auswählen
      Sword(name, value, usable = false, dmg, block, oneHanded = true, rarity)
  }
}

private object RandomWeapon {
  //stats noch anpassen maybe je nach waffenart extra stats also extra def getWeaponStats(weaponType:String)
  def getStats(rarity: String): (Int,Int,Int) = { //(value,dmg,block)
    val random = new Random()
    rarity match {
      case "Common" =>            (random.nextInt(10) + 1, random.nextInt(5) + 1, random.nextInt(10) + 1)
      case "Uncommon" =>          (random.nextInt(20) + 10, random.nextInt(20) + 10, random.nextInt(10) + 2)
      case "Rare" =>              (random.nextInt(30) + 20, random.nextInt(30) + 20, random.nextInt(15) + 3)
      case "Epic" =>              (random.nextInt(40) + 30, random.nextInt(40) + 30, random.nextInt(20) + 3)
      case "Legendary" =>         (random.nextInt(50) + 50, random.nextInt(50) + 50, random.nextInt(25) + 5)
      case "Golden-Legendary" =>  (random.nextInt(60) + 100,random.nextInt(60) + 60, random.nextInt(30) + 15)
      case "Seraph" =>            (random.nextInt(100) + 1000, random.nextInt(100) + 100, random.nextInt(50) + 25)
      case "Pearlescent" =>       (random.nextInt(1000) + 10000, random.nextInt(200) + 200, random.nextInt(100) + 50)
      case "Unknown" =>           (random.nextInt(9999999) + 9999999, random.nextInt(999) + 999, random.nextInt(999) + 999)
      case _ => (0,0,0)
    }
  }

  def getWeaponName(): String = {
    val fileStream = getClass.getResourceAsStream("Weapons.txt")
    val lines = Source.fromInputStream(fileStream).getLines
    var nameList = Vector("")
    lines.foreach(line => nameList = nameList :+ line)
    val random = new Random()
    val index = random.nextInt(nameList.size)
    nameList(index)
  }

  def getRarity(): String = {
    val random = new Random()
    val rarity = random.nextInt(100) + 1 //zwischen 0 und zahl-1 => (0-99) + 1
    rarity match {
      case x if 1  until 41 contains x => "Common" //White                            40%
      case x if 41 until 61 contains x => "Uncommon" //Green                          20%
      case x if 61 until 76 contains x => "Rare" //Blue                               15%
      case x if 76 until 86 contains x => "Epic" //Purple                             10%
      case x if 86 until 91 contains x => "Legendary" //Orange                        5%
      case x if 91 until 95 contains x => "Golden-Legendary" //Gold (E-tech Magenta)  4%
      case x if 95 until 98 contains x => "Seraph" //Pink                             3%
      case x if 98 until 100 contains x =>"Pearlescent" //Cyan                        2%
      case x if x == 100 =>               "Unknown" //Rainbow                         1%
      case _ => println(rarity)
        "FEHLER"
    }
  }
}
