// xoxoxclient.cpp : Defines the entry point for the console application.
//

#include "stdafx.h"
#include <iostream>
#include <string>
#include <memory>
#include "NetSock.h"
#include "console.h"


class XoXoXo {
private:
    enum class ProtoclRequest {
        GET_MOVE_RQ = 1,
        GET_MOVE_RS = 2,
        SHOW_WELCOME = 3,
        SHOW_BOARD = 4,
        SHOW_ERROR = 6,
        SHOW_ERROR_CONTINUE = 7,
        SHOW_DRAW = 8,
        SHOW_WINNER = 9,
        PLAYER_TURN = 10
    };
     int max_move;
     int rows_cols;
     static const char delimiter = '|';
public:

    XoXoXo():
        s_circle(new DecompressedXP("s_circle", 2)),
        s_cross(new DecompressedXP("s_cross", 5))
    {
        
    }

    bool  HandleSingleRequest(NetSock &s)
    {
        
        std::string request;
        while (true)
        {
            char current;
            const int ret = s.Read(&current, 1);
            if (ret <= 0) 
            {
                std::cout << "disconnected";
                return false;
            }

            if(current == delimiter)
            {
                break;
            }

            request += current;
        }

        auto requestid = static_cast<ProtoclRequest>(std::atoi(request.c_str()));

        switch (requestid) {
        case ProtoclRequest::GET_MOVE_RQ: 
            HandleGetMoveRequest(s);
            break;
        case ProtoclRequest::SHOW_WELCOME: 
            HandleShowWelcome();
            break;
        case ProtoclRequest::SHOW_BOARD: 
            HandleShowBoard(s);
            break;
        case ProtoclRequest::SHOW_ERROR:
            HandleShowMoveError(s);
            break;
        case ProtoclRequest::SHOW_ERROR_CONTINUE:
            HandleShowMoveErrorContinue(s);
            break;
        case ProtoclRequest::SHOW_DRAW:
            HandleShowDraw();
            break;
        case ProtoclRequest::SHOW_WINNER:
            HandleShowWinner(s);
            break;
        case ProtoclRequest::PLAYER_TURN: 
            HandleShowPlayerTurn(s);
            break;
        default: ;
        }

        while (true)
        {
            char endLine[3];
            s.Read(endLine, 2);
            endLine[2] = '\0';
            
            if (strcmp(endLine, "\n\r") == 0)
            {
                break;
            }
        }

        return true;
    }



private:
    wchar_t *convertCharArrayToLPCWSTR(const char* charArray)
    {
        wchar_t* wString = new wchar_t[4096];
        MultiByteToWideChar(CP_ACP, 0, charArray, -1, wString, 4096);
        return wString;
    }

    std::unique_ptr<DecompressedXP> s_circle;
    std::unique_ptr<DecompressedXP> s_cross;

    void  HandleShowWelcome() {
        DecompressedXP img("xoxoxo-title", 3);
        img.SetPalette();
        img.Render(0, 0);
        SetConsoleTitle(convertCharArrayToLPCWSTR("Welcome to the xoxox game!"));
        getchar(); 
    }

    void  HandleShowBoard(NetSock &s) 
    {
        std::string number;
        std::string board;
        int delim_nbr = 0;
        int index = 0;
        while(true)
        {
            char current;
            if(s.Read(&current, 1) != -1)
            {
                if (current == delimiter)
                {
                    ++delim_nbr;
                    if (delim_nbr == 1)
                    {
                        rows_cols = std::atoi(number.c_str());
                        max_move = rows_cols * rows_cols;

                        s_circle->SetPalette();
                        s_cross->SetPalette();

                        HANDLE con = GetStdHandle(STD_OUTPUT_HANDLE);
                        COORD topleft = { 0, 0 };

                        SetConsoleCursorPosition(con, topleft);
                        DWORD num;
                        FillConsoleOutputCharacter(con, ' ', (25* rows_cols) * 38, topleft, &num);
                        FillConsoleOutputAttribute(con, 1, (25 * rows_cols) * 38, topleft, &num);

                        continue;
                    }

                    if (delim_nbr == 2)
                    {
                        break;
                    }
                }
                if (delim_nbr == 1)
                {
                    DrawBoard(current, index, *s_cross, *s_circle);
                    index++;
                }
                else
                {
                    number += current;
                }
            }
        }
    }

    void DrawBoard(char current, int index, DecompressedXP &s_cross, DecompressedXP &s_circle)
    {
        int x = (index % rows_cols) * 8 + 40;
        int y = (index / rows_cols) * 8;

        switch (current) {
        default:
            break;

        case 'X':
            s_cross.Render(x, y);
            break;

        case 'O':
            s_circle.Render(x, y);
            break;
        }
    }

    void  HandleShowPlayerTurn(NetSock &s) 
    {
        std::string player;
        while (true)
        {

            char current;
            if (s.Read(&current, 1) != -1)
            {
                if (current == delimiter)
                {
                    break;
                }
                player += current;
            }
        }
        std::cout << "player " << player << " turn\n";
    }

    void  HandleShowMoveErrorContinue(NetSock &s)
    {
        std::string player;
        while (true)
        {

            char current;
            if (s.Read(&current, 1) != -1)
            {
                if (current == delimiter)
                {
                    break;
                }
                player += current;
            }
        }

        std::cout << "error error wrong move (continue) player " << player << '\n';
    }


    void  HandleShowMoveError(NetSock &s)
    {
        std::string player;
        while (true)
        {

            char current;
            if (s.Read(&current, 1) != -1)
            {
                if (current == delimiter)
                {
                    break;
                }
                player += current;
            }
        }

        std::cout << "error error wrong move player "<<player<<'\n';
    }

    void  HandleShowWinner(NetSock &s) 
    {
        std::string player;
        while (true)
        {

            char current;
            if (s.Read(&current, 1) != -1)
            {
                if (current == delimiter)
                {
                    break;
                }
                player += current;
            }
        }

        std::cout << "player "<<player<<" won\n";
    }

    void  HandleShowDraw() 
    {
        std::cout<<"draw!\n";
    }

    void  HandleGetMoveRequest(NetSock &s) 
    {
        int move = 0;
        while (true)
        {
            std::cout << "your move !\n";

            
            std::cin >> move;


            if (move > 0 && move <= max_move)
                break;
        }

        auto response = static_cast<char>(ProtoclRequest::GET_MOVE_RS);

        std::string mve = std::to_string(move);

        s.Write(&response, 1);
        s.Write(mve.c_str(), mve.length());
        s.Write(&delimiter,1);
    }
};

//int XoXoXo::max_move = 0;

static PaletteArchiver *g_pa;

BOOL WINAPI CtrlCHandler(DWORD /*dwCtrlType*/) {
    g_pa->Restore();
    return FALSE;
}

void usage();

int main(int argc, char **argv) 
{
   
    PaletteArchiver pa;
    g_pa = &pa;
    SetConsoleCtrlHandler(CtrlCHandler, TRUE);

    if (argc != 3)
    {
        usage();
        return -1;
    }

    unsigned short port = 0;
    try
    {
        port = std::stoi(argv[2]);
    }
    catch (std::invalid_argument)
    {
        usage();
        return -2;
    }

    std::string host = argv[1];

    NetSock s;
    s.InitNetworking();
    if (!s.Connect(host.c_str(), port))
    {
        std::cerr << "Connection error\n";
        return -3;
    }

    XoXoXo xoxox;

    do {} while (xoxox.HandleSingleRequest(s));

    
    s.Disconnect();

    return 0;
}

void usage() {
    std::cout << "Usage <HOST> <PORT>\n";
}

