package com.darkprograms.speech.raiseYourHand;

import java.io.File;
import java.nio.file.Files;

import javaFlacEncoder.FLACFileWriter;

import com.darkprograms.speech.microphone.Microphone;
import com.darkprograms.speech.recognizer.GSpeechDuplex;
import com.darkprograms.speech.recognizer.GSpeechResponseListener;
import com.darkprograms.speech.recognizer.GoogleResponse;

public class EventDrivenSpeachToTextController {

	private GooglesAnswer googleSaid = new GooglesAnswer();
	private boolean stopRecording = false;
	private long startTime,currentTime;

	public void startAudioCapture() {
		System.out.println("DEBUG: Start Audio capture request recieved.");
		// Instantiate the API
		final GSpeechDuplex dup = new GSpeechDuplex(ConfigVariables.GOOGLE_API_KEY);
		boolean stopRecording = false;
		googleSaid.setBestGuess(null);

		// Adds the listener
		dup.addResponseListener(new GSpeechResponseListener() {		
			public void onResponse(GoogleResponse gr) {
				System.out.println("DEBUG: Recieved reply from Google API");
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

				startTime=System.nanoTime();
				
				while (!stopRecording && System.nanoTime()-startTime<ConfigVariables.AUDIO_RECORDING_MAX_LENGTH) {
					System.out.println("DEBUG: Wait time= "+(System.nanoTime()-startTime));
				} // wait until stopped or max timeout
					//Thread.sleep(10000);// Records for 10 seconds
				mic.close();// Stops recording

				// Sends voice recording to Google
				// Saves data into memory
				byte[] data = Files.readAllBytes(mic.getAudioFile().toPath());

				dup.recognize(data, (int) mic.getAudioFormat().getSampleRate());
				mic.getAudioFile().delete();// Deletes Buffer file

			} catch (Exception e) {
				e.printStackTrace();
			}
		
		
		
		
	}// end startAudioCapture

	// TODO: trigger stop recording something real
	public GooglesAnswer endAudioCapture() {
		stopRecording = true;
		// TODO: there has to be a better way to do this
		// also might never happen?
		while(googleSaid.getBestGuess()==null)
			{
			System.out.println("DEBUG: meaningless waiting till GOOGLE replies..."); 
			}
		return googleSaid;
	}

}
