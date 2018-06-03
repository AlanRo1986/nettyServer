package com.lanxinbase.system.basic;


import com.lanxinbase.system.utils.CommonUtil;
import com.lanxinbase.system.utils.DateUtils;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

/**
 * Created by alan.luo on 2017/9/18.
 */
@Service
public abstract class CompactService extends Compact {


    public CompactService(Class classz){
        super(classz);
    }

    public String formatMoney(double money){
        return CommonUtil.moneyFormat(money);
    }

    public String formatDateTime(String time){
        if (time.length() < 11){
            return "";
        }
        return DateUtils.getFullDateTime(Long.parseLong(time));
    }

    public int getTime(){
        return (int) (DateUtils.getTime()/1000);
    }


}
