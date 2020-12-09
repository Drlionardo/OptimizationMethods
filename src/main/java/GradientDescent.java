public class GradientDescent {
    //Runner method
    public static void main(String[] args) {
        System.out.println("First function");
        Minimize(x01, f1, gradF1);

        System.out.println("Second function");
        Minimize(x02, f2, gradF2);
    }
    /**
     * @param e1,e2 - точность промежутка
     * @param d - величина шага
     * @param x01,x02
     */
    static double e1 = 0.0001;
    static double e2 = 0.0001;
    static double d = 10; // Величина шага
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

    /**
     * @param x1 Координаты первыой точки на плосоксти
     * @param x2 Координаты второй точки на плосоксти
     * @return Норма разности x1 x2 на плоскости
     */
    static double norm(double[] x1, double[] x2){
        return Math.sqrt((x1[0]-x2[0])*(x1[0]-x2[0]) + (x1[1]-x2[1])*(x1[1]-x2[1]));
    }

    static Fun f1 = (double[] x) -> (100 * (x[1] - x[0] * x[0]) * (x[1] - x[0] * x[0]) + (1 - x[0]) * (1 - x[0]));
    static Fun f2 = (double[] x) -> ((x[0] * x[0] + x[1] - 11) * (x[0] * x[0] + x[1] - 11) + (x[0] + x[1] * x[1] - 7) * (x[0] + x[1] * x[1] - 7));

    static Grad gradF1 = (double[] x) -> new double[]{400 * x[0] * (x[0] * x[0] - x[1]) + 2 * x[0] - 2, 200 * x[0] * x[0] - 200 * x[1]};
    static Grad gradF2 = (double[] x) -> new double[]{4 * x[0] * (x[0] * x[0] + x[1] - 11) + 2 * x[0] + 2 * x[1] * x[1] - 14, 2 * x[0] * x[0] + 4 * x[1] * (x[0] + x[1] * x[1] - 7) + 2 * x[1] - 22};

    /**
     * @param x - Начальная точка
     * @param f - Функция для минимизации
     * @param g - Градиент функции f
     */
    static void Minimize(double[] x, Fun f, Grad g) {
        int k = 0;
        while (g.module(x)>=e1){
            double[] x1 = {x[0] - d * g.values(x)[0], x[1] - d * g.values(x)[1]};
            //Условие выхода
           while (f.value(x1) - f.value(x) >= 0){
               d=d/2;
               x1[0] = x[0] - d * g.values(x)[0];
               x1[1] = x[1] - d * g.values(x)[1];
           }
           //Финальное условие итерации
            if(norm(x1,x) < e2 && Math.abs(f.value(x1)-f.value(x)) < e2){
                break;
            }
            else{
                System.out.println("Iteration "+k+": f(x)= "+f.value(x1)+"  x=("+x1[0]+";"+x1[1]+")");
                k++;
                x=x1;
            }
        }

        System.out.println("Iteration "+k+": f(x)= "+f.value(x)+"  x=("+x[0]+";"+x[1]+")");
    }
}
