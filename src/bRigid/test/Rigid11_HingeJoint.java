package bRigid.test;

import javax.vecmath.Vector3f;

import processing.core.PApplet;
import peasy.*;
import bRigid.*;

@SuppressWarnings("serial")
public class Rigid11_HingeJoint extends PApplet {

	PeasyCam cam;

	BPhysics physics;

	public void setup() {
		size(1280, 720, P3D);
		frameRate(30);

		cam = new PeasyCam(this, 400);
		cam.rotateX(-HALF_PI+.2f);

		physics = new BPhysics();
		physics.world.setGravity(new Vector3f(0, 0, 0));


		float sizeX = 30;
		float sizeY = 150;
		float sizeZ = 2;

		for (int i = -10; i <= 10; i++) {

			float mass = 1;
			//set body passive
			if (i == 10) {
				mass = 0;
			}

			BBox box = new BBox(this, mass, 0, 0, 0, sizeX, sizeY, sizeZ );
			box.rigidBody.setAngularFactor(1.1f);
			box.setPosition(i * sizeX, 0, 0);
			physics.addBody(box);

			if (physics.rigidBodies.size() > 1) {

				//get the neighbor
				BBox prevBox = (BBox) physics.rigidBodies.get(physics.rigidBodies.size() - 2);
				//calculate the distance to its center
				Vector3f dist = box.rigidBody.getCenterOfMassPosition(new Vector3f());
				Vector3f t = prevBox.rigidBody.getCenterOfMassPosition(new Vector3f());
				dist.sub(t);
				Vector3f pivA = (Vector3f) dist.clone();
				pivA.scale(-.5f);
				Vector3f pivB = (Vector3f) dist.clone();
				pivB.scale(.5f);

				//axxis for hinge rotation
				Vector3f axisInA = new Vector3f(0, 1, 0);
				axisInA.normalize();
				Vector3f axisInB = (Vector3f) axisInA.clone();
				BJointHinge hinge = new BJointHinge(box, prevBox, pivA, pivB, axisInA, axisInB);

				physics.addJoint(hinge);

				//enableAngularMotor(boolean enableMotor, float targetVelocity, float maxMotorImpulse)
				hinge.enableAngularMotor(true, 2, 2f);
				hinge.setLimit(0, 1.5f);
			}
		}
	}

	public void draw() {
		background(255);
		lights();

		physics.update();
		physics.display(240,240,240);
		//saveFrame("D:/LEDS/2013_bRigid_library/Pics/Pic28/pic28####.jpeg");
	}

}
