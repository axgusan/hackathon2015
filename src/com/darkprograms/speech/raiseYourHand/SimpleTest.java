package com.darkprograms.speech.raiseYourHand;

public class SimpleTest {

	public static void main(String[] args) throws Exception {

		GooglesAnswer theResult = null;

		EventDrivenSpeachToTextController mcontroller = EventDrivenSpeachToTextController.startAudioCapture("a tag", null);

		System.out.println("INFO: Attempting to start audio capture.");

		 
 
			System.out.println("INFO: Waiting for 5 sec while recording.");
			Thread.sleep(5000); 
			
		System.out.println("INFO: Finished 5 sec wait, sending stopRecording.");
		theResult = mcontroller.endAudioCapture();

		System.out.println("INFO: Hitting STOP.");

		System.out.println("INFO: Attempting to end audio capture.");

		System.out.println("INFO: ↓↓↓↓↓↓↓↓RESULTS RECIEVED↓↓↓↓↓↓↓↓");
		System.out.println("INFO: best guess    > " + theResult.getBestGuess());
		System.out.println("INFO: probability   > "
				+ theResult.getProbability());
		System.out.println("INFO: other guesses > "
				+ theResult.getOtherGuesses());
		System.out.println("INFO: ↑↑↑↑↑↑↑↑RESULTS RECIEVED↑↑↑↑↑↑↑↑");
	}

}
