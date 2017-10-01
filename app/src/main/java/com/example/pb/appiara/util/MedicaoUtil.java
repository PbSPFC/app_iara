package com.example.pb.appiara.util;

import com.example.pb.appiara.entity.Medicao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PB on 19/08/2017.
 */

public class MedicaoUtil {

    public static boolean temperaturaApitar(Medicao medicao){
        if(medicao.getTemperatura() >= 22)
            return false;
        return true;
    }

    public static List<Medicao> gerarListaMedicao(){
        List<Medicao> medicaoList = new ArrayList<Medicao>();
        int i = 10 + (int)(Math.random() * 20);
        for (int x = 0; x < i; x++){
            Medicao medicao = medicaoRandom();
            medicaoList.add(medicao);
        }
        return medicaoList;
    }
    
    public static Long randomNum(){
        long randomNum = 0 + (int)(Math.random() * 30) ;
        return randomNum;
    }

    public static Long randomPH(){
        long randomNum = 0 + (int)(Math.random() * 15) ;
        return randomNum;
    }

    public static Float randomLongitude(){
        Float randomNum = 0 + (float)(Math.random() * 4) ;
        return randomNum;
    }

    public static Medicao medicaoRandom(){
        Medicao medicao = new Medicao(
                randomNum(),
                Float.parseFloat(randomNum().toString()),
                Float.parseFloat(randomNum().toString()),
                randomPH(),
                randomLongitude(),
                randomLongitude(),
                randomNum().toString() + "/09/2017",
                randomNum() + ":" + randomNum()
        );
        return medicao;
    }

    public static String getTempColor(Medicao medicao){
        if(medicao.getTemperatura() < 10){
            return "#6495ED";
        }else if(medicao.getTemperatura() >= 10 && medicao.getTemperatura() <20){
            return "#ffff00";
        }else if(medicao.getTemperatura() >= 20){
            return "#ff0000";
        }

        return "#00ff00";
    }

    public static String getPHColor(Medicao medicao){
        if(medicao.getLuminosidade() > 10){
            return "#6495ED";
        }else if(medicao.getLuminosidade() >= 7){
            return "#ffff00";
        }else if(medicao.getLuminosidade() >= 2){
            return "#00ff00";
        }
        return "#ff0000";
    }

    public static String getCondColor(Medicao medicao){
        if(medicao.getLatitude() < 0){
            return "#6495ED";
        }else if(medicao.getLatitude() >= 1 && medicao.getLatitude() <2){
            return "#ffff00";
        }else if(medicao.getLatitude() >= 2){
            return "#ff0000";
        }

        return "#00ff00";
    }
    
}
