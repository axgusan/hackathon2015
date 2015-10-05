package com.darkprograms.speech.raiseYourHand;

import javax.sound.sampled.LineUnavailableException;

import javaFlacEncoder.FLACFileWriter;

import com.darkprograms.speech.microphone.Microphone;

public  class MicAvailabilityChecker {
	
	public static void validateMicAvailability() throws MicUnaccessibleException {
		Microphone mic = new Microphone(FLACFileWriter.FLAC);
	    try{
	        if(mic.getState() != Microphone.CaptureState.CLOSED ){
	            throw new MicUnaccessibleException("Mic didn't successfully initialized");
	        }

	        mic.captureAudioToFile("test.mp3");
	        if(mic.getState() != Microphone.CaptureState.PROCESSING_AUDIO ||mic.getState() != Microphone.CaptureState.STARTING_CAPTURE){
	            throw new MicUnaccessibleException("Mic is in use and can't be accessed");
	        }
	        mic.close();
	    } catch (LineUnavailableException e) {
			throw new MicUnaccessibleException("Mic is in use and can't be accessed");
		} finally{
	    	mic.close();
	    	mic=null;
	    }
	}

}
