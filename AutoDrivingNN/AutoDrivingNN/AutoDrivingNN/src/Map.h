#pragma once

#include <vector>
#include "Obstacle.h"
#include <algorithm>



class Map
{
	std::vector<Obstacle> obstacles;

public:
	void Add(Obstacle);
	double GetDistance(pnt Point, pnt Direction);
	void DrawAll();

	bool Save(std::string path);
	bool Read(std::string path);
};

