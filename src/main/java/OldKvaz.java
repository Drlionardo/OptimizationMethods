public class OldKvaz {
    //Runner method
    public static void main(String[] args) {
        System.out.println("First function");
        Minimize(x01, f1, gradF1);

        System.out.println("Second function");
       // Minimize(x02, f2, gradF2);
    }
    /**
     * @param enableSingleOptDEBUG - отображение итераций одномерной оптимизации
     * @param e1,e2 - точность промежутка
     * @param x01,x02
     */
    static boolean enableSingleOptDEBUG=false;
    static double e1 = 0.0015;
    static double e2 = 0.001;
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
    static Grad gradF1 = (double[] x) -> new double[]{400*x[0]*x[0]*x[0]-400*x[0]*x[1]+2*x[0]-2,200*x[1]-200*x[0]*x[0]};
    static Grad gradF2 = (double[] x) -> new double[]{4 * x[0] * (x[0] * x[0] + x[1] - 11) + 2 * x[0] + 2 * x[1] * x[1] - 14, 2 * x[0] * x[0] + 4 * x[1] * (x[0] + x[1] * x[1] - 7) + 2 * x[1] - 22};
    /**
     * @param x1 Координаты первыой точки на плосоксти
     * @param x2 Координаты второй точки на плосоксти
     * @return Норма разности x1 x2 на плоскости
     */
    static double norm(double[] x1, double[] x2){
        return Math.sqrt((x1[0]-x2[0])*(x1[0]-x2[0]) + (x1[1]-x2[1])*(x1[1]-x2[1]));
    }
    //Функция дли минимизации fi(t)
    private static double tK(Fun f, Grad g, double[] x, double t, double[]A ){
        double[] argument = {x[0]- t*(A[0]*g.values(x)[0]+A[1]*g.values(x)[1]),x[1]- t*(A[2]*g.values(x)[0]+A[3]*g.values(x)[1])};
        return f.value(argument);
    }
    //Золоое сечение для минимизации fi(t)
    private static double mintK(Fun f, Grad g, double[] x, double[] A){
        final double ratio = (1+Math.sqrt(5))/2;
        double lb = -100; //Левая граница
        double rb = 100;  // Правая граница
        double e = 0.00000000001; // Точность промежутка
        int k =1;
        while (Math.abs(rb-lb) > e){
            double t1 =rb - (rb-lb)/ratio;
            double t2 =lb + (rb-lb)/ratio;
            double y1 = tK(f,g,x,t1,A);
            double y2 = tK(f,g,x,t2,A);
            if(y1 >= y2){
                lb = t1;
            }
            else{
                rb= t2;
            }
            if(enableSingleOptDEBUG) System.out.println("SINGLE_OPT" + k + ": x:= " + (rb+lb)/2 +" y:= " + tK(f,g,x,(rb+lb)/2,A));
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
        double[] xPrev = new double[]{0.5,0.5}; // x(k-1)
        double[] dPrev =new double[]{0.0,0.0};  // d(l-1)
        double[] A0 = {1,0,
                       0,1};
        double[] A;
        //Пока модуль градиента больше значения
        while (g.module(x)>=e1){
            if(k>0){
                //g(x)-g(x-1)
                double[] dG= {g.values(x)[0]-g.values(xPrev)[0],g.values(x)[1]-g.values(xPrev)[1]};
                //x-x-1
                double[] dX={x[0] - xPrev[0], x[1] - xPrev[1]};
                //For AkC compute:
                double dividerA = dX[0]*dG[0]+dX[1]*dG[1]; // первый
                double dividerB = dG[0]*dG[0]*A0[0]+A0[3]*dG[1]*dG[1]+A0[1]*dG[0]*dG[1]+A0[2]*dG[0]*dG[1];// второй
                //Дроби 1 и 2
                double[] a1 ={dX[0]*dX[0]/dividerA,dX[0]*dX[1]/dividerA,dX[0]*dX[1]/dividerA,dX[1]*dX[1]/dividerA};
                double[] a2= {(A0[0]*A0[0]*dG[0]*dG[0] + A0[1]*A0[2]*dG[1]*dG[1] + A0[0]*A0[1]*dG[0]*dG[1] + A0[0]*A0[2]*dG[0]*dG[1])/dividerB,
                            (A0[0]*A0[1]*dG[0]*dG[0] + A0[1]*A0[3]*dG[1]*dG[1] + A0[1]*A0[1]*dG[0]*dG[1] + A0[0]*A0[3]*dG[0]*dG[1])/dividerB,
                            (A0[0]*A0[2]*dG[0]*dG[0] + A0[2]*A0[2]*dG[0]*dG[1] + A0[2]*A0[2]*dG[0]*dG[1] + A0[0]*A0[3]*dG[0]*dG[1])/dividerB,
                            (A0[1]*A0[2]*dG[0]*dG[0] + A0[3]*A0[3]*dG[1]*dG[1] + A0[1]*A0[3]*dG[0]*dG[1] + A0[2]*A0[3]*dG[0]*dG[1])/dividerB};
                double[]AkC={a1[0]-a2[0],a1[1]-a2[1],a1[2]-a2[2],a1[3]-a2[3]}; //Result
                //New A
                A = new double[]{A0[0] + AkC[0], A0[1] + AkC[1], A0[2] + AkC[2], A0[3] + AkC[3]};
            }
            else {
                A=A0;
            }
            double[] d ={-1*A[0]*g.values(x)[0] - A[1]*g.values(x)[1], -1*A[2]*g.values(x)[0]-A[3]*g.values(x)[1]};
            //Минимизация fi(t)
            double[] gGrad=g.values(x);
            double t = mintK(f,g,x,A);
            double[] x1={x[0] - t * (A[0] * g.values(x)[0] + A[1] * g.values(x)[1]), x[1] - t * (A[2] * g.values(x)[0] + A[3] * g.values(x)[1])};

            //Условие выхода из алгоритма
            if(norm(x1,x) < e2 && Math.abs(f.value(x1)-f.value(x)) < e2){
                break;
            }
            else{
                System.out.println("Iteration "+k+": f(x)= "+f.value(x1)+"  x=("+x1[0]+";"+x1[1]+")" +" Норма градиента:=" +g.module(x1));

                k++;
                xPrev=x;
                A0=A;
                x=x1;
            }

        }
        //Final output
        System.out.println("Iteration "+k+": f(x)= "+f.value(x)+"  x=("+x[0]+";"+x[1]+")" +" Норма градиента:=" +g.module(x));
    }
}
