package com.priorsoftware.goodstein.scala

object Summand {
  def getPrimitive(k: Long): Summand = {
    return new Summand() {
      override def getExponent() = OrderedSum.ZERO;

      override def getFactor() = k

      override def decrement(base: Long) = {
        require(k > 0)
        if (k == 1)
          OrderedSum.ZERO
        else
          OrderedSum.getSingleton(getPrimitive(k - 1))
      }
    };
  }

  def get(factor: Long, exponent: OrderedSum): Summand = {
    if (factor == 0 || (exponent isZero))
      getPrimitive(factor);
    else new Summand() {
      override def getExponent() = exponent;

      override def getFactor() = factor

      override def decrement(base: Long) = {
        val r = get(base, exponent.decrement(base)).decrement(base);
        val ms = get(factor - 1, exponent);
        OrderedSum.add(ms, r);
      }
    }
  }
}

abstract class Summand {
  def getFactor(): Long
  def getExponent(): OrderedSum
  def decrement(base: Long): OrderedSum

  def isZero = getFactor() == 0

  override def toString() = {
    val factor = getFactor();
    if (factor == 0)
      ""
    else {
      val ret = new StringBuffer()
      val exponent = getExponent()

      if (factor > 1 || (exponent isZero))
        ret.append(getFactor());

      if (!(exponent isZero)) {
        ret.append("b");

        if (!(exponent isOne)) {
          ret.append("^");

          val compound = exponent.getSummands().size > 1;

          if (compound)
            ret.append("(");

          ret.append(exponent);

          if (compound)
            ret.append(")");
        }
      }

      ret.toString();
    }
  }

  def incrementBase(oldBase: Long, newBase: Long) = {
    val factor = getFactor();
    val exponent = getExponent();
    if (exponent isZero) {
      if (factor == oldBase)
        Summand.getPrimitive(newBase);
      else
        this;
    } else
      Summand.get(factor, exponent.incrementBase(oldBase, newBase));
  }

  def valueAt(base: Int) = {
    if (isZero)
      0;
    else {
      val exponent = getExponent().valueAt(base);
      getFactor() * Math.pow(base, exponent);
    }
  }
}
