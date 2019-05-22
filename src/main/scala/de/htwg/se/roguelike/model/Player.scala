package de.htwg.se.roguelike.model

case class Player(name: String,
                  health:Int = 100,
                  maxHealth:Int = 100,
                  mana:Int = 100,
                  maxMana:Int = 100,
                  attack:Int = 10,
                  lvl:Int = 0,
                  exp:Int = 0,
                  posX:Int = 0, posY:Int = 0,
                  inventory:Inventory = new Inventory,
                  rightHand:Weapon = new Weapon("RightFist",0,false,5,5),
                  leftHand:Weapon = new Weapon("LeftFist",0,false,5,5)
                 //helmet:Armor = new Helmet
                 ) extends Entity {

   override def toString: String =
      "Name: " + name +
        "\nhealth: " + health +
        "\nAttack: " + attack +
        "\nExperience: " + exp +
        "\nposX: " + posX +
        "\nposY: " + posY
}

