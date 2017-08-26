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
        long randomNum = 10 + (int)(Math.random() * 20) ;
        return randomNum;
    }

    public static Float randomLongitude(){
        Float randomNum = -50 + (float)(Math.random() * 40) ;
        return randomNum;
    }

    public static Medicao medicaoRandom(){
        Medicao medicao = new Medicao(
                randomNum(),
                Float.parseFloat(randomNum().toString()),
                Float.parseFloat(randomNum().toString()),
                randomNum(),
                randomLongitude(),
                randomLongitude(),
                randomNum().toString() + "/09/2017)",
                randomNum() + ":" + randomNum()
        );
        return medicao;
    }
    
}
