package com.priorsoftware.goodstein.scala

object OrderedSum {

  val ZERO = new OrderedSum() {
    override def getSummands() = List()
  };

  def getSingleton(primitive: Summand) = {
    if (primitive.isZero)
      ZERO;
    else
      new OrderedSum() {
        override def getSummands() = List(primitive)
      };
  }

  def fromList(list: List[Summand]) =
    new OrderedSum() {
      override def getSummands() = list
    };

  def add(ms: Summand, r: OrderedSum) = {
    if (r isZero) {
      if (ms isZero)
        OrderedSum.ZERO;
      else
        OrderedSum.getSingleton(ms);
    } else {
      if (ms isZero)
        r
      else
        OrderedSum.fromList(ms :: r.getSummands());
    }
  }

  def getBinaryExpansion(n: Int): OrderedSum = {
    require(n >= 0)
    if (n <= 2)
      getSingleton(Summand.getPrimitive(n));
    else {
      var k = n;
      var e = 0;
      var ret = List[Summand]()
      while (k > 0) {
        if ((k & 1) == 1) {
          val el = Summand.get(1, getBinaryExpansion(e));
          ret ::= el;
        }
        e += 1;
        k >>= 1;
      }
      return fromList(ret);
    }
  }
}

abstract class OrderedSum {
  def getSummands(): List[Summand]

  def isZero = getSummands().isEmpty;

  def isOne = {
    val elements = getSummands()
    elements match {
      case List(first) if first.getFactor() == 1 => first getExponent() isZero
      case _ => false
    }
  }

  def decrement(base: Long) = {
    val elements = getSummands()

    val sz = elements.size
    require (sz > 0)

    val lastDec = elements.last.decrement(base)
    if (lastDec.isZero) {
      if (sz == 1)
        OrderedSum.ZERO;
      else
        OrderedSum.fromList(elements init);
    } else
      OrderedSum.fromList(elements.init ::: lastDec.getSummands())
  }

  def getGap() = {
    val elements = getSummands()

    val sz = elements.size
    if (sz == 0)
      0
    else {
      val last = elements last;
      if (last getExponent() isZero) last.getFactor() else 1
    }
  }

  def incrementBase(oldBase: Long, newBase: Long): OrderedSum = {
    val elements = getSummands()
    val sz = elements.size
    if (sz == 0)
      this;
    else
      OrderedSum.fromList(elements.map(_.incrementBase(oldBase, newBase)));
  }

  override def toString() = {
    val elements = getSummands();
    if (elements.isEmpty)
      "";
    else {
      val ret = new StringBuffer()
      var firstDone = true;
      for (val element <- elements) {
        if (firstDone)
          firstDone = false
        else
          ret.append(" + ")
        ret.append(element);
      }
      ret.toString();
    }
  }

  def valueAt(base: Int): Double = {
    getSummands().map(_.valueAt(base)).foldLeft[Double](0)(_+_)
  }

  def dropLast() = {
    OrderedSum.fromList(getSummands().init);
  }
}
