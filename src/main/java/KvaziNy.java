public class KvaziNy {
    //Runner method
    public static void main(String[] args) {
        System.out.println("First function");
        Minimize(x01, f1, gradF1);

        System.out.println("Second function");
        Minimize(x02, f2, gradF2);
    }
    /**
     * @param enableSingleOptDEBUG - отображение итераций одномерной оптимизации
     * @param e1,e2 - точность промежутка
     * @param x01,x02
     */
    static boolean enableSingleOptDEBUG=false;
    static double e1 = 0.0001;
    static double e2 = 0.0015;
    static double[] x01 = {0.5, 0.5};
    static double[] x02 = {2.0, 2.0};

    // Интерейсы для задания функций и вектора-градиента
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

    static Grad gradF1 = (double[] x) -> new double[]{400 * x[0] * (x[0] * x[0] - x[1]) + 2 * x[0] - 2, 200 * x[0] * x[0] - 200 * x[1]};
    static Grad gradF2 = (double[] x) -> new double[]{4 * x[0] * (x[0] * x[0] + x[1] - 11) + 2 * x[0] + 2 * x[1] * x[1] - 14, 2 * x[0] * x[0] + 4 * x[1] * (x[0] + x[1] * x[1] - 7) + 2 * x[1] - 22};
    //Функция дли минимизации fi(t)
    private static double tK(Fun f, double t, double[] x, double[] d0){
        double[] x0= {x[0]+t*d0[0],x[1]+t*d0[1]};
        return f.value(x0);
    }
    //Золоое сечение для минимизации fi(t)
    private static double mintK(Fun f, double[] x, double[] d0){
        final double ratio = (1+Math.sqrt(5))/2;
        double lb = -10; //Левая граница
        double rb = 10;  // Правая граница
        double e = 0.000001; // Точность промежутка
        int k =1;
        while (Math.abs(rb-lb) > e){
            double x1 =rb - (rb-lb)/ratio;
            double x2 =lb + (rb-lb)/ratio;
            double y1 = tK(f,x1,x,d0);
            double y2 = tK(f,x2,x,d0);
            if(y1 >= y2){
                lb = x1;
            }
            else{
                rb= x2;
            }
            if(enableSingleOptDEBUG) System.out.println("SINGLE_OPT" + k + ": x:= " + (rb+lb)/2 +" y:= " + tK(f,((rb+lb)/2),x,d0));
            k++;
        }
        return (rb+lb)/2;
    }
    /**
     * @param x - Начальная точка
     * @param f - Функция для минимизации
     * @param g - Градиент функции f
     */
    static void Minimize(double[] x, Fun f, Grad g) {
        int k = 0;
        double[] xPrev = new double[]{0.0,0.0}; // x(k-1)
        double[] dPrev =new double[]{0.0,0.0};  // d(l-1)
        double[][] A0= { {1,0},
                        {0,1} };
        double[][] A=A0;
        //Пока модуль градиента больше значения
        while (g.module(x)>=e1){
            if(k>0){

            }
            
            double[] d = {-1*A[0][0]* ,}

            //Минимизация fi(t)
            t = mintK(f,x,d);
        }

        //Final output
        System.out.println("Iteration "+k+": f(x)= "+f.value(x)+"  x=("+x[0]+";"+x[1]+")");
    }
}
