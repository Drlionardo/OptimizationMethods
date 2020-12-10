public class Dichotomy {
    private static double fun(double x, double a, double b){
        return a/Math.exp(x)+b*x;
    }
    public static void main(String[] args) {
        int a = 1; // Коэф. исходной функции
        int b = 2; // Коэф. исходной функции
        //Установка границ метода
        double lB = -10; //Левая граница
        double rB = 10;  // Правая граница
        double e = 10e-5; // Точность промежутка
        double delta=0.000001;
        int k =0;
        while (Math.abs(rB-lB) > e){
            double x = (rB+lB)/2;
           if(fun(x-delta,a,b)<fun(x+delta,a,b)){
               rB=x;
           }
           else lB=x;
            System.out.println("Итерация "+k+":x:= " + (rB+lB)/2 + " f(x):= " + fun((rB+lB)/2,a,b) +" Отрезок ["+lB+";"+rB+"]");
            k++;
        }
    }
}