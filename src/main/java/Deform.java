import java.util.Arrays;
import java.util.Comparator;

public class Deform {
    //Runner method
    public static void main(String[] args) {
        System.out.println("First function");
        Minimize(f1, x01, x02, x03);

        System.out.println("Second function");
        Minimize(f2,x01, x02, x03);
    }
    static double e = 0.00001;
    static double alpha = 1.0;
    static double beta = 0.5;
    static double gamma = 2.9;
    static double[] x01 = {0.0, 1.0};
    static double[] x02 = {-1.0, 0.0};
    static double[] x03 = {1.0, 0.0};

    // Интерейсы для задания функций
    interface Fun {
        double value(double[] x);
    }
    static Fun f1 = (double[] x) -> (100 * (x[1] - x[0] * x[0]) * (x[1] - x[0] * x[0]) + (1 - x[0]) * (1 - x[0]));
    static Fun f2 = (double[] x) -> ((x[0] * x[0] + x[1] - 11) * (x[0] * x[0] + x[1] - 11) + (x[0] + x[1] * x[1] - 7) * (x[0] + x[1] * x[1] - 7));
    static void Minimize(Fun f, double[] x01, double[] x02, double[] x03) {
        int k = 0;
        double[][] d = {x01, x02, x03}; //triangle
        //min l = 0 avg s= 1 max h=2
        Arrays.sort(d, (a, b) -> (int) (f.value(a) - f.value(b)));
        //Center
        double[] x4 ={(d[0][0]+d[1][0])/2,(d[0][1]+d[1][1])/2};
        double cond =Math.sqrt( (Math.pow(f.value(d[0])-f.value(x4),2) + Math.pow(f.value(d[1])-f.value(x4),2) +Math.pow(f.value(d[2])-f.value(x4),2))/3);
        while (cond>=e){
            Arrays.sort(d, Comparator.comparingDouble(f::value));
            x4 =new double[]{(d[0][0]+d[1][0])/2,(d[0][1]+d[1][1])/2};
            cond =Math.sqrt( (Math.pow(f.value(d[0])-f.value(x4),2) + Math.pow(f.value(d[1])-f.value(x4),2) +Math.pow(f.value(d[2])-f.value(x4),2))/3);
            //x n+3
            double[] x5 ={x4[0]+alpha*(x4[0]-d[2][0]),x4[1]+alpha*(x4[1]-d[2][1])};
            //Condition A:
            if(f.value(x5)<=f.value(d[0])){
                double[]x6= {x4[0]+gamma*(x5[0]-x4[0]),x4[1]+gamma*(x5[1]-x4[1])};
                if(f.value(x6)<f.value(d[0])) d[2]=x6;
                else d[2]=x5;
            }
            //Condition B:
            else if (f.value(d[1])<f.value(x5) && f.value(x5)<=f.value(d[2])){
                double[]x7 = {x4[0]+beta*(d[2][0]-x4[0]),x4[1]+beta*(d[2][1]-x4[1])};
                d[2]=x7;
            }
            //Condition C
            else if(f.value(d[0])<f.value(x5) && f.value(x5)<=f.value(d[1])){
                d[2]=x5;
            }
            //Condition  редукция
            else if(f.value(x5)>f.value(d[2])){
                var xl= d[0];
                for (int i = 0; i < d.length; i++) {
                    var t= new double[]{xl[0]+0.5*(d[i][0]-xl[0]),xl[1]+0.5*(d[i][1]-xl[1]) };
                    d[i]=t;
                }
            }
            k++;
            Arrays.sort(d, Comparator.comparingDouble(f::value));
            System.out.println("Iteration "+k+": f(x)= "+f.value(d[0])+"  x=("+d[0][0]+";"+d[0][1]+")");
        }
    }
}
