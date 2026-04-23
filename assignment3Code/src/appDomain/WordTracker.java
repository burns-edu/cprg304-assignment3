// package appDomain;

// import java.util.Scanner;
// import java.io.File;
// import java.io.FileNotFoundException;

// import implementations.BSTree;
// import implementations.BSTreeNode;

// public class WordTracker {

// 	public static void main(String[] args) {
// 		try {
// 			// Read file
// 			File file = new File(args[0]);
// 			String fileName = file.getName();
// 			Scanner scanner = new Scanner(file);
// 			int lineCount = 1;
			
// 			// Read lines in file
// 			while (scanner.hasNextLine()) {
// 				String line = scanner.nextLine();
//                 String[] words = line.split(" ");
                
//                 // Add word and location data to new Node and insert into BSTree
//                 for (String word : words) {
//                 	word = word.replaceAll("[^a-zA-Z0-9]", ""); // Remove punctuation from words
//                 	if (word.isEmpty()) continue; // Skip if word is empty after stripping punctuation
//                 	BSTreeNode entry = new BSTreeNode(word); // *** Add in file name and line number [new BSTreeNode(word, fileName, lineNum)]***
//                 	tracker.add(entry);
//                 }
//                 lineCount++;
// 			}
// 			scanner.close();
// 		} catch (FileNotFoundException e) {
//             System.out.println("File not found: " + e.getMessage());
//         }
// 	}

// }

package appDomain;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

import implementations.BSTree;
import implementations.BSTreeNode;
import utilities.Iterator;

/*
 * Member 4
 * Serialization + CLI + Output Reporting
 *
 * Supported arguments:
 * -pf              print words + filenames
 * -pl              print words + line numbers
 * -po              print words only
 * -f<output.txt>   save report to file
 *
 * Example:
 * java appDomain.WordTracker sample.txt -pf
 * java appDomain.WordTracker sample.txt -pl -fresult.txt
 */

public class WordTracker {

    private static final String REPOSITORY = "repository.ser";

    public static void main(String[] args) {

        if (args.length == 0) {
            System.out.println("Usage: java WordTracker <inputfile> [-pf|-pl|-po] [-foutput.txt]");
            return;
        }

        String inputFile = args[0];

        boolean printFile = false;
        boolean printLine = false;
        boolean printOnly = false;

        String outputFile = null;

        
        // Read command line options
        
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

        
        // Load saved BST if exists
        
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

    
    // Load repository.ser
    
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

    
    // Save repository.ser
    
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

  
    // Build report using inorderIterator()
   
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

    
    // Save report text file
    
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



