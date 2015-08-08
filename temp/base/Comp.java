// compare

import com.jsortware.jn.base.base;
import com.jsortware.jn.base.comp;

/*
 * x,y,XY = original and combined Stringlists
 * AX = indices of x in XY
 * X  = items of x not yet accounted for
 * NX = indices of x not yet accounted for
 * SX = indices of x not matched
*/


boolean qvcontainseach(QVector<int>v,QVector<int>w);
QVector<int> qvlastseq(QVector<int>v,QVector<int>w);
int qvremseq(QVector<int>x,QVector<int>nx,QVector<int>p,QVector<int>*s);

// ---------------------------------------------------------------------
String Compare::comp(String[] x,String[] y)
{
  int i,m,n;
  String[] r,rx,ry;

  XY=x+y;
  n=x.length();
  if(n==0) return "empty left argument";
  X.resize(n);
  for (i=0; i<n; i++)
    X.replace(i,XY.indexOf(x[i]));
  AX=X;
  NX.resize(n);
  for (i=0; i<n; i++)
    NX.replace(i,i);

  n=y.length();
  if(n==0) return "empty right argument";
  Y.resize(n);
  for (i=0; i<n; i++)
    Y.replace(i,XY.indexOf(y[i]));
  AY=Y;
  NY.resize(n);
  for (i=0; i<n; i++)
    NY.replace(i,i);

  while (compend()) complcs();

  qSort(SX);
  qSort(SY);

  for (i=0; i<SX.length(); i++) {
    n=SX[i];
    rx.append("0 [" + String::number(n) + "] " + XY.[AX[n]]));
  }

  for (i=0; i<SY.length(); i++) {
    n=SY[i];
    ry.append("1 [" + String::number(n) + "] " + XY[AY[n]]);
  }

  for(i=0; i<SX.length(); i++)
    SX.replace(i,SX[i]*2);

  for(i=0; i<SY.length(); i++)
    SY.replace(i,1+SY[i]*2);

  SX=SX+SY;
  qSort(SX);

  m=n=0;
  for(i=0; i<SX.length(); i++)
    if (SX[i]%2)
      r.append(ry[n++]);
    else
      r.append(rx[m++]);
  return r.join("\n");
}

// ---------------------------------------------------------------------
// repeats until no change:
// remove same head and tails
// remove different lines
// return true if continue with lcs
boolean Compare::compend()
{
  int i,m,n,t;
  int ox=0,oy=0;
  int nx=X.length(),ny=Y.length();
  QVector<int> p;

  while (ox != nx || oy != ny) {
    ox=nx;
    oy=ny;
    t=qMin(nx,ny);
    if (t == 0) break;

// remove head and tail matches
    for (m=0; m<t; m++)
      if (X[m] != Y[m]) break;
    X = X.mid(m);
    Y = Y.mid(m);
    NX = NX.mid(m);
    NY = NY.mid(m);
    nx=X.length();
    ny=Y.length();

    t -= m;
    for (n=0; n<t; n++)
      if (X[nx-1-n] != Y[ny-1-n]) break;
    if (n) {
      X = X.mid(0,nx-n);
      Y = Y.mid(0,ny-n);
      NX = NX.mid(0,nx-n);
      NY = NY.mid(0,ny-n);
    }

// remove lines not in both
    p.clear();
    for (i=0; i<X.length(); i++)
      if (!Y.contains(X[i])) {
        p.append(i);
        SX.append(NX[i]);
      }
    for (i=p.length(); i>0; i--) {
      X.remove(p[i-1]);
      NX.remove(p[i-1]);
    }
    p.clear();
    for (i=0; i<Y.length(); i++)
      if (!X.contains(Y[i])) {
        p.append(i);
        SY.append(NY[i]);
      }
    for (i=p.length(); i>0; i--) {
      Y.remove(p[i-1]);
      NY.remove(p[i-1]);
    }

    nx=X.length(),ny=Y.length();
  }

  if (nx != 0 && ny != 0) return true;

  SX = SX + NX;
  SY = SY + NY;
  return false;
}

// ---------------------------------------------------------------------
void Compare::complcs()
{
  int n;
  QVector<int> w;

  w=seqlcs(X,Y);

  n=qvremseq(X,NX,w,&SX);
  X=X.mid(n);
  NX=NX.mid(n);

  n=qvremseq(Y,NY,w,&SY);
  Y=Y.mid(n);
  NY=NY.mid(n);
}

// ---------------------------------------------------------------------
QVector<int> Compare::seqlcs(QVector<int> x,QVector<int> y)
{
  int i,j;
  int mx=100;
  QVector<int> r;

  int m=min(mx,(int)x.length());
  int n=min(mx,(int)y.length());

  x = x.mid(0,m);
  y = y.mid(0,n);

  if (m < n) {
    swap(x,y);
    swap(m,n);
  }

  vector< vector<int> > c(m+1, vector<int>(n+1,0));

  for (i=0; i<m; ++i)
    for (j=0; j<n; ++j)
      if (x[i] == y[j])
        c[i+1][j+1] = c[i][j] + 1;
      else
        c[i+1][j+1] = max(c[i+1][j], c[i][j+1]);

  i = c[m][n];
  r.resize(i);
  while (m > 0 && n > 0) {
    if (x[m-1] == y[n-1] && n--)
      r.replace(--i,x[--m]);
    else
      c[m][n-1] >= c[m-1][n] ? n-- : m--;
  }

  return r;
}

// ---------------------------------------------------------------------
String compare(String[] s,String[] t)
{
  Compare n;
  return n.comp(s,t);
}

// ---------------------------------------------------------------------
String fcompare(String s, String t)
{
  Compare n;
  String r=n.comp(cfreads(s),cfreads(t));
  String p="comparing:\n";
  return p + r;
}

// ---------------------------------------------------------------------
// qvector contains on list
boolean qvcontainseach(QVector<int>v,QVector<int>w)
{
  for (int i=0; i<w.length(); i++)
    if (!v.contains(w[i])) return false;
  return true;
}

// ---------------------------------------------------------------------
// get positions of lcs sequence in vector
// starts from front with last value
// that could form part of the lcs
QVector<int> qvlastseq(QVector<int>v,QVector<int>w)
{
  int i,n=w.length();
  QVector<int> r;
  r.resize(n);

  int p=v.indexOf(w.first());
  if (n==1) {
    r.replace(0,p);
    return r;
  }

  while (true) {
    i=v.indexOf(w.first(),p+1);
    if (i<0 || ! qvcontainseach(v.mid(i),w)) break;
    p=i;
  }

  r.replace(0,p);
  for (i=1; i<n; i++)
    r.replace(i,v.indexOf(w[i],r[i-1]));

  return r;
}

// ---------------------------------------------------------------------
// return drop amount on x,nx
int qvremseq(QVector<int>x,QVector<int>nx,QVector<int>w,QVector<int>*s)
{
  int e,i;
  QVector<int> p;
  p=qvlastseq(x,w);
  e=p.last();
  for (i=0; i<e; i++)
    if (!p.contains(i))
      s.append(nx[i]);
  return e+1;
}
