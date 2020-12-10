public class KvazNyutonMethod {

    static double e = 0.00001;
    static double[] x01 = {10, 10};
    static double[] x02 = {2.0, 1.0};

    interface Fun {
        double value(double[] x);
    }

    interface Grad {
        double[] values(double[] x);

        /**
         * @param x - Точка на плоскости
         * @return - Норма градиента
         */
        default double module(double[] x) {
            return Math.sqrt(this.values(x)[0] * this.values(x)[0] + this.values(x)[1] * this.values(x)[1]);
        }
    }

    static Fun f1 = (double[] x) -> (100 * (x[1] - x[0] * x[0]) * (x[1] - x[0] * x[0]) + (1 - x[0]) * (1 - x[0]));
    static Fun f2 = (double[] x) -> ((x[0] * x[0] + x[1] - 11) * (x[0] * x[0] + x[1] - 11) + (x[0] + x[1] * x[1] - 7) * (x[0] + x[1] * x[1] - 7));
    static Grad gradF1 = (double[] x) -> new double[]{400*x[0]*x[0]*x[0]-400*x[0]*x[1]+2*x[0]-2,200*x[1]-200*x[0]*x[0]};
    static Grad gradF2 = (double[] x) -> new double[]{4 * x[0] * (x[0] * x[0] + x[1] - 11) + 2 * x[0] + 2 * x[1] * x[1] - 14, 2 * x[0] * x[0] + 4 * x[1] * (x[0] + x[1] * x[1] - 7) + 2 * x[1] - 22};

    static double GoldenRatio(Fun f, double[] s, double[] x) {
        double lB = 0;
        double rB = 10;
        double eps = e / 1000;
        while ((rB - lB) / 2 >= eps) {
            double a1 = (lB + rB - eps) / 2;
            double a2 = (lB + rB + eps) / 2;
            double f1 = f.value(new double[]{x[0] + a1 * s[0], x[1] + a1 * s[1]});
            double f2 = f.value(new double[]{x[0] + a2 * s[0], x[1] + a2 * s[1]});
            if (f1 > f2) {
                lB = a1;
            } else {
                rB = a2;
            }
        }
        return (lB + rB) / 2;
    }

    static int k=0;
    static void Minimization(double[][] h, double[] x, Fun f, Grad gr) {
        double[] p = new double[]{-h[0][0] * gr.values(x)[0] - h[0][1] * gr.values(x)[1], -h[1][0] * gr.values(x)[0] - h[1][1] * gr.values(x)[1]};
        double alpha = GoldenRatio(f, p, x);
        double[] x1 = new double[]{x[0] + alpha * p[0], x[1] + alpha * p[1]};
        double[] y = new double[]{gr.values(x1)[0] - gr.values(x)[0], gr.values(x1)[1] - gr.values(x)[1]};
        double[][] ht = new double[][]{{h[0][0] * y[0] * y[0] + h[0][1] * y[0] * y[1], h[0][0] * y[0] * y[1] + h[0][1] * y[1] * y[1]},
                {h[1][0] * y[0] * y[0] + h[1][1] * y[0] * y[1], h[1][0] * y[0] * y[1] + h[1][1] * y[1] * y[1]}};
        double[][] hg = new double[][]{{ht[0][0] * h[0][0] + ht[0][1] * h[1][0], ht[0][0] * h[0][1] + ht[0][1] * h[1][1]},
                {ht[1][0] * h[0][0] + ht[1][1] * h[1][0], ht[1][0] * h[0][1] + ht[1][1] * h[1][1]}};
        double g = h[0][0] * y[0] * y[0] + (h[0][1] + h[1][0]) * y[0] * y[1] + h[1][1] * y[1] * y[1];
        double[][] dhh = new double[][]{{hg[0][0] / g, hg[0][1] / g},
                {hg[1][0] / g, hg[1][1] / g}};
        double gp = p[0] * y[0] + p[1] * y[1];
        double[][] dph = new double[][]{{alpha * p[0] * p[0] / gp, alpha * p[0] * p[1] / gp},
                {alpha * p[1] * p[0] / gp, alpha * p[1] * p[1] / gp}};
        double[][] h1 = new double[][]{{h[0][0] - dhh[0][0] + dph[0][0], h[0][1] - dhh[0][1] + dph[0][1]},
                {h[1][0] - dhh[1][0] + dph[1][0], h[1][1] - dhh[1][1] + dph[1][1]}};

        if (gr.module(x1) > e) {
            System.out.println("Iteration "+k+": f(x)= "+f.value(x)+"  x=("+x[0]+";"+x[1]+")" +" Норма градиента:=" +gr.module(x));
            k++;
            Minimization(h1, x1, f, gr);
        } else {
            System.out.println("Iteration "+k+": f(x)= "+f.value(x)+"  x=("+x[0]+";"+x[1]+")" +" Норма градиента:=" +gr.module(x));
            k=0;
        }

    }

    public static void main(String[] args) {
        double[][] h0 = new double[][]{{0,1},{1,0}};
        System.out.println("First function");
        Minimization(h0, x01, f1, gradF1);

        System.out.println("Second function");
       Minimization(h0, x02, f2, gradF2);
    }
}
