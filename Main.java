import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ProductManager manager = new ProductManager();

        //Load the product data
        String filePath = "amazon-product-data.csv";
        manager.loadProducts(filePath);

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the product ID to search: ");
        String searchKey = scanner.nextLine().trim();

        //perform the search
        Product result = manager.searchProduct(searchKey);

        if (result != null) {
            System.out.println("Product found: " + result);
        } else {
            System.out.println("Product not found.");
        }

        scanner.close();

        // two insertions: One valid and one duplicate
        Product newProduct = new Product("new123", "Product", "Category", 49.99);

        // Valid insertion
        if (manager.insertProduct(newProduct)) {
            System.out.println("Product inserted: " + newProduct);
        } else {
            System.out.println("Failed to insert product: " + newProduct);
        }

        // Attempt duplicate insertion
        Product duplicateProduct = new Product("new123", "Product", "Category", 49.99);
        if (manager.insertProduct(duplicateProduct)) {
            System.out.println("Product inserted: " + duplicateProduct);
        } else {
            System.out.println("Error: Product with ID '" + duplicateProduct.getProductId() + "' already exists.");
        }
    }
}
