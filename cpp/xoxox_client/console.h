#pragma once

#include <vector>
#include <map>
#include <Windows.h>
#include <consoleapi2.h>
#include <unordered_map>

class DecompressedXP 
{
public:
    struct RGB 
    {
        uint8_t red, green, blue;
    };

    DecompressedXP(const char *filename, int palette_offset);

    void SetPalette();
    void Render(int x, int y);

    int32_t width_{}, height_{};
    std::vector<CHAR_INFO> img_;

    int palette_offset_;
    std::unordered_map<uint32_t, int> palette_;

private:
    int AddToPalette(RGB &rgb);
};


class PaletteArchiver 
{
public:
    PaletteArchiver();
    ~PaletteArchiver();

    void Restore();

private:
    CONSOLE_SCREEN_BUFFER_INFOEX bi_backup_{};
};




