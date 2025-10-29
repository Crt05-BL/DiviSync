package com.divisync.data

import androidx.compose.runtime.mutableStateOf

object LanguageManager {
    var currentLanguage = mutableStateOf("es") // "es", "en", "pt", "fr"
    
    fun setLanguage(lang: String) {
        currentLanguage.value = lang
    }
}

object Strings {
    // Títulos y principales
    val appName = mapOf(
        "es" to "DiviSync",
        "en" to "DiviSync",
        "pt" to "DiviSync",
        "fr" to "DiviSync"
    )
    
    val intelligentConverter = mapOf(
        "es" to "Conversor Inteligente",
        "en" to "Smart Converter",
        "pt" to "Conversor Inteligente",
        "fr" to "Convertisseur Intelligent"
    )
    
    val realTimeRates = mapOf(
        "es" to "Tasas en tiempo real",
        "en" to "Real-time rates",
        "pt" to "Taxas em tempo real",
        "fr" to "Taux en temps réel"
    )
    
    val updating = mapOf(
        "es" to "Actualizando...",
        "en" to "Updating...",
        "pt" to "Atualizando...",
        "fr" to "Mise à jour..."
    )
    
    val youSend = mapOf(
        "es" to "ENVÍAS",
        "en" to "YOU SEND",
        "pt" to "VOCÊ ENVIA",
        "fr" to "VOUS ENVOYEZ"
    )
    
    val youReceive = mapOf(
        "es" to "RECIBES",
        "en" to "YOU RECEIVE",
        "pt" to "VOCÊ RECEBE",
        "fr" to "VOUS RECEVEZ"
    )
    
    val convertNow = mapOf(
        "es" to "CONVERTIR AHORA",
        "en" to "CONVERT NOW",
        "pt" to "CONVERTER AGORA",
        "fr" to "CONVERTIR MAINTENANT"
    )
    
    val finalResult = mapOf(
        "es" to "RESULTADO FINAL",
        "en" to "FINAL RESULT",
        "pt" to "RESULTADO FINAL",
        "fr" to "RÉSULTAT FINAL"
    )
    
    val viewHistory = mapOf(
        "es" to "VER HISTORIAL DE CONVERSIONES",
        "en" to "VIEW CONVERSION HISTORY",
        "pt" to "VER HISTÓRICO DE CONVERSÕES",
        "fr" to "VOIR L'HISTORIQUE DES CONVERSIONS"
    )
    
    val history = mapOf(
        "es" to "Historial",
        "en" to "History",
        "pt" to "Histórico",
        "fr" to "Historique"
    )
    
    val settings = mapOf(
        "es" to "Configuración",
        "en" to "Settings",
        "pt" to "Configurações",
        "fr" to "Paramètres"
    )
    
    val back = mapOf(
        "es" to "Volver",
        "en" to "Back",
        "pt" to "Voltar",
        "fr" to "Retour"
    )
    
    val noConversions = mapOf(
        "es" to "No hay conversiones",
        "en" to "No conversions",
        "pt" to "Sem conversões",
        "fr" to "Aucune conversion"
    )
    
    val makeFirstConversion = mapOf(
        "es" to "Realiza tu primera conversión",
        "en" to "Make your first conversion",
        "pt" to "Faça sua primeira conversão",
        "fr" to "Effectuez votre première conversion"
    )
    
    val clearHistory = mapOf(
        "es" to "Limpiar historial",
        "en" to "Clear history",
        "pt" to "Limpar histórico",
        "fr" to "Effacer l'historique"
    )
    
    val confirmClearHistory = mapOf(
        "es" to "¿Estás seguro de que quieres eliminar todo el historial?",
        "en" to "Are you sure you want to delete all history?",
        "pt" to "Tem certeza de que deseja excluir todo o histórico?",
        "fr" to "Êtes-vous sûr de vouloir supprimer tout l'historique ?"
    )
    
    val delete = mapOf(
        "es" to "Eliminar",
        "en" to "Delete",
        "pt" to "Excluir",
        "fr" to "Supprimer"
    )
    
    val cancel = mapOf(
        "es" to "Cancelar",
        "en" to "Cancel",
        "pt" to "Cancelar",
        "fr" to "Annuler"
    )
    
    val close = mapOf(
        "es" to "Cerrar",
        "en" to "Close",
        "pt" to "Fechar",
        "fr" to "Fermer"
    )
    
    val appearance = mapOf(
        "es" to "Apariencia",
        "en" to "Appearance",
        "pt" to "Aparência",
        "fr" to "Apparence"
    )
    
    val language = mapOf(
        "es" to "Idioma",
        "en" to "Language",
        "pt" to "Idioma",
        "fr" to "Langue"
    )
    
    val darkTheme = mapOf(
        "es" to "Tema oscuro",
        "en" to "Dark theme",
        "pt" to "Tema escuro",
        "fr" to "Thème sombre"
    )
    
    val activated = mapOf(
        "es" to "Activado",
        "en" to "Enabled",
        "pt" to "Ativado",
        "fr" to "Activé"
    )
    
    val deactivated = mapOf(
        "es" to "Desactivado",
        "en" to "Disabled",
        "pt" to "Desativado",
        "fr" to "Désactivé"
    )
    
    val notifications = mapOf(
        "es" to "Notificaciones",
        "en" to "Notifications",
        "pt" to "Notificações",
        "fr" to "Notifications"
    )
    
    val currencyAlerts = mapOf(
        "es" to "Alertas de divisas",
        "en" to "Currency alerts",
        "pt" to "Alertas de moedas",
        "fr" to "Alertes de devises"
    )
    
    val receiveImportantNotifications = mapOf(
        "es" to "Recibe notificaciones de cambios importantes",
        "en" to "Receive notifications of important changes",
        "pt" to "Receba notificações de mudanças importantes",
        "fr" to "Recevez des notifications de changements importants"
    )
    
    val information = mapOf(
        "es" to "Información",
        "en" to "Information",
        "pt" to "Informação",
        "fr" to "Information"
    )
    
    val about = mapOf(
        "es" to "Acerca de",
        "en" to "About",
        "pt" to "Sobre",
        "fr" to "À propos"
    )
    
    val versionSupportPolicies = mapOf(
        "es" to "Versión, soporte y políticas",
        "en" to "Version, support and policies",
        "pt" to "Versão, suporte e políticas",
        "fr" to "Version, support et politiques"
    )
    
    val selectLanguage = mapOf(
        "es" to "Seleccionar idioma",
        "en" to "Select language",
        "pt" to "Selecionar idioma",
        "fr" to "Sélectionner la langue"
    )
    
    val smartCurrencyConverter = mapOf(
        "es" to "Smart Currency Converter",
        "en" to "Smart Currency Converter",
        "pt" to "Conversor Inteligente de Moedas",
        "fr" to "Convertisseur de Devises Intelligent"
    )
    
    val convertDescription = mapOf(
        "es" to "Convierte divisas de forma rápida y sencilla con tasas de cambio actualizadas.",
        "en" to "Convert currencies quickly and easily with updated exchange rates.",
        "pt" to "Converta moedas de forma rápida e fácil com taxas de câmbio atualizadas.",
        "fr" to "Convertissez des devises rapidement et facilement avec des taux de change mis à jour."
    )
    
    val searchCurrency = mapOf(
        "es" to "Buscar moneda...",
        "en" to "Search currency...",
        "pt" to "Buscar moeda...",
        "fr" to "Rechercher devise..."
    )
    
    fun get(key: Map<String, String>): String {
        return key[LanguageManager.currentLanguage.value] ?: key["es"] ?: ""
    }
}

