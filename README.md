# Chebyshev Secret Sharing

This Java program implements a solution for Chebyshev Secret Sharing using polynomial equations. The program reads input from a JSON file containing test cases with roots in specific formats, decodes the values from different bases, and solves for the constant term of the polynomial using Matrix Method.

## Output Format
The program outputs the constant term 'C' of the polynomial equation as an integer value.

### Sample Results:
Test Case 1: 3
Test Case 2: -6290016743607649280

## Features

- Reads input from JSON file
- Supports multiple number bases (2-16)
- Uses Gaussian elimination for solving polynomial equations
- Handles large numbers using BigInteger

## Input Format

```json
{
    "keys": {
        "n": 4,  // number of roots
        "k": 3   // minimum roots required (degree + 1)
    },
    "1": {
        "base": "10",
        "value": "4"
    },
    ...
}
```

## How to Run

1. Compile the program:
```bash
javac ChebyshevSecretSharing.java
```

2. Run the program:
```bash
java ChebyshevSecretSharing
```

The program will read from `input.json` in the same directory and output the decoded roots and constant term.
