package appDomain;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

import implementations.BSTree;
import implementations.BSTreeNode;
import utilities.Iterator;

/**
 * WordTracker reads a text file and tracks the location of each word
 * using a Binary Search Tree. Results can be printed to the console
 * or saved to an output file. The BST is persisted between runs via
 * a serialized repository file.
 */
public class WordTracker {

    private static final String REPOSITORY = "repository.ser";

    /**
     * Entry point for the WordTracker application.
     * 
     * @param args command line arguments: {@code <inputfile> [-pf|-pl|-po] [-f<outputfile>]}
     *             where {@code -pf} prints files, {@code -pl} prints lines,
     *             {@code -po} prints word count only, and {@code -f} specifies an output file
     */
    public static void main(String[] args) {
    	String inputFile = args[0];
        String outputFile = null;
        boolean printFile = false;
        boolean printLine = false;
        boolean printOnly = false;

	     // Parse command line flags: -pf (print file), -pl (print line),
	     // -po (print only), -f<filename> (output file)
        if (args.length == 0) {
            System.out.println("Usage: java WordTracker <inputfile> [-pf|-pl|-po] [-foutput.txt]");
            return;
        }
        
        for (int i = 1; i < args.length; i++) {

            if (args[i].equalsIgnoreCase("-pf")) {
                printFile = true;
            } else if (args[i].equalsIgnoreCase("-pl")) {
                printLine = true;
            } else if (args[i].equalsIgnoreCase("-po")) {
                printOnly = true;
            } else if (args[i].startsWith("-f")) {
                outputFile = args[i].substring(2);
            }
        }

        BSTree<String> tracker = loadRepository();

        // Read input text file
        try {
            File file = new File(inputFile);
            String fileName = file.getName();
            Scanner scanner = new Scanner(file);
            
            int lineNumber = 1;

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                String[] words = line.split(" ");

                for (String word : words) {

                    word = word.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();

                    if (!word.isEmpty()) {
                        tracker.add(word);
                        BSTreeNode<String> node = tracker.search(word);
                        node.addLocation(fileName, lineNumber);
                    }
                }
                
                lineNumber++;
            }

            scanner.close();

        } catch (FileNotFoundException e) {
            System.out.println("Input file not found.");
            return;
        }

        // Build report using inorder iterator
        String report = buildReport(tracker, printFile, printLine, printOnly);

        // print screen
        System.out.println(report);

        // save file if requested
        if (outputFile != null) {
            saveOutput(outputFile, report);
        }

        // Save BST to repository.ser
        saveRepository(tracker);
    }

    
    /**
     * Loads the persisted BST from repository.ser if it exists.
     * 
     * @return the deserialized BSTree, or a new empty BSTree if none is found
     */
    @SuppressWarnings("unchecked")
	private static BSTree<String> loadRepository() {

        try {
            ObjectInputStream in =
                    new ObjectInputStream(new FileInputStream(REPOSITORY));

            BSTree<String> tree = (BSTree<String>) in.readObject();

            in.close();

            return tree;

        } catch (Exception e) {
            return new BSTree<String>();
        }
    }

    /**
     * Serializes the BST to repository.ser for persistence between runs.
     * 
     * @param tree the BSTree to save
     */
    private static void saveRepository(BSTree<String> tree) {

        try {
            ObjectOutputStream out =
                    new ObjectOutputStream(new FileOutputStream(REPOSITORY));

            out.writeObject(tree);
            out.close();

        } catch (IOException e) {
            System.out.println("Unable to save repository.");
        }
    }

    /**
     * Builds a formatted word tracking report using an in-order traversal of the tree.
     * 
     * @param tree the BSTree containing tracked words
     * @param pf   if true, report includes the files each word was found in
     * @param pl   if true, report includes the line numbers for each word
     * @param po   if true, report includes only the word count per entry
     * @return the formatted report as a String
     */
    private static String buildReport(BSTree<String> tree,
                                      boolean pf,
                                      boolean pl,
                                      boolean po) {

        StringBuilder sb = new StringBuilder();

        Iterator<String> itr = tree.inorderIterator();

        while (itr.hasNext()) {

            String word = itr.next();
            BSTreeNode<String> node = tree.search(word);

            if (pf) {
            	String files = String.join(", ", node.getLocations().keySet());
                sb.append("Key: ===" + word + "=== found in files: " + files + "\n");
            }
            else if (pl) {
            	sb.append("Key: ===" + word + "===");
            	
            	for (String file : node.getLocations().keySet()) {
            		ArrayList<Integer> lines = node.getLocations().get(file);
            		String lineNumbers = lines.toString().replace("[", "").replace("]", "");
            		sb.append(" found in file: " + file + " on lines: " + lineNumbers + ", ");
            	}
            	
            	sb.append("\n");
            }
            else {
            	sb.append("Key: ===" + word + "===");
            	
            	int wordCount = 0;
            	for (ArrayList<Integer> lines : node.getLocations().values()) {
            	    wordCount += lines.size();
            	}
            	sb.append(" number of entries: " + wordCount + ", ");
            	
            	for (String file : node.getLocations().keySet()) {
            		ArrayList<Integer> lines = node.getLocations().get(file);
            		String lineNumbers = lines.toString().replace("[", "").replace("]", "");
            		sb.append(" found in file: " + file + " on lines: " + lineNumbers + ", ");
            	}
            	
            	sb.append("\n");
            }
        }

        return sb.toString();
    }

    
    /**
     * Writes the report string to the specified output file.
     * 
     * @param fileName the path of the file to write to
     * @param report   the report content to save
     */
    private static void saveOutput(String fileName, String report) {

        try {
            PrintWriter writer = new PrintWriter(fileName);

            writer.print(report);

            writer.close();

            System.out.println("Report saved to " + fileName);

        } catch (FileNotFoundException e) {
            System.out.println("Cannot create output file.");
        }
    }
}



