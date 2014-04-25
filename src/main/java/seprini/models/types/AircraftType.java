package seprini.models.types;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * A class to represent aircraft types and associated data.
 * @author Jonathan
 *
 */
public class AircraftType {
	private float initialSpeed;
	private int radius;
	private int separationRadius;
	private TextureRegion texture;
	private float maxTurningSpeed;
	private float maxClimbRate;
	private float maxSpeed;
	private float minSpeed;

	public float getInitialSpeed() {
		return initialSpeed;
	}

	public AircraftType setInitialSpeed(float initialSpeed) {
		this.initialSpeed = initialSpeed;
		return this;
	}

	public int getRadius() {
		return radius;
	}

	public AircraftType setRadius(int radius) {
		this.radius = radius;
		return this;
	}

	public int getSeparationRadius() {
		return separationRadius;
	}

	public AircraftType setSeparationRadius(int separationRadius) {
		this.separationRadius = separationRadius;
		return this;
	}

	public TextureRegion getTexture() {
		return texture;
	}

	public AircraftType setTexture(TextureRegion texture) {
		this.texture = texture;
		return this;
	}

	public float getMaxTurningSpeed() {
		return maxTurningSpeed;
	}

	public AircraftType setMaxTurningSpeed(float maxTurningSpeed) {
		this.maxTurningSpeed = maxTurningSpeed;
		return this;
	}

	public float getMaxClimbRate() {
		return maxClimbRate;
	}

	public AircraftType setMaxClimbRate(float maxClimbRate) {
		this.maxClimbRate = maxClimbRate;
		return this;
	}

	public float getMaxSpeed() {
		return maxSpeed;
	}

	public AircraftType setMaxSpeed(float maxSpeed) {
		this.maxSpeed = maxSpeed;
		return this;
	}

	public float getMinSpeed() {
		return minSpeed;
	}

	public AircraftType setMinSpeed(float minSpeed) {
		this.minSpeed = minSpeed;
		return this;
	}
}
