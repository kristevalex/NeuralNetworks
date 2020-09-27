#pragma once

#include <windows.h>
#include <iostream>
#include <gl/gl.h>
#include <io.h>
#include "Map.h"

#pragma comment(lib, "opengl32.lib")

//int MouseButtons = 0;
//int MousePosX = 0, MousePosY = 0;


LRESULT CALLBACK WindowProc(HWND, UINT, WPARAM, LPARAM);
void EnableOpenGL(HWND hwnd, HDC*, HGLRC*);
void DisableOpenGL(HWND, HDC, HGLRC);


class Drawing
{
	HWND hwnd;
	HDC hDC;
	HGLRC hRC;

	Map map;

public:
	Drawing(int nCmdShow);
	~Drawing();

	int Run(int mode = 0);
	pnt ScrToOpenGL(int x, int y);
};

