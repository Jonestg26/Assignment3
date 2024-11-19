import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class ProductManager {
    private BTree<String, Product> tree = new BTree<>();

    public void loadProducts(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {
                //Skip header row
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                //Split the line by commas
                String[] details = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

                //Ensure 4 columns
                if (details.length != 4) {
                    System.err.println("Invalid line format: " + line);
                    continue;
                }

                //Parse fields and remove quotes
                String productId = details[0].trim();
                String name = details[1].replaceAll("^\"|\"$", "").trim();
                String category = details[2].replaceAll("^\"|\"$", "").trim();

                double price;
                try {
                    //Normalize the price field:
                    String normalizedPrice = details[3].replaceAll("[\"$]", "").replaceAll(" +", " ").trim();
                    String[] priceParts = normalizedPrice.split(" ");
                    normalizedPrice = priceParts[0];

                    if (!normalizedPrice.matches("\\d+(\\.\\d+)?")) {
                        throw new NumberFormatException("Malformed price format");
                    }

                    price = Double.parseDouble(normalizedPrice);
                } catch (NumberFormatException e) {
                    System.err.println("Invalid price in line: " + line);
                    continue;
                }

                //insert the product object into the tree
                Product product = new Product(productId.isEmpty() ? "UnknownId" : productId, name.isEmpty() ? "Unknown Name" : name, category.isEmpty() ? "Unknown Category" : category, price);
                tree.insert(productId, product);
            }
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
    }

    public boolean insertProduct(Product product) {
        //Check if the product already exists
        if (tree.search(product.getProductId()) != null) {
            return false;
        }

        //Insert the product into the tree
        tree.insert(product.getProductId(), product);
        return true;
    }


    //Search Product
    public Product searchProduct(String productId) {
        return tree.search(productId);
    }
}
