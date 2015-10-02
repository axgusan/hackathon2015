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
	private boolean stopRecording = false;// user initiated stop recording event
	private boolean messageRecieved = false;// Received reply from google API
	private long startTime;
	private SpeechListener listener;
	private Object tag;
	public static Microphone mic ;

	public static EventDrivenSpeachToTextController startAudioCapture(Object tag, SpeechListener listener){
		EventDrivenSpeachToTextController mcontroller = new EventDrivenSpeachToTextController();

		mcontroller.tag = tag;
		mcontroller.listener = listener;
		System.out.println("INFO: Attempting to start audio capture.");

		Thread t = new Thread(mcontroller);
		t.start();
		return mcontroller;
	}
	
	
	public void run() {
		System.out.println("DEBUG: Start Audio capture request recieved.");
		// Instantiate the API
		final GSpeechDuplex dup = new GSpeechDuplex(
				ConfigVariables.GOOGLE_API_KEY);

			// reinitialize in case reused
		 stopRecording = false;

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
				if(listener != null){
					listener.onReceived(tag, googleSaid);
				}
			}
		}); // end addResponseListener

		// Instantiate microphone and have it record FLAC file.
		System.out.println("DEBUG: getting the default mic.");
		
		System.out.println("DEBUG: default mic dataline is "+mic.getTargetDataLine());

		// The File to record the buffer to.
		File file = new File("temp_audio_file.flac");

		System.out.println("DEBUG: Starting recording phase.");

		try {
			System.out.println("DEBUG: Recording audio.");
			mic.captureAudioToFile(file);// Begins recording

			startTime = System.nanoTime();

			while (true){// System.nanoTime() - startTime < ConfigVariables.AUDIO_RECORDING_MAX_LENGTH) {
				Thread.sleep(1000);
				if (stopRecording) {
					System.out.println("DEBUG: recording stopped");
					break;
				}
				System.out.println("DEBUG: Recording and waiting, time(rounded sec)= "
						+ (System.nanoTime() - startTime)/1000000000
						+" Thread ID: " 
						+ Thread.currentThread().getId());

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

	public GooglesAnswer endAudioCapture() {
		System.out.println("DEBUG: stop recording flag set");
		stopRecording = true;

		System.out.println("DEBUG: waiting for google reply");
		while (messageRecieved == false) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("DEBUG: got google reply, transaction complete.");
		messageRecieved=false;//reinitialize in case will be reused
		return googleSaid;
	}

}

	