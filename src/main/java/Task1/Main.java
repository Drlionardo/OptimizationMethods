package Task1;

public class Main {
    static int k = 0;
    static double e = 0.01;
    static double alpha = 1.0;
    static double beta = 2.8;
    static double gamma = 0.5;
    static double[] x01 = {0.0, 1.0};
    static double[] x02 = {-1.0, 0.0};
    static double[] x03 = {1.0, 0.0};
    static double[] x1;
    static double[] x2;
    static double[] x3;
    static double[] xc;
    static double[] xp;
    static double[] xm;
    static double[] c;

    static double[][] sortF(double[][] mas, Operational f) {
        for (int i = 0; i < 2; i++) {
            for (int j = 2; j > i; j--) {
                if (f.calculate(mas[j - 1]) > f.calculate(mas[j])) {
                    double[] t = mas[j - 1];
                    mas[j - 1] = mas[j];
                    mas[j] = t;
                }
            }
        }
        return mas;
    }


    interface Operational {
        double calculate(double[] x);
    }

    static Operational f1 = (double[] x) -> 100 * (x[1] - x[0] * x[0]) * (x[1] - x[0] * x[0]) + (1 - x[0]) * (1 - x[0]);
    static Operational f2 = (double[] x) -> (x[0] * x[0] + x[1] - 11) * (x[0] * x[0] + x[1] - 11) + (x[0] + x[1] * x[1] - 7) * (x[0] + x[1] * x[1] - 7);

    static void NelderMead(double[] x11, double[] x22, double[] x33, Operational f) {
        double[][] simplex = sortF(new double[][]{x11, x22, x33}, f);
        x1 = simplex[0];
        x2 = simplex[1];
        x3 = simplex[2];
        c = new double[]{(x1[0] + x2[0]) / 2, (x1[1] + x2[1]) / 2};
        if (Math.sqrt(((f.calculate(x1) - f.calculate(c)) * (f.calculate(x1) - f.calculate(c))
                + (f.calculate(x2) - f.calculate(c)) * (f.calculate(x2) - f.calculate(c)) +
                (f.calculate(x3) - f.calculate(c)) * (f.calculate(x3) - f.calculate(c))) / 3) >= e) {
            System.out.println(k + " iteration: f(x1) = " + String.format("%.7g",f.calculate(x1))+" x1 = ("+ String.format("%.4g",x1[0])+"; "+String.format("%.4g",x1[1])+") x2 = ("+ String.format("%.4g",x2[0])+"; "+String.format("%.4g",x2[1])+") x3 = ("+ String.format("%.4g",x3[0])+"; "+String.format("%.4g",x3[1])+")");

            //отражение
            xc = new double[]{c[0] + alpha * (c[0] - x3[0]), c[1] + alpha * (c[1] - x3[1])};
            if (f.calculate(x1) <= f.calculate(xc) && f.calculate(xc) <= f.calculate(x3)) {
                k++;
                NelderMead(x1, x2, xc, f);
            } else if (f.calculate(xc) <= f.calculate(x1)) {
                //Растяжение
                xp = new double[]{c[0] + beta * (xc[0] - c[0]), c[1] + beta * (xc[1] - c[1])};
                if (f.calculate(xp) <= f.calculate(xc)) {
                    k++;
                    NelderMead(x1, x2, xp, f);
                } else {
                    k++;
                    NelderMead(x1, x2, xc, f);
                }
            } else if (f.calculate(xc) > f.calculate(x2)) {
                if (f.calculate(xc) >= f.calculate(x3)) {
                    xm = new double[]{c[0] + gamma * (x3[0] - c[0]), c[1] + gamma * (x3[1] - c[1])};
                } else {
                    xm = new double[]{c[0] + gamma * (xc[0] - c[0]), c[1] + gamma * (xc[1] - c[1])};
                }
                if (f.calculate(xm) < Math.min(f.calculate(xc), f.calculate(x3))) {
                    k++;
                    NelderMead(x1, x2, xm, f);
                } else {
                    x2 = new double[]{x1[0] + 0.5 * (x2[0] - x1[0]), x1[1] + 0.5 * (x2[1] - x1[1])};
                    x3 = new double[]{x1[0] + 0.5 * (x3[0] - x1[0]), x1[1] + 0.5 * (x3[1] - x1[1])};
                    k++;
                    NelderMead(x1, x2, x3, f);
                }
            }


        } else {
            System.out.println("Result: f(x1) = " + String.format("%.7g",f.calculate(x1))+" x1 = ("+ String.format("%.4g",x1[0])+"; "+String.format("%.4g",x1[1])+")");
        }


    }

    public static void main(String[] args) {
        System.out.println("First function");
        k=0;
        NelderMead(x01, x02, x03, f1);


        System.out.println("Second function");
        k=0;
        NelderMead(x01, x02, x03, f2);
    }
}
