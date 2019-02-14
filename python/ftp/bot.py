from PIL import ImageGrab, ImageOps
from numpy import *
import pyautogui
import time

class Coordinates():
    replayBtn = (340, 390)
    dino = (703, 396 )

def restartGame():
    pyautogui.click(Coordinates.replayBtn)

def pressSpace():
    pyautogui.keyDown('space')
    time.sleep(0.05)
    print("JUMP")
    pyautogui.keyUp('space')

def imageGrab(idx):
    box = (Coordinates.dino[0]+35, Coordinates.dino[1] , Coordinates.dino[0] + 110, Coordinates.dino[1] + 50)
    image = ImageGrab.grab(box)
    #print(image)
    grayImage = ImageOps.grayscale(image)
    a = array (grayImage.getcolors())
    #time.sleep(0.1)
    print (a.sum())
    image.save("botImgDino%s.png" % idx)
    return a.sum()
    

if __name__ == "__main__":
    restartGame()  
    time.sleep(10)
    pressSpace()
    idx = 0
    while True:
        #imageGrab()
        if(imageGrab(idx) != 4080):
            pressSpace()
            time.sleep(0.1)
        idx = idx + 1
        