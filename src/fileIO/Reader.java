package fileIO;

import qubits.ComplexNumber;
import qubits.ComplexQubit;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

public class Reader {

    public static ComplexQubit[] loadQubits(String filePath){
        Map<String, List<String>> dataMap = loadData(filePath);
        System.out.println("\nDatamap: "+dataMap);
        ArrayList<String> keys = new ArrayList<String>();
        keys.addAll(dataMap.keySet());
        System.out.println("\nKeys: "+keys);

        ComplexQubit[] fileContents = new ComplexQubit[dataMap.size()];

        for(int i = 0; i < dataMap.size(); i++){
            String[] qubitStates = dataMap.get(keys.get(i)).get(0).split(",");

            fileContents[i] = new ComplexQubit(
                    new ComplexNumber((Double.valueOf(qubitStates[0])),(Double.valueOf(qubitStates[1]))),
                    new ComplexNumber((Double.valueOf(qubitStates[2])),(Double.valueOf(qubitStates[3]))),
                    Integer.parseInt(keys.get(i)));
            fileContents[i].setQubitID(Integer.parseInt(keys.get(i)));
        }

        for(ComplexQubit qubit : fileContents){
            System.out.println("\n"+qubit);
        }
        return fileContents;
    }

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
            System.out.println("Loaded "+records.size()+" qubits from file.");
        } catch (FileNotFoundException _e) {
            System.out.println("File not found, check path and spelling");
            return null;
        }
        return dataMap;
    }

    /**
     * Find the first colon, allows for slicing off the name from the rest of the line without needing to write special
     * a case for lines with multiple colons before the first | character.
     */
    private static int indexOfFirstColon(String line) {
        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == ':') {
                return i;
            }
        }
        return -1;
    }
}
