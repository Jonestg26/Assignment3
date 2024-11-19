public class Main {
    public static void main(String[] args) {
        ProductManager manager = new ProductManager();

        // Load the product data
        String filePath = "amazon-product-data.csv";
        manager.loadProducts(filePath);

        // three search queries
        String[] searchIds = {
                "6d38858169064c8b7069a19c90dd0ae4",
                "8d78447af25f8b12fa76ad2617898627",
                "719f2de9289b1fc7e6d90ac50bdddc23"
        };

        for (String id : searchIds) {
            Product result = manager.searchProduct(id);
            if (result != null) {
                System.out.println("Product found: " + result);
            } else {
                System.out.println("Product not found for ID: " + id);
            }
        }

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
