package com.data.krakowskismog;

public class Stacje
{
    private Integer id_stacji=401;
    int id_so2;
    int id_no2;
    int id_co;
    int id_pm10;
    int id_pm25;
    int id_o3;
    int id_c6h6;

    Stacje()
    {
        nowe_dane();
    }
    //set current station
    void zmiana(int numer)
    {
        switch(numer)
        {
            case 1:
                this.id_stacji=401;
                nowe_dane();
                break;
            case 2:
                this.id_stacji=402;
                nowe_dane();
                break;
            case 3:
                this.id_stacji=10121;
                nowe_dane();
                break;
            case 4:
                this.id_stacji=400;
                nowe_dane();
                break;
            case 5:
                this.id_stacji=10139;
                nowe_dane();
                break;
            case 6:
                this.id_stacji=11303;
                nowe_dane();
                break;
            case 7:
                this.id_stacji=10447;
                nowe_dane();
                break;
            case 8:
                this.id_stacji=10123;
                nowe_dane();
                break;
        }
    }
    public Integer getId_stacji()
    {
        return this.id_stacji;
    }
    //set id of substances tested at the station
    public void nowe_dane()
    {
        switch(id_stacji)
        {
            case 401:
                id_so2 = 2774;
                id_no2 = 2766;
                id_co = 0;
                id_pm10 = 2770;
                id_pm25 = 2772;
                id_o3 = 2768;
                id_c6h6 = 17896;
                break;
            case 402:
                id_so2 = 2797;
                id_no2 = 2788;
                id_co = 2783;
                id_pm10 = 2792;
                id_pm25 = 2794;
                id_o3 = 0;
                id_c6h6 = 2779;
                break;
            case 10121:
                id_so2 = 0;
                id_no2 = 16516;
                id_co = 0;
                id_pm10 = 16377;
                id_pm25 = 0;
                id_o3 = 0;
                id_c6h6 = 0;
                break;
            case 400:
                id_so2 = 0;
                id_no2 = 2747;
                id_co = 2745;
                id_pm10 = 2750;
                id_pm25 = 2752;
                id_o3 = 0;
                id_c6h6 = 16500;
                break;
            case 10139:
                id_so2 = 0;
                id_no2 = 0;
                id_co = 0;
                id_pm10 = 16784;
                id_pm25 = 0;
                id_o3 = 0;
                id_c6h6 = 0;
                break;
            case 11303:
                id_so2 = 0;
                id_no2 = 0;
                id_co = 0;
                id_pm10 = 20320;
                id_pm25 = 0;
                id_o3 = 0;
                id_c6h6 = 0;
                break;
            case 10447:
                id_so2 = 0;
                id_no2 = 0;
                id_co = 0;
                id_pm10 = 17309;
                id_pm25 = 0;
                id_o3 = 0;
                id_c6h6 = 0;
                break;
            case 10123:
                id_so2 = 0;
                id_no2 = 0;
                id_co = 0;
                id_pm10 = 16786;
                id_pm25 = 0;
                id_o3 = 0;
                id_c6h6 = 0;
                break;
        }
    }
    public int getId_so2()
    {
        return this.id_so2;
    }
    public int getId_co()
    {
        return this.id_co;
    }
    public int getId_no2()
    {
        return this.id_no2;
    }
    public int getId_pm10()
    {
        return this.id_pm10;
    }
    public int getId_pm25()
    {
        return this.id_pm25;
    }
    public int getId_c6h6()
    {
        return this.id_c6h6;
    }
    public int getId_o3()
    {
        return this.id_o3;
    }
    public void setId_stacji(Integer id)
    {
        if(id == null)id=402;
        if(id == 0)id=402;
        this.id_stacji=id;
    }
}
