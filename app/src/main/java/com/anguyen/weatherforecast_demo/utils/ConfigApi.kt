package com.anguyen.weatherforecast_demo.utils

class ConfigApi {

    companion object{
        const val BASE_URL = "http://api.openweathermap.org/"
    }

    interface Api{
        companion object{

            const val SAMPLE_WEATHER = "data/2.5/weather?q=Bac Giang&units=metric&appid=3265ce131abbe389a416268d3cd91e0b"

            const val CITY_LIST_FILE_PATH = "https://firebasestorage.googleapis.com/v0/b/weatherforecast-97fc4.appspot.com/o/city.list.json?alt=media&token=f8c14786-ef53-4e54-ba1d-16cf6213fe48"

        }
    }

}