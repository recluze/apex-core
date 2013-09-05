package org.csrdu.apex.functions;

import java.io.*;


import org.csrdu.apex.helpers.Log;

class UpdatePolicy {
	public static String TAG = "APEX:UpdatePolicy";
	static String path = "C:/Users/Saeed Iqbal/workspace/apex-jpf/policies/attribs/com.android.mms";

	public static void removeLineFromFile(String file, String lineToRemove) {

		try {
			Log.d(TAG, "Update Policy....");
			File inFile = new File(file);

			if (!inFile.isFile()) {
				System.out.println("Parameter is not an existing file");
				return;
			}

			// Construct the new file that will later be renamed to the original
			// filename.
			File tempFile = new File(inFile.getAbsolutePath() + ".tmp");

			BufferedReader br = new BufferedReader(new FileReader(file));
			PrintWriter pw = new PrintWriter(new FileWriter(tempFile));

			String line = null;

			// Read from the original file and write to the new
			// unless content matches data to be removed.
			while ((line = br.readLine()) != null) {

				if (!line.trim().equals(lineToRemove)) {
					System.out.println("WHAT ?"
							+ !line.trim().equals(lineToRemove));
					pw.println(line);
					pw.flush();
				}
			}
			pw.close();
			br.close();

			// Delete the original file
			if (!inFile.delete()) {
				System.out.println("Could not delete file" + !inFile.delete());
				return;
			}

			// Rename the new file to the filename the original file had.
			if (!tempFile.renameTo(inFile))
				System.out.println("Could not rename file");

		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public static void writeToFile(String text) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(
					new File(path), true));
			bw.write(text);
			bw.newLine();
			bw.close();
		} catch (Exception e) {
		}
	}

	public static void deleteFile(String file) {
		// TODO Auto-generated method stub
		File inFile = new File(file);
		if (!inFile.delete()) {
			//System.out.println("Could not delete file" + !inFile.delete());
			return;
		}
	}

}