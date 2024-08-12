package supportClasses.FileIO;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * The {@code Writer} class provides methods to save  objects to a file.
 * <p>
 * The class includes methods to create a file and write the states of the qubits to the file.
 * </p>
 *
 * @author Robert Smith
 * @version 0.1
 * @since 2024-06-25
 */
public class Writer {
//
//    /**
//     * Saves an array of {@link ComplexQubit} objects to a file.
//     *
//     * @param fileName The name of the file to save the qubits.
//     * @param qubits An array of {@link ComplexQubit} objects to be saved.
//     * @return {@code true} if the file is created and written successfully; {@code false} otherwise.
//     */
//    public static boolean saveFile(String fileName, ComplexQubit[] qubits) {
//        boolean fileCreatedStatus = createFile(fileName),
//                fileWrittenStatus = writeFile(fileName, qubits);
//        return fileCreatedStatus && fileWrittenStatus;
//    }
//
//    /**
//     * Writes the states of the {@link ComplexQubit} objects to the specified file.
//     *
//     * @param fileName The name of the file to write to.
//     * @param qubits An array of {@link ComplexQubit} objects to be written.
//     * @return {@code true} if the file is written successfully; {@code false} otherwise.
//     */
//    private static boolean writeFile(String fileName, ComplexQubit[] qubits) {
//        try (FileWriter writer = new FileWriter("./savedStates/" + fileName)) {
//            StringBuilder sBuild = new StringBuilder();
//            for (int i = 0; i < qubits.length; i++) {
//                sBuild.append(qubits[i].getQubitID()).append(":");
//                sBuild.append(qubits[i].getState().get(0, 0).getReal()).append(',');
//                sBuild.append(qubits[i].getState().get(0, 0).getImag()).append(',');
//                sBuild.append(qubits[i].getState().get(1, 0).getReal()).append(',');
//                sBuild.append(qubits[i].getState().get(1, 0).getImag()).append('|');
//                if (i < qubits.length - 1) {
//                    sBuild.append("\n");
//                }
//                writer.write(sBuild.toString());
//                sBuild.delete(0, sBuild.length());
//                //TODO build the writer for the gates being applied to each qubit, pending the work queue being built.
//            }
//        } catch (Exception e) {
//            System.out.println("Error writing to file");
//        }
//        return true;
//    }

    /**
     * Creates a file with the specified name.
     *
     * @param fileName The name of the file to be created.
     * @return {@code true} if the file is created successfully; {@code false} otherwise.
     */
    private static boolean createFile(String fileName) {
        boolean status = false;
        try {
            File myObj = new File("./savedStates/" + fileName);

            if (myObj.createNewFile()) {
                status = true;
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
        }
        return status;
    }
}
