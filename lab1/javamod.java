import java.util.Scanner; 

public class javamod {
    public static void main(String[] args) {
        // Create a Scanner object
        Scanner scanner = new Scanner(System.in); 
        System.out.println("This program takes the modulus of two numbers");

        System.out.print("Enter an integer: ");
        int num = scanner.nextInt(); // Read first integer

        System.out.print("Enter an integer: ");
        int div = scanner.nextInt(); // Read second integer
        
        // Compute modulus using bitwise AND  
        int modulus = num & (div - 1);

        System.out.println("Modulus:  " + modulus);

        scanner.close(); 
    }
}