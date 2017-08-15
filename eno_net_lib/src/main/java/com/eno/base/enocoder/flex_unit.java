package com.eno.base.enocoder;

// Provides storage allocation and index checking
public class flex_unit
{
  public int n; // used units (read-only)
  public long a[]; // array of units
  public int z; // units allocated

  public static final int BPU=32;       // 一个字节位 DWORD
  public static final long IMASK = 0xffffffffL;

  public long lo(long x) {
    return (x & 0xffff); // lower half of DWORD
  }

  public long hi(long x) {
    return (x >>= 16);
  }

  public long lh(long x) {
    return ((x << 16) & IMASK);
  }

  public long get( int i )
  {
    if (i >= n)
      return 0;
    return a[i];
  }

  public void clear()
  {
    n = 0;
  }

  public flex_unit()
  {
    z = 0;
    n = 0;
  }

  public void reserve(int x) {
    if (x > z) {
      long na[] = new long[x];
      for (int i = 0; i < n; i += 1)
        na[i] = a[i];
      a = na;
      z = x;
    }
  }

  public void set(int i, long x) {
    if (i < n) {
      a[i] = x;
      if (x == 0) {
        while (n > 0 && (a[n - 1] == 0))
          n -= 1; // normalise
      }
    }
    else if (x != 0) {
      reserve(i + 1);
//            for (int j = n; j < i; j += 1)
//              a[j].val = 0;
      a[i] = x;
      n = i + 1;
    }
  }

  public void fast_mul(flex_unit x, flex_unit y, int keep) {
    // *this = (x*y) % (2**keep)
    int i, j, limit = (keep + BPU - 1) / BPU; // size of result in words
    reserve(limit);
    for (i = 0; i < limit; i += 1)
      a[i] = 0;
    int nMin = x.n;
    if (nMin > limit)
      nMin = limit;

    long m,w,v,p,c,tmp;
    for (i = 0; i < nMin; i += 1) {
      m = x.a[i];
      int min = i + y.n;
      if (min > limit)
        min = limit;
      c = 0;
      for (j = i; j < min; j += 1) {
        // This is the critical loop
        // Machine dependent code could help here
        // c:a[j] = a[j] + c + m*y.a[j-i];
        v = a[j];
        p = y.a[j - i];
        v += c; v &= IMASK;
        c = v < c ? 1 : 0;

        w = lo(p) * lo(m); w &= IMASK;
        v += w; v &= IMASK;
        c += (v < w ? 1 : 0);

        w = lo(p) * hi(m); w &= IMASK;
        c += hi(w); c &= IMASK;
        w = lh(w);
        v += w; v &= IMASK;
        c += (v < w ? 1 : 0); c &= IMASK;

        w = hi(p) * lo(m); w &= IMASK;
        c += hi(w); c &= IMASK;
        w = lh(w);
        v += w; v &= IMASK;
        c += (v < w ? 1 : 0);  c &= IMASK;

        tmp = hi(p) * hi(m); tmp &= IMASK;
        c += tmp; c &= IMASK;
        a[j] = v;
      }
      while (c > 0 && (j < limit)) {
        a[j] += c; a[j] &= IMASK;
        c = a[j] < c ? 1 : 0;
        j += 1;
      }
    }

    // eliminate unwanted bits
    keep %= BPU;
    if (keep > 0)
      a[limit - 1] &= ( (1 << keep) - 1);

    // calculate n
    while (limit > 0 && (a[limit - 1] == 0))
      limit -= 1;
    n = limit;
  }
}

