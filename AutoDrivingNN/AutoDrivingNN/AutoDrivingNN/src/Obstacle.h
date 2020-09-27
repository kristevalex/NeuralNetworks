#pragma once

#include <iostream> 
#include <utility> 
#include <windows.h>
#include <iostream>
#include <gl/gl.h>
#include <io.h>

#pragma comment(lib, "opengl32.lib")

#define pnt std::pair<float, float>
#define xcrd first
#define ycrd second

class Obstacle
{
	pnt start, end;
	int thickness;

public:
	Obstacle();
	Obstacle(pnt Start, pnt End, int Thickness);

	void DrawObstacle();

	double DistanseFrom(pnt Point, pnt Direction);
};

