package com.priorsoftware.goodstein.scala;

class Goodstein(n: Int) {
  require(n > 0)
  var seq = OrderedSum.getBinaryExpansion(n)
  var base = 2L

  def next() {
    if (seq isZero)
      return

    val gap = seq.getGap();

    seq = seq.incrementBase(base, base + gap);
    base += gap;

    seq = if (gap == 1) seq.decrement(base) else seq.dropLast()
  }

  override def toString() = {
    val ret = new StringBuffer("b = ");
    ret.append(base);
    ret.append(" : ");
    ret.append(seq);
    //		ret.append(" = ");
    //		ret.append(seq.valueAt(base));
    ret.toString();
  }
}
