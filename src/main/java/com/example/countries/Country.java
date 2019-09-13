package com.example.countries;

class Country {
    String name = null;
    String capital = null;

    public  Country(String gname, String gcapital) {
        this.name = new String(gname);
        this.capital = new String(gcapital);
    }

    public String getName() {
        return this.name;
    }

    public String getCapital() {
        return this.capital;
    }
}