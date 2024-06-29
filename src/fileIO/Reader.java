package fileIO;

import complexClasses.ComplexNumber;
import complexClasses.ComplexQubit;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * The {@code Reader} class provides methods to load {@link ComplexQubit} objects from a file.
 * <p>
 * The class includes methods to read data from a file and parse it into {@link ComplexQubit} objects.
 * </p>
 *
 * @author Robert Smith
 * @version 0.1
 * @since 2024-06-25
 */
public class Reader {

    /**
     * Loads an array of {@link ComplexQubit} objects from the specified file.
     *
     * @param filePath The path to the file containing the qubit data.
     * @return An array of {@link ComplexQubit} objects.
     */
    public static ComplexQubit[] loadQubits(String filePath) {
        Map<String, List<String>> dataMap = loadData(filePath);
        assert dataMap != null;
        ArrayList<String> keys = new ArrayList<>(dataMap.keySet());

        ComplexQubit[] fileContents = new ComplexQubit[dataMap.size()];

        for (int i = 0; i < dataMap.size(); i++) {
            String[] qubitStates = dataMap.get(keys.get(i)).get(0).split(",");

            fileContents[i] = new ComplexQubit(
                    new ComplexNumber((Double.parseDouble(qubitStates[0])), (Double.parseDouble(qubitStates[1]))),
                    new ComplexNumber((Double.parseDouble(qubitStates[2])), (Double.parseDouble(qubitStates[3]))),
                    Integer.parseInt(keys.get(i)));
            fileContents[i].setQubitID(Integer.parseInt(keys.get(i)));
        }

        for (ComplexQubit qubit : fileContents) {
            System.out.println("\n" + qubit);
        }
        return fileContents;
    }

    /**
     * Loads data from the specified file and returns it as a map.
     *
     * @param filePath The path to the file containing the data.
     * @return A map where the key is a string and the value is a list of strings.
     */
    public static Map<String, List<String>> loadData(String filePath) {
        Map<String, List<String>> dataMap = new HashMap<>();

        try (Scanner scanIn = new Scanner(new FileInputStream(filePath))) {
            List<String> records = new ArrayList<>();
            while (scanIn.hasNextLine()) {
                records.add(scanIn.nextLine());
            }
            for (String line : records) {
                String name = line.substring(0, indexOfFirstColon(line));
                line = line.substring(indexOfFirstColon(line) + 1);

                String[] lineSplit = line.split("\\|");

                List<String> recordsList = new ArrayList<>();

                for (int i = 0; i < lineSplit.length; i++) {
                    recordsList.add(lineSplit[i].trim());
                }

                dataMap.put(name, recordsList);
            }
            System.out.println("Loaded " + records.size() + " qubits from file.");
        } catch (FileNotFoundException _e) {
            System.out.println("File not found, check path and spelling");
            return null;
        }
        return dataMap;
    }

    /**
     * Finds the index of the first colon in a string.
     * <p>
     * This method allows for slicing off the qubit ID from the rest of the line
     * </p>
     *
     * @param line The string to search.
     * @return The index of the first colon, or -1 if no colon is found.
     */
    private static int indexOfFirstColon(String line) {
        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == ':') {
                return i;
            }
        }
        return -1; //should never reach this in a properly formatted save file
    }
}
