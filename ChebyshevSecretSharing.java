import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.util.stream.*;
import java.math.BigInteger;

public class ChebyshevSecretSharing {

    public static void main(String[] args) {
        try (BufferedReader reader = new BufferedReader(new FileReader("input.json"))) {
            // Read the JSON file
            String jsonString = reader.lines().collect(Collectors.joining());
            
            // Simple JSON parsing
            Map<String, Map<String, String>> jsonObject = new HashMap<>();
            
            // Parse keys section
            Pattern keysPattern = Pattern.compile("\"keys\":\\s*\\{\\s*\"n\":\\s*(\\d+),\\s*\"k\":\\s*(\\d+)\\s*\\}");
            Matcher keysMatcher = keysPattern.matcher(jsonString);
            if (!keysMatcher.find()) {
                throw new IllegalArgumentException("Invalid JSON format: keys section not found");
            }
            
            Map<String, String> keysMap = new HashMap<>();
            keysMap.put("n", keysMatcher.group(1));
            keysMap.put("k", keysMatcher.group(2));
            jsonObject.put("keys", keysMap);
            
            // Parse root values
            Pattern rootPattern = Pattern.compile("\"(\\d+)\":\\s*\\{\\s*\"base\":\\s*\"([^\"]+)\",\\s*\"value\":\\s*\"([^\"]+)\"\\s*\\}");
            Matcher rootMatcher = rootPattern.matcher(jsonString);
            while (rootMatcher.find()) {
                Map<String, String> rootMap = new HashMap<>();
                rootMap.put("base", rootMatcher.group(2));
                rootMap.put("value", rootMatcher.group(3));
                jsonObject.put(rootMatcher.group(1), rootMap);

            }
            
            // Extract keys n and k
            Map<String, String> keys = jsonObject.get("keys");
            int n = Integer.parseInt(keys.get("n"));
            int k = Integer.parseInt(keys.get("k"));

            // Extract roots from the JSON
            Map<Integer, BigInteger> roots = new HashMap<>();
            for (Map.Entry<String, Map<String, String>> entry : jsonObject.entrySet()) {
                if (!entry.getKey().equals("keys")) {
                    Map<String, String> root = entry.getValue();
                    int base = Integer.parseInt(root.get("base"));
                    BigInteger value = new BigInteger(root.get("value"), base);
                    roots.put(Integer.parseInt(entry.getKey()), value);
                }
            }

            // Ensure we have enough roots to solve the polynomial
            if (roots.size() < k) {
                throw new IllegalArgumentException("Not enough roots to solve the polynomial.");
            }

            // Solve for the constant term 'C' using the first k roots
            double[][] matrix = new double[k][k];
            double[] constants = new double[k];
            int index = 0;

            for (Map.Entry<Integer, BigInteger> entry : roots.entrySet()) {
                if (index >= k) break;
                int x = entry.getKey();
                double y = entry.getValue().doubleValue();

                // Fill the matrix row with powers of x
                for (int j = 0; j < k; j++) {
                    matrix[index][j] = Math.pow(x, k - j - 1);
                }

                // Fill the constants array with y values
                constants[index] = y;
                index++;
            }

            // Solve the system of equations using Gaussian elimination
            double[] coefficients = gaussianElimination(matrix, constants);

            // Output the results
            System.out.println("Decoded roots: " + roots);
            System.out.println("Constant term C: " + coefficients[k - 1]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Gaussian elimination method to solve the system of linear equations
    private static double[] gaussianElimination(double[][] matrix, double[] constants) {
        int n = constants.length;

        for (int i = 0; i < n; i++) {
            // Make the diagonal element 1
            double max = matrix[i][i];
            for (int j = i; j < n; j++) {
                matrix[i][j] /= max;
            }
            constants[i] /= max;

            // Eliminate the current column in rows below
            for (int j = i + 1; j < n; j++) {
                double factor = matrix[j][i];
                for (int k = i; k < n; k++) {
                    matrix[j][k] -= factor * matrix[i][k];
                }
                constants[j] -= factor * constants[i];
            }
        }

        // Back substitution
        double[] solution = new double[n];
        for (int i = n - 1; i >= 0; i--) {
            solution[i] = constants[i];
            for (int j = i + 1; j < n; j++) {
                solution[i] -= matrix[i][j] * solution[j];
            }
        }

        return solution;
    }
}
