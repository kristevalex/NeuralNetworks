#include <windows.h>
#include <iostream>
#include <gl/gl.h>
#include <io.h>

#pragma comment(lib, "opengl32.lib")

#include "Drawing.h"

int WINAPI WinMain(HINSTANCE hInstance,
	HINSTANCE hPrevInstance,
	LPSTR lpCmdLine,
	int nCmdShow)
{
	auto graphics = Drawing(nCmdShow);
	graphics.Run();

	return 0;
}
