package appDomain;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

import implementations.BSTree;
import implementations.BSTreeNode;

public class WordTracker {

	public static void main(String[] args) {
		try {
			// Read file
			File file = new File(args[0]);
			String fileName = file.getName();
			Scanner scanner = new Scanner(file);
			int lineCount = 1;
			
			// Read lines in file
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
                String[] words = line.split(" ");
                
                // Add word and location data to new Node and insert into BSTree
                for (String word : words) {
                	word = word.replaceAll("[^a-zA-Z0-9]", ""); // Remove punctuation from words
                	if (word.isEmpty()) continue; // Skip if word is empty after stripping punctuation
                	BSTreeNode entry = new BSTreeNode(word); // *** Add in file name and line number [new BSTreeNode(word, fileName, lineNum)]***
                	tracker.add(entry);
                }
                lineCount++;
			}
			scanner.close();
		} catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        }
	}

}
