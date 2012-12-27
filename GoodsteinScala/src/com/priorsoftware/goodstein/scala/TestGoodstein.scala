package com.priorsoftware.goodstein.scala

object TestGoodstein {

  def main(args: Array[String]): Unit = {
    val n = args(0).toInt
    val g = new Goodstein(n);

    for (i <- 0 until 100) {
      println(g);
      g.next();
    }
  }
}
