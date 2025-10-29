package com.divisync.data

data class Currency(
    val code: String,
    val name: String,
    val flag: String
)

object CurrencyData {
    val currencies = listOf(
        // Américas
        Currency("USD", "Dólar estadounidense", "🇺🇸"),
        Currency("EUR", "Euro", "🇪🇺"),
        Currency("GBP", "Libra esterlina", "🇬🇧"),
        Currency("COP", "Peso colombiano", "🇨🇴"),
        Currency("MXN", "Peso mexicano", "🇲🇽"),
        Currency("BRL", "Real brasileño", "🇧🇷"),
        Currency("ARS", "Peso argentino", "🇦🇷"),
        Currency("CLP", "Peso chileno", "🇨🇱"),
        Currency("PEN", "Sol peruano", "🇵🇪"),
        Currency("CAD", "Dólar canadiense", "🇨🇦"),
        Currency("UYU", "Peso uruguayo", "🇺🇾"),
        Currency("BOB", "Boliviano", "🇧🇴"),
        Currency("PYG", "Guaraní paraguayo", "🇵🇾"),
        Currency("CRC", "Colón costarricense", "🇨🇷"),
        Currency("GTQ", "Quetzal guatemalteco", "🇬🇹"),
        Currency("DOP", "Peso dominicano", "🇩🇴"),
        Currency("CUP", "Peso cubano", "🇨🇺"),
        Currency("PAB", "Balboa panameño", "🇵🇦"),
        Currency("NIO", "Córdoba nicaragüense", "🇳🇮"),
        Currency("HNL", "Lempira hondureño", "🇭🇳"),
        
        // Europa
        Currency("CHF", "Franco suizo", "🇨🇭"),
        Currency("SEK", "Corona sueca", "🇸🇪"),
        Currency("NOK", "Corona noruega", "🇳🇴"),
        Currency("DKK", "Corona danesa", "🇩🇰"),
        Currency("PLN", "Zloty polaco", "🇵🇱"),
        Currency("CZK", "Corona checa", "🇨🇿"),
        Currency("HUF", "Forinto húngaro", "🇭🇺"),
        Currency("RON", "Leu rumano", "🇷🇴"),
        Currency("BGN", "Lev búlgaro", "🇧🇬"),
        Currency("HRK", "Kuna croata", "🇭🇷"),
        Currency("RUB", "Rublo ruso", "🇷🇺"),
        Currency("TRY", "Lira turca", "🇹🇷"),
        Currency("UAH", "Grivna ucraniana", "🇺🇦"),
        
        // Asia
        Currency("JPY", "Yen japonés", "🇯🇵"),
        Currency("CNY", "Yuan chino", "🇨🇳"),
        Currency("KRW", "Won surcoreano", "🇰🇷"),
        Currency("INR", "Rupia india", "🇮🇳"),
        Currency("IDR", "Rupia indonesia", "🇮🇩"),
        Currency("THB", "Baht tailandés", "🇹🇭"),
        Currency("MYR", "Ringgit malayo", "🇲🇾"),
        Currency("SGD", "Dólar singapurense", "🇸🇬"),
        Currency("PHP", "Peso filipino", "🇵🇭"),
        Currency("VND", "Dong vietnamita", "🇻🇳"),
        Currency("PKR", "Rupia pakistaní", "🇵🇰"),
        Currency("BDT", "Taka bangladesí", "🇧🇩"),
        Currency("LKR", "Rupia esrilanquesa", "🇱🇰"),
        Currency("NPR", "Rupia nepalí", "🇳🇵"),
        Currency("MMK", "Kyat birmano", "🇲🇲"),
        Currency("KHR", "Riel camboyano", "🇰🇭"),
        Currency("LAK", "Kip laosiano", "🇱🇦"),
        Currency("HKD", "Dólar hongkonés", "🇭🇰"),
        Currency("TWD", "Dólar taiwanés", "🇹🇼"),
        
        // Oceanía
        Currency("AUD", "Dólar australiano", "🇦🇺"),
        Currency("NZD", "Dólar neozelandés", "🇳🇿"),
        Currency("FJD", "Dólar fiyiano", "🇫🇯"),
        
        // África
        Currency("ZAR", "Rand sudafricano", "🇿🇦"),
        Currency("EGP", "Libra egipcia", "🇪🇬"),
        Currency("NGN", "Naira nigeriana", "🇳🇬"),
        Currency("KES", "Chelín keniano", "🇰🇪"),
        Currency("GHS", "Cedi ghanés", "🇬🇭"),
        Currency("UGX", "Chelín ugandés", "🇺🇬"),
        Currency("TZS", "Chelín tanzano", "🇹🇿"),
        Currency("MAD", "Dirham marroquí", "🇲🇦"),
        Currency("TND", "Dinar tunecino", "🇹🇳"),
        Currency("DZD", "Dinar argelino", "🇩🇿"),
        Currency("AOA", "Kwanza angoleño", "🇦🇴"),
        Currency("XOF", "Franco CFA", "🇸🇳"),
        
        // Medio Oriente
        Currency("AED", "Dirham emiratí", "🇦🇪"),
        Currency("SAR", "Riyal saudí", "🇸🇦"),
        Currency("ILS", "Shekel israelí", "🇮🇱"),
        Currency("QAR", "Riyal qatarí", "🇶🇦"),
        Currency("KWD", "Dinar kuwaití", "🇰🇼"),
        Currency("BHD", "Dinar bahreiní", "🇧🇭"),
        Currency("OMR", "Rial omaní", "🇴🇲"),
        Currency("JOD", "Dinar jordano", "🇯🇴"),
        Currency("LBP", "Libra libanesa", "🇱🇧"),
        Currency("IQD", "Dinar iraquí", "🇮🇶"),
        Currency("IRR", "Rial iraní", "🇮🇷"),
        
        // Criptomonedas y otras
        Currency("BTC", "Bitcoin", "₿"),
        Currency("ETH", "Ethereum", "Ξ"),
        Currency("XAU", "Oro (onza)", "🏆"),
        Currency("XAG", "Plata (onza)", "⚡")
    ).sortedBy { it.name }
    
    fun searchCurrencies(query: String): List<Currency> {
        if (query.isBlank()) return currencies
        val lowercaseQuery = query.lowercase()
        return currencies.filter {
            it.code.lowercase().contains(lowercaseQuery) ||
            it.name.lowercase().contains(lowercaseQuery)
        }
    }
}

