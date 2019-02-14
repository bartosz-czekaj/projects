#include "stdafx.h"
#include <cstdio>
#include <fstream>
#include <iostream>
#include "console.h"

// Source for these definitions: MSDN
// http://msdn.microsoft.com/en-us/library/windows/desktop/ms686039(v=vs.85).aspx
// http://msdn.microsoft.com/en-us/library/windows/desktop/ms683172(v=vs.85).aspx
// http://msdn.microsoft.com/en-us/library/windows/desktop/ms682091(v=vs.85).aspx

void DecompressedXP::SetPalette() 
{
    HANDLE con = GetStdHandle(STD_OUTPUT_HANDLE);

    CONSOLE_SCREEN_BUFFER_INFOEX bi;
    bi.cbSize = sizeof(CONSOLE_SCREEN_BUFFER_INFOEX);
    GetConsoleScreenBufferInfoEx(con, &bi);

    for(auto const& elem : palette_)
    {
        bi.ColorTable[elem.second + palette_offset_] = elem.first;
    }

    SetConsoleScreenBufferInfoEx(con, &bi);
}

void DecompressedXP::Render(int x, int y) 
{
    HANDLE con = GetStdHandle(STD_OUTPUT_HANDLE);

    COORD dxp_c = {static_cast<SHORT>(width_), static_cast<SHORT>(height_)};
    COORD pos = { 0, 0 };
    SMALL_RECT dst = 
    {
        static_cast<SHORT>(0 + x),
        static_cast<SHORT>(0 + y),
        static_cast<SHORT>(width_ + x),
        static_cast<SHORT>(height_ + y)
    };

    WriteConsoleOutput(
        con,
        &img_[0],
        dxp_c,
        pos,
        &dst);
}

int DecompressedXP::AddToPalette(RGB &rgb) 
{
    uint32_t urgb = rgb.red | (rgb.green << 8) | (rgb.blue << 16);
    if (palette_.find(urgb) == palette_.end()) 
    {
        size_t idx = palette_.size();
        palette_[urgb] = idx;
    }

    return palette_[urgb] + palette_offset_;
}

DecompressedXP::DecompressedXP(const char *filename, int palette_offset) 
: palette_offset_(palette_offset) 
{
    std::ifstream myfile;
    myfile.open(filename, std::ios::in | std::ios::binary);

    myfile.seekg(8, std::ios::beg);
    myfile.read((char*)&width_, 4);
    myfile.read((char*)&height_, 4);

    size_t size = size_t(width_) * height_;
    img_.resize(size);


    for (size_t i = 0; i < size; i++) 
    {
        int32_t ascii_code;
        myfile.read((char*)&ascii_code, 4);

        RGB fore{}, back{};
        myfile.read((char*)&fore, 3);
        myfile.read((char*)&back, 3);

        int fore_idx = AddToPalette(fore);
        int back_idx = AddToPalette(back);

        CHAR_INFO ci;
        ci.Char.UnicodeChar = ascii_code;
        ci.Attributes = fore_idx | (back_idx << 4);

        int x = i / height_;
        int y = i % height_;
        int xy = x + y * width_;

        img_[xy] = ci;
    }

    myfile.close();
}

PaletteArchiver::PaletteArchiver() 
{
    HANDLE con = GetStdHandle(STD_OUTPUT_HANDLE);
    bi_backup_.cbSize = sizeof(CONSOLE_SCREEN_BUFFER_INFOEX);
    GetConsoleScreenBufferInfoEx(con, &bi_backup_);
}

PaletteArchiver::~PaletteArchiver() 
{
    Restore();
}

void PaletteArchiver::Restore() 
{
    HANDLE con = GetStdHandle(STD_OUTPUT_HANDLE);
    SetConsoleScreenBufferInfoEx(con, &bi_backup_);
}