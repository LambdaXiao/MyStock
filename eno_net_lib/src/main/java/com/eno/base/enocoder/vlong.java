package com.eno.base.enocoder;

import com.eno.base.utils.ENOUtils;

public class vlong
{
	public vlong_value value;
	public int negative;

	public static final vlong ZERO = new vlong();

	// Implementation of vlong
	public static boolean notEqual( vlong x, vlong y ){ return x.cf( y ) != 0; }
	public static boolean isEqual( vlong x, vlong y ){ return x.cf( y ) == 0; }
	public static boolean isME( vlong x, vlong y ){ return x.cf( y ) >= 0; }
	public static boolean isLE( vlong x, vlong y ){ return x.cf( y ) <= 0; }
	public static boolean isMore( vlong x, vlong y ){ return x.cf( y ) > 0; }
	public static boolean isLess( vlong x, vlong y ){ return x.cf( y ) < 0; }

	public void load( long[] a, int n )
	{
          docopy();
          value.clear();
          for (int i = 0; i < n; i += 1)
            value.set(i, a[i]);
	}

	public void load( byte[] a, int off, int len )
	{
          int nWords = (len + 3) / 4;
          long[] iVal = new long[nWords];
          int nOff = off;
          for (int i = 0; i < nWords; i++) {
            iVal[i] = ENOUtils.bytes2Integer(a, nOff);
            if (iVal[i] < 0) iVal[i] += flex_unit.IMASK + 1;
            nOff += 4;
          }

          load(iVal, nWords);
	}

	public byte[] getBytes()
	{
          byte[] out = new byte[4 * value.n];
          int off = 0;
          for (int i = 0; i < value.n; i++) {
            ENOUtils.integer2Bytes(out, off, (int)value.a[i]);
            off += 4;
          }

          return out;
	}

	public void docopy()
	{
          if (value.share > 0) {
            value.share -= 1;
            vlong_value nv = new vlong_value();
            nv.copy(value);
            value = nv;
          }
	}

	public long bits()
	{
          return value.bits();
	}

	public long bit(int i)
	{
          return value.bit(i);
	}

	public int cf(vlong x )
	{
          boolean neg = negative > 0 && (!value.is_zero());
          if (neg == (x.negative > 0 && !x.value.is_zero()))
            return value.cf(x.value);
          else if (neg)
            return -1;
          else
            return +1;
	}

	public vlong ()
	{
          value = new vlong_value();
          negative = 0;
          value.init(0);
	}

	public vlong (long x)
	{
            value = new vlong_value();
            negative = 0;
            value.init(x);
	}

	public vlong ( vlong x ) // copy constructor
	{
          value = new vlong_value();
          negative = 0;
          value.init(0);
          setTo(x);
	}

	public void setTo(vlong x)
	{
          if (value.share > 0)
            value.share -= 1;
          value = x.value;
          value.share += 1;
          negative = x.negative;
	}

	public void xorTo(vlong x)
	{
          docopy();
          value.xor(x.value);
	}

	public void andTo( vlong x)
	{
          docopy();
          value.and(x.value);
	}

	public void shr( int n ) // divide by 2**n
	{
          docopy();
          value.shr(n);
	}

	public void addTo(vlong x)
	{
          if (negative == x.negative) {
            docopy();
            value.add(x.value);
          }
          else if (value.cf(x.value) >= 0) {
            docopy();
            value.subtract(x.value);
          }
          else {
            vlong tmp = new vlong(this);
            setTo(x);
            addTo(tmp);
          }
	}

	public void subTo (vlong x)
	{
          if (negative != x.negative) {
            docopy();
            value.add(x.value);
          }
          else if (value.cf(x.value) >= 0) {
            docopy();
            value.subtract(x.value);
          }
          else {
            vlong tmp = new vlong(this);
            setTo(x);
            subTo(tmp);
            negative = 1 - negative;
          }
	}

	public static vlong add( vlong x, vlong y )
	{
          vlong result = new vlong(x);
          result.addTo(y);
          return result;
	}

	public static vlong sub( vlong x, vlong y )
	{
          vlong result = new vlong(x);
          result.subTo(y);
          return result;
	}

	public static vlong mul( vlong x, vlong y )
	{
          vlong result = new vlong();
          result.value.mul(x.value, y.value);
          result.negative = x.negative ^ y.negative;
          return result;
	}

	public static vlong divide( vlong x, vlong y )
	{
          vlong result = new vlong();
          vlong_value rem = new vlong_value();
          result.value.divide(x.value, y.value, rem);
          result.negative = x.negative ^ y.negative;
          return result;
	}

	public static vlong mod( vlong x, vlong y )
	{
          vlong result = new vlong();
          vlong_value divide = new vlong_value();
          divide.divide(x.value, y.value, result.value);
          result.negative = x.negative; // not sure about this?
          return result;
	}

	public static vlong xor( vlong x, vlong y )
	{
          vlong result = new vlong(x);
          result.xorTo(y);
          return result;
	}

	public static vlong and( vlong x, vlong y )
	{
          vlong result = new vlong(x);
          result.andTo(y);
          return result;
	}

	public static vlong shl( vlong x, int n ) // multiply by 2**n
	{
          vlong result = new vlong(x);
          while (n > 0) {
            n -= 1;
            result.addTo(result);
          }
          return result;
	}

	public static vlong abs( vlong x )
	{
          vlong result = new vlong(x);
          result.negative = 0;
          return result;
	}

	public static int product( vlong X, vlong Y )
	{
          return X.value.product(Y.value);
	}

	public static vlong modinv( vlong a, vlong m ) // modular inverse
	// returns i in range 1..m-1 such that i*a = 1 mod m
	// a must be in range 1..m-1
	{
          vlong j = new vlong(1);
          vlong i = new vlong(0);
          vlong b = new vlong(m);
          vlong c = new vlong(a);
          vlong x, y;
          while (vlong.notEqual(c, vlong.ZERO)) {
            x = vlong.divide(b, c);
            y = vlong.sub(b, vlong.mul(x, c));
            b = c;
            c = y;
            y = j;
            j = vlong.sub(i, vlong.mul(j, x));
            i = y;
          }
          if (vlong.isLess(i, vlong.ZERO))
            i.addTo(m);
          return i;
	}
}
