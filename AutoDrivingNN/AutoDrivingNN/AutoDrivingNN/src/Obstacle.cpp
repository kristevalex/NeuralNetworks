#include "Obstacle.h"

Obstacle::Obstacle() :
	start(),
	end(),
	thickness(0)
{
}

Obstacle::Obstacle(pnt Start, pnt End, int Thickness) :
	start(Start),
	end(End),
	thickness(Thickness)
{
}

void Obstacle::DrawObstacle()
{
	glLineWidth(thickness);
	glBegin(GL_LINES);

	glColor3f(0.0f, 0.0f, 0.0f);

	glVertex2f(start.xcrd, start.ycrd);
	glVertex2f(end.xcrd, end.ycrd);
	
	glEnd();
}

double Obstacle::DistanseFrom(pnt Point, pnt Direction)
{
	return 0.0;
}
