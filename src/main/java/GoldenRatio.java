public class GoldenRatio {
    private static double fun(double x, double a, double b){
        return a/Math.exp(x)+b*x;
    }
    private final static  double ratio = (1+Math.sqrt(5))/2; // Золотое сечение
    public static void main(String[] args) {
        int a = 1; // Коэф. исходной функции
        int b = 2; // Коэф. исходной функции
        //Установка границ метода
        double lb = -10; //Левая граница
        double rb = 10;  // Правая граница
        double e = 10e-5; // Точность промежутка
        int k =1;
        while (Math.abs(rb-lb) > e){
            double x1 =rb - (rb-lb)/ratio;
            double x2 =lb + (rb-lb)/ratio;
            double y1 = fun(x1,a,b);
            double y2 = fun(x2,a,b);

            if(y1 >= y2){
                lb = x1;
            }
            else{
                rb= x2;
            }
            System.out.println(k+": x:= " + (rb+lb)/2 +" y:= " + fun((rb+lb)/2,a,b));
            k++;
        }
    }
}