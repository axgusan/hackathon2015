package com.darkprograms.speech.raiseYourHand;

import java.io.File;
import java.nio.file.Files;

import javaFlacEncoder.FLACFileWriter;

import com.darkprograms.speech.microphone.Microphone;
import com.darkprograms.speech.recognizer.GSpeechDuplex;
import com.darkprograms.speech.recognizer.GSpeechResponseListener;
import com.darkprograms.speech.recognizer.GoogleResponse;

public class EventDrivenSpeachToTextController implements Runnable {

	private GooglesAnswer googleSaid = new GooglesAnswer();
	private boolean stopRecording, messageRecieved = false;
	private long startTime;

	public void run() {
		System.out.println("DEBUG: Start Audio capture request recieved.");
		// Instantiate the API
		final GSpeechDuplex dup = new GSpeechDuplex(
				ConfigVariables.GOOGLE_API_KEY);
		boolean stopRecording = false;
		googleSaid.setBestGuess(null);

		// Adds the listener
		dup.addResponseListener(new GSpeechResponseListener() {
			public void onResponse(GoogleResponse gr) {
				System.out.println("DEBUG: Recieved reply from Google API");
				messageRecieved = true;
				googleSaid.setBestGuess(gr.getResponse());
				googleSaid.setOtherPoscibilities(gr.getOtherPossibleResponses());

				if (gr.getConfidence() != null) {
					googleSaid.setProbability(Double.parseDouble(gr
							.getConfidence()) * 100);
				} else {
					googleSaid.setProbability(0);
				}

				System.out.println("DEBUG: Google thinks you said: "
						+ googleSaid.getBestGuess());

				System.out.println("DEBUG: with " + googleSaid.getProbability()
						+ "% probability.");
				System.out.println("DEBUG: other guesses : "
						+ gr.getOtherPossibleResponses());

			}
		}); // end addResponseListener

		// Instantiate microphone and have it record FLAC file.
		Microphone mic = new Microphone(FLACFileWriter.FLAC);

		// The File to record the buffer to.
		File file = new File("temp_audio_file.flac");

		System.out.println("DEBUG: Starting recording phase.");

		try {
			System.out.println("DEBUG: Recording audio.");
			mic.captureAudioToFile(file);// Begins recording

			startTime = System.nanoTime();

			while (System.nanoTime() - startTime < ConfigVariables.AUDIO_RECORDING_MAX_LENGTH) {
				Thread.sleep(1000);
				if (stopRecording) {
					System.out.println("DEBUG: recording stopped");
					break;
				}
				System.out.println("DEBUG: Recording and waiting, time= "
						+ (System.nanoTime() - startTime));
			}
			mic.close();// Stops recording

			// Saves data into memory
			byte[] data = Files.readAllBytes(mic.getAudioFile().toPath());

			System.out.println("DEBUG: Sending data to Google.");
			dup.recognize(data, (int) mic.getAudioFormat().getSampleRate());
			mic.getAudioFile().delete();// Deletes Buffer file

		} catch (Exception e) {
			e.printStackTrace();
		}
	}// end startAudioCapture

	public GooglesAnswer endAudioCapture() throws InterruptedException {
		System.out.println("DEBUG: stop recording flag set");
		stopRecording = true;

		while (messageRecieved == false) {
			Thread.sleep(100);
			System.out.println("DEBUG: waiting for google reply");
		}
		return googleSaid;
	}

}
