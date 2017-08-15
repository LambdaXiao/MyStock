package com.eno.base.enocoder;

public class vlong_value extends flex_unit
{
	static final byte[] bittab = {
	  0,1,2,2,3,3,3,3,4,4,4,4,4,4,4,4,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,
		6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,
		7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,
		7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,
		8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,
		8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,
		8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,
		8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8
    };

	public int share; // share count, used by vlong to delay physical copying

	public boolean is_zero()
	{
          return (n==0);
	}

	public long bit( int i )
	{
          long tmp = get(i/flex_unit.BPU);
          tmp &= (1<<(i%flex_unit.BPU));
          if (tmp == 0) return 0;
          else return 1;
	}

        public long bits() {
          // slightly more efficient version
          int x = n;
          if (x > 0) {
            long msw = get(x - 1);
            x = (x - 1) * flex_unit.BPU;
            int w = flex_unit.BPU;
            do {
              w >>= 1;
              if (msw >= (1 << w)) {
                x += w;
                msw >>= w;
              }
            }
            while (w > 8);
            x += bittab[(int)msw];
          }
          return x;
        }

        public int cf(vlong_value x) {
          if (n > x.n)
            return +1;
          if (n < x.n)
            return -1;
          int i = n;
          while (i > 0) {
            i -= 1;
            if (get(i) > x.get(i))
              return +1;
            if (get(i) < x.get(i))
              return -1;
          }
          return 0;
        }

	public void shl()
	{
          long carry=0, old, u;
          int N = n; // necessary, since n can change
          for (int i = 0; i <= N; i += 1) {
            u = get(i);
            old = u;
            u <<= 1; u &= IMASK; u += carry; u &= IMASK;
            set(i, u);
            old >>= (flex_unit.BPU - 1);
            carry = old;
          }
	}

	public int shr()
	{
          long carry = 0, old, u;
          int i = n;
          while (i > 0) {
            i -= 1;
            u = get(i);
            old = u;
            u >>= 1; u += carry; u &= IMASK;
            set(i, u);
            old <<= (flex_unit.BPU - 1);
            carry = old & IMASK;
          }

          if (carry != 0)
            return 1;
          else
            return 0;
	}

	public void shr( int xx )
	{
          int x = xx;
          int delta = x / flex_unit.BPU;
          x %= flex_unit.BPU;
          long u, tmp;
          for (int i = 0; i < n; i += 1) {
            u = get(i + delta);
            if (x > 0) {
              u >>= x;
              tmp = get(i + delta + 1);
              tmp <<= (flex_unit.BPU - x);
              u += (tmp&IMASK); u &= IMASK;
            }
            set(i, u);
          }
	}

	public void add( vlong_value x )
	{
          long carry = 0, u, ux;
          int max = n;
          if (max < x.n)
            max = x.n;
          reserve(max);
          for (int i = 0; i < max + 1; i += 1) {
            u = get(i);
            u += carry; u &= IMASK;
            carry = u < carry ? 1 : 0;
            ux = x.get(i);
            u += ux; u &= IMASK;
            carry += u < ux ? 1 : 0; carry &= IMASK;
            set(i, u);
          }
	}

	public void xor( vlong_value x )
        {
          int max = n;
          if (max < x.n)
            max = x.n;
          reserve(max);
          long tmp;
          for (int i = 0; i < max; i += 1) {
            tmp = get(i);
            tmp ^= x.get(i);
            set(i, tmp);
          }
	}

	public void and( vlong_value x )
	{
          int max = n;
          if (max < x.n)
            max = x.n;
          reserve(max);
          long tmp;
          for (int i = 0; i < max; i += 1) {
            tmp = get(i);
            tmp &= x.get(i);
            set(i, tmp);
          }
	}

	public int product( vlong_value x )
	{
          int max = n;
          if (max < x.n)
            max = x.n;
          long tmp = 0, t;
          for (int i = 0; i < max; i += 1) {
            t = get(i);
            t &= x.get(i);
            tmp ^= t;
          }
          int count = 0;
          while (tmp > 0) {
            if ( (tmp & 1) > 0)
              count ++;
            tmp >>= 1;
          }
          return (count & 1);
	}

	public void subtract( vlong_value x )
	{
          long carry = 0, ux, u, nu;
          int N = n;
          for (int i = 0; i < N; i += 1) {
            ux = x.get(i);
            ux += carry; ux &= IMASK;
            if (ux >= carry) {
              u = get(i);
              nu = u;
              if (nu >= ux)
                nu -= ux;
              else
                nu = nu + IMASK + 1 - ux;

              carry = nu > u ? 1 : 0;
              set(i, nu);
            }
          }
	}

	public void init( long x )
	{
            clear();
            set(0,x);
	}

	public void copy( vlong_value x )
	{
              clear();
              int i=x.n;
              while (i>0) { i -= 1; set( i, x.get(i) ); }
	}

	public vlong_value()
	{
          share = 0;
	}

	public void mul( vlong_value x, vlong_value y )
	{
          long tmp = x.bits();
          tmp += y.bits(); tmp &= IMASK;
          fast_mul( x, y,  (int)tmp);
	}

	public void divide( vlong_value x, vlong_value y, vlong_value rem )
	{
          init(0);
          rem.copy(x);
          vlong_value m = new vlong_value();
          vlong_value s = new vlong_value();
          m.copy(y);
          s.init(1);
          while (rem.cf(m) > 0) {
            m.shl();
            s.shl();
          }
          while (rem.cf(y) >= 0) {
            while (rem.cf(m) < 0) {
              m.shr();
              s.shr();
            }
            rem.subtract(m);
            add(s);
          }
	}
}
