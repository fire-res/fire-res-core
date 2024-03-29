package io.github.fireres.core.generator;

import lombok.val;

public final class Noise {

   public static int seed = 100;

   private static final int P = 8;
   private static final int B = 1 << P;
   private static final int M = B - 1;

   private static final int NP = 8;
   private static final int N = 1 << NP;

   private static int p[] = new int[B + B + 2];
   private static double g2[][] = new double[B + B + 2][2];
   private static double g1[] = new double[B + B + 2];
   private static double[][] points = new double[32][3];

   static {
      init();
   }

   private static double lerp(double t, double a, double b) {
      return a + t * (b - a);
   }

   private static double s_curve(double t) {
      return t * t * (3 - t - t);
   }

   public static double noise(Double min, Double max, Integer time, Double dispersion, Integer seed) {
      val noiseMin = -0.12 - (1 - dispersion);
      val noiseMax = 0.12 + (1 - dispersion);

      val noise = noise(time, seed + 0.1);

      return (noise - noiseMin) * (max - min) / (noiseMax - noiseMin) + min;
   }

   private static double noise(double x, double y) {

      int bx0, bx1, by0, by1, b00, b10, b01, b11;
      double rx0, rx1, ry0, ry1, sx, sy, a, b, t, u, v, q[];
      int i, j;

      t = x + N;
      bx0 = ((int) t) & M;
      bx1 = (bx0 + 1) & M;
      rx0 = t - (int) t;
      rx1 = rx0 - 1;

      t = y + N;
      by0 = ((int) t) & M;
      by1 = (by0 + 1) & M;
      ry0 = t - (int) t;
      ry1 = ry0 - 1;

      i = p[bx0];
      j = p[bx1];

      b00 = p[i + by0];
      b10 = p[j + by0];
      b01 = p[i + by1];
      b11 = p[j + by1];

      sx = s_curve(rx0);
      sy = s_curve(ry0);

      q = g2[b00];
      u = rx0 * q[0] + ry0 * q[1];
      q = g2[b10];
      v = rx1 * q[0] + ry0 * q[1];
      a = lerp(sx, u, v);

      q = g2[b01];
      u = rx0 * q[0] + ry1 * q[1];
      q = g2[b11];
      v = rx1 * q[0] + ry1 * q[1];
      b = lerp(sx, u, v);

      return lerp(sy, a, b);
   }

   private static void init() {
      int i, j, k;
      double u, v, w, U, V, W, Hi, Lo;
      java.util.Random r = new java.util.Random(seed);
      for (i = 0; i < B; i++) {
         p[i] = i;
         g1[i] = 2 * r.nextDouble() - 1;

         do {
            u = 2 * r.nextDouble() - 1;
            v = 2 * r.nextDouble() - 1;
         } while (u * u + v * v > 1 || Math.abs(u) > 2.5 * Math.abs(v) || Math.abs(v) > 2.5 * Math.abs(u) || Math.abs(Math.abs(u) - Math.abs(v)) < .4);
         g2[i][0] = u;
         g2[i][1] = v;
         normalize2(g2[i]);

         do {
            u = 2 * r.nextDouble() - 1;
            v = 2 * r.nextDouble() - 1;
            w = 2 * r.nextDouble() - 1;
            U = Math.abs(u);
            V = Math.abs(v);
            W = Math.abs(w);
            Lo = Math.min(U, Math.min(V, W));
            Hi = Math.max(U, Math.max(V, W));
         } while (u * u + v * v + w * w > 1 || Hi > 4 * Lo || Math.min(Math.abs(U - V), Math.min(Math.abs(U - W), Math.abs(V - W))) < .2);
      }

      while (--i > 0) {
         k = p[i];
         j = (int) (r.nextLong() & M);
         p[i] = p[j];
         p[j] = k;
      }
      for (i = 0; i < B + 2; i++) {
         p[B + i] = p[i];
         g1[B + i] = g1[i];
         for (j = 0; j < 2; j++) {
            g2[B + i][j] = g2[i][j];
         }
      }

      points[3][0] = points[3][1] = points[3][2] = Math.sqrt(1. / 3);
      double r2 = Math.sqrt(1. / 2);
      double s = Math.sqrt(2 + r2 + r2);

      for (i = 0; i < 3; i++)
         for (j = 0; j < 3; j++)
            points[i][j] = (i == j ? 1 + r2 + r2 : r2) / s;
      for (i = 0; i <= 1; i++)
         for (j = 0; j <= 1; j++)
            for (k = 0; k <= 1; k++) {
               int n = i + j * 2 + k * 4;
               if (n > 0)
                  for (int m = 0; m < 4; m++) {
                     points[4 * n + m][0] = (i == 0 ? 1 : -1) * points[m][0];
                     points[4 * n + m][1] = (j == 0 ? 1 : -1) * points[m][1];
                     points[4 * n + m][2] = (k == 0 ? 1 : -1) * points[m][2];
                  }
            }
   }

   private static void normalize2(double v[]) {
      double s;
      s = Math.sqrt(v[0] * v[0] + v[1] * v[1]);
      v[0] = v[0] / s;
      v[1] = v[1] / s;
   }
}