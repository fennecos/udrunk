package net.udrunk.domain;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Point implements Serializable {
	public double x;
	public double y;
	
	public Point() {
	}
	
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public Point(String x, String y) {
		this.x = Double.parseDouble(x);
		this.y = Double.parseDouble(y);
	}
	
}
