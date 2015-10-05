package com.pearson.hackathon.controller;

import java.util.Scanner;

import com.darkprograms.speech.microphone.Microphone;
import com.darkprograms.speech.raiseYourHand.EventDrivenSpeachToTextController;
import com.darkprograms.speech.raiseYourHand.GooglesAnswer;
import com.darkprograms.speech.raiseYourHand.SpeechListener;
import com.darkprograms.speech.recognizer.GSpeechResponseListener;
import com.pearson.hackathon.ui.NotePanel;

import j4kdemo.videoviewerapp.Kinect;
import j4kdemo.videoviewerapp.KinectGesture;
import j4kdemo.videoviewerapp.KinectGestureListener;
import javaFlacEncoder.FLACFileWriter;

public class KinectSpeechController implements KinectGestureListener, SpeechListener {

	GSpeechResponseListener speechResponseListener;
	NotePanel notePanel;
	EventDrivenSpeachToTextController mcontroller ;
	boolean audioBeingRecorded = false;
	
	public KinectSpeechController(boolean useKinect) {
		Microphone mic = new Microphone(FLACFileWriter.FLAC);
		EventDrivenSpeachToTextController.mic = new Microphone(FLACFileWriter.FLAC);
		notePanel = NotePanel.createAndShowGUI();
		if(useKinect){
			Kinect kinect = new Kinect();
			kinect.registerGestureListener(this);
		
			kinect.start(Kinect.DEPTH | Kinect.COLOR | Kinect.SKELETON | Kinect.XYZ | Kinect.PLAYER_INDEX);
		}
		
	}
	
	
	public void onReceiveGesture(KinectGesture gesture){
		
		// when a gesture is received get speech and put it in the ui
		
		if(gesture.type.equals("LEFT")){
			if(mcontroller!=null)
				return;
			if(!audioBeingRecorded){
				mcontroller = EventDrivenSpeachToTextController.startAudioCapture(gesture,this);
			}
		}
		if(gesture.type.equals("LEFT_LOW")&&mcontroller!=null){
			if(!audioBeingRecorded){
				mcontroller.endAudioCapture();
				mcontroller=null;

			}
		}
		
		
	}
	

	public void onReceived(Object gesture, GooglesAnswer answer) {
		KinectGesture g =(KinectGesture )gesture;
		
		notePanel.addNote("normal", answer.getBestGuess() +"\n");
		audioBeingRecorded = false;
		
	}
	
	private String getStyleFromGesture(KinectGesture gesture){
		String returnString = null;
		
		
		return returnString;
	}
	
	public static void main(String[] args) {
		boolean useKinect = false;
		KinectSpeechController ksc = new KinectSpeechController(useKinect);
		
		KinectGesture start = new KinectGesture();
		start.type ="LEFT";
		
		KinectGesture end = new KinectGesture();
		end.type ="LEFT_LOW";
		
		
		// wait until return is hit
		new Scanner(System.in).nextLine();
		ksc.onReceiveGesture(start);
		new Scanner(System.in).nextLine();
		ksc.onReceiveGesture(start);
		new Scanner(System.in).nextLine();
		ksc.onReceiveGesture(start);
		
		new Scanner(System.in).nextLine();
		ksc.onReceiveGesture(end);
		new Scanner(System.in).nextLine();
	}


}
