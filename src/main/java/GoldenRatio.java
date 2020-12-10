public class GoldenRatio {
    private static double fun(double x, double a, double b){
        return a/Math.exp(x)+b*x;
    }
    private final static  double ratio = (1+Math.sqrt(5))/2; // Золотое сечение
    public static void main(String[] args) {
        int a = 1; // Коэф. исходной функции
        int b = 2; // Коэф. исходной функции
        //Установка границ метода
        double lB = -10; //Левая граница
        double rB = 10;  // Правая граница
        double e = 10e-5; // Точность промежутка
        int k =1;
        while (Math.abs(rB-lB) > e){
            double x1 =rB - (rB-lB)/ratio;
            double x2 =lB + (rB-lB)/ratio;
            double y1 = fun(x1,a,b);
            double y2 = fun(x2,a,b);

            if(y1 >= y2){
                lB = x1;
            }
            else{
                rB= x2;
            }
            System.out.println("Итерация "+k+":x:= " + (rB+lB)/2 + " f(x):= " + fun((rB+lB)/2,a,b) +" Отрезок ["+lB+";"+rB+"]");

            k++;
        }
    }
}