package com.priorsoftware.goodstein.scala

object TestSequence {

  def main(args: Array[String]): Unit = {
    val arg = args(0).toInt
    val seq = OrderedSum.getBinaryExpansion(arg.intValue);
    println(seq);
    val orig = seq.valueAt(2);
    println(orig);
  }
}