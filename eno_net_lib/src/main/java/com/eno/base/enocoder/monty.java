package com.eno.base.enocoder;

public class monty // class for montgomery modular exponentiation
{
	private vlong m;
	private vlong n1 = new vlong();
	private vlong T = new vlong();
	private vlong k = new vlong();   // work registers
	private int N;  // bits for R
        public vlong R = new vlong();
        public vlong R1 = new vlong();

	public monty( vlong M )
	{
          m = new vlong(M);
          N = 0;
          R = new vlong(1);

          while (vlong.isLess(R, M)) {
            R.addTo(R);
            N += 1;
          }

          vlong val = vlong.sub(R, m);
          R1 = vlong.modinv(val, m);
          n1 = vlong.sub(R, vlong.modinv(m, R));
	}

	public void mul( vlong x, vlong y )
	{
          // T = x*y;
          T.value.fast_mul(x.value, y.value, N * 2);

          // k = ( T * n1 ) % R;
          k.value.fast_mul(T.value, n1.value, N);

          // x = ( T + k*m ) / R;
          x.value.fast_mul(k.value, m.value, N * 2);
          x.addTo(T);
          x.value.shr(N);

          if (vlong.isME(x, m))
            x.subTo(m);
	}

	public vlong monty_exp( vlong x, vlong e )
	{
          vlong result = vlong.sub(R, m);
          vlong t = new vlong(x);
          t.docopy(); // careful not to modify input
          long bits = e.value.bits();
          int i = 0;
          while (true) {
            if (e.value.bit(i) > 0)
              mul(result, t);
            i += 1;
            if (i == (int)bits)
              break;
            mul(t, t);
          }
          return result; // don't convert output
	}

	public vlong exp( vlong x, vlong e )
	{
          vlong val = vlong.mul(x, R);
          val = vlong.mod(val, m);
          val = monty_exp(val, e);
          val = vlong.mul(val, R1);
          val = vlong.mod(val, m);
          return val;
	}

	//// 求 x的e次幂，再对m求模
	public static vlong modexp( vlong x, vlong e, vlong m )
	{
          monty me = new monty(m);
          return me.exp(x, e);
	}

	public static vlong monty_exp( vlong x, vlong e, vlong m )
	{
          monty me = new monty(m);
          return me.monty_exp(x, e);
	}
}
