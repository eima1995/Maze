import com.sun.org.apache.regexp.internal.RE;

import java.io.*;
import java.util.Collections;
import java.util.Scanner;
import java.util.Stack;

public class Maze {
    static int m, n; // labirinto matmenys
    static int lab[][]; // labirintas (1 - siena, 0 - laisvas langelis)
    static int cx[] = {0,-1,0,1,0}; // 4 produkcijos - x ir y poslinkiai
    static int cy[] = {0,0,-1,0,1};
    static int l; // ejimo eiles nr. (zymi aplankyta langeli)
    static int x,y;
    static int bandSk;
    static boolean yra = false;
    static String tab = "   ";
    static BufferedWriter bw;
    static File fout;
    static FileOutputStream fos;
    static int gylis;
    static int counter;
    static Stack<Rez> stack = new Stack<Rez>();
    static int labVar = 1; // 1 - is pav. , 2 - 20X15.
    public static void inicializuoti() throws Exception{
        // nuskaitau labirinta is failo, suzinau m ir n
        String fileName = "";
        if(labVar == 2){
            fileName = "lab2.txt";
        }else{
            fileName = "lab1.txt";
        }
        FileReader fr = new FileReader(fileName);
        BufferedReader bufferedReader = new BufferedReader(fr);
        StringBuffer stringBuffer = new StringBuffer();
        String line;
        while ((line =  bufferedReader.readLine()) != null){
            stringBuffer.append(line);
            stringBuffer.append("\n");
            n++;
        }

        int index = 0;
        while (stringBuffer.charAt(index) != '\n'){
            if(stringBuffer.charAt(index) != ' '){
                m++;
            }
            index++;
        }

        // labirinto uzpildymas
        lab = new int [m + 1] [n + 1]; // m + 1 ir n + 1, nes indeksai nuo 1
        index = 0;
        for(int i = n; i > 0; i--){  // per y asi
            for (int j = 1; j <= m; j++){ // per x asi
                if(stringBuffer.charAt(index) == '\n'){
                    index++;
                }
                lab[j][i] = Character.getNumericValue(stringBuffer.charAt(index));
                index = index + 2;
            }
        }
        fr.close();
    }

    public /*static*/ void eiti(int x, int y) throws Exception{
        String str = String.join("", Collections.nCopies(gylis, "."));
        int k; // einamosios produkcijos numeris
        int u,v; // nauja padietis pritaikius produkcija


        if((x == 1) || (x == m) || (y == 1) || (y == n)){
            yra = true; // terminaline busena, jeigu krastas
            // data
        }else {
            k = 0;
            do{
                k++; // imama kita produkcija
                u = x + cx[k];
                v = y + cy[k];

                System.out.print( '\n' + tab + String.format("%5s", ++counter) + ") " + str + "R" + k + ". " + "U=" + u  + ", V=" + v + "." );
                bw.write('\n' + tab + String.format("%5s", counter) + ") " + str + "R" + k + ". " + "U=" + u  + ", V=" + v + ".");


                if(lab[u][v] == 0){ //langelis tuscias
                    bandSk ++;
                    l ++;
                    lab [u][v] = l;

                    System.out.print(" Laisva." + " L:=L+1=" + l + "." +  " LAB[" + u + "," + v + "]:=" + lab [u][v] + ".");
                    bw.write(" Laisva." + " L:=L+1=" + l + "." +  " LAB[" + u + "," + v + "]:=" + lab [u][v] + ".");

                    gylis++;
                    eiti(u,v);

                    Rez temp = new Rez(k,u,v);
                    stack.push(temp);

                    --gylis;
                    if(!yra){
                        String str2 = String.join("", Collections.nCopies(gylis, "."));

                        System.out.print('\n' + tab + tab + tab +  " " + str2 + "Backtrack iš " + "X=" + u  + ", Y=" + v + "." + " L=" + l + ".");
                        bw.write('\n' + tab + tab + tab +  " " + str2 + "Backtrack iš " + "X=" + u  + ", Y=" + v + "." + " L=" + l + ".");

                        lab [u][v] = -1;
                        l--;

                        System.out.print(" LAB[" + u + "," + v + "]:=" + lab [u][v] + ". " + "L:= L-1=" + l + ".");
                        bw.write(" LAB[" + u + "," + v + "]:=" + lab [u][v] + ". " + "L:= L-1=" + l + ".");

                        stack.pop();
                    }
                }else{
                    if(lab[u][v] == 1){
                        System.out.print(" Siena.");
                        bw.write(" Siena.");
                    }else{
                        System.out.print("Siūlas.");
                        bw.write("Siūlas.");
                    }

                }
            }while(!((yra) || (k == 4)));
        }
    }


    public static void atspausdintiLabirinta() throws Exception{
        System.out.println(tab + "Y, V ^");
        bw.write(tab + "Y, V ^" + '\n');
        for(int i = n; i >= 1; i--) {
            System.out.print(String.format(tab + "%4s",i) + " | ");
            bw.write(String.format(tab + "%4s",i) + " | ");
            for(int j = 1; j <= m; j++) {
                System.out.print(String.format("%3s",lab[j][i]) + " ");
                bw.write(String.format("%3s",lab[j][i]) + " ");
            }
            System.out.println();
            bw.write('\n');
        }
        System.out.println(tab +"     -----------------------------------------------------------------------------------------> X, U");
        System.out.println();

        bw.write((tab +"     --------------------------------------------------------------------------------------------------> X, U"));
        bw.write('\n');


        for(int i = 1; i <= m; i++) {
            if(i ==  1) {
                System.out.print(tab + String.format("%10s", i));
                bw.write(tab + String.format("%10s", i));
            }else {
                System.out.print(String.format("%4s", i));
                bw.write(String.format("%4s", i));
            }
        }

    }

    public static void main (String [] args) throws Exception{
        System.out.println("Iveskite labirinto varianta: \n" + tab + "1 - labirintas 7.3 pav \n" + tab +
                "2 - 20 X 15 labirintas, kuriame kelias viršyja 100 višūnių.");
        Scanner sc = new Scanner(System.in);
        labVar = sc.nextInt();


        Maze m = new Maze();
        long startTime = System.currentTimeMillis();
        fout = new File("out.txt");
        fos = new FileOutputStream(fout);
        bw = new BufferedWriter(new OutputStreamWriter(fos));

        inicializuoti();

        // Pradine pozicija labirinte !!!!!
        x = 5;
        y = 4;
        l = 2;
        lab[x][y] = l;

        System.out.println("1 DALIS. Duomenys");
        System.out.println(tab + "1.1. Labirintas" + '\n');

        bw.write("1 DALIS. Duomenys");
        bw.write("\n" + tab + "1.1. Labirintas" + "\n \n");

        atspausdintiLabirinta();

        System.out.println("\n\n" + tab + "1.2. Pradinė padėtis X=" + x + ", Y=" + y  + ", L=" + l + "."+ '\n');
        bw.write("\n\n" + tab + "1.2. Pradinė padėtis X=" + x + ", Y=" + y  + ", L=" + l + "."+ "\n\n");

        System.out.println("2 DALIS. Vykdymas");
        bw.write("2 DALIS. Vykdymas" + '\n');

        m.eiti(x, y);

        if(yra){
            System.out.print(" Terminal." + '\n');
            System.out.println(" \n 3 Dalis Rezultatai");
            System.out.println(tab + "3.1 Kelias rastas.");
            System.out.println(tab + "3.2 Kelias grafiškai." + '\n');

            bw.write(" Terminal." + '\n');
            bw.write(" \n 3 Dalis Rezultatai" + '\n');
            bw.write(tab + "3.1 Kelias rastas." + '\n');
            bw.write(tab + "3.2 Kelias grafiškai." + "\n \n");

            atspausdintiLabirinta();

            System.out.print("\n \n" + tab + "3.3 Kelias taisyklėmis: ");
            bw.write("\n \n" + tab + "3.3 Kelias taisyklėmis: ");

            int temp = stack.size();
            String kelias = "";
            for(int i = 0; i < temp; i++){
                Rez r = stack.pop();
                if(i != temp - 1){
                    System.out.print("R " + r.r + ", ");
                    bw.write("R " + r.r + ", ");
                    kelias = kelias +"[X="+ r.u +",Y=" + r.v + "], ";

                }else{
                    System.out.print("R " + r.r + ".");
                    bw.write("R " + r.r + ", ");
                    kelias = kelias +"[X="+ r.u +",Y=" + r.v + "]. ";
                }
            }
            System.out.println('\n' + tab + "3.4 Kelias viršūnėmis: [X="+ x +",Y=" + y + "], "+ kelias);
            bw.write('\n' + tab + "3.4 Kelias viršūnėmis: [X="+ x +",Y=" + y + "], "+ kelias + '\n');

        }else{
            System.out.println("\n \n 3 Dalis Rezultatai");
            System.out.println(tab  + "Kelias neegzistuoja.");

            bw.write("\n \n 3 Dalis Rezultatai" + '\n');
            bw.write(tab  + "Kelias neegzistuoja.");

        }

        long endTime   = System.currentTimeMillis();
        long totalTime = endTime - startTime;

        System.out.println("\n\n" /*+ "Bandymu sk. " + bandSk*/);
        bw.write("\n\n"/* + "Bandymu sk. " + bandSk + '\n'*/);

        System.out.println("Vykdymo laikas " + (totalTime /1000)+ "s.");
        bw.write("Vykdymo laikas " + (totalTime /1000) + "s.");

        sc.close();
        bw.close();

    }

    class Rez {
        private int r,u,v;
        Rez(int r, int u, int v){
            this.r = r;
            this.u = u;
            this.v = v;
        }
    }
}

