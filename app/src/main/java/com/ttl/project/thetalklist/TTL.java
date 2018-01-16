package com.ttl.project.thetalklist;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.telephony.TelephonyManager;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.StandardExceptionParser;
import com.google.android.gms.analytics.Tracker;
import com.ttl.project.thetalklist.Analytics.AnalyticsTrackers;
import com.ttl.project.thetalklist.util.TypefaceUtil;
import com.parse.Parse;

import java.util.Locale;

/**
 * Created by Saubhagyam on 16/06/2017.
 */

public class TTL extends Application {


    public static final String TAG = TTL.class
            .getSimpleName();

    private static TTL mInstance;

    public int ExitBit=1;
    public int MessageBit=0;
    public boolean isCall=false;
    public String Callfrom="";
    public int Callmin;

    public boolean manuallyTurnOn=false;

    public String locale;

    public int getCallmin() {
        return Callmin;
    }

    public void setCallmin(int callmin) {
        Callmin = callmin;
    }

    public String json="[\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"AFG\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Asia/Kabul\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"Afghanistan Standard Time\",\n" +
            "        \"Name\": \"(UTC+04:30) Kabul\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Afghanistan\",\n" +
            "    \"IsoAlpha2\": \"AF\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"ALB\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Europe/Tirane\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"Central Europe Standard Time\",\n" +
            "        \"Name\": \"(UTC+01:00) Belgrade, Bratislava, Budapest, Ljubljana, Prague\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Albania\",\n" +
            "    \"IsoAlpha2\": \"AL\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"DZA\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Africa/Algiers\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"W. Central Africa Standard Time\",\n" +
            "        \"Name\": \"(UTC+01:00) West Central Africa\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Algeria\",\n" +
            "    \"IsoAlpha2\": \"DZ\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"ARG\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"America/Argentina/Buenos_Aires\",\n" +
            "      \"America/Argentina/Cordoba\",\n" +
            "      \"America/Argentina/Salta\",\n" +
            "      \"America/Argentina/Jujuy\",\n" +
            "      \"America/Argentina/Tucuman\",\n" +
            "      \"America/Argentina/Catamarca\",\n" +
            "      \"America/Argentina/La_Rioja\",\n" +
            "      \"America/Argentina/San_Juan\",\n" +
            "      \"America/Argentina/Mendoza\",\n" +
            "      \"America/Argentina/San_Luis\",\n" +
            "      \"America/Argentina/Rio_Gallegos\",\n" +
            "      \"America/Argentina/Ushuaia\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"Argentina Standard Time\",\n" +
            "        \"Name\": \"(UTC-03:00) City of Buenos Aires\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Argentina\",\n" +
            "    \"IsoAlpha2\": \"AR\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"ARM\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Asia/Yerevan\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"Caucasus Standard Time\",\n" +
            "        \"Name\": \"(UTC+04:00) Yerevan\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Armenia\",\n" +
            "    \"IsoAlpha2\": \"AM\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"AUS\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Australia/Lord_Howe\",\n" +
            "      \"Antarctica/Macquarie\",\n" +
            "      \"Australia/Hobart\",\n" +
            "      \"Australia/Currie\",\n" +
            "      \"Australia/Melbourne\",\n" +
            "      \"Australia/Sydney\",\n" +
            "      \"Australia/Broken_Hill\",\n" +
            "      \"Australia/Brisbane\",\n" +
            "      \"Australia/Lindeman\",\n" +
            "      \"Australia/Adelaide\",\n" +
            "      \"Australia/Darwin\",\n" +
            "      \"Australia/Perth\",\n" +
            "      \"Australia/Eucla\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"Central Pacific Standard Time\",\n" +
            "        \"Name\": \"(UTC+11:00) Solomon Is., New Caledonia\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"Id\": \"Tasmania Standard Time\",\n" +
            "        \"Name\": \"(UTC+10:00) Hobart\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"Id\": \"AUS Eastern Standard Time\",\n" +
            "        \"Name\": \"(UTC+10:00) Canberra, Melbourne, Sydney\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"Id\": \"Cen. Australia Standard Time\",\n" +
            "        \"Name\": \"(UTC+09:30) Adelaide\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"Id\": \"E. Australia Standard Time\",\n" +
            "        \"Name\": \"(UTC+10:00) Brisbane\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"Id\": \"AUS Central Standard Time\",\n" +
            "        \"Name\": \"(UTC+09:30) Darwin\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"Id\": \"W. Australia Standard Time\",\n" +
            "        \"Name\": \"(UTC+08:00) Perth\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Australia\",\n" +
            "    \"IsoAlpha2\": \"AU\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"AUT\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Europe/Vienna\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"W. Europe Standard Time\",\n" +
            "        \"Name\": \"(UTC+01:00) Amsterdam, Berlin, Bern, Rome, Stockholm, Vienna\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Austria\",\n" +
            "    \"IsoAlpha2\": \"AT\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"AZE\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Asia/Baku\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"Azerbaijan Standard Time\",\n" +
            "        \"Name\": \"(UTC+04:00) Baku\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Azerbaijan\",\n" +
            "    \"IsoAlpha2\": \"AZ\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"BHR\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Asia/Bahrain\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"Arab Standard Time\",\n" +
            "        \"Name\": \"(UTC+03:00) Kuwait, Riyadh\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Bahrain\",\n" +
            "    \"IsoAlpha2\": \"BH\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"BGD\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Asia/Dhaka\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"Bangladesh Standard Time\",\n" +
            "        \"Name\": \"(UTC+06:00) Dhaka\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Bangladesh\",\n" +
            "    \"IsoAlpha2\": \"BD\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"BLR\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Europe/Minsk\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"Belarus Standard Time\",\n" +
            "        \"Name\": \"(UTC+03:00) Minsk\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Belarus\",\n" +
            "    \"IsoAlpha2\": \"BY\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"BEL\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Europe/Brussels\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"Romance Standard Time\",\n" +
            "        \"Name\": \"(UTC+01:00) Brussels, Copenhagen, Madrid, Paris\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Belgium\",\n" +
            "    \"IsoAlpha2\": \"BE\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"BLZ\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"America/Belize\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"Central America Standard Time\",\n" +
            "        \"Name\": \"(UTC-06:00) Central America\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Belize\",\n" +
            "    \"IsoAlpha2\": \"BZ\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"BTN\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Asia/Thimphu\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"Bangladesh Standard Time\",\n" +
            "        \"Name\": \"(UTC+06:00) Dhaka\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Bhutan\",\n" +
            "    \"IsoAlpha2\": \"BT\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"BOL\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"America/La_Paz\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"SA Western Standard Time\",\n" +
            "        \"Name\": \"(UTC-04:00) Georgetown, La Paz, Manaus, San Juan\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Bolivia\",\n" +
            "    \"IsoAlpha2\": \"BO\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"BIH\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Europe/Sarajevo\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"Central Europe Standard Time\",\n" +
            "        \"Name\": \"(UTC+01:00) Belgrade, Bratislava, Budapest, Ljubljana, Prague\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Bosnia and Herzegovina\",\n" +
            "    \"IsoAlpha2\": \"BA\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"BWA\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Africa/Gaborone\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"South Africa Standard Time\",\n" +
            "        \"Name\": \"(UTC+02:00) Harare, Pretoria\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Botswana\",\n" +
            "    \"IsoAlpha2\": \"BW\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"BRA\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"America/Noronha\",\n" +
            "      \"America/Belem\",\n" +
            "      \"America/Fortaleza\",\n" +
            "      \"America/Recife\",\n" +
            "      \"America/Araguaina\",\n" +
            "      \"America/Maceio\",\n" +
            "      \"America/Bahia\",\n" +
            "      \"America/Sao_Paulo\",\n" +
            "      \"America/Campo_Grande\",\n" +
            "      \"America/Cuiaba\",\n" +
            "      \"America/Santarem\",\n" +
            "      \"America/Porto_Velho\",\n" +
            "      \"America/Boa_Vista\",\n" +
            "      \"America/Manaus\",\n" +
            "      \"America/Eirunepe\",\n" +
            "      \"America/Rio_Branco\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"UTC-02\",\n" +
            "        \"Name\": \"(UTC-02:00) Coordinated Universal Time-02\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"Id\": \"SA Eastern Standard Time\",\n" +
            "        \"Name\": \"(UTC-03:00) Cayenne, Fortaleza\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"Id\": \"Bahia Standard Time\",\n" +
            "        \"Name\": \"(UTC-03:00) Salvador\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"Id\": \"E. South America Standard Time\",\n" +
            "        \"Name\": \"(UTC-03:00) Brasilia\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"Id\": \"Central Brazilian Standard Time\",\n" +
            "        \"Name\": \"(UTC-04:00) Cuiaba\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"Id\": \"SA Western Standard Time\",\n" +
            "        \"Name\": \"(UTC-04:00) Georgetown, La Paz, Manaus, San Juan\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"Id\": \"SA Pacific Standard Time\",\n" +
            "        \"Name\": \"(UTC-05:00) Bogota, Lima, Quito, Rio Branco\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Brazil\",\n" +
            "    \"IsoAlpha2\": \"BR\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"BRN\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Asia/Brunei\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"Singapore Standard Time\",\n" +
            "        \"Name\": \"(UTC+08:00) Kuala Lumpur, Singapore\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Brunei\",\n" +
            "    \"IsoAlpha2\": \"BN\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"BGR\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Europe/Sofia\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"FLE Standard Time\",\n" +
            "        \"Name\": \"(UTC+02:00) Helsinki, Kyiv, Riga, Sofia, Tallinn, Vilnius\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Bulgaria\",\n" +
            "    \"IsoAlpha2\": \"BG\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"KHM\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Asia/Phnom_Penh\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"SE Asia Standard Time\",\n" +
            "        \"Name\": \"(UTC+07:00) Bangkok, Hanoi, Jakarta\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Cambodia\",\n" +
            "    \"IsoAlpha2\": \"KH\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"CMR\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Africa/Douala\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"W. Central Africa Standard Time\",\n" +
            "        \"Name\": \"(UTC+01:00) West Central Africa\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Cameroon\",\n" +
            "    \"IsoAlpha2\": \"CM\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"CAN\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"America/St_Johns\",\n" +
            "      \"America/Halifax\",\n" +
            "      \"America/Glace_Bay\",\n" +
            "      \"America/Moncton\",\n" +
            "      \"America/Goose_Bay\",\n" +
            "      \"America/Blanc-Sablon\",\n" +
            "      \"America/Toronto\",\n" +
            "      \"America/Nipigon\",\n" +
            "      \"America/Thunder_Bay\",\n" +
            "      \"America/Iqaluit\",\n" +
            "      \"America/Pangnirtung\",\n" +
            "      \"America/Atikokan\",\n" +
            "      \"America/Winnipeg\",\n" +
            "      \"America/Rainy_River\",\n" +
            "      \"America/Resolute\",\n" +
            "      \"America/Rankin_Inlet\",\n" +
            "      \"America/Regina\",\n" +
            "      \"America/Swift_Current\",\n" +
            "      \"America/Edmonton\",\n" +
            "      \"America/Cambridge_Bay\",\n" +
            "      \"America/Yellowknife\",\n" +
            "      \"America/Inuvik\",\n" +
            "      \"America/Creston\",\n" +
            "      \"America/Dawson_Creek\",\n" +
            "      \"America/Fort_Nelson\",\n" +
            "      \"America/Vancouver\",\n" +
            "      \"America/Whitehorse\",\n" +
            "      \"America/Dawson\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"Newfoundland Standard Time\",\n" +
            "        \"Name\": \"(UTC-03:30) Newfoundland\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"Id\": \"Atlantic Standard Time\",\n" +
            "        \"Name\": \"(UTC-04:00) Atlantic Time (Canada)\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"Id\": \"SA Western Standard Time\",\n" +
            "        \"Name\": \"(UTC-04:00) Georgetown, La Paz, Manaus, San Juan\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"Id\": \"Eastern Standard Time\",\n" +
            "        \"Name\": \"(UTC-05:00) Eastern Time (US & Canada)\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"Id\": \"SA Pacific Standard Time\",\n" +
            "        \"Name\": \"(UTC-05:00) Bogota, Lima, Quito, Rio Branco\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"Id\": \"Central Standard Time\",\n" +
            "        \"Name\": \"(UTC-06:00) Central Time (US & Canada)\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"Id\": \"Canada Central Standard Time\",\n" +
            "        \"Name\": \"(UTC-06:00) Saskatchewan\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"Id\": \"Mountain Standard Time\",\n" +
            "        \"Name\": \"(UTC-07:00) Mountain Time (US & Canada)\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"Id\": \"US Mountain Standard Time\",\n" +
            "        \"Name\": \"(UTC-07:00) Arizona\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"Id\": \"Pacific Standard Time\",\n" +
            "        \"Name\": \"(UTC-08:00) Pacific Time (US & Canada)\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Canada\",\n" +
            "    \"IsoAlpha2\": \"CA\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"CHL\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"America/Santiago\",\n" +
            "      \"Pacific/Easter\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"Pacific SA Standard Time\",\n" +
            "        \"Name\": \"(UTC-04:00) Santiago\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"Id\": \"SA Pacific Standard Time\",\n" +
            "        \"Name\": \"(UTC-05:00) Bogota, Lima, Quito, Rio Branco\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Chile\",\n" +
            "    \"IsoAlpha2\": \"CL\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"CHN\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Asia/Shanghai\",\n" +
            "      \"Asia/Urumqi\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"China Standard Time\",\n" +
            "        \"Name\": \"(UTC+08:00) Beijing, Chongqing, Hong Kong, Urumqi\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"Id\": \"Central Asia Standard Time\",\n" +
            "        \"Name\": \"(UTC+06:00) Astana\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"China\",\n" +
            "    \"IsoAlpha2\": \"CN\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"COL\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"America/Bogota\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"SA Pacific Standard Time\",\n" +
            "        \"Name\": \"(UTC-05:00) Bogota, Lima, Quito, Rio Branco\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Colombia\",\n" +
            "    \"IsoAlpha2\": \"CO\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"COD\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Africa/Kinshasa\",\n" +
            "      \"Africa/Lubumbashi\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"W. Central Africa Standard Time\",\n" +
            "        \"Name\": \"(UTC+01:00) West Central Africa\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"Id\": \"South Africa Standard Time\",\n" +
            "        \"Name\": \"(UTC+02:00) Harare, Pretoria\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Congo (DRC)\",\n" +
            "    \"IsoAlpha2\": \"CD\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"CRI\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"America/Costa_Rica\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"Central America Standard Time\",\n" +
            "        \"Name\": \"(UTC-06:00) Central America\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Costa Rica\",\n" +
            "    \"IsoAlpha2\": \"CR\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"CIV\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Africa/Abidjan\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"Greenwich Standard Time\",\n" +
            "        \"Name\": \"(UTC+00:00) Monrovia, Reykjavik\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Côte d’Ivoire\",\n" +
            "    \"IsoAlpha2\": \"CI\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"HRV\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Europe/Zagreb\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"Central Europe Standard Time\",\n" +
            "        \"Name\": \"(UTC+01:00) Belgrade, Bratislava, Budapest, Ljubljana, Prague\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Croatia\",\n" +
            "    \"IsoAlpha2\": \"HR\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"CUB\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"America/Havana\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"Eastern Standard Time\",\n" +
            "        \"Name\": \"(UTC-05:00) Eastern Time (US & Canada)\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Cuba\",\n" +
            "    \"IsoAlpha2\": \"CU\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"CZE\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Europe/Prague\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"Central Europe Standard Time\",\n" +
            "        \"Name\": \"(UTC+01:00) Belgrade, Bratislava, Budapest, Ljubljana, Prague\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Czech Republic\",\n" +
            "    \"IsoAlpha2\": \"CZ\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"DNK\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Europe/Copenhagen\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"Romance Standard Time\",\n" +
            "        \"Name\": \"(UTC+01:00) Brussels, Copenhagen, Madrid, Paris\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Denmark\",\n" +
            "    \"IsoAlpha2\": \"DK\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"DJI\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Africa/Djibouti\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"E. Africa Standard Time\",\n" +
            "        \"Name\": \"(UTC+03:00) Nairobi\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Djibouti\",\n" +
            "    \"IsoAlpha2\": \"DJ\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"DOM\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"America/Santo_Domingo\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"SA Western Standard Time\",\n" +
            "        \"Name\": \"(UTC-04:00) Georgetown, La Paz, Manaus, San Juan\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Dominican Republic\",\n" +
            "    \"IsoAlpha2\": \"DO\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"ECU\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"America/Guayaquil\",\n" +
            "      \"Pacific/Galapagos\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"SA Pacific Standard Time\",\n" +
            "        \"Name\": \"(UTC-05:00) Bogota, Lima, Quito, Rio Branco\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"Id\": \"Central America Standard Time\",\n" +
            "        \"Name\": \"(UTC-06:00) Central America\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Ecuador\",\n" +
            "    \"IsoAlpha2\": \"EC\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"EGY\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Africa/Cairo\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"Egypt Standard Time\",\n" +
            "        \"Name\": \"(UTC+02:00) Cairo\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Egypt\",\n" +
            "    \"IsoAlpha2\": \"EG\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"SLV\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"America/El_Salvador\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"Central America Standard Time\",\n" +
            "        \"Name\": \"(UTC-06:00) Central America\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"El Salvador\",\n" +
            "    \"IsoAlpha2\": \"SV\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"ERI\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Africa/Asmara\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"E. Africa Standard Time\",\n" +
            "        \"Name\": \"(UTC+03:00) Nairobi\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Eritrea\",\n" +
            "    \"IsoAlpha2\": \"ER\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"EST\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Europe/Tallinn\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"FLE Standard Time\",\n" +
            "        \"Name\": \"(UTC+02:00) Helsinki, Kyiv, Riga, Sofia, Tallinn, Vilnius\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Estonia\",\n" +
            "    \"IsoAlpha2\": \"EE\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"ETH\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Africa/Addis_Ababa\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"E. Africa Standard Time\",\n" +
            "        \"Name\": \"(UTC+03:00) Nairobi\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Ethiopia\",\n" +
            "    \"IsoAlpha2\": \"ET\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"FRO\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Atlantic/Faroe\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"GMT Standard Time\",\n" +
            "        \"Name\": \"(UTC+00:00) Dublin, Edinburgh, Lisbon, London\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Faroe Islands\",\n" +
            "    \"IsoAlpha2\": \"FO\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"FIN\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Europe/Helsinki\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"FLE Standard Time\",\n" +
            "        \"Name\": \"(UTC+02:00) Helsinki, Kyiv, Riga, Sofia, Tallinn, Vilnius\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Finland\",\n" +
            "    \"IsoAlpha2\": \"FI\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"FRA\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Europe/Paris\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"Romance Standard Time\",\n" +
            "        \"Name\": \"(UTC+01:00) Brussels, Copenhagen, Madrid, Paris\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"France\",\n" +
            "    \"IsoAlpha2\": \"FR\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"GEO\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Asia/Tbilisi\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"Georgian Standard Time\",\n" +
            "        \"Name\": \"(UTC+04:00) Tbilisi\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Georgia\",\n" +
            "    \"IsoAlpha2\": \"GE\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"DEU\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Europe/Berlin\",\n" +
            "      \"Europe/Busingen\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"W. Europe Standard Time\",\n" +
            "        \"Name\": \"(UTC+01:00) Amsterdam, Berlin, Bern, Rome, Stockholm, Vienna\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Germany\",\n" +
            "    \"IsoAlpha2\": \"DE\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"GRC\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Europe/Athens\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"GTB Standard Time\",\n" +
            "        \"Name\": \"(UTC+02:00) Athens, Bucharest\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Greece\",\n" +
            "    \"IsoAlpha2\": \"GR\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"GRL\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"America/Godthab\",\n" +
            "      \"America/Danmarkshavn\",\n" +
            "      \"America/Scoresbysund\",\n" +
            "      \"America/Thule\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"Greenland Standard Time\",\n" +
            "        \"Name\": \"(UTC-03:00) Greenland\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"Id\": \"UTC\",\n" +
            "        \"Name\": \"UTC\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"Id\": \"Azores Standard Time\",\n" +
            "        \"Name\": \"(UTC-01:00) Azores\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"Id\": \"Atlantic Standard Time\",\n" +
            "        \"Name\": \"(UTC-04:00) Atlantic Time (Canada)\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Greenland\",\n" +
            "    \"IsoAlpha2\": \"GL\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"GTM\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"America/Guatemala\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"Central America Standard Time\",\n" +
            "        \"Name\": \"(UTC-06:00) Central America\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Guatemala\",\n" +
            "    \"IsoAlpha2\": \"GT\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"HTI\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"America/Port-au-Prince\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"Eastern Standard Time\",\n" +
            "        \"Name\": \"(UTC-05:00) Eastern Time (US & Canada)\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Haiti\",\n" +
            "    \"IsoAlpha2\": \"HT\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"HND\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"America/Tegucigalpa\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"Central America Standard Time\",\n" +
            "        \"Name\": \"(UTC-06:00) Central America\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Honduras\",\n" +
            "    \"IsoAlpha2\": \"HN\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"HKG\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Asia/Hong_Kong\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"China Standard Time\",\n" +
            "        \"Name\": \"(UTC+08:00) Beijing, Chongqing, Hong Kong, Urumqi\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Hong Kong SAR\",\n" +
            "    \"IsoAlpha2\": \"HK\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"HUN\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Europe/Budapest\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"Central Europe Standard Time\",\n" +
            "        \"Name\": \"(UTC+01:00) Belgrade, Bratislava, Budapest, Ljubljana, Prague\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Hungary\",\n" +
            "    \"IsoAlpha2\": \"HU\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"ISL\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Atlantic/Reykjavik\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"Greenwich Standard Time\",\n" +
            "        \"Name\": \"(UTC+00:00) Monrovia, Reykjavik\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Iceland\",\n" +
            "    \"IsoAlpha2\": \"IS\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"IND\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Asia/Kolkata\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"India Standard Time\",\n" +
            "        \"Name\": \"(UTC+05:30) Chennai, Kolkata, Mumbai, New Delhi\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"India\",\n" +
            "    \"IsoAlpha2\": \"IN\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"IDN\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Asia/Jakarta\",\n" +
            "      \"Asia/Pontianak\",\n" +
            "      \"Asia/Makassar\",\n" +
            "      \"Asia/Jayapura\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"SE Asia Standard Time\",\n" +
            "        \"Name\": \"(UTC+07:00) Bangkok, Hanoi, Jakarta\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"Id\": \"Singapore Standard Time\",\n" +
            "        \"Name\": \"(UTC+08:00) Kuala Lumpur, Singapore\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"Id\": \"Tokyo Standard Time\",\n" +
            "        \"Name\": \"(UTC+09:00) Osaka, Sapporo, Tokyo\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Indonesia\",\n" +
            "    \"IsoAlpha2\": \"ID\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"IRN\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Asia/Tehran\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"Iran Standard Time\",\n" +
            "        \"Name\": \"(UTC+03:30) Tehran\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Iran\",\n" +
            "    \"IsoAlpha2\": \"IR\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"IRQ\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Asia/Baghdad\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"Arabic Standard Time\",\n" +
            "        \"Name\": \"(UTC+03:00) Baghdad\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Iraq\",\n" +
            "    \"IsoAlpha2\": \"IQ\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"IRL\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Europe/Dublin\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"GMT Standard Time\",\n" +
            "        \"Name\": \"(UTC+00:00) Dublin, Edinburgh, Lisbon, London\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Ireland\",\n" +
            "    \"IsoAlpha2\": \"IE\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"ISR\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Asia/Jerusalem\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"Israel Standard Time\",\n" +
            "        \"Name\": \"(UTC+02:00) Jerusalem\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Israel\",\n" +
            "    \"IsoAlpha2\": \"IL\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"ITA\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Europe/Rome\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"W. Europe Standard Time\",\n" +
            "        \"Name\": \"(UTC+01:00) Amsterdam, Berlin, Bern, Rome, Stockholm, Vienna\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Italy\",\n" +
            "    \"IsoAlpha2\": \"IT\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"JAM\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"America/Jamaica\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"SA Pacific Standard Time\",\n" +
            "        \"Name\": \"(UTC-05:00) Bogota, Lima, Quito, Rio Branco\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Jamaica\",\n" +
            "    \"IsoAlpha2\": \"JM\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"JPN\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Asia/Tokyo\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"Tokyo Standard Time\",\n" +
            "        \"Name\": \"(UTC+09:00) Osaka, Sapporo, Tokyo\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Japan\",\n" +
            "    \"IsoAlpha2\": \"JP\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"JOR\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Asia/Amman\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"Jordan Standard Time\",\n" +
            "        \"Name\": \"(UTC+02:00) Amman\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Jordan\",\n" +
            "    \"IsoAlpha2\": \"JO\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"KAZ\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Asia/Almaty\",\n" +
            "      \"Asia/Qyzylorda\",\n" +
            "      \"Asia/Aqtobe\",\n" +
            "      \"Asia/Aqtau\",\n" +
            "      \"Asia/Oral\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"Central Asia Standard Time\",\n" +
            "        \"Name\": \"(UTC+06:00) Astana\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"Id\": \"West Asia Standard Time\",\n" +
            "        \"Name\": \"(UTC+05:00) Ashgabat, Tashkent\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Kazakhstan\",\n" +
            "    \"IsoAlpha2\": \"KZ\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"KEN\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Africa/Nairobi\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"E. Africa Standard Time\",\n" +
            "        \"Name\": \"(UTC+03:00) Nairobi\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Kenya\",\n" +
            "    \"IsoAlpha2\": \"KE\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"KOR\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Asia/Seoul\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"Korea Standard Time\",\n" +
            "        \"Name\": \"(UTC+09:00) Seoul\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Korea\",\n" +
            "    \"IsoAlpha2\": \"KR\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"KWT\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Asia/Kuwait\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"Arab Standard Time\",\n" +
            "        \"Name\": \"(UTC+03:00) Kuwait, Riyadh\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Kuwait\",\n" +
            "    \"IsoAlpha2\": \"KW\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"KGZ\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Asia/Bishkek\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"Central Asia Standard Time\",\n" +
            "        \"Name\": \"(UTC+06:00) Astana\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Kyrgyzstan\",\n" +
            "    \"IsoAlpha2\": \"KG\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"LAO\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Asia/Vientiane\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"SE Asia Standard Time\",\n" +
            "        \"Name\": \"(UTC+07:00) Bangkok, Hanoi, Jakarta\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Laos\",\n" +
            "    \"IsoAlpha2\": \"LA\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"LVA\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Europe/Riga\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"FLE Standard Time\",\n" +
            "        \"Name\": \"(UTC+02:00) Helsinki, Kyiv, Riga, Sofia, Tallinn, Vilnius\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Latvia\",\n" +
            "    \"IsoAlpha2\": \"LV\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"LBN\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Asia/Beirut\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"Middle East Standard Time\",\n" +
            "        \"Name\": \"(UTC+02:00) Beirut\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Lebanon\",\n" +
            "    \"IsoAlpha2\": \"LB\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"LBY\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Africa/Tripoli\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"Libya Standard Time\",\n" +
            "        \"Name\": \"(UTC+02:00) Tripoli\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Libya\",\n" +
            "    \"IsoAlpha2\": \"LY\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"LIE\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Europe/Vaduz\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"W. Europe Standard Time\",\n" +
            "        \"Name\": \"(UTC+01:00) Amsterdam, Berlin, Bern, Rome, Stockholm, Vienna\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Liechtenstein\",\n" +
            "    \"IsoAlpha2\": \"LI\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"LTU\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Europe/Vilnius\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"FLE Standard Time\",\n" +
            "        \"Name\": \"(UTC+02:00) Helsinki, Kyiv, Riga, Sofia, Tallinn, Vilnius\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Lithuania\",\n" +
            "    \"IsoAlpha2\": \"LT\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"LUX\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Europe/Luxembourg\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"W. Europe Standard Time\",\n" +
            "        \"Name\": \"(UTC+01:00) Amsterdam, Berlin, Bern, Rome, Stockholm, Vienna\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Luxembourg\",\n" +
            "    \"IsoAlpha2\": \"LU\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"MAC\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Asia/Macau\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"China Standard Time\",\n" +
            "        \"Name\": \"(UTC+08:00) Beijing, Chongqing, Hong Kong, Urumqi\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Macao SAR\",\n" +
            "    \"IsoAlpha2\": \"MO\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"MKD\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Europe/Skopje\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"Central Europe Standard Time\",\n" +
            "        \"Name\": \"(UTC+01:00) Belgrade, Bratislava, Budapest, Ljubljana, Prague\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Macedonia, FYRO\",\n" +
            "    \"IsoAlpha2\": \"MK\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"MYS\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Asia/Kuala_Lumpur\",\n" +
            "      \"Asia/Kuching\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"Singapore Standard Time\",\n" +
            "        \"Name\": \"(UTC+08:00) Kuala Lumpur, Singapore\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Malaysia\",\n" +
            "    \"IsoAlpha2\": \"MY\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"MDV\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Indian/Maldives\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"West Asia Standard Time\",\n" +
            "        \"Name\": \"(UTC+05:00) Ashgabat, Tashkent\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Maldives\",\n" +
            "    \"IsoAlpha2\": \"MV\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"MLI\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Africa/Bamako\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"Greenwich Standard Time\",\n" +
            "        \"Name\": \"(UTC+00:00) Monrovia, Reykjavik\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Mali\",\n" +
            "    \"IsoAlpha2\": \"ML\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"MLT\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Europe/Malta\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"W. Europe Standard Time\",\n" +
            "        \"Name\": \"(UTC+01:00) Amsterdam, Berlin, Bern, Rome, Stockholm, Vienna\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Malta\",\n" +
            "    \"IsoAlpha2\": \"MT\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"MEX\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"America/Mexico_City\",\n" +
            "      \"America/Cancun\",\n" +
            "      \"America/Merida\",\n" +
            "      \"America/Monterrey\",\n" +
            "      \"America/Matamoros\",\n" +
            "      \"America/Mazatlan\",\n" +
            "      \"America/Chihuahua\",\n" +
            "      \"America/Ojinaga\",\n" +
            "      \"America/Hermosillo\",\n" +
            "      \"America/Tijuana\",\n" +
            "      \"America/Bahia_Banderas\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"Central Standard Time (Mexico)\",\n" +
            "        \"Name\": \"(UTC-06:00) Guadalajara, Mexico City, Monterrey\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"Id\": \"Eastern Standard Time (Mexico)\",\n" +
            "        \"Name\": \"(UTC-05:00) Chetumal\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"Id\": \"Central Standard Time\",\n" +
            "        \"Name\": \"(UTC-06:00) Central Time (US & Canada)\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"Id\": \"Mountain Standard Time (Mexico)\",\n" +
            "        \"Name\": \"(UTC-07:00) Chihuahua, La Paz, Mazatlan\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"Id\": \"Mountain Standard Time\",\n" +
            "        \"Name\": \"(UTC-07:00) Mountain Time (US & Canada)\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"Id\": \"US Mountain Standard Time\",\n" +
            "        \"Name\": \"(UTC-07:00) Arizona\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"Id\": \"Pacific Standard Time\",\n" +
            "        \"Name\": \"(UTC-08:00) Pacific Time (US & Canada)\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Mexico\",\n" +
            "    \"IsoAlpha2\": \"MX\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"MDA\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Europe/Chisinau\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"E. Europe Standard Time\",\n" +
            "        \"Name\": \"(UTC+02:00) Chisinau\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Moldova\",\n" +
            "    \"IsoAlpha2\": \"MD\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"MCO\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Europe/Monaco\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"W. Europe Standard Time\",\n" +
            "        \"Name\": \"(UTC+01:00) Amsterdam, Berlin, Bern, Rome, Stockholm, Vienna\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Monaco\",\n" +
            "    \"IsoAlpha2\": \"MC\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"MNG\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Asia/Ulaanbaatar\",\n" +
            "      \"Asia/Hovd\",\n" +
            "      \"Asia/Choibalsan\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"Ulaanbaatar Standard Time\",\n" +
            "        \"Name\": \"(UTC+08:00) Ulaanbaatar\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Mongolia\",\n" +
            "    \"IsoAlpha2\": \"MN\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"MNE\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Europe/Podgorica\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"Central Europe Standard Time\",\n" +
            "        \"Name\": \"(UTC+01:00) Belgrade, Bratislava, Budapest, Ljubljana, Prague\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Montenegro\",\n" +
            "    \"IsoAlpha2\": \"ME\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"MAR\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Africa/Casablanca\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"Morocco Standard Time\",\n" +
            "        \"Name\": \"(UTC+00:00) Casablanca\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Morocco\",\n" +
            "    \"IsoAlpha2\": \"MA\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"MMR\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Asia/Rangoon\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"Myanmar Standard Time\",\n" +
            "        \"Name\": \"(UTC+06:30) Yangon (Rangoon)\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Myanmar\",\n" +
            "    \"IsoAlpha2\": \"MM\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"NPL\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Asia/Kathmandu\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"Nepal Standard Time\",\n" +
            "        \"Name\": \"(UTC+05:45) Kathmandu\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Nepal\",\n" +
            "    \"IsoAlpha2\": \"NP\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"NLD\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Europe/Amsterdam\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"W. Europe Standard Time\",\n" +
            "        \"Name\": \"(UTC+01:00) Amsterdam, Berlin, Bern, Rome, Stockholm, Vienna\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Netherlands\",\n" +
            "    \"IsoAlpha2\": \"NL\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"NZL\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Pacific/Auckland\",\n" +
            "      \"Pacific/Chatham\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"New Zealand Standard Time\",\n" +
            "        \"Name\": \"(UTC+12:00) Auckland, Wellington\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"New Zealand\",\n" +
            "    \"IsoAlpha2\": \"NZ\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"NIC\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"America/Managua\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"Central America Standard Time\",\n" +
            "        \"Name\": \"(UTC-06:00) Central America\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Nicaragua\",\n" +
            "    \"IsoAlpha2\": \"NI\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"NGA\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Africa/Lagos\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"W. Central Africa Standard Time\",\n" +
            "        \"Name\": \"(UTC+01:00) West Central Africa\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Nigeria\",\n" +
            "    \"IsoAlpha2\": \"NG\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"NOR\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Europe/Oslo\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"W. Europe Standard Time\",\n" +
            "        \"Name\": \"(UTC+01:00) Amsterdam, Berlin, Bern, Rome, Stockholm, Vienna\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Norway\",\n" +
            "    \"IsoAlpha2\": \"NO\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"OMN\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Asia/Muscat\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"Arabian Standard Time\",\n" +
            "        \"Name\": \"(UTC+04:00) Abu Dhabi, Muscat\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Oman\",\n" +
            "    \"IsoAlpha2\": \"OM\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"PAK\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Asia/Karachi\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"Pakistan Standard Time\",\n" +
            "        \"Name\": \"(UTC+05:00) Islamabad, Karachi\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Pakistan\",\n" +
            "    \"IsoAlpha2\": \"PK\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"PAN\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"America/Panama\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"SA Pacific Standard Time\",\n" +
            "        \"Name\": \"(UTC-05:00) Bogota, Lima, Quito, Rio Branco\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Panama\",\n" +
            "    \"IsoAlpha2\": \"PA\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"PRY\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"America/Asuncion\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"Paraguay Standard Time\",\n" +
            "        \"Name\": \"(UTC-04:00) Asuncion\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Paraguay\",\n" +
            "    \"IsoAlpha2\": \"PY\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"PER\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"America/Lima\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"SA Pacific Standard Time\",\n" +
            "        \"Name\": \"(UTC-05:00) Bogota, Lima, Quito, Rio Branco\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Peru\",\n" +
            "    \"IsoAlpha2\": \"PE\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"PHL\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Asia/Manila\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"Singapore Standard Time\",\n" +
            "        \"Name\": \"(UTC+08:00) Kuala Lumpur, Singapore\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Philippines\",\n" +
            "    \"IsoAlpha2\": \"PH\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"POL\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Europe/Warsaw\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"Central European Standard Time\",\n" +
            "        \"Name\": \"(UTC+01:00) Sarajevo, Skopje, Warsaw, Zagreb\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Poland\",\n" +
            "    \"IsoAlpha2\": \"PL\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"PRT\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Europe/Lisbon\",\n" +
            "      \"Atlantic/Madeira\",\n" +
            "      \"Atlantic/Azores\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"GMT Standard Time\",\n" +
            "        \"Name\": \"(UTC+00:00) Dublin, Edinburgh, Lisbon, London\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"Id\": \"Azores Standard Time\",\n" +
            "        \"Name\": \"(UTC-01:00) Azores\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Portugal\",\n" +
            "    \"IsoAlpha2\": \"PT\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"PRI\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"America/Puerto_Rico\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"SA Western Standard Time\",\n" +
            "        \"Name\": \"(UTC-04:00) Georgetown, La Paz, Manaus, San Juan\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Puerto Rico\",\n" +
            "    \"IsoAlpha2\": \"PR\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"QAT\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Asia/Qatar\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"Arab Standard Time\",\n" +
            "        \"Name\": \"(UTC+03:00) Kuwait, Riyadh\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Qatar\",\n" +
            "    \"IsoAlpha2\": \"QA\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"REU\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Indian/Reunion\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"Mauritius Standard Time\",\n" +
            "        \"Name\": \"(UTC+04:00) Port Louis\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Réunion\",\n" +
            "    \"IsoAlpha2\": \"RE\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"ROU\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Europe/Bucharest\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"GTB Standard Time\",\n" +
            "        \"Name\": \"(UTC+02:00) Athens, Bucharest\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Romania\",\n" +
            "    \"IsoAlpha2\": \"RO\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"RUS\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Europe/Kaliningrad\",\n" +
            "      \"Europe/Moscow\",\n" +
            "      \"Europe/Simferopol\",\n" +
            "      \"Europe/Volgograd\",\n" +
            "      \"Europe/Astrakhan\",\n" +
            "      \"Europe/Samara\",\n" +
            "      \"Europe/Ulyanovsk\",\n" +
            "      \"Asia/Yekaterinburg\",\n" +
            "      \"Asia/Omsk\",\n" +
            "      \"Asia/Novosibirsk\",\n" +
            "      \"Asia/Barnaul\",\n" +
            "      \"Asia/Novokuznetsk\",\n" +
            "      \"Asia/Krasnoyarsk\",\n" +
            "      \"Asia/Irkutsk\",\n" +
            "      \"Asia/Chita\",\n" +
            "      \"Asia/Yakutsk\",\n" +
            "      \"Asia/Khandyga\",\n" +
            "      \"Asia/Vladivostok\",\n" +
            "      \"Asia/Ust-Nera\",\n" +
            "      \"Asia/Magadan\",\n" +
            "      \"Asia/Sakhalin\",\n" +
            "      \"Asia/Srednekolymsk\",\n" +
            "      \"Asia/Kamchatka\",\n" +
            "      \"Asia/Anadyr\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"Kaliningrad Standard Time\",\n" +
            "        \"Name\": \"(UTC+02:00) Kaliningrad\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"Id\": \"Russian Standard Time\",\n" +
            "        \"Name\": \"(UTC+03:00) Moscow, St. Petersburg, Volgograd\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"Id\": \"Russia Time Zone 3\",\n" +
            "        \"Name\": \"(UTC+04:00) Izhevsk, Samara\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"Id\": \"Ekaterinburg Standard Time\",\n" +
            "        \"Name\": \"(UTC+05:00) Ekaterinburg\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"Id\": \"N. Central Asia Standard Time\",\n" +
            "        \"Name\": \"(UTC+07:00) Novosibirsk\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"Id\": \"North Asia Standard Time\",\n" +
            "        \"Name\": \"(UTC+07:00) Krasnoyarsk\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"Id\": \"North Asia East Standard Time\",\n" +
            "        \"Name\": \"(UTC+08:00) Irkutsk\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"Id\": \"Yakutsk Standard Time\",\n" +
            "        \"Name\": \"(UTC+09:00) Yakutsk\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"Id\": \"Vladivostok Standard Time\",\n" +
            "        \"Name\": \"(UTC+10:00) Vladivostok\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"Id\": \"Magadan Standard Time\",\n" +
            "        \"Name\": \"(UTC+11:00) Magadan\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"Id\": \"Russia Time Zone 10\",\n" +
            "        \"Name\": \"(UTC+11:00) Chokurdakh\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"Id\": \"Russia Time Zone 11\",\n" +
            "        \"Name\": \"(UTC+12:00) Anadyr, Petropavlovsk-Kamchatsky\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Russia\",\n" +
            "    \"IsoAlpha2\": \"RU\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"RWA\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Africa/Kigali\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"South Africa Standard Time\",\n" +
            "        \"Name\": \"(UTC+02:00) Harare, Pretoria\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Rwanda\",\n" +
            "    \"IsoAlpha2\": \"RW\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"SAU\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Asia/Riyadh\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"Arab Standard Time\",\n" +
            "        \"Name\": \"(UTC+03:00) Kuwait, Riyadh\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Saudi Arabia\",\n" +
            "    \"IsoAlpha2\": \"SA\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"SEN\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Africa/Dakar\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"Greenwich Standard Time\",\n" +
            "        \"Name\": \"(UTC+00:00) Monrovia, Reykjavik\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Senegal\",\n" +
            "    \"IsoAlpha2\": \"SN\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"SRB\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Europe/Belgrade\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"Central Europe Standard Time\",\n" +
            "        \"Name\": \"(UTC+01:00) Belgrade, Bratislava, Budapest, Ljubljana, Prague\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Serbia\",\n" +
            "    \"IsoAlpha2\": \"RS\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"SGP\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Asia/Singapore\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"Singapore Standard Time\",\n" +
            "        \"Name\": \"(UTC+08:00) Kuala Lumpur, Singapore\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Singapore\",\n" +
            "    \"IsoAlpha2\": \"SG\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"SVK\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Europe/Bratislava\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"Central Europe Standard Time\",\n" +
            "        \"Name\": \"(UTC+01:00) Belgrade, Bratislava, Budapest, Ljubljana, Prague\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Slovakia\",\n" +
            "    \"IsoAlpha2\": \"SK\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"SVN\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Europe/Ljubljana\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"Central Europe Standard Time\",\n" +
            "        \"Name\": \"(UTC+01:00) Belgrade, Bratislava, Budapest, Ljubljana, Prague\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Slovenia\",\n" +
            "    \"IsoAlpha2\": \"SI\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"SOM\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Africa/Mogadishu\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"E. Africa Standard Time\",\n" +
            "        \"Name\": \"(UTC+03:00) Nairobi\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Somalia\",\n" +
            "    \"IsoAlpha2\": \"SO\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"ZAF\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Africa/Johannesburg\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"South Africa Standard Time\",\n" +
            "        \"Name\": \"(UTC+02:00) Harare, Pretoria\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"South Africa\",\n" +
            "    \"IsoAlpha2\": \"ZA\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"ESP\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Europe/Madrid\",\n" +
            "      \"Africa/Ceuta\",\n" +
            "      \"Atlantic/Canary\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"Romance Standard Time\",\n" +
            "        \"Name\": \"(UTC+01:00) Brussels, Copenhagen, Madrid, Paris\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"Id\": \"GMT Standard Time\",\n" +
            "        \"Name\": \"(UTC+00:00) Dublin, Edinburgh, Lisbon, London\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Spain\",\n" +
            "    \"IsoAlpha2\": \"ES\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"LKA\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Asia/Colombo\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"Sri Lanka Standard Time\",\n" +
            "        \"Name\": \"(UTC+05:30) Sri Jayawardenepura\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Sri Lanka\",\n" +
            "    \"IsoAlpha2\": \"LK\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"SWE\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Europe/Stockholm\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"W. Europe Standard Time\",\n" +
            "        \"Name\": \"(UTC+01:00) Amsterdam, Berlin, Bern, Rome, Stockholm, Vienna\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Sweden\",\n" +
            "    \"IsoAlpha2\": \"SE\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"CHE\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Europe/Zurich\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"W. Europe Standard Time\",\n" +
            "        \"Name\": \"(UTC+01:00) Amsterdam, Berlin, Bern, Rome, Stockholm, Vienna\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Switzerland\",\n" +
            "    \"IsoAlpha2\": \"CH\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"SYR\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Asia/Damascus\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"Syria Standard Time\",\n" +
            "        \"Name\": \"(UTC+02:00) Damascus\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Syria\",\n" +
            "    \"IsoAlpha2\": \"SY\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"TWN\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Asia/Taipei\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"Taipei Standard Time\",\n" +
            "        \"Name\": \"(UTC+08:00) Taipei\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Taiwan\",\n" +
            "    \"IsoAlpha2\": \"TW\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"TJK\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Asia/Dushanbe\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"West Asia Standard Time\",\n" +
            "        \"Name\": \"(UTC+05:00) Ashgabat, Tashkent\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Tajikistan\",\n" +
            "    \"IsoAlpha2\": \"TJ\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"THA\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Asia/Bangkok\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"SE Asia Standard Time\",\n" +
            "        \"Name\": \"(UTC+07:00) Bangkok, Hanoi, Jakarta\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Thailand\",\n" +
            "    \"IsoAlpha2\": \"TH\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"TTO\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"America/Port_of_Spain\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"SA Western Standard Time\",\n" +
            "        \"Name\": \"(UTC-04:00) Georgetown, La Paz, Manaus, San Juan\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Trinidad and Tobago\",\n" +
            "    \"IsoAlpha2\": \"TT\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"TUN\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Africa/Tunis\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"W. Central Africa Standard Time\",\n" +
            "        \"Name\": \"(UTC+01:00) West Central Africa\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Tunisia\",\n" +
            "    \"IsoAlpha2\": \"TN\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"TUR\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Europe/Istanbul\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"Turkey Standard Time\",\n" +
            "        \"Name\": \"(UTC+02:00) Istanbul\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Turkey\",\n" +
            "    \"IsoAlpha2\": \"TR\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"TKM\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Asia/Ashgabat\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"West Asia Standard Time\",\n" +
            "        \"Name\": \"(UTC+05:00) Ashgabat, Tashkent\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Turkmenistan\",\n" +
            "    \"IsoAlpha2\": \"TM\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"UKR\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Europe/Kiev\",\n" +
            "      \"Europe/Uzhgorod\",\n" +
            "      \"Europe/Zaporozhye\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"FLE Standard Time\",\n" +
            "        \"Name\": \"(UTC+02:00) Helsinki, Kyiv, Riga, Sofia, Tallinn, Vilnius\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Ukraine\",\n" +
            "    \"IsoAlpha2\": \"UA\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"ARE\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Asia/Dubai\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"Arabian Standard Time\",\n" +
            "        \"Name\": \"(UTC+04:00) Abu Dhabi, Muscat\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"United Arab Emirates\",\n" +
            "    \"IsoAlpha2\": \"AE\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"GBR\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Europe/London\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"GMT Standard Time\",\n" +
            "        \"Name\": \"(UTC+00:00) Dublin, Edinburgh, Lisbon, London\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"United Kingdom\",\n" +
            "    \"IsoAlpha2\": \"GB\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"USA\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"America/New_York\",\n" +
            "      \"America/Detroit\",\n" +
            "      \"America/Kentucky/Louisville\",\n" +
            "      \"America/Kentucky/Monticello\",\n" +
            "      \"America/Indiana/Indianapolis\",\n" +
            "      \"America/Indiana/Vincennes\",\n" +
            "      \"America/Indiana/Winamac\",\n" +
            "      \"America/Indiana/Marengo\",\n" +
            "      \"America/Indiana/Petersburg\",\n" +
            "      \"America/Indiana/Vevay\",\n" +
            "      \"America/Chicago\",\n" +
            "      \"America/Indiana/Tell_City\",\n" +
            "      \"America/Indiana/Knox\",\n" +
            "      \"America/Menominee\",\n" +
            "      \"America/North_Dakota/Center\",\n" +
            "      \"America/North_Dakota/New_Salem\",\n" +
            "      \"America/North_Dakota/Beulah\",\n" +
            "      \"America/Denver\",\n" +
            "      \"America/Boise\",\n" +
            "      \"America/Phoenix\",\n" +
            "      \"America/Los_Angeles\",\n" +
            "      \"America/Anchorage\",\n" +
            "      \"America/Juneau\",\n" +
            "      \"America/Sitka\",\n" +
            "      \"America/Metlakatla\",\n" +
            "      \"America/Yakutat\",\n" +
            "      \"America/Nome\",\n" +
            "      \"America/Adak\",\n" +
            "      \"Pacific/Honolulu\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"Eastern Standard Time\",\n" +
            "        \"Name\": \"(UTC-05:00) Eastern Time (US & Canada)\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"Id\": \"US Eastern Standard Time\",\n" +
            "        \"Name\": \"(UTC-05:00) Indiana (East)\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"Id\": \"Central Standard Time\",\n" +
            "        \"Name\": \"(UTC-06:00) Central Time (US & Canada)\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"Id\": \"Mountain Standard Time\",\n" +
            "        \"Name\": \"(UTC-07:00) Mountain Time (US & Canada)\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"Id\": \"US Mountain Standard Time\",\n" +
            "        \"Name\": \"(UTC-07:00) Arizona\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"Id\": \"Pacific Standard Time\",\n" +
            "        \"Name\": \"(UTC-08:00) Pacific Time (US & Canada)\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"Id\": \"Alaskan Standard Time\",\n" +
            "        \"Name\": \"(UTC-09:00) Alaska\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"Id\": \"Hawaiian Standard Time\",\n" +
            "        \"Name\": \"(UTC-10:00) Hawaii\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"United States\",\n" +
            "    \"IsoAlpha2\": \"US\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"URY\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"America/Montevideo\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"Montevideo Standard Time\",\n" +
            "        \"Name\": \"(UTC-03:00) Montevideo\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Uruguay\",\n" +
            "    \"IsoAlpha2\": \"UY\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"UZB\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Asia/Samarkand\",\n" +
            "      \"Asia/Tashkent\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"West Asia Standard Time\",\n" +
            "        \"Name\": \"(UTC+05:00) Ashgabat, Tashkent\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Uzbekistan\",\n" +
            "    \"IsoAlpha2\": \"UZ\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"VEN\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"America/Caracas\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"Venezuela Standard Time\",\n" +
            "        \"Name\": \"(UTC-04:00) Caracas\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Venezuela\",\n" +
            "    \"IsoAlpha2\": \"VE\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"VNM\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Asia/Ho_Chi_Minh\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"SE Asia Standard Time\",\n" +
            "        \"Name\": \"(UTC+07:00) Bangkok, Hanoi, Jakarta\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Vietnam\",\n" +
            "    \"IsoAlpha2\": \"VN\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"YEM\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Asia/Aden\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"Arab Standard Time\",\n" +
            "        \"Name\": \"(UTC+03:00) Kuwait, Riyadh\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Yemen\",\n" +
            "    \"IsoAlpha2\": \"YE\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"IsoAlpha3\": \"ZWE\",\n" +
            "    \"TimeZones\": [\n" +
            "      \"Africa/Harare\"\n" +
            "    ],\n" +
            "    \"WindowsTimeZones\": [\n" +
            "      {\n" +
            "        \"Id\": \"South Africa Standard Time\",\n" +
            "        \"Name\": \"(UTC+02:00) Harare, Pretoria\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"CountryName\": \"Zimbabwe\",\n" +
            "    \"IsoAlpha2\": \"ZW\"\n" +
            "  }\n" +
            "]";

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(TTL.this);
    }

//            public static final String  PUBLISHABLE_KEY = "pk_test_m2095bSj8vVA0n55nBjcBRDH";
    public static final String  PUBLISHABLE_KEY = "pk_live_cZQHiFEshZyEHQrIzyqc2rA9";
    public static final String APPLICATION_ID = "RKNck9SdN6sqcznBvy5lqnN2ln1FrrSabNcq8YEK";
    public static final String CLIENT_KEY = "zWtkaYFS0Ia91jKkgmIHJql30cARcrDmKUGAXLTY";
    public static final String BACK4PAPP_API = "https://parseapi.back4app.com/";


    @Override
    public void onCreate() {
        super.onCreate();
        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/GothamBookRegular.ttf");
        Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
                .applicationId(APPLICATION_ID)
                .clientKey(CLIENT_KEY)
                .server(BACK4PAPP_API).build());


        locale = getApplicationContext().getResources().getConfiguration().locale.getCountry();

        mInstance = this;




        AnalyticsTrackers.initialize(this);

        AnalyticsTrackers.getInstance().get(AnalyticsTrackers.Target.APP);



    }

    public static synchronized TTL getInstance() {
        return mInstance;
    }

    public synchronized Tracker getGoogleAnalyticsTracker() {
        AnalyticsTrackers analyticsTrackers = AnalyticsTrackers.getInstance();
        return analyticsTrackers.get(AnalyticsTrackers.Target.APP);
    }

    /***
     * Tracking screen view
     *
     * @param screenName screen name to be displayed on GA dashboard
     */
    public void trackScreenView(String screenName) {
        Tracker t = getGoogleAnalyticsTracker();

        // Set screen name.
        t.setScreenName(screenName);

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());

        GoogleAnalytics.getInstance(this).dispatchLocalHits();
    }

    /***
     * Tracking exception
     *
     * @param e exception to be tracked
     */
    public void trackException(Exception e) {
        if (e != null) {
            Tracker t = getGoogleAnalyticsTracker();

            t.send(new HitBuilders.ExceptionBuilder()
                    .setDescription(
                            new StandardExceptionParser(this, null)
                                    .getDescription(Thread.currentThread().getName(), e))
                    .setFatal(false)
                    .build()
            );
        }
    }

    /***
     * Tracking event
     *
     * @param category event category
     * @param action   action of the event
     * @param label    label
     */
    public void trackEvent(String category, String action, String label) {
        Tracker t = getGoogleAnalyticsTracker();

        // Build and send an Event.
        t.send(new HitBuilders.EventBuilder().setCategory(category).setAction(action).setLabel(label).build());
    }

    public static String getUserCountry(Context context) {
        try {
            final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            final String simCountry = tm.getSimCountryIso();
            if (simCountry != null && simCountry.length() == 2) { // SIM country code is available
                return simCountry.toLowerCase(Locale.US);
            }
            else if (tm.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA) { // device is not 3G (would be unreliable)
                String networkCountry = tm.getNetworkCountryIso();
                if (networkCountry != null && networkCountry.length() == 2) { // network country code is available
                    return networkCountry.toLowerCase(Locale.US);
                }
            }
        }
        catch (Exception e) { }
        return null;
    }
}
