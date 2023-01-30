//> using lib "org.typelevel::cats-core:2.9.0"
//> using lib "org.typelevel::cats-parse:0.3.9"

import cats.syntax.all._
import cats.parse.{Parser => P}
import cats.parse.Rfc5234.{sp, alpha, digit, hexdig}

case class Base16(
  scheme: String, author: String,
  base00: String, base01: String, base02: String, base03: String,
  base04: String, base05: String, base06: String, base07: String,
  base08: String, base09: String, base0A: String, base0B: String,
  base0C: String, base0D: String, base0E: String, base0F: String,
) {
  val printCSS: String =
    s"""|
        |/* $scheme
        | * $author
        | */
        |:root {
        |  --primary-color:   #$base0E; /*base0E*/
        |  --secondary-color: #$base09; /*base09*/
        |  --primary-medium:  #$base02; /*base02*/
        |  --primary-light:   #$base01; /*base01*/
        |  --text-color:      #$base05; /*base05*/
        |  --bg-color:        #$base00; /*base00*/
        |  --syntax-base1:    #$base01; /*base01*/
        |  --syntax-base2:    #$base02; /*base02*/
        |  --syntax-base3:    #$base0A; /*base0A*/
        |  --syntax-base4:    #$base06; /*base06*/
        |  --syntax-base5:    #$base07; /*base07*/
        |  --syntax-wheel1:   #$base0E; /*base0E*/
        |  --syntax-wheel2:   #$base08; /*base08*/
        |  --syntax-wheel3:   #$base0A; /*base0A*/
        |  --syntax-wheel4:   #$base0B; /*base0B*/
        |  --syntax-wheel5:   #$base0C; /*base0C*/
        |  --gradient-top:    #$base0D; /*base0D*/
        |  --gradient-bottom: #$base0C; /*base0C*/
        |}
        |""".stripMargin
}
object Base16 {
  def parse(s: String) = {
    val alphanums = (alpha | digit).rep.string
    val txt: P[String] = P.anyChar.filter(c => c != '"').backtrack.rep.string
    val k: P[String] = (alphanums <* (P.char(':') <* sp))
    val v: P[String] = txt.surroundedBy(P.char('"'))
    val kv: P[(String, String)] = k ~ v
    val kvs = ((kv <* sp.?) <* P.char('\n').rep.?).rep
    kvs.parse(s)
  }
  def fromString(s: String): Option[Base16] = {
    val map = parse(s).map(_._2.toList.toMap).toOption
    def d(k: String): Option[String] = map.flatMap(_.get(k))
    (
      d("scheme") :: d("author") ::
      d("base00") :: d("base01") :: d("base02") :: d("base03") ::
      d("base04") :: d("base05") :: d("base06") :: d("base07") ::
      d("base08") :: d("base09") :: d("base0A") :: d("base0B") ::
      d("base0C") :: d("base0D") :: d("base0E") :: d("base0F") ::
      Nil
    ).sequence.map(args =>
      Base16(
        args(0), args(1),
        args(2), args(3), args(4), args(5),
        args(6), args(7), args(8), args(9),
        args(10), args(11), args(12), args(13),
        args(14), args(15), args(16), args(17),
      )
    )
  }
}

object ParseApp {
  def main(args: Array[String]): Unit = {
    val s: String = scala.io.Source.fromFile("base16.yml").getLines.mkString("\n")
    println(Base16.fromString(s))
    println(Base16.fromString(s).map(_.printCSS))
  }
}
