package catsnotes.monoids.gameExample

object SlimeBank {

  val fireSlime = Slime(
    level = 10,
    element = Fire,
    hp = 10,
    att = 12,
    defence = 50,
    speed = 4
  )

  val iceSlime = Slime(
    level = 10,
    element = Ice,
    hp = 8,
    att = 9,
    defence = 60,
    speed = 3
  )

  val thunderSlime = Slime(
    level = 15,
    element = Lightning,
    hp = 10,
    att = 8,
    defence = 50,
    speed = 6
  )

  val fireSlimeKnight = SlimeKnight(
    level = 20,
    element = Fire,
    hp = 20,
    att = 24,
    defence = 100,
    speed = 8
  ) //total stats = 83.6

  val iceSlimeKnight = SlimeKnight(
    level = 20,
    element = Ice,
    hp = 16,
    att = 18,
    defence = 120,
    speed = 6
  )

  val thunderSlimeKnight = SlimeKnight(
    level = 25,
    Lightning,
    hp = 20,
    att = 18,
    defence = 100,
    speed = 12
  )

}
