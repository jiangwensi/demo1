package com.codetest.service;

import org.springframework.stereotype.Service;

/**
 * Created by Jiang Wensi on 9/12/2020
 */
@Service
public class Utility {
    public String month(int mth) {
        String mthStr;
        switch (mth){
            case 1: mthStr = "Jan"; break;
            case 2: mthStr = "Feb"; break;
            case 3: mthStr = "Mar"; break;
            case 4: mthStr = "Apr"; break;
            case 5: mthStr = "May"; break;
            case 6: mthStr = "Jun"; break;
            case 7: mthStr = "Jul"; break;
            case 8: mthStr = "Aug"; break;
            case 9: mthStr = "Sep"; break;
            case 10: mthStr = "Oct"; break;
            case 11: mthStr = "Nov"; break;
            case 12: mthStr = "Dec"; break;
            default: mthStr = "";
        }
        return mthStr;
    }
}
