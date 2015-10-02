package j4kdemo.videoviewerapp;

import edu.ufl.digitalworlds.j4k.J4KSDK;
import edu.ufl.digitalworlds.j4k.DepthMap;
import edu.ufl.digitalworlds.j4k.Skeleton;
import edu.ufl.digitalworlds.j4k.VideoFrame;
import java.util.Scanner;
 
 
public class Kinect extends J4KSDK {
 
	public static final double MOVE_AREA = 0.2;
	
	VideoPanel viewer=null;
	VideoFrame videoTexture;

	private KinectGestureListener registerGestureListener; 
	private KinectGesture prevGesture = new KinectGesture();
	
	
	public static void main(String[] args) {
		Kinect kinect = new Kinect();
		kinect.start(Kinect.DEPTH | Kinect.COLOR | Kinect.SKELETON | Kinect.XYZ | Kinect.PLAYER_INDEX);
		new Scanner(System.in).nextLine();
	}
	
	public Kinect() {
        super();
        videoTexture=new VideoFrame();
    } 
 
	public void setViewer(VideoPanel viewer){this.viewer=viewer;}
	
	@Override
	public void onColorFrameEvent(byte[] color_frame) {
		
		if(viewer==null || viewer.videoTexture==null) return;
		viewer.videoTexture.update(getColorWidth(), getColorHeight(), color_frame);
	}
 

    @Override
    public void onDepthFrameEvent(short[] depth_frame, byte[] player_index, float[] XYZ, float[] UV) {
	  
      DepthMap map=new DepthMap(getDepthWidth(),getDepthHeight(),XYZ);
      if(UV!=null) map.setUV(UV);
    } 
  
	/*The following method will run every time a new skeleton frame
    is received from the Kinect sensor. The skeletons are converted
    into Skeleton objects.*/ 
    @Override
    public void onSkeletonFrameEvent(boolean[] skeleton_tracked, float[] joint_position, float[] joint_orientation, byte[] joint_status) {
	  
	  Skeleton currentSkeleton;
      Skeleton skeletons[]=new Skeleton[getMaxNumberOfSkeletons()];
      
      // MaxNumberOfSkeletons=6
      //load all skeletons from the Kinect
      for(int i=0;i<getMaxNumberOfSkeletons();i++){
          skeletons[i]=Skeleton.getSkeleton(i, skeleton_tracked, joint_position, joint_orientation, joint_status, this); 
      }
      for(int i=0;i<getMaxNumberOfSkeletons();i++){
    	  
    	  currentSkeleton = skeletons[i];
    	  //only tracked skeletons contain any date
    	  if(currentSkeleton.isTracked()){
    		 
    		  KinectGesture gesture = new KinectGesture();
    		  gesture.type = "NONE";
    		   // We have determined that raising or lowering the hand (or any joint) causes the Y coordinate of the joint to increase or decrease.
    		   // Thus we can test if the Y coordinate of the left hand is greater than the Y coordinate of the head. 
    		   if( (currentSkeleton.get3DJointY(Skeleton.HAND_LEFT)>currentSkeleton.get3DJointY(Skeleton.HEAD)) 
    				   && (currentSkeleton.get3DJointY(Skeleton.HAND_RIGHT)>currentSkeleton.get3DJointY(Skeleton.HEAD)) )
   		       {
   			       System.out.println("Both hands are raised above the head!!!!!!!!!!!!!!!");
   			       
   			       gesture.type = "BOTH";
   		       }
    		   else if(currentSkeleton.get3DJointY(Skeleton.HAND_LEFT)>currentSkeleton.get3DJointY(Skeleton.HEAD) )
    		   {
    			   System.out.println("The LEFT hand is raised above the head!!!!!!!!!!!!!!!");
    			   gesture.type = "LEFT";
    		   }
    		   else if(currentSkeleton.get3DJointY(Skeleton.HAND_RIGHT)>currentSkeleton.get3DJointY(Skeleton.HEAD) )
    		   {
    			   System.out.println("The RIGHT hand is raised above the head!!!!!!!!!!!!!!!");
    			   gesture.type = "LEFT";
    		   }
    		   else if(currentSkeleton.get3DJointY(Skeleton.HAND_RIGHT)<currentSkeleton.get3DJointY(Skeleton.SPINE_MID) )
    		   {
    			  // System.out.println("The RIGHT hand is below mid-spine!!!!!!!!!!!!!!!");
    			   gesture.type = "RIGHT_LOW";
    		   }
    		   else if(currentSkeleton.get3DJointY(Skeleton.HAND_LEFT)<currentSkeleton.get3DJointY(Skeleton.SPINE_MID) )
    		   {
    			   System.out.println("The LEFT hand is below mid-spine!!!!!!!!!!!!!!!");
    			   gesture.type = "LEFT_LOW";
    		   }
    		   
    		   if(! gesture.equals(prevGesture)){
    			   this.registerGestureListener.onReceiveGesture(gesture);
    		   }
    	  }
      }
      
     }

	public void registerGestureListener(KinectGestureListener kinectSpeechController) {
		 this.registerGestureListener =kinectSpeechController;
		
	} 
	
    
 

}

