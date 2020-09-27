#include "Map.h"

void Map::Add(Obstacle obs)
{
	obstacles.push_back(obs);
}

double Map::GetDistance(pnt Point, pnt Direction)
{
	double Dist = 10000000.0;
	for (auto obs : obstacles)
	{
		Dist = min(Dist, obs.DistanseFrom(Point, Direction));
	}

	return Dist;
}

void Map::DrawAll()
{
	for (auto obs : obstacles)
	{
		obs.DrawObstacle();
	}
}

bool Map::Save(std::string path)
{
	return false;
}

bool Map::Read(std::string path)
{
	return false;
}
