package jbookreader.rendering;

/**
 * This interface represents a basic typing convention &mdash;
 * a box. The height is calculated over and the depth
 * is calculated below the baseline. The contents of the box
 * need not fit inside the boundaries of the box.
 *  
 * @author Dmitry Baryshkov (dbaryshkov@gmail.com)
 *
 */
public interface IDrawable<T> {
	float getWidth(Position position);
	float getHeight();
	float getDepth();
	
	void draw(Position position);
	
	T getContext();
}
