import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static Resource resource;
    public static Outcome outcome;
    public static LinkedList<Thread> workers;
    public static Scanner scan = new Scanner(System.in);
    public static void main(String[] args){
        String op;
        int tmp;
        workers = new LinkedList<>();
        resource = new Resource();
        outcome = new Outcome();


        while(true){
            System.out.println("type in : new | quit | input | random entries | file");
            op = scan.nextLine();
            switch (op) {
                case "new" -> {
                    System.out.println("Type in a number of threads to be created:");
                    tmp = scan.nextInt();
                    startNewThreads(tmp);
                }
                case "quit" -> quit();
                case "input" -> {
                    System.out.println("Type in numbers to be processed:");
                    inputs();
                }
                case "random entries" -> {
                    System.out.println("Type in: number of inputs | origin | bounds");
                    int noOfInpts = scan.nextInt();
                    int origin = scan.nextInt();
                    int bounds = scan.nextInt();
                    randomInputs(noOfInpts, origin, bounds);
                }
                case "file" -> readFromFile();
            }
        }
    }

    public static void readFromFile(){
        String name = "liczby.txt";
        try {
            File myObj = new File(name);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                Long data = (long) Integer.parseInt(myReader.nextLine());
                resource.put(data);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static void inputs(){
        String op;
        long tmp;
        while(true){
            op = scan.nextLine();
            if(op.equals("q"))
                break;
            try {
                tmp = (long) Integer.parseInt(op);
            }catch (NumberFormatException ex){
                continue;
            }
            resource.put(tmp);
        }
    }

    public static int quit(){
        for (Thread th : workers) {
            th.interrupt();
        }
        for (Thread th : workers) {
            try {
                th.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        resource.printMe();
        resource.getNumbers().clear();
        outcome.printMe();
        outcome.printToFile();
        return 0;
    }
    
    public static void startNewThreads(int noOfThs){
        for(int i = 0; i < noOfThs; i++){
            Thread thread = new Thread(new Worker(resource,outcome));
            thread.start();
            workers.add(thread);
        }
    }

    public static void randomInputs(int numberOfInputs, int origin, int bound){
        Random rand = new Random();
        for(int i = 0; i<numberOfInputs;i++){
            resource.put((long) rand.nextInt(origin, bound));
        }
    }
}

