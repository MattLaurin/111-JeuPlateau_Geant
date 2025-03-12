

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");

        plateau plt= new plateau();
        plt.printBoard();
        plt.moveConvertInt("A8"); // Board index 0
        plt.moveConvertInt("C1"); // Board index 6 
        plt.moveConvertInt("F2"); // Board index 7
        plt.moveConvertInt("I6"); // Board index 5 

    }
}