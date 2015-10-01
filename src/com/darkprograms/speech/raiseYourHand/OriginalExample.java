package com.darkprograms.speech.raiseYourHand;

import java.io.File;
import java.nio.file.Files;
import javaFlacEncoder.FLACFileWriter;
import com.darkprograms.speech.microphone.Microphone;
import com.darkprograms.speech.recognizer.GSpeechDuplex;
import com.darkprograms.speech.recognizer.GSpeechResponseListener;
import com.darkprograms.speech.recognizer.GoogleResponse;

public class OriginalExample {

	public static void main(String[] args) {
		GSpeechDuplex dup = new GSpeechDuplex(ConfigVariables.GOOGLE_API_KEY); // Instantiate the API

		dup.addResponseListener(new GSpeechResponseListener() {// Adds the listener
			public void onResponse(GoogleResponse gr) {
				System.out.println("Google thinks you said: "
						+ gr.getResponse());
				System.out.println("with "
						+ ((gr.getConfidence() != null) ? (Double
								.parseDouble(gr.getConfidence()) * 100) : null)
						+ "% confidence.");
				System.out
						.println("Google also thinks that you might have said:"
								+ gr.getOtherPossibleResponses());
			}
		});
		Microphone mic = new Microphone(FLACFileWriter.FLAC);// Instantiate
																// microphone
																// and have
		// it record FLAC file.
		File file = new File("CRAudioTest.flac");// The File to record the
													// buffer to.
		// You can also create your own buffer using the getTargetDataLine()
		// method.
		while (true) {
			try {
				mic.captureAudioToFile(file);// Begins recording
				Thread.sleep(10000);// Records for 10 seconds
				mic.close();// Stops recording
				// Sends 10 second voice recording to Google
				byte[] data = Files.readAllBytes(mic.getAudioFile().toPath());// Saves data into memory

				dup.recognize(data, (int) mic.getAudioFormat().getSampleRate());
				mic.getAudioFile().delete();// Deletes Buffer file
				// REPEAT
			} catch (Exception ex) {
				ex.printStackTrace();// Prints an error if something goes wrong.
			}
		}
	}
}